package io.openslice.oas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;

import io.openslice.tmf.am642.model.Alarm;
import io.openslice.tmf.am642.model.AlarmCreateEvent;

/**
 * @author ctranoris
 *
 */
@Configuration
public class AlarmHandling {

	private static final transient Log logger = LogFactory.getLog(AlarmHandling.class.getName());

	public static void onAlarmCreateEvent(AlarmCreateEvent anAlarmCreateEvent) {
		if ((anAlarmCreateEvent != null) 
				&& (anAlarmCreateEvent.getEvent() != null)
				&& (anAlarmCreateEvent.getEvent().getAlarm() != null)) {
			Alarm alarm = anAlarmCreateEvent.getEvent().getAlarm();
			logger.info("onAlarmCreateEvent AlarmType=" + alarm.getAlarmType() 
			+ ", ProbableCause=" + alarm.getProbableCause() 
			+ ", PerceivedSeverity=" + alarm.getPerceivedSeverity()
			+ ", SourceSystemId=" + alarm.getSourceSystemId() 
			+ ", SpecificProblem=" + alarm.getSpecificProblem()
			+ ", AlarmDetails=" + alarm.getAlarmDetails() );
		}

	}
}
