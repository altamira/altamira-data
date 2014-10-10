/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.altamira.data.rest.purchasing.request;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author PARTH
 */
public class RequestReportData {
    
    private Long id;//0
    private String lamination;//1
    private String treatment;//2
    private BigDecimal thickness;//3
    private BigDecimal width;//4
    private BigDecimal length;//5
    private BigDecimal weight;//6
    private Date arrivalDate;//7

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

    /**
     *
     * @return
     */
    public String getLamination() {
        return lamination;
    }

    /**
     *
     * @param lamination
     */
    public void setLamination(String lamination) {
        this.lamination = lamination;
    }

    /**
     *
     * @return
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     *
     * @param treatment
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     *
     * @return
     */
    public BigDecimal getThickness() {
        return thickness;
    }

    /**
     *
     * @param thickness
     */
    public void setThickness(BigDecimal thickness) {
        this.thickness = thickness;
    }

    /**
     *
     * @return
     */
    public BigDecimal getWidth() {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public BigDecimal getLength() {
        return length;
    }

    /**
     *
     * @param length
     */
    public void setLength(BigDecimal length) {
        this.length = length;
    }

    /**
     *
     * @return
     */
    public BigDecimal getWeight() {
        return weight;
    }

    /**
     *
     * @param weight
     */
    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public Date getArrivalDate() {
        return arrivalDate;
    }

    /**
     *
     * @param arrivalDate
     */
    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    
}
