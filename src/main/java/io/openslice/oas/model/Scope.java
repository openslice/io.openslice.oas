package io.openslice.oas.model;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ctranoris
 * 
 * The scope that the Rule is related
 *
 */
@ApiModel(description = "The scope that the Rule is related.")
@Validated
@Data
@Embeddable
public class Scope {
	
	@JsonProperty("entityUUID")
	String entityUUID;
	
}
