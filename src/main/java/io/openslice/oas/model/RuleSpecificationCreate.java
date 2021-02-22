package io.openslice.oas.model;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;

/**
 * @author ctranoris
 * 
 * A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service
 *
 */
@ApiModel(description = "A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service.")
@Validated
public class RuleSpecificationCreate extends RuleSpecificationUpdate {

}
