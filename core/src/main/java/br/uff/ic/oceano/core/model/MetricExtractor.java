/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author dheraclio
 *
 */
@NamedQueries({
    @NamedQuery(name = "MetricExtractor.getAll", query = "select me from MetricExtractor me "),
    @NamedQuery(name = "MetricExtractor.getMetricExtractorsByMetric", query = "select me from MetricExtractor me where me.metric = ? ")
})
@Entity
@Table(name = "MetricExtractor")
@SequenceGenerator(name = "MetricExtractor_seq", sequenceName = "MetricExtractor_seq")
public class MetricExtractor implements Serializable {

    //
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MetricExtractor_seq")
    private Long id;
    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    @JoinColumn(name = "idMetric",nullable=false)
    private Metric metric;
    @Column(nullable = false)
    private String lang; //LANGUAGE is sql reserved word
    @Column(nullable = false)
    private String metricExtractorClass;

    /**
     * @return the language
     */
    public String getLanguage() {
        return lang;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.lang = language;
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
     * @return the metricExtractorClass
     */
    public String getMetricExtractorClass() {
        return metricExtractorClass;
    }

    /**
     * @param metricExtractorClass the metricExtractorClass to set
     */
    public void setMetricExtractorClass(String metricExtractorClass) {
        this.metricExtractorClass = metricExtractorClass;
    }

    @Override
    public String toString() {
        return getMetric() + " extractor for " + getLanguage() + " is " + getMetricExtractorClass();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetricExtractor other = (MetricExtractor) obj;
        return (((this.getMetric() == null) ? (other.getMetric() == null) : this.getMetric().equals(other.getMetric()))
                && ((this.getLanguage() == null) ? (other.getLanguage() == null) : this.getLanguage().equals(other.getLanguage()))
                && ((this.getMetricExtractorClass() == null) ? (other.getMetricExtractorClass() == null) : this.getMetricExtractorClass().equals(other.getMetricExtractorClass()))
                );
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.getMetric() != null ? this.getMetric().hashCode() : 0);
        hash = 83 * hash + (this.getLanguage() != null ? this.getLanguage().hashCode() : 0);
        hash = 83 * hash + (this.getMetricExtractorClass() != null ? this.getMetricExtractorClass().hashCode() : 0);
        return hash;
    }
}
