package br.com.altamira.data.model.manufacturing;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "MN_OPERATION")
public class Operation {

	@Id
	Long id;
	
	int sequence;
	String name;
	String description;
	String sketch;
    
	@JsonIgnore
	@JoinColumn(name = "PROCESS", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Process	process;
    
	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY, orphanRemoval = true)
	Set<Consume> consume = new HashSet<Consume>();
	
	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY, orphanRemoval = true)
	Set<Produce> produce = new HashSet<Produce>();
	
	public int getSequence() {
		return sequence;
	}
	
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSketch() {
		return sketch;
	}
	
	public void setSketch(String sketch) {
		this.sketch = sketch;
	}
	
	public Set<Consume> getConsume() {
		return consume;
	}
	
	public void setUseconsume(Set<Consume> useconsume) {
		this.consume = useconsume;
	}
	
	public Set<Produce> getProduce() {
		return produce;
	}
	
	public void setProduce(Set<Produce> produce) {
		this.produce = produce;
	}
	
}
