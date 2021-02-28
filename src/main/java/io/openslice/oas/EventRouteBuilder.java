package io.openslice.oas;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.openslice.tmf.am642.model.AlarmCreateEvent;

/**
 * @author ctranoris
 *
 */
@Configuration
@Component
public class EventRouteBuilder extends RouteBuilder {

	@Value("${EVENT_ALARM_CREATE}")
	private String EVENT_ALARM_CREATE = "";

	private static final transient Log logger = LogFactory.getLog(EventRouteBuilder.class.getName());

	public void configure() {


		/**
		 * Create user route, from Individual event
		 */
		
		

		from( EVENT_ALARM_CREATE )
		.unmarshal().json( JsonLibrary.Jackson, AlarmCreateEvent.class, true)
		.bean( AlarmHandling.class, "onAlarmCreateEvent")
		.to("stream:out");
		
		
	}
}
