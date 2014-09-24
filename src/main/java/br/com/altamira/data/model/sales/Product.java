package br.com.altamira.data.model.sales;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.model.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SL_PRODUCT")
public class Product extends Resource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4871377387938455032L;

	@NotNull
	@Size(min=10)
	String code;
	
	String color;
	
	@NotNull
	@Size(min=10)
	String description;
	
    @JsonIgnore
	@JoinColumn(name = "ORDER_ITEM", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrderItem orderItem;

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

}
