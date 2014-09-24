package br.com.altamira.data.model.sales;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "SL_ORDER_ITEM")
public class OrderItem {
	
	@Id
	@GeneratedValue
	Long id;
	
	@NotNull
	@Min(1)
	int item;
	
	@NotNull
	@Size(min = 10)
	String description;
	
    @NotNull
    @JsonIgnore
	@JoinColumn(name = "ORDER", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Order order;
    
    @NotNull
    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orderItem", fetch = FetchType.LAZY, orphanRemoval = true)
    Set<Product> product = new HashSet<Product>();

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<Product> getProduct() {
		return product;
	}
	
	public void setProduct(Set<Product> product) {
		this.product = product;
	}

}
