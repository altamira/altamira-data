/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.model.purchasing;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
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

/**
 *
 * @author Alessandro
 */
@Entity
@Table(name = "PR_REQUEST")
@NamedQueries({
    @NamedQuery(name = "Request.list", query = "SELECT r FROM Request r"),
    @NamedQuery(name = "Request.findById", query = "SELECT r FROM Request r WHERE r.id = :id"),
	@NamedQuery(name = "Request.current", query = "SELECT r FROM Request r WHERE r.id = (SELECT MAX(rr.id) FROM Request rr WHERE rr.sent IS NULL)"),
	@NamedQuery(name = "Request.items", query = "SELECT r FROM RequestItem r WHERE r.request = :requestId")})
public class Request extends Resource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2066957562882901235L;

	@NotNull
    @Basic(optional = false)
    @Column(name = "CREATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @NotNull
    @Size(min = 3, max = 50)
    @Basic(optional = false)
    @Column(name = "CREATOR", columnDefinition = "nvarchar2(255)")
    private String creator;
    
    //@JsonSerialize(using = DateSerializer.class)
    @Column(name = "SENT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sent;
    
    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "request", fetch = FetchType.LAZY, orphanRemoval = true)
    //@JoinColumn(name="REQUEST")
    private Set<RequestItem> item = new HashSet<RequestItem>();
    
    /*@ManyToOne(optional = true, fetch = FetchType.LAZY)
    private QuotationRequest quotationRequest;*/

    public Request() {
    }

    public Request(Date created, String creator) {
        this.created = created;
        this.creator = creator;
        this.sent = null;
    }
    
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    @XmlTransient
    public Set<RequestItem> getItem() {
        return item;
    }

    public void setItem(Set<RequestItem> items) {
        this.item = items;
    }

    /*@XmlTransient
    public QuotationRequest getQuotationRequest() {
        return quotationRequest;
    }

    public void setQuotationRequest(QuotationRequest quotationRequest) {
        this.quotationRequest = quotationRequest;
    }*/

}
