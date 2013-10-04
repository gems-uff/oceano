/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author DanCastellani
 */
@Entity
@Table(name = "MetricValue",
uniqueConstraints =
@UniqueConstraint(columnNames = {"idRevision", "idMetric", "delta"}))
@NamedQueries({
    @NamedQuery(name = "MetricValue.getMetricValueId", query = "select mv.id from MetricValue mv where mv.revision = ? and mv.metric = ? and mv.delta = ?"),
    @NamedQuery(name = "MetricValue.getAll", query = "select m from MetricValue m"),
    @NamedQuery(name = "MetricValue.getByRevision", query = "select mv from MetricValue mv where mv.revision = ?"),
    @NamedQuery(name = "MetricValue.getByRevisionMetricAndDelta", query = "select mv from MetricValue mv where mv.revision = ? and mv.metric = ? and mv.delta = ?"),
    @NamedQuery(name = "MetricValue.getValuesByProjectAndMetric", query = "select mv from MetricValue mv where mv.revision.project = ? and mv.metric = ? and mv.delta = ? order by mv.revision.number"),
    @NamedQuery(name = "MetricValue.getAllSortedByConfigurationAndMetric", query = "select mv from MetricValue mv join mv.metric me order by mv.revision.number, me.name")
})
@SequenceGenerator(name = "MetricValue_seq", sequenceName = "MetricValue_seq")
public class MetricValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MetricValue_seq")
    private Long id;
    private Double doubleValue;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idRevision")
    private Revision revision;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMetric")
    private Metric metric;
    private boolean delta = false;

    public static MetricValue createMetricValueWithZero(Revision revision, Metric metric) {
        MetricValue mv = new MetricValue();
        mv.setDoubleValue(0D);
        mv.setRevision(revision);
        mv.setMetric(metric);
        return mv;
    }

    public MetricValue() {
    }

    public MetricValue(Revision revision, Metric metric, Double metricValue) {
        this.revision = revision;
        this.metric = metric;
        this.doubleValue = metricValue;
    }

    @Override
    public String toString() {
        return this.metric.getName() + "(" + revision.getProject().getConfigurationItem().getName() + ":" + revision.getNumber() + ") = " + this.doubleValue;
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
     * @return the revision
     */
    public Revision getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Revision revision) {
        this.revision = revision;
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
     * @return the delta
     */
    public boolean isDelta() {
        return delta;
    }

    /**
     * @param delta the delta to set
     */
    public void setDelta(boolean delta) {
        this.delta = delta;
    }

    /**
     * @return the doubleValue
     */
    public Double getDoubleValue() {
        return doubleValue;
    }

    /**
     * @param doubleValue the doubleValue to set
     */
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
