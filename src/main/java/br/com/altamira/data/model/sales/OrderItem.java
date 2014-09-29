package br.com.altamira.data.model.sales;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import br.com.altamira.data.model.Resource;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "SL_ORDER_ITEM"/*, uniqueConstraints = @UniqueConstraint(columnNames = {"\"ORDER\"", "ITEM"})*/)
@NamedQueries({
	@NamedQuery(name = "OrderItem.list", query = "SELECT i FROM OrderItem i WHERE i.order.number = :number"),
	@NamedQuery(name = "OrderItem.findById", query = "SELECT i FROM OrderItem i WHERE i.id = :id"),
	@NamedQuery(name = "OrderItem.findByItem", query = "SELECT i FROM OrderItem i WHERE i.order.number = :number AND i.item = :item")})
public class OrderItem extends Resource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7448803904699786256L;

	@JsonIgnore
	@XmlTransient
	@JsonView(JSonViews.EntityView.class)
	@JoinColumn(name = "\"ORDER\"", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Order order;
    
	@NotNull
	@Min(1)
	@Column(name = "ITEM")
	private int item = 0;
	
	@NotNull
	@Size(min = 10)
	@Column(name = "DESCRIPTION")
	private String description = "";
	
    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "orderItem", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<OrderItemPart> parts = new ArrayList<OrderItemPart>();

    @JsonIgnore
    @XmlTransient
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

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
	
	public List<OrderItemPart> getParts() {
		return parts;
	}
	
	public void setPart(List<OrderItemPart> parts) {
		this.parts = parts;
	}

}
