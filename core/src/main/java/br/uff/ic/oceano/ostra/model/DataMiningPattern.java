/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.util.NumberUtil;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "DataMiningPattern.getAll", query = "select dmp from DataMiningPattern dmp order by suport desc, confidence desc")
})
@Entity
@Table(name = "ostra_DataMiningPattern")
@SequenceGenerator(name = "ostra_DataMiningPattern_seq", sequenceName = "ostra_DataMiningPattern_seq")
public class DataMiningPattern implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ostra_DataMiningPattern_seq")
    private Long id;
    @Lob
    private String pattern;
    private Double count;
    private Double confidence;
    private Double conviction;
    private Double lift;
    private Double leverage;
    @ManyToOne
    @JoinColumn(name = "idDataMiningResult")
    private DataMiningResult dataMiningResult;

    @Override
    public String toString() {
        return pattern + " Count=" + getCountAsString() + ", Support=" + getSupportAsString() + ", Conf=" + getConfidenceAsString() + ", Conv=" + getConvictionAsString() + ", Lift=" + getLiftAsString() + ", Lev=" + getLeverageAsString();
    }

    public int getSize() {
        if (pattern == null) {
            System.out.println("=0");
            return 0;
        }
        String temp = pattern.replace("==>", "");
        int count = 0;
        //each attribute has an equal sign that separates the label from the value.
        //if we count the '='s we know the number of attributes.
        while (temp.contains("=")) {
            count++;
            temp = temp.replaceFirst("=", "");
        }

        return count;
    }

    public int getPrecedentSize() {
        if (pattern == null) {
            System.out.println("=0");
            return 0;
        }
        String temp = pattern.split("==>")[0];
        int count = 0;
        //each attribute has an equal sign that separates the label from the value.
        //if we count the '='s we know the number of attributes.
        while (temp.contains("=")) {
            count++;
            temp = temp.replaceFirst("=", "");
        }

        return count;
    }

    public int getConsequentSize() {
        if (pattern == null) {
            System.out.println("=0");
            return 0;
        }
        String temp = pattern.split("==>")[1];
        int count = 0;
        //each attribute has an equal sign that separates the label from the value.
        //if we count the '='s we know the number of attributes.
        while (temp.contains("=")) {
            count++;
            temp = temp.replaceFirst("=", "");
        }

        return count;
    }

    public String getPrecedent() {
        if (pattern == null) {
            return "";
        }
        return pattern.split("==>")[0];
    }

    public String getConsequent() {
        if (pattern == null) {
            return "";
        }
        return pattern.split("==>")[1];
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the count
     */
    public Double getCount() {
        return count;
    }
    
    public String getCountAsString() {
        return NumberUtil.format(getCount());
    }

    /**
     * 
     * @return the support
     */
    public Double getSupport() {
        return NumberUtil.ratio(getCount(),dataMiningResult.getNumberOfInstances());
    }

    public String getSupportAsString() {
        return NumberUtil.format(getSupport());
    }

    /**
     * @param support the support to set
     */
    public void setCount(Double support) {
        this.count = support;
    }

    /**
     * @return the confidence
     */
    public Double getConfidence() {
        return confidence;
    }

    public String getConfidenceAsString() {
        return NumberUtil.format(getConfidence());
    }

    /**
     * @param confidence the confidence to set
     */
    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    /**
     * @return the dataMiningResult
     */
    public DataMiningResult getDataMiningResult() {
        return dataMiningResult;
    }

    /**
     * @param dataMiningResult the dataMiningResult to set
     */
    public void setDataMiningResult(DataMiningResult dataMiningResult) {
        this.dataMiningResult = dataMiningResult;
    }

    /**
     * @return the conviction
     */
    public Double getConviction() {
        return conviction;
    }

    public String getConvictionAsString() {
        return NumberUtil.format(getConviction());
    }

    /**
     * @param conviction the conviction to set
     */
    public void setConviction(Double conviction) {
        this.conviction = conviction;
    }

    /**
     * @return the lift
     */
    public Double getLift() {
        return lift;
    }

    public String getLiftAsString() {
        return NumberUtil.format(getLift());
    }

    /**
     * @param lift the lift to set
     */
    public void setLift(Double lift) {
        this.lift = lift;
    }

    /**
     * @return the leverage
     */
    public Double getLeverage() {
        return leverage;
    }

    public String getLeverageAsString() {
        return NumberUtil.format(getLeverage());
    }

    /**
     * @param leverage the leverage to set
     */
    public void setLeverage(Double leverage) {
        this.leverage = leverage;
    }

    public static String getAttribute(String attributeAndValue) {
        return attributeAndValue.trim().split("=")[0].substring("dAvg-".length());
    }

    public static String getValue(String attributeAndValue) {
        return attributeAndValue.trim().split("=")[1].split(" ")[0];
    }

    public double getMetricValue(String ruleMetricName) {
        try {
            Method m = this.getClass().getMethod("get" + ruleMetricName.substring(0, 1).toUpperCase() + ruleMetricName.substring(1));
            return (Double) m.invoke(this);

        } catch (IllegalAccessException ex) {
            Logger.getLogger(DataMiningPattern.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DataMiningPattern.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(DataMiningPattern.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(DataMiningPattern.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(DataMiningPattern.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Double.NaN;
    }

    public void setCount(double ruleSupport) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
