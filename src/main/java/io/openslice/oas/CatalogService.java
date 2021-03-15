package io.openslice.oas;

import java.io.IOException;
import java.util.HashMap;
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

import io.openslice.tmf.sim638.model.ServiceUpdate;

@Service
public class CatalogService {

	private static final transient Log logger = LogFactory.getLog(CatalogService.class.getName());

	@Autowired
	CamelContext contxt;

	@Autowired
	ProducerTemplate template;

	@Value("${CATALOG_UPD_SERVICE}")
	private String CATALOG_UPD_SERVICE = "";
	
	
	
	public io.openslice.tmf.sim638.model.Service updateService(String serviceId, ServiceUpdate s, boolean propagateToSO) {
		logger.info("will update Service : " + serviceId );
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("serviceid", serviceId );
			map.put("propagateToSO", propagateToSO );
			
			Object response = template.requestBodyAndHeaders( CATALOG_UPD_SERVICE, toJsonString(s), map);

			if ( !(response instanceof String)) {
				logger.error("Service Instance object is wrong.");
			}

			io.openslice.tmf.sim638.model.Service serviceInstance = toJsonObj( (String)response, io.openslice.tmf.sim638.model.Service.class); 
			//logger.debug("createService response is: " + response);
			return serviceInstance;
			
			
		}catch (Exception e) {
			logger.error("Cannot update Service: " + serviceId + ": " + e.toString());
		}
		return null;
		
	}
	
	

	
	static <T> T toJsonObj(String content, Class<T> valueType)  throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.readValue( content, valueType);
    }
	static String toJsonString(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(object);
	}

}
