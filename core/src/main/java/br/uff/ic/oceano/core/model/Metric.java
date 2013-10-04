/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 * 
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "Metric.getByNome", query = "select m from Metric m where m.name = ? "),
    @NamedQuery(name = "Metric.getByAcronym", query = "select m from Metric m where m.acronym = ? "),
    @NamedQuery(name = "Metric.getAll", query = "select m from Metric m order by m.name "),
    //hibernate 3.5.0 error on order by
    //@NamedQuery(name = "Metric.getMetricsByProjectRevisions", query = "select distinct mv.metric from MetricValue mv where mv.revision.project = ? order by mv.metric.name"),
    //@NamedQuery(name = "Metric.getMetricsByProjectVersionedItems", query = "select distinct mv.metric from VersionedItemMetricValue mv where mv.versionedItem.revision.project = ? order by mv.metric.name")
    @NamedQuery(name = "Metric.getMetricsByProjectRevisions", query = "select distinct mv.metric from MetricValue mv where mv.revision.project = ?"), 
    @NamedQuery(name = "Metric.getMetricsByProjectVersionedItems", query = "select distinct mv.metric from VersionedItemMetricValue mv where mv.versionedItem.revision.project = ?")
})
@Entity
@Table(name = "Metric")
@SequenceGenerator(name = "Metric_seq", sequenceName = "Metric_seq")
public class Metric implements Serializable, Comparable {

    public static final int TYPE_INTEGER = 0;
    public static final int TYPE_FLOAT = 1;
    public static final int TYPE_BOOLEAN = 2;
    //
    public static final int EXTRACTS_FROM_FILE = 0;
    public static final int EXTRACTS_FROM_PACKAGE = 1;
    public static final int EXTRACTS_FROM_PROJECT = 2;
    /////////////////////////////////////////////////


    //
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Metric_seq")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String acronym;
    @Column(length=10000)
    private String description;
    private int type = TYPE_INTEGER;
    private int extratcsFrom = EXTRACTS_FROM_FILE;
    private boolean extractsFromFont;
    private boolean preRelease;
    //Derived metric
    private String expression;
    private boolean derived;

    //Metric extractors for diferent languages
    @OneToMany(mappedBy = "metric", fetch = FetchType.EAGER )
    private Set<MetricExtractor> metricExtractors;

    public String getTypeAsString() {
        switch (this.type) {
            case TYPE_BOOLEAN:
                return "Boolean";
            case TYPE_FLOAT:
                return "Float";
            case TYPE_INTEGER:
                return "Integer";
        }
        return null;
    }

    public String getTargetAsString() {
        switch (this.extratcsFrom) {
            case EXTRACTS_FROM_FILE:
                return "File";
            case EXTRACTS_FROM_PACKAGE:
                return "Package";
            case EXTRACTS_FROM_PROJECT:
                return "Project";
        }
        return null;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the preRelease
     * @deprecated not used anywhere
     */
    public boolean isPreRelease() {
        return preRelease;
    }

    /**
     * @param preRelease the preRelease to set
     * @deprecated not used anywhere
     */
    public void setPreRelease(boolean preRelease) {
        this.preRelease = preRelease;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Metric other = (Metric) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    /**
     * @return the extracsFrom
     */
    public int getExtratcsFrom() {
        return extratcsFrom;
    }

    /**
     * @param extracsFrom the extracsFrom to set
     */
    public void setExtratcsFrom(int extracsFrom) {
        this.extratcsFrom = extracsFrom;
    }

    /**
     * @return the extractsFromFontFile
     */
    public boolean isExtractsFromFont() {
        return extractsFromFont;
    }

    /**
     * @param extractsFromFont the extractsFromFont to set
     */
    public void setExtractsFromFont(boolean extractsFromFont) {
        this.extractsFromFont = extractsFromFont;
    }

    public int compareTo(Object o) {
        Metric m = (Metric) o;
        return this.name.compareTo(m.name);
    }

    /**
     * @return the acronym
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * @param acronym the acronym to set
     */
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * @return the derived
     */
    public boolean isDerived() {
        return derived;
    }

    /**
     * @param derived the derived to set
     */
    public void setDerived(boolean derived) {
        this.derived = derived;
    }

    /**
     * @return the extrators
     */
    public Set<MetricExtractor> getMetricExtractors() {
        return metricExtractors;
    }

    /**
     * @param extrators the extrators to set
     */
    public void setMetricExtractors(Set<MetricExtractor> metricExtractors) {
        this.metricExtractors = metricExtractors;
    }

    public boolean isFromProject() {
        return getExtratcsFrom() == EXTRACTS_FROM_PROJECT;
    }
}
