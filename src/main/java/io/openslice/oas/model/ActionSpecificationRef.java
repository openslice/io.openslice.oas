package io.openslice.oas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

/**
 * @author ctranoris
 *
 *Action reference
 */
@ApiModel(description = "Action reference")
@Validated
@Entity(name = "OASActionSpecRef")
@JsonIgnoreProperties({ "uuid" })
@Data
public class ActionSpecificationRef {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;

	@JsonProperty("actionId")
	private String actionId = null;

	
}
