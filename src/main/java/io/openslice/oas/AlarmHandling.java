package io.openslice.oas;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import io.openslice.oas.model.Action;
import io.openslice.oas.model.ActionSpecification;
import io.openslice.oas.model.ActionSpecificationRef;
import io.openslice.oas.model.Condition;
import io.openslice.oas.model.RuleSpecification;
import io.openslice.oas.reposervices.ActionSpecificationRepoService;
import io.openslice.oas.reposervices.RuleSpecificationRepoService;
import io.openslice.tmf.am642.model.AffectedService;
import io.openslice.tmf.am642.model.Alarm;
import io.openslice.tmf.am642.model.AlarmCreateEvent;
import io.openslice.tmf.am642.model.AlarmStateType;
import io.openslice.tmf.am642.model.AlarmUpdate;
import io.openslice.tmf.am642.model.Comment;
import lombok.extern.apachecommons.CommonsLog;

/**
 * @author ctranoris
 *
 */
@Configuration
@CommonsLog
public class AlarmHandling {


	@Autowired
	RuleSpecificationRepoService ruleSpecificationRepoService;

	@Autowired
	ActionSpecificationRepoService actionSpecificationRepoService;

	@Autowired
	AlarmsService alarmsService;

	@Value("${spring.application.name}")
	private String compname;

	@Transactional 
	public void onAlarmCreateEvent(AlarmCreateEvent anAlarmCreateEvent) {
		
		
		if ((anAlarmCreateEvent != null) 
				&& (anAlarmCreateEvent.getEvent() != null)
				&& (anAlarmCreateEvent.getEvent().getAlarm() != null)) {
		
			
			Alarm alarm = anAlarmCreateEvent.getEvent().getAlarm();
			log.info("onAlarmCreateEvent AlarmType=" + alarm.getAlarmType() 
			+ ", ProbableCause=" + alarm.getProbableCause() 
			+ ", PerceivedSeverity=" + alarm.getPerceivedSeverity()
			+ ", SourceSystemId=" + alarm.getSourceSystemId() 
			+ ", SpecificProblem=" + alarm.getSpecificProblem()
			+ ", AlarmDetails=" + alarm.getAlarmDetails() );
			
			
			performActionOnAlarm( alarm );
			
		}	
		
		
	}

	@Transactional 
	public void performActionOnAlarm(Alarm alarm) {

		//decide If we handle this.
		var actions = decideForExecutionAction( alarm );
		
		if ( ( actions == null ) || ( actions.size() == 0 )) { //we did not find an action to perform 
			patchAlarm(alarm, false, null);
			return;
		}
		
			

		patchAlarm(alarm, true, actions);
		
		// send ack to the alarm management
		
		//patch alarm
//		ackState( "acknowledged" )
//		- serviceAffecting = true
//		- affectedService (id = serv_inventory_ID)
//		- comment (“specific action made”
		
		//execute the desired action
				
		//if action is successful clear alarm
		
	}
	

	/**
	 * @param alarm
	 * 
	 * decide Action to perform on alarm based on certain criteria or return null.
	 * The decision is based on registered  RuleSpecification conditions of alarms
	 * 
	 * 
	 */
	@Transactional 
	public List<Action> decideForExecutionAction(Alarm alarm) {

		// examine our list of rulespecs if we can have a match for a specific service in inventory
		// In future a rules engine should be more efficient. 
		//	For now and for our PoC we will use query in DB and search the attributes			

		var actionsToApply = new ArrayList<Action>();
		
		for (AffectedService affectedService : alarm.getAffectedService()) {
			var rulespecs = ruleSpecificationRepoService.findByScopeUuid( affectedService.getId() );
			for (RuleSpecification ruleSpecification : rulespecs) {
				RuleSpecification spec = ruleSpecificationRepoService.findById( ruleSpecification.getUuid() );
				
//				if (ruleSpecification.getCondition().size() == 0 ) { // if there are no conditions, this means any condition so add all of the actions!
//					actionsToApply.addAll( ruleSpecification.getActions()  );
//				}
				
				Boolean conditionInitialStatus = true; // if there are no conditions, this means any condition so add all of the actions!
				
				for (Condition condition : spec.getCondition()) {

					Boolean conditionStatus = false;
					//match here the criteria		
					try {
						
						
						Field field = Alarm.class.getDeclaredField(condition.getOpensliceEventAttributeName());			
						field.setAccessible(true);
						String alarmvalue = field.get(alarm).toString();
						String operator = condition.getOperator();
						switch ( operator) {
						case "EQUALS":
							conditionStatus = alarmvalue == condition.getEventAttributeValue();
							break;
						case "NOTEQUAL":
							conditionStatus = alarmvalue != condition.getEventAttributeValue();
							break;
						case "GREATER_THAN":
							conditionStatus = Double.valueOf(alarmvalue) >  Double.valueOf( condition.getEventAttributeValue());
							break;
						case "LESS_THAN":
							conditionStatus = Double.valueOf(alarmvalue) <  Double.valueOf( condition.getEventAttributeValue());
							break;
							
						default:
							break;
						}
						
						
						
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (condition.getBooleanOperator().equals("OR") ) {
						conditionInitialStatus = conditionInitialStatus || conditionStatus;						
					} else {
						conditionInitialStatus = conditionInitialStatus && conditionStatus;						
					}
				}			
				
				
				
				
				if (conditionInitialStatus) {
					actionsToApply.addAll( ruleSpecification.getActions()  );
				}
			}
			
		}
		
				
		
		
		
		return actionsToApply;
	}
	
	

	

	private void patchAlarm(Alarm alarm, boolean canBehandled, List<Action> actions) {
		AlarmUpdate aupd = new AlarmUpdate();
		Comment comment = new Comment();
		comment.setTime(OffsetDateTime.now(ZoneOffset.UTC));
		comment.setSystemId(compname);
		
		if (canBehandled) {
			aupd.setAckState("acknowledged");
			aupd.setAckSystemId( compname );
			
			String[] names = actions.stream().map(p -> p.getName() ).toArray( size -> new String[actions.size()] );
			String acts = String.join(",", names);			
			comment.setComment("Alarm handled automatically, by applying actions: " + acts);
			
		}else {

			comment.setComment("Alarm cannot be handled automatically");
			
		}
		aupd.setState(AlarmStateType.updated.name());		

		aupd.addCommentItem(comment);		

		try {
			log.info("Alarm id = " + alarm.getId() + "." + comment.getComment());
			String response = alarmsService.updateAlarm(aupd, alarm.getId());
		} catch (IOException e) {
			log.error("patchAlarm Alarm id = " + alarm.getId() );
			e.printStackTrace();
		}
	}
	
	
}
