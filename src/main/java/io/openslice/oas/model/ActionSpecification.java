package io.openslice.oas.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author ctranoris
 * 
 * An ActionSpecification is an entity that describes an action to perform on certain entities
 *
 */
@ApiModel(description = "An ActionSpecification is an entity that describes an action to perform on certain entities.")
@Validated
@Entity(name = "ActionSpecification")
public class ActionSpecification {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;

	@JsonProperty("name")
	protected String name = null;

	public ActionSpecification name(String name) {
		this.name = name;
		return this;
	}
	

	@Lob
	@Column(name = "LDESCRIPTION", columnDefinition = "LONGTEXT")
	@JsonProperty("description")
	protected String description = null;

	/**
	 * Name of the entity
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "Name of the entity")

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
	/**
	 * Description of this entity
	 * 
	 * @return description
	 **/
	@ApiModelProperty(value = "Description of this entity")

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ActionSpecification Entity = (ActionSpecification) o;
		return Objects.equals(this.uuid, Entity.uuid) && Objects.equals(this.name, Entity.name);
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(uuid, 
//
//				baseType, schemaLocation);
//	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Entity {\n");

		sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	public void copyFromObj(ActionSpecification be) {
		this.uuid = be.uuid;
		this.name = be.name;

	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
