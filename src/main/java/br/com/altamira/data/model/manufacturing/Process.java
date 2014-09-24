package br.com.altamira.data.model.manufacturing;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "MN_PROCESS")
public class Process extends br.com.altamira.data.model.Process {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5549369785798723928L;
	
	@NotNull
	@Size(min=10)
	String code;
	
	@NotNull
	@Size(min=10)
	String description;

	String color;
	
	@Min(0)
	float weight;
	
	@Min(0)
	float length;
	
	@Min(0)
	float width;
	
	String finish;
	
	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
	Set<Revision> revision = new HashSet<Revision>();

	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
	Set<Operation> operation = new HashSet<Operation>();

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public float getWeight() {
		return weight;
	}
	
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	public float getLength() {
		return length;
	}
	
	public void setLength(float length) {
		this.length = length;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public String getFinish() {
		return finish;
	}
	
	public void setFinish(String finish) {
		this.finish = finish;
	}
	
	public Set<Revision> getRevision() {
		return revision;
	}
	
	public void setRevision(Set<Revision> revision) {
		this.revision = revision;
	}
	
	public Set<Operation> getOperation() {
		return operation;
	}
	
	public void setOperation(Set<Operation> operation) {
		this.operation = operation;
	}

}
