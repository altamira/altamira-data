package br.com.altamira.data.model.sales;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SL_ORDER")
public class Order {
	
	@Id
	Long id;
	
	Long number = 0l;
	String customer = "";
	String representative = "";
	Date created = new Date();
	Date delivery = new Date();
	String quotation = "";
	String comment = "";
	
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
