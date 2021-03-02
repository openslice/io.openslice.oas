package io.openslice.oas.model;

import java.util.Set;

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
 */
@ApiModel(description = "An ActionParam is an entity that describes parameteres of an action.")
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
