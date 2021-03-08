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
  *
 */
@ApiModel(description = "An ActionCharacteristic is an entity that describes the values of the characteristics of ana ction in a rule.")

@Validated
@Entity(name = "OASActionCharacteristic")
@Data
public class ActionCharacteristic {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;
	
	
	@JsonProperty("name")
	protected String name = null;
	
	@JsonProperty("value")
	protected String value = null;
}
