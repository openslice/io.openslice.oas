package io.openslice.oas.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

import lombok.Data;

/**
 * @author ctranoris
 * 
 * A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service
 *
 */
@ApiModel(description = "A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service.")
@Validated
@Data
public class RuleSpecificationUpdate {



	@JsonProperty("name")
	protected String name = null;

	@JsonProperty("description")
	protected String description = null;
	
	@JsonProperty("eventType")
	String opensliceEventType;
	

	@JsonProperty("actions")
	@Valid
	private List<ActionSpecificationRef> actions = new ArrayList<>(); 

	@JsonProperty("scope")
	Scope scope;

	@JsonProperty("condition")	
	@Valid
	private List<Condition> condition = new ArrayList<>(); 
}
