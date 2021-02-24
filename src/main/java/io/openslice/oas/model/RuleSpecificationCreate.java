package io.openslice.oas.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ctranoris
 * 
 * A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service
 *
 */
@ApiModel(description = "A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service.")
@Validated
@Data
@EqualsAndHashCode(callSuper=true)
public class RuleSpecificationCreate extends RuleSpecificationUpdate {

	
	
	
}
