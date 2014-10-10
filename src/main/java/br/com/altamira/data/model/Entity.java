package br.com.altamira.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alessandro.holanda
 */
@javax.persistence.MappedSuperclass
public abstract class Entity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7631337160713217378L;

	// Guarantee unique id for all entities
	@Id
    @SequenceGenerator(name = "EntitySequence", sequenceName = "ENTITY_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EntitySequence")
    @Column(name = "ID")
    private Long id;
	
	@NotNull
	@Column(name = "LAST_MODIFIED")
	Long lastModified = System.currentTimeMillis();
    
    /**
     *
     * @return
     */
    public Long getId() {
		return id;
	}

    /**
     *
     * @param id
     */
    public void setId(Long id) {
		this.id = id;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are
        // not set
        if (!(object instanceof Entity)) {
            return false;
        }
        Entity other = (Entity) object;
        if ((this.id == null && other.getId() != null)
                || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().toString() + "[ id=" + id + " ]";
    }

    /**
     *
     * @return
     */
    public static long getSerialversionuid() {
		return serialVersionUID;
	}  

	@PreUpdate
	void updateModificationTimestamp() {
	 lastModified = System.currentTimeMillis();
	}
}
