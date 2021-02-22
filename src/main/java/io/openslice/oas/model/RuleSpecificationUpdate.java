package io.openslice.oas.model;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ctranoris
 * 
 * A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service
 *
 */
@ApiModel(description = "A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service.")
@Validated
public class RuleSpecificationUpdate {



	@JsonProperty("name")
	protected String name = null;
	
	
	@JsonProperty("description")
	protected String description = null;
	
	/**
	 * The name of the service test
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "The name of the entity")

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * Description of the service test
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of the entity")

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
