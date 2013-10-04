/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Kann
 */
@NamedQueries({

    @NamedQuery(name="MetricQualityAttribute.getAll",query="select t from MetricQualityAttribute t"),
    @NamedQuery(name="MetricQualityAttribute.getByQualityAttribute",query="select t from MetricQualityAttribute t where t.qualityAttribute = ? ")

})

@Entity
@Table(name="espada_MetricQualityAttribute", uniqueConstraints={@UniqueConstraint(columnNames={"idQualityAttribute", "idMetric"})})
@SequenceGenerator(name = "espada_MetricQualityAttribute_seq", sequenceName = "espada_MetricQualityAttribute_seq")
public class MetricQualityAttribute implements Serializable{

    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="espada_MetricQualityAttribute_seq")
    private Long id;

    
    @ManyToOne
    @JoinColumn(name="idQualityAttribute")
    private QualityAttribute qualityAttribute;

    private float factor;

    @ManyToOne
    @JoinColumn(name="idMetric")
    private Metric metric;

    @Transient
    private Double metricValue;

   

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof MetricQualityAttribute)){
            return false;
        }
        return id.equals(((MetricQualityAttribute)obj).id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
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
     * @return the qualityAttribute
     */
    public QualityAttribute getQualityAttribute() {
        return qualityAttribute;
    }

    /**
     * @param qualityAttribute the qualityAttribute to set
     */
    public void setQualityAttribute(QualityAttribute qualityAttribute) {
        this.qualityAttribute = qualityAttribute;
    }

    /**
     * @return the metric
     */
    public Metric getMetric() {
        return metric;
    }

    /**
     * @param metric the metric to set
     */
    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    /**
     * @return the factor
     */
    public float getFactor() {
        return factor;
    }

    /**
     * @param factor the factor to set
     */
    public void setFactor(float factor) {
        this.factor = factor;
    }

    /**
     * @return the metricValue
     */
    public Double getMetricValue() {
        return metricValue;
    }

    /**
     * @param metricValue the metricValue to set
     */
    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

}
