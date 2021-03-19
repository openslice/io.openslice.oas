package io.openslice.oas;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.oas.model.Action;
import io.openslice.oas.model.ActionExecutionStatus;
import io.openslice.tmf.am642.model.Alarm;
import io.openslice.tmf.am642.model.AlarmCreate;
import io.openslice.tmf.am642.model.AlarmStateType;
import io.openslice.tmf.am642.model.AlarmUpdate;
import io.openslice.tmf.am642.model.Comment;
import io.openslice.tmf.so641.model.ServiceOrder;

@Service
public class AlarmsService {

	private static final transient Log logger = LogFactory.getLog(AlarmsService.class.getName());

	@Autowired
	CamelContext contxt;

	@Autowired
	ProducerTemplate template;

	@Value("${ALARMS_ADD_ALARM}")
	private String ALARMS_ADD_ALARM ="";

	@Value("${ALARMS_UPDATE_ALARM}")
	private String ALARMS_UPDATE_ALARM ="";

	@Value("${ALARMS_GET_ALARM}")
	private String ALARMS_GET_ALARM ="";


	@Value("${spring.application.name}")
	private String compname;

	
	private Map<String, List<ActionExecutionStatus> > pendingAlarmsToCheck = new HashMap<>();
	
	/**
	 * @param al
	 * @return a response in string
	 * @throws IOException
	 */
	public String createAlarm(AlarmCreate al) throws IOException {
			
		String body;
		body = toJsonString(al);
		logger.info("createAlarm body = " + body);
		Object response = template.requestBody( ALARMS_ADD_ALARM, body);
		return response.toString();
	}
	
	/**
	 * @param al
	 * @return a response in string
	 * @throws IOException
	 */
	public String updateAlarm(AlarmUpdate al, String alarmid, String commentText) throws IOException {
		Comment comment = new Comment();
		comment.setTime(OffsetDateTime.now(ZoneOffset.UTC));
		comment.setSystemId(compname);
		comment.setComment( commentText);
		al.addCommentItem(comment);	
		
		String body;
		body = toJsonString(al);
		logger.info("updateAlarm body = " + body);
		Object response = template.requestBodyAndHeader( ALARMS_UPDATE_ALARM, body , "alarmid", alarmid);
		return response.toString();
	}
	
	

	/**
	 * update the alarm status in alarm repo
	 * 
	 * @param alarm
	 * @param canBehandled
	 * @param actions
	 */
	void patchAlarmAck(Alarm alarm, boolean canBehandled, List<Action> actions) {
		AlarmUpdate aupd = new AlarmUpdate();
		String comment;
		
		if (canBehandled) {
			aupd.setAckState("acknowledged");
			aupd.setAckSystemId( compname );
			
			String[] names = actions.stream().map(p -> p.getName() ).toArray( size -> new String[actions.size()] );
			String acts = String.join(",", names);			
			comment = "Alarm handled automatically, by applying actions: " + acts ;
			
		}else {

			comment = "Alarm cannot be handled automatically";
			
		}
		aupd.setState(AlarmStateType.updated.name());		
	

		try {
			logger.info("Alarm id = " + alarm.getId() + "." + comment);
			String response = this.updateAlarm(aupd, alarm.getId(), comment);
			logger.info("Alarm id updated = " + response.toString() );
		} catch (IOException e) {
			logger.error("patchAlarm Alarm id = " + alarm.getId() );
			e.printStackTrace();
		}
	}
	
	
	void patchAlarmClear( String alarmId) {
		AlarmUpdate aupd = new AlarmUpdate();
		
		aupd.setClearSystemId( compname );
		String comment = "Alarm cleared." ;
		aupd.setState(AlarmStateType.cleared.name());				
		

		try {
			logger.info("Alarm id = " + alarmId + "." + comment);
			String response = this.updateAlarm(aupd, alarmId, comment);
			logger.info("Alarm id updated = " + response.toString() );
		} catch (IOException e) {
			logger.error("patchAlarmClear Alarm id = " + alarmId );
			e.printStackTrace();
		}
	}
	
	public Alarm getAlarm(String alarmid) {
		
		logger.info("will retrieve Alarm from id=" + alarmid   );
		try {
			Object response = template.
					requestBody( ALARMS_GET_ALARM, alarmid);
			
			if ( !(response instanceof String)) {
				logger.error("Alarm object is wrong.");
				return null;
			}
			logger.debug("retrieve Alarm response is: " + response);
			Alarm sor = toJsonObj( (String)response, Alarm.class); 
			
			return sor;
			
		}catch (Exception e) {
			logger.error("Cannot retrieve Service Order details from catalog. " + e.toString());
		}
		return null;
		
	}

	public Map<String, List<ActionExecutionStatus> > getPendingAlarmsToCheck() {
		return pendingAlarmsToCheck;
	}

	public void setPendingAlarmsToCheck(Map<String, List<ActionExecutionStatus> > pendingAlarmsToCheck) {
		this.pendingAlarmsToCheck = pendingAlarmsToCheck;
	}


	static String toJsonString(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

	static <T> T toJsonObj(String content, Class<T> valueType)  throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue( content, valueType);
    }


}
