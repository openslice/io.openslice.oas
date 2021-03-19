package io.openslice.oas;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.Action;
import io.openslice.oas.model.ActionCharacteristic;
import io.openslice.oas.model.ActionExecutionStatus;
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
import io.openslice.tmf.common.model.Any;
import io.openslice.tmf.common.model.EValueType;
import io.openslice.tmf.common.model.service.Characteristic;
import io.openslice.tmf.common.model.service.Note;
import io.openslice.tmf.sim638.model.ServiceUpdate;
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
	
	@Autowired
	CatalogService catalogService;

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
			alarmsService.patchAlarmAck(alarm, false, null);
			return;
		}
		
			

		// send ack to the alarm management
		alarmsService.patchAlarmAck(alarm, true, actions);
		
		//execute the desired action		
		executeActions(actions,alarm);
				
		//if action is successful clear alarm
		//clear probably will be done by process CheckActionsStatus
		//by checking the EXEC_ACTION_LAST_ACK on related service, by serviceId 
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
							conditionStatus = alarmvalue.equals( condition.getEventAttributeValue() );
							break;
						case "NOTEQUAL":
							conditionStatus = ! alarmvalue.equals( condition.getEventAttributeValue() );
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
	
	

	

	


	/**
	 * execute each action, by passing it as a ServiceUpdate to the system
	 * @param actions
	 * @param alarm 
	 */
	private void executeActions(List<Action> actions, Alarm alarm) {

		List<ActionExecutionStatus> actList= new ArrayList<>();
		
		for (Action action : actions) {

			ServiceUpdate supd = new ServiceUpdate();;
			Note n = new Note();
			n.setText("Service Action executeAction. Action: " + action.getName() + ", for acknowledged alarm " + alarm.getUuid() );
			n.setAuthor( compname );
			n.setDate( OffsetDateTime.now(ZoneOffset.UTC).toString() );
			supd.addNoteItem( n );
			String serviceid= null;
			
			Characteristic characteristic = new Characteristic();
			characteristic.setName("EXEC_ACTION");
			characteristic.setValueType(  EValueType.MAP.getValue()  );
			var map = new HashMap<String, String>();
			map.put("ACTION_NAME", action.getName());
			map.put("ACTION_UUID", action.getUuid() );
			map.put("ALARM_UUID", alarm.getUuid() );
			for (ActionCharacteristic achar : action.getActionCharacteristics() ) {
				if ( achar.getName().equalsIgnoreCase("serviceid")) {
					serviceid = achar.getValue(); 
				}
				map.put( achar.getName() , achar.getValue() );
			}
			map.put("ALARM_DETAILS", alarm.getAlarmDetails());
			
			String[] tokens = alarm.getAlarmDetails().split(";");
			for (String token : tokens) {
				String[] kv = token.split("=");
				if ( kv.length == 2) {
					map.put( kv[0], kv[1]);					
				}
			}

			Any value = new Any();
			String str;
			try {
				str = toJsonString( map );
				value.setValue( str );				
				characteristic.setValue( value  );
				supd.addServiceCharacteristicItem( characteristic );				
				if ( serviceid != null ) {
					catalogService.updateService( serviceid , supd, true);				
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			ActionExecutionStatus aes = new ActionExecutionStatus();
			aes.setAction(action);
			aes.setServiceId(serviceid);
			actList.add( aes  );
			
		}
		

		//add to list of Alarms to Check the Actions in order to clear it or unack it!
		alarmsService.getPendingAlarmsToCheck().put( alarm.getId() , actList);
		
	}
	
	static String toJsonString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }

	
}
