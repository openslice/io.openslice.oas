package io.openslice.oas.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import lombok.Data;

/**
 * @author ctranoris
 * 
 * A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service
 *
 */
@Schema(description = "A RuleSpecification is an entity that describes a rule to apply an action on certain conditions in the context of a service.")
@Validated
@Entity(name = "OASRuleSpec")
@Data
public class RuleSpecification {

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	protected String uuid = null;

	@JsonProperty("name")
	protected String name = null;

	@Lob
	@Column(name = "LDESCRIPTION", columnDefinition = "LONGTEXT")
	@JsonProperty("description")
	protected String description = null;

	@JsonProperty("eventType")
	String opensliceEventType;
	
	@JsonProperty("actions")
	@Valid
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<Action> actions = new HashSet<>(); 
	
	@JsonProperty("scope")
	Scope scope;

	@JsonProperty("condition")	
	@Valid
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Set<Condition> condition = new HashSet<>(); 
	
	public RuleSpecification name(String name) {
		this.name = name;
		return this;
	}
	
	
	
	/**
	 * Name of the entity
	 * 
	 * @return name
	 **/
	@Schema(description = "Name of the entity")

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
	@Schema(description = "Description of this entity")

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Entity {\n");

		sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	public void copyFromObj(RuleSpecification be) {
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
