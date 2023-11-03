package io.openslice.oas.model;

import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author ctranoris
 * 
 * An ActionSpecification is an entity that describes an action to perform on certain entities
 *
 */
@Schema(description = "An ActionSpecification is an entity that describes an action to perform on certain entities.")
@Validated
public class ActionSpecificationCreate extends ActionSpecificationUpdate {

}
