package br.com.altamira.data.model.sales;

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
import javax.xml.bind.annotation.XmlTransient;

import br.com.altamira.data.model.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SL_ORDER_ITEM_PART")
public class OrderItemPart extends Resource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4871377387938455032L;

	@NotNull
	@Size(min=10)
	@Column(name = "CODE")
	private String code = "";
	
	@NotNull
	@Size(min=10)
	@Column(name = "DESCRIPTION")
	private String description = "";
	
	@Column(name = "COLOR")
	private String color = "";
	
	@NotNull
	@Min(0)
	@Column(name = "QUANTITY")
	private BigDecimal quantity = BigDecimal.valueOf(0);
	
	@NotNull
	@Min(0)
	@Column(name = "WIDTH")
	private BigDecimal width = BigDecimal.valueOf(0);
	
	@NotNull
	@Min(0)
	@Column(name = "HEIGHT")
	private BigDecimal height = BigDecimal.valueOf(0);
	
	@NotNull
	@Min(0)
	@Column(name = "LENGTH")
	private BigDecimal length = BigDecimal.valueOf(0);
	
	@NotNull
	@Min(0)
	@Column(name = "WEIGHT")
	private BigDecimal weight = BigDecimal.valueOf(0);

    @JsonIgnore
	@JoinColumn(name = "ORDER_ITEM", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private OrderItem orderItem;

	//@JoinColumn(name = "CODE", referencedColumnName = "CODE", insertable=false, updatable=false)
    /*@JoinColumn(name = "PRODUCT", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Product product;*/
    
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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	
    @JsonIgnore
	@XmlTransient
	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	/*public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}*/

}
