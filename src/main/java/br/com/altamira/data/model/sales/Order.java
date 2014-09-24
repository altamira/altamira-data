package br.com.altamira.data.model.sales;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "SL_ORDER")
public class Order {
	
	@Id
	@GeneratedValue
	Long id;
	
	@NotNull
	Long number = 0l;
	
	@NotNull
	@Size(min = 3)
	String customer = "";
	
	@NotNull
	@Size(min = 5)
	String representative = "";
	
	@NotNull
    @Temporal(value=TemporalType.DATE)
	Date created = new Date();
	
	@NotNull
    @Temporal(value=TemporalType.DATE)
	Date delivery = new Date();
	
	@NotNull
	@Size(min = 8, max = 8)
	String quotation = "";
	
	String comment = "";
	
    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
	Set<OrderItem> item = new HashSet<OrderItem>();

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDelivery() {
		return delivery;
	}

	public void setDelivery(Date delivery) {
		this.delivery = delivery;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<OrderItem> getItem() {
		return item;
	}

	public void setItem(Set<OrderItem> item) {
		this.item = item;
	}

	public Long getNumber() {
		return number;
	}
	
	public void setNumber(Long number) {
		this.number = number;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	public String getQuotation() {
		return quotation;
	}
	
	public void setQuotation(String quotation) {
		this.quotation = quotation;
	}

}
