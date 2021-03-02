package io.openslice.oas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ctranoris
 * 
 * A Condition is an entity that describes the condition that must be tru in order to apply an action
 *
 */
@ApiModel(description = "A Condition is an entity that describes the condition that must be tru in order to apply an action.")
@Validated
@Entity(name = "OASCondition")
@Data
public class Condition {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;

	@JsonProperty("booleanOperator")
	String booleanOperator;

	@JsonProperty("eventAttributeName")
	String opensliceEventAttributeName;

	@JsonProperty("eventAttributeValue")
	String eventAttributeValue;

	
	/**
	 * EQUAL
	 * NOTEQUAL
	 * GREATER_THAN
	 * LESS_THAN
	 */
	@JsonProperty("operator")
	String operator;
}
