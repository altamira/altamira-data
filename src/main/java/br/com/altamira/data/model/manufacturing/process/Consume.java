package br.com.altamira.data.model.manufacturing.process;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MN_CONSUME")
public class Consume extends br.com.altamira.data.model.Relation {

	/**
	 * 
	 */
	private static final long serialVersionUID = -492716374383567653L;
	
	@NotNull
	@Size(min=10)
	@Column(name = "CODE", columnDefinition = "nvarchar2(255)")
	private String code;
	
	@NotNull
	@Size(min=10)
	@Column(name = "DESCRIPTION", columnDefinition = "nvarchar2(255)")
	private String description;
	
	@NotNull
	@Min(0)
	@Column(name = "QUANTITY")
	private BigDecimal quantity;
	
	@NotNull
	@Size(min=1)
	@Column(name = "UNIT")
	private String unit;
	
	@JsonIgnore
	@JoinColumn(name = "OPERATION", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private br.com.altamira.data.model.manufacturing.process.Operation operation;

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

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
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
}
