/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Heliomar
 */
@NamedQueries({

    @NamedQuery(name="QualityAttribute.getByName",query="select t from QualityAttribute t where t.name = ? "),
    @NamedQuery(name="QualityAttribute.getByIdWithMetrics",query="select t from QualityAttribute t left outer join fetch t.metricQualityAttributes ma where t.id = ? "),
    @NamedQuery(name="QualityAttribute.getAll",query="select t from QualityAttribute t order by t.name ")
})

@Entity
@Table(name="espada_QualityAttribute")
@SequenceGenerator(name = "espada_QualityAttribute_seq", sequenceName = "espada_QualityAttribute_seq")
public class QualityAttribute implements Serializable{

    public static final String NAME_REUSABILITY = "REUSABILITY";
    public static final String NAME_FLEXIBILITY = "FLEXIBILITY";
    public static final String NAME_UNDERSTANDIBILITY = "UNDERSTANDIBILITY";
    public static final String NAME_FUNCIONALITY = "FUNCIONALITY";
    public static final String NAME_EXTENDIBILITY = "EXTENDIBILITY";
    public static final String NAME_EFFECTIVENESS = "EFFECTIVENESS";

    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="espada_QualityAttribute_seq")
    private Long id;

    private String name;
    private String descricao;
    @Transient
    private Double currentValue;
    @OneToMany(cascade=CascadeType.ALL, mappedBy="qualityAttribute")
    private List<MetricQualityAttribute> metricQualityAttributes;

    @Transient
    private Map<String, Metric> metricsMap;

    public Map<String, Metric> getMetricsMap() {
        if(metricsMap == null){
            metricsMap = new HashMap<String, Metric>();
            for (MetricQualityAttribute metricQualityAttribute : metricQualityAttributes) {
                metricsMap.put(metricQualityAttribute.getMetric().getAcronym(), metricQualityAttribute.getMetric());
            }
        }
        return metricsMap;
    }
    /**
     * @return the nome
     */
    public String getName() {
        return name;
    }

    /**
     * @param nome the nome to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof QualityAttribute)){
            return false;
        }
        return id.equals(((QualityAttribute)obj).id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return name;
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
     * @return the metricQualityAttributes
     */
    public List<MetricQualityAttribute> getMetricQualityAttributes() {
        return metricQualityAttributes;
    }

    /**
     * @param metricQualityAttributes the metricQualityAttributes to set
     */
    public void setMetricQualityAttributes(List<MetricQualityAttribute> metricQualityAttributes) {
        this.metricQualityAttributes = metricQualityAttributes;
    }

    /**
     * @return the currentValue
     */
    public Double getCurrentValue() {
        return currentValue;
    }

    /**
     * @param currentValue the currentValue to set
     */
    public void setCurrentValue(Double currentValue) {
        this.currentValue = currentValue;
    }

}
