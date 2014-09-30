package br.com.altamira.data.model.manufacturing.process;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "MN_OPERATION")
public class Operation extends br.com.altamira.data.model.Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4778350055794788171L;
	
	@NotNull
	@Min(1)
	@Column(name = "SEQUENCE")
	private int sequence;
	
	@NotNull
	@Size(min=3)
	@Column(name = "NAME", columnDefinition = "nvarchar2(255)")
	private String name;
	
	@NotNull
	@Size(min=1)
	@Column(name = "DESCRIPTION", columnDefinition = "nvarchar2(500)")
	private String description;
	
	@Column(name = "SKETCH", columnDefinition = "nvarchar2(500)")
	private String sketch;
    
	@JsonIgnore
	@JoinColumn(name = "PROCESS", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Process process;
    
	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Consume> consume = new ArrayList<Consume>();
	
	@JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<Produce> produce = new ArrayList<Produce>();

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public void setConsume(List<Consume> consume) {
		this.consume = consume;
	}

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
	
	public List<Consume> getConsume() {
		return consume;
	}
	
	public void setUseconsume(List<Consume> useconsume) {
		this.consume = useconsume;
	}
	
	public List<Produce> getProduce() {
		return produce;
	}
	
	public void setProduce(List<Produce> produce) {
		this.produce = produce;
	}
	
}
