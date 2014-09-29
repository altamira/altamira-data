package br.com.altamira.data.model.manufacturing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	@Column(name = "CODE", columnDefinition = "nvarchar2(255)")
	private String code;
	
	@NotNull
	@Size(min=10)
	@Column(name = "DESCRIPTION", columnDefinition = "nvarchar2(255)")
	private String description;

	@Column(name = "COLOR", columnDefinition = "nvarchar2(255)")
	private String color;
	
	@Min(0)
	@Column(name = "WEIGHT")
	private float weight;
	
	@Min(0)
	@Column(name = "LENGTH")
	private float length;
	
	@Min(0)
	@Column(name = "WIDTH")
	private float width;
	
	@Column(name = "FINISH", columnDefinition = "nvarchar2(255)")
	private String finish;
	
	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Revision> revision = new ArrayList<Revision>();

	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<Operation> operation = new HashSet<Operation>();

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
	
	public List<Revision> getRevision() {
		return revision;
	}
	
	public void setRevision(List<Revision> revision) {
		this.revision = revision;
	}
	
	public Set<Operation> getOperation() {
		return operation;
	}
	
	public void setOperation(Set<Operation> operation) {
		this.operation = operation;
	}

}
