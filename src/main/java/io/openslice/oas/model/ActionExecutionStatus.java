package io.openslice.oas.model;

import lombok.Data;

/**
 * ActionExecutionStatus element. Keeps the status of the registered exec actions for specific alarm. Used to monitor if the alarm is cleared or not
 * @author ctranoris
 *
 */
@Data
public class ActionExecutionStatus {
	
	Action action ;	
	String serviceId ;
	boolean actionfulfilled = false;
}
