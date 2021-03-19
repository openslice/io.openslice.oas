package io.openslice.oas;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.ActionExecutionStatus;
import io.openslice.tmf.am642.model.Alarm;
import io.openslice.tmf.am642.model.AlarmUpdate;
import io.openslice.tmf.common.model.service.Characteristic;
import io.openslice.tmf.sim638.model.Service;


@Component(value = "CheckActionsStatus") // bean name
public class CheckActionsStatus implements JavaDelegate {

	@Autowired
	AlarmsService alarmsService;
	
	@Autowired
	CatalogService catalogService;
	

	@Autowired
	ObjectMapper mapper;
	
	
	private static final transient Log logger = LogFactory.getLog( CheckActionsStatus.class.getName() );
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("===================== CheckActionsStatus  ====================");
		
		//retrieve services for each pending alarms to check
		for (String alarmId : Collections.unmodifiableSet(alarmsService.getPendingAlarmsToCheck().keySet()) ) {
			
			List<ActionExecutionStatus> aes = alarmsService.getPendingAlarmsToCheck().get(alarmId);
			for (ActionExecutionStatus actionExecutionStatus : aes) {
				String serviceId = actionExecutionStatus.getServiceId();
				Service aService = catalogService.retrieveService(serviceId);

				Characteristic execAction = aService.getServiceCharacteristicByName("EXEC_ACTION");
				Characteristic execActionAck = aService.getServiceCharacteristicByName("EXEC_ACTION_LAST_ACK");
				if ( (execAction != null) && (execActionAck !=  null ) && (execAction.getValue()  != null) && (execActionAck.getValue()  !=  null ) ) {
					
					if ( execActionAck.getValue().getValue().contains("ERROR") ) {
						AlarmUpdate aupd = new AlarmUpdate();
						try {
							alarmsService.updateAlarm(aupd, alarmId, "ERROR cannot clear alarm automatically");
							alarmsService.getPendingAlarmsToCheck().remove(alarmId);
							break;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					} else {
						String characteristicValue = execAction.getValue().getValue();
						try {
							Map<String, String> valsexecAction = mapper.readValue( characteristicValue, new TypeReference< Map<String, String>>() {});
							Map<String, String> valsexecexecActionAck = mapper.readValue( characteristicValue, new TypeReference< Map<String, String>>() {});
							
							if ( valsexecAction.get("ACTION_UUID").equals(alarmId) &&
									valsexecexecActionAck.get("ACTION_UUID").equals(alarmId))  {
								
								actionExecutionStatus.setActionfulfilled( true );								
								
								
							}
							
							
							
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						
					}
					
					
				}
					
				
				
			}
			
			boolean alarmCanbeCleared = true;
			for (ActionExecutionStatus actionExecutionStatus : aes) { //check all actions of fulfilled
				alarmCanbeCleared = alarmCanbeCleared && actionExecutionStatus.isActionfulfilled();
			}
			
			
			if (alarmCanbeCleared) {
				//seems everything ok, clear alarm
				alarmsService.patchAlarmClear(alarmId);
				alarmsService.getPendingAlarmsToCheck().remove(alarmId);
			}
		}


		//if alarm is older than 15minutes. Unack it and remove from hashmap
		for (String alarmId : Collections.unmodifiableSet(alarmsService.getPendingAlarmsToCheck().keySet()) ) {
			Alarm alarm = alarmsService.getAlarm(alarmId);
			OffsetDateTime anow = OffsetDateTime.now(ZoneOffset.UTC);
			
			if ( anow.toEpochSecond()- alarm.getAlarmRaisedTime().toEpochSecond() > 30*60*1000 ) {
				AlarmUpdate aupd = new AlarmUpdate();
				try {
					alarmsService.updateAlarm(aupd, alarmId, "Action expired, cannot clear alarm automatically after 30min");
				} catch (IOException e) {
					e.printStackTrace();
				}
				alarmsService.getPendingAlarmsToCheck().remove(alarmId);
				
			}
		}
		
	}
}
