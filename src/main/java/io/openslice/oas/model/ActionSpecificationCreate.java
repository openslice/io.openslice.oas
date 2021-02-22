package io.openslice.oas.model;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;

/**
 * @author ctranoris
 * 
 * An ActionSpecification is an entity that describes an action to perform on certain entities
 *
 */
@ApiModel(description = "An ActionSpecification is an entity that describes an action to perform on certain entities.")
@Validated
public class ActionSpecificationCreate extends ActionSpecificationUpdate {

}
