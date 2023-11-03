package io.openslice.oas.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.Data;

/**
 * @author ctranoris
 * 
 * The scope that the Rule is related
 *
 */
@Schema(description = "The scope that the Rule is related.")
@Validated
@Data
@Embeddable
public class Scope {
	
	@JsonProperty("entityUUID")
	String entityUUID;
	
}
