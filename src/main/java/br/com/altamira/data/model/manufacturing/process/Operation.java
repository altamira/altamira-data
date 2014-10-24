package br.com.altamira.data.model.manufacturing.process;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 *
 * @author alessandro.holanda
 */
@Entity
@Table(name = "MN_OPERATION")
public class Operation extends br.com.altamira.data.model.Operation {

    /**
     *
     */
    private static final long serialVersionUID = 4778350055794788171L;

//    @Id
//    @SequenceGenerator(name = "OperationSequence", sequenceName = "MN_OPERATION_SEQ", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OperationSequence")
//    @Column(name = "ID")
//    private Long id;

    @JsonIgnore
    @JoinColumn(name = "PROCESS", referencedColumnName = "ID")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Process process;

    @NotNull
    @Min(1)
    @Column(name = "SEQ")
    private int sequence;

    @NotNull
    @Size(min = 3)
    @Column(name = "NAME", columnDefinition = "nvarchar2(255)")
    private String name;

    @NotNull
    @Size(min = 1)
    @Column(name = "DESCRIPTION", columnDefinition = "nvarchar2(500)")
    private String description;

    @Column(name = "SKETCH", columnDefinition = "nvarchar2(500)")
    private String sketch;

    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Consume> consume = new ArrayList<>();

    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operation", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Produce> produce = new ArrayList<>();

    public Operation() {
    }

    public Operation(long id, int sequence, String name) {
        this.id = id;
        this.sequence = sequence;
        this.name = name;
    }

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
    public Process getProcess() {
        return process;
    }

    /**
     *
     * @param process
     */
    public void setProcess(Process process) {
        this.process = process;
    }

    /**
     *
     * @param consume
     */
    public void setConsume(List<Consume> consume) {
        this.consume = consume;
    }

    /**
     *
     * @return
     */
    public int getSequence() {
        return sequence;
    }

    /**
     *
     * @param sequence
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
    public String getSketch() {
        return sketch;
    }

    /**
     *
     * @param sketch
     */
    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    /**
     *
     * @return
     */
    public List<Consume> getConsume() {
        return consume;
    }

    /**
     *
     * @param useconsume
     */
    public void setUseconsume(List<Consume> useconsume) {
        this.consume = useconsume;
    }

    /**
     *
     * @return
     */
    public List<Produce> getProduce() {
        return produce;
    }

    /**
     *
     * @param produce
     */
    public void setProduce(List<Produce> produce) {
        this.produce = produce;
    }

}
