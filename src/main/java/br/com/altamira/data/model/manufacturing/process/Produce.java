package br.com.altamira.data.model.manufacturing.process;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author alessandro.holanda
 */
@Entity
@Table(name = "MN_PRODUCE")
public class Produce extends br.com.altamira.data.model.Relation {

    /**
     *
     */
    private static final long serialVersionUID = 8786534973807974496L;
    
//    @Id
//    @SequenceGenerator(name = "ProduceSequence", sequenceName = "MN_PRODUCE_SEQ", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProduceSequence")
//    @Column(name = "ID")
//    private Long id;
    
    @NotNull
    @Size(min = 10)
    @Column(name = "CODE", columnDefinition = "nvarchar2(255)")
    private String code;

    @NotNull
    @Size(min = 10)
    @Column(name = "DESCRIPTION", columnDefinition = "nvarchar2(255)")
    private String description;

    @NotNull
    @Min(0)
    @Column(name = "QUANTITY")
    private BigDecimal quantity;

    @NotNull
    @Size(min = 1)
    @Column(name = "UNIT", columnDefinition = "nvarchar2(255)")
    private String unit;

    @JsonIgnore
    @JoinColumn(name = "OPERATION", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Operation operation;

    /**
     * @return the id
     */
//    public Long getId() {
//        return id;
//    }

    /**
     * @param id the id to set
     */
//    public void setId(Long id) {
//        this.id = id;
//    }
    
    /**
     *
     * @return
     */
    public Operation getOperation() {
        return operation;
    }

    /**
     *
     * @param operation
     */
    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    /**
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     */
    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     *
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
