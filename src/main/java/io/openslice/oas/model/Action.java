package io.openslice.oas.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * @author ctranoris
 *
 */
@Schema(description = "Action element")
@Validated
@Entity(name = "OASAction")
@Data
public class Action {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;

	@JsonProperty("name")
	protected String name = null;

	@JsonProperty("actionCharacteristics")
	@Valid
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<ActionCharacteristic> actionCharacteristics = new HashSet<>();

	@JsonProperty("actionSpecificationRef")
	@Valid
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private ActionSpecificationRef actionSpecificationRef;
}
