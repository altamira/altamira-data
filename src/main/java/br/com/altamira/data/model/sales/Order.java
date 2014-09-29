package br.com.altamira.data.model.sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import br.com.altamira.data.model.Resource;
import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "SL_ORDER")
@NamedQueries({
	@NamedQuery(name = "Order.list", query = "SELECT o FROM Order o"),
	@NamedQuery(name = "Order.findById", query = "SELECT o FROM Order o WHERE o.id = :id"),
	@NamedQuery(name = "Order.findByNumber", query = "SELECT o FROM Order o WHERE o.number = :number")})
public class Order extends Resource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3725014293364656727L;

	@NotNull
	@Column(name = "NUMBER", unique=true, nullable=false) 
	private Long number = 0l;
	
	@NotNull
	@Size(min = 3)
	@Column(name = "CUSTOMER")
	private String customer = "";
	
	@NotNull
	@Size(min = 5)
	@Column(name = "REPRESENTATIVE")
	private String representative = "";
	
	@NotNull
    @Temporal(value=TemporalType.DATE)
	@Column(name = "CREATED")
	private Date created = new Date();
	
	@NotNull
    @Temporal(value=TemporalType.DATE)
	@Column(name = "DELIVERY")
	private Date delivery = new Date();
	
	@NotNull
	@Size(min = 8, max = 8)
	@Column(name = "QUOTATION")
	private String quotation = "";
	
	@Column(name = "COMMENT")
	private String comment = "";
	
	@Column(name = "FINISH")
	private String finish = "";
	
	@Column(name = "PROJECT")
	private Long project = 0l;
	
    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<OrderItem> items = new ArrayList<OrderItem>();

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

	public String getQuotation() {
		return quotation;
	}

	public void setQuotation(String quotation) {
		this.quotation = quotation;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}

	public Long getProject() {
		return project;
	}

	public void setProject(Long project) {
		this.project = project;
	}

	@XmlTransient
	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = (List<OrderItem>) items;
	}

}
