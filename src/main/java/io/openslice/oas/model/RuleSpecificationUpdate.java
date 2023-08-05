package io.openslice.oas.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * @author ctranoris
 * 
 * A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service
 *
 */
@Schema(description = "A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service.")
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
	private List<Action> actions = new ArrayList<>(); 

	@JsonProperty("scope")
	Scope scope;

	@JsonProperty("condition")	
	@Valid
	private List<Condition> condition = new ArrayList<>(); 
}
