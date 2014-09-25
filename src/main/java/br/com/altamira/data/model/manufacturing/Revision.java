package br.com.altamira.data.model.manufacturing;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "MN_REVISION")
public class Revision extends br.com.altamira.data.model.Resource {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6900819206533333287L;
	
	@NotNull
	@Temporal(value=TemporalType.DATE)
	@Column(name = "CHANGE_DATE")
	private Date date;
	
	@NotNull
	@Size(min=5)
	@Column(name = "CHANGE_BY", columnDefinition = "nvarchar2(255)")
	private String by;

	@JsonIgnore
	@JoinColumn(name = "PROCESS", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Process	process;

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getBy() {
		return by;
	}
	
	public void setBy(String by) {
		this.by = by;
	}
	
}
