package br.com.altamira.data.model.sales;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.model.Resource;

@Entity
@Table(name = "SL_PRODUCT", uniqueConstraints = @UniqueConstraint(columnNames = {"CODE", "DESCRIPTION"}))
@NamedQueries({
	@NamedQuery(name = "Product.list", query = "SELECT p FROM Product p"),
	@NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
	@NamedQuery(name = "Product.findByCode", query = "SELECT p FROM Product p WHERE p.code = :code")})
public class Product extends Resource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4871377387938455032L;

	@NotNull
	@Size(min=10)
	@Column(name = "CODE", unique=true, nullable=false) 
	private String code = "";
	
	@NotNull
	@Size(min=10)
	@Column(name = "DESCRIPTION", unique=true, nullable=false) 
	private String description = "";
	
	@Column(name = "COLOR")
	private String color = "";
	
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

	public Product() {
		
	}
	
	public Product(String code, String description, String color,
			BigDecimal width, BigDecimal height, BigDecimal length,
			BigDecimal weight) {
		super();
		this.code = code;
		this.description = description;
		this.color = color;
		this.width = width;
		this.height = height;
		this.length = length;
		this.weight = weight;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
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

}
