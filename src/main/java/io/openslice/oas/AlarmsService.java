package io.openslice.oas;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.openslice.tmf.am642.model.AlarmCreate;
import io.openslice.tmf.am642.model.AlarmUpdate;

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
	public String updateAlarm(AlarmUpdate al, String alarmid) throws IOException {
			
		String body;
		body = toJsonString(al);
		logger.info("updateAlarm body = " + body);
		Object response = template.requestBodyAndHeader( ALARMS_UPDATE_ALARM, body , "alarmid", alarmid);
		return response.toString();
	}
	
	

	static String toJsonString(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

}
