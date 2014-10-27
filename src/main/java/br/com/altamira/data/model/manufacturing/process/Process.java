package br.com.altamira.data.model.manufacturing.process;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.data.serialize.JSonViews;
import br.com.altamira.data.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author alessandro.holanda
 */
@Entity
@Table(name = "MN_PROCESS")
public class Process extends br.com.altamira.data.model.Process {

    /**
     *
     */
    private static final long serialVersionUID = 5549369785798723928L;
    
//    @Id
//    @SequenceGenerator(name = "ProcessSequence", sequenceName = "MN_PROCESS_SEQ", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProcessSequence")
//    @Column(name = "ID")
//    private Long id;
    
    @NotNull
    @Size(min = 10)
    @Column(name = "CODE", columnDefinition = "nvarchar2(255)", unique=true, nullable=false)
    private String code;

    @NotNull
    @Size(min = 10)
    @Column(name = "DESCRIPTION", columnDefinition = "nvarchar2(255)")
    private String description;

    @Column(name = "COLOR", columnDefinition = "nvarchar2(255)")
    private String color;

    @Min(0)
    @Column(name = "WEIGHT")
    private float weight;

    @Min(0)
    @Column(name = "LENGTH")
    private float length;

    @Min(0)
    @Column(name = "WIDTH")
    private float width;

    @Column(name = "FINISH", columnDefinition = "nvarchar2(255)")
    private String finish;

    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Revision> revision = new ArrayList<Revision>();

    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Operation> operation = new ArrayList<Operation>();
    
    /**
     *
     */
    public Process() {
        this.operation = new ArrayList<>();
    }
    
    /**
     *
     * @param id
     * @param code
     * @param description
     */
    public Process(long id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
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
    public String getColor() {
        return color;
    }

    /**
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     *
     * @return
     */
    public float getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public float getLength() {
        return length;
    }

    /**
     *
     * @param length
     */
    public void setLength(float length) {
        this.length = length;
    }

    /**
     *
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public String getFinish() {
        return finish;
    }

    /**
     *
     * @param finish
     */
    public void setFinish(String finish) {
        this.finish = finish;
    }

    /**
     *
     * @return
     */
    public List<Revision> getRevision() {
        return revision;
    }

    /**
     *
     * @param revision
     */
    public void setRevision(List<Revision> revision) {
        this.revision = revision;
    }

    /**
     *
     * @return
     */
    public List<Operation> getOperation() {
        return operation;
    }

    /**
     *
     * @param operation
     */
    public void setOperation(List<Operation> operation) {
        this.operation = operation;
    }

}
