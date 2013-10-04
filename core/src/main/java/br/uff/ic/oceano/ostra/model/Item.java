/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "Item.getByPathAndProject", query = "select vi.item from VersionedItem vi where vi.item.path = ? and vi.revision.project = ?"),
    @NamedQuery(name = "Item.getByProject", query = "select distinct vi.item from VersionedItem vi where vi.revision.project = ? order by vi.item.path")
})
@Entity
@Table(name = "ostra_Item")
@SequenceGenerator(name = "Item_seq", sequenceName = "Item_seq")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Item_seq")
    private Long id;
    @Column(nullable = false)
    private String path;
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<VersionedItem> versionedItems;

    public Item() {
    }

    
    public Item(String filePath) {
        this.path = filePath;
    }

    @Override
    public String toString() {
        return this.path;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.path != null ? this.path.hashCode() : 0);
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
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the versionedItem
     */
    public Set<VersionedItem> getVersionedItems() {
        return versionedItems;
    }

    /**
     * @param versionedItem the versionedItem to set
     */
    public void setVersionedItems(Set<VersionedItem> versionedItem) {
        this.versionedItems = versionedItem;
    }
    }
