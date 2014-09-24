package br.com.altamira.data.model.manufacturing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MN_OPERATION")
public class Operation {

	@Id
	Long id;
	
	int sequence;
	String name;
	String description;
	String sketch;
	
	List<Consume> useconsume = new ArrayList<Consume>();
	List<Produce> produce = new ArrayList<Produce>();
	
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
		return useconsume;
	}
	
	public void setUseconsume(List<Consume> useconsume) {
		this.useconsume = useconsume;
	}
	
	public List<Produce> getProduce() {
		return produce;
	}
	
	public void setProduce(List<Produce> produce) {
		this.produce = produce;
	}
	
}
