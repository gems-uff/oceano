/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.core.model.*;
import br.uff.ic.oceano.ostra.model.*;
import br.uff.ic.oceano.core.model.Revision;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.tmatesoft.svn.core.SVNLogEntryPath;

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
//    @NamedQuery(name = "VersionedItem.getAll", query = "select vi from VersionedItem vi"),
    @NamedQuery(name = "VersionedItem.getByItemAndRevision", query = "select vi from VersionedItem vi where vi.item = ? and vi.revision = ?")
})
@Entity
@Table(name = "ostra_VersionedItem", uniqueConstraints = @UniqueConstraint(columnNames = {"idRevision", "idItem"}))
@SequenceGenerator(name = "VersionedItem_seq", sequenceName = "VersionedItem_seq")
public class VersionedItem implements Serializable {

    public static final char TYPE_ADDED = SVNLogEntryPath.TYPE_ADDED;
    public static final char TYPE_DELETED = SVNLogEntryPath.TYPE_DELETED;
    public static final char TYPE_MODIFIED = SVNLogEntryPath.TYPE_MODIFIED;
    public static final char TYPE_REPLACED = SVNLogEntryPath.TYPE_REPLACED;
    //
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "VersionedItem_seq")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idRevision")
    private Revision revision;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "idItem")
    private Item item;
    private char type;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "versionedItem")
    private Set<VersionedItemMetricValue> metricValues;

    @Override
    public String toString() {
        return this.type + " - " + this.item.getPath();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final VersionedItem other = (VersionedItem) obj;
        if (this.revision != other.revision && (this.revision == null || !this.revision.equals(other.revision))) {
            return false;
        }
        if (this.item != other.item && (this.item == null || !this.item.equals(other.item))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
     * @return the type
     */
    public char getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(char type) {
        this.type = type;
    }

    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the metricValues
     */
    public Set<VersionedItemMetricValue> getMetricValues() {
        return metricValues;
    }

    /**
     * @param metricValues the metricValues to set
     */
    public void setMetricValues(Set<VersionedItemMetricValue> metricValues) {
        this.metricValues = metricValues;
    }
}
