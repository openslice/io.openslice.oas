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
  *
 */
@Schema(description = "An ActionCharacteristic is an entity that describes the values of the characteristics of ana ction in a rule.")

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
