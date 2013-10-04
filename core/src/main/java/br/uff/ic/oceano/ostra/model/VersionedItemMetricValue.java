/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.core.model.Metric;
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

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "VersionedItemMetricValue.getByRevisionAndMetric", query = "select vimv from VersionedItemMetricValue vimv where vimv.versionedItem.revision = ? and vimv.metric = ?"),
    @NamedQuery(name = "VersionedItemMetricValue.getByRevision", query = "select vimv from VersionedItemMetricValue vimv where versionedItem.revision = ? "),
    @NamedQuery(name = "VersionedItemMetricValue.getNewestByItemAndMetricUntilRevision",
    query = "select vimv "
    + " from VersionedItemMetricValue vimv "
    + " where vimv.versionedItem.item = ? "
    + "     and vimv.metric = ? "
    + "     and vimv.versionedItem.revision.number <= ? "
    + " order by vimv.versionedItem.revision.number desc")
})
@Entity
@Table(name = "ostra_VersionedItemMetricValue")
@SequenceGenerator(name = "VersionedItemMetricValue_seq", sequenceName = "VersionedItemMetricValue_seq")
public class VersionedItemMetricValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "VersionedItemMetricValue_seq")
    private Long id;
    private Double doubleValue;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idVersionedItem")
    private VersionedItem versionedItem;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMetric")
    private Metric metric;

    @Override
    public String toString() {
        return this.metric.getName() + "(" + versionedItem.getItem().getPath() + ") = " + this.doubleValue;
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

//    /**
//     * @return the value
//     */
//    public String getValue() {
//        return value;
//    }
//
//    /**
//     * @param value the value to set
//     */
//    public void setValue(String value) {
//        this.value = value;
//    }
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

    public void setDoubleValue(Double d) {
        this.doubleValue = d;
    }

    /**
     * @return the versionedItem
     */
    public VersionedItem getVersionedItem() {
        return versionedItem;
    }

    /**
     * @param versionedItem the versionedItem to set
     */
    public void setVersionedItem(VersionedItem versionedItem) {
        this.versionedItem = versionedItem;
    }

    /**
     * @return the doubleValue
     */
    public Double getDoubleValue() {
        return doubleValue;
    }
}
