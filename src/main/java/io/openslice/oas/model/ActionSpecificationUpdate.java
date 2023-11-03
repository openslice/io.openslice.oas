package io.openslice.oas.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

/**
 * @author ctranoris
 * 
 * An ActionSpecification is an entity that describes an action to perform on certain entities
 *
 */
@Schema(description = "An ActionSpecification is an entity that describes an action to perform on certain entities.")
@Validated
public class ActionSpecificationUpdate {



	@JsonProperty("name")
	protected String name = null;
	
	
	@JsonProperty("description")
	protected String description = null;
	

	@JsonProperty("params")
	@Valid
	private List<ActionParam> params = new ArrayList<>(); 
	
	
	/**
	 * @return the params
	 */
	public List<ActionParam> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<ActionParam> params) {
		this.params = params;
	}

	/**
	 * The name of the service test
	 * 
	 * @return name
	 **/
	@Schema(description = "The name of the entity")

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
	@Schema(description = "Description of the entity")

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
