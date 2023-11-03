package io.openslice.oas.model;

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
 */
@Schema(description = "An ActionParam is an entity that describes parameteres of an action.")
@Validated
@Entity(name = "OASActionParam")
@Data
public class ActionParam {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;
	

	@JsonProperty("paramName")
	protected String paramName = null;
	

	@JsonProperty("paramValue")
	protected String paramValue = null;
}
