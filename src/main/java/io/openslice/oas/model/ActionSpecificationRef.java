package io.openslice.oas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @author ctranoris
 *
 *Action reference
 */
@Schema(description = "Action reference")
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
