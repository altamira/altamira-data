package br.com.altamira.data.model.manufacturing;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MN_PROCESS")
public class Process {
	
	@Id
	Long id;
	
	String code;
	String description;
	String color;
	float weight;
	float length;
	float width;
	String finish;
	
	Set<Revision> revision = new HashSet<Revision>();
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
