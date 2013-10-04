/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Rafael Santos
 */
@NamedQueries({
    @NamedQuery(name = "BranchingModel.getAll", query = "select b from BranchingModel b order by b.name "),
    @NamedQuery(name = "BranchingModel.getByName", query = "select b from BranchingModel b where b.name = ?")
})
@Entity
@Table(name = "polvo_BranchingModel")
@SequenceGenerator(name = "BranchingModel_seq", sequenceName = "BranchingModel_seq")
public class BranchingModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "BranchingModel_seq")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private int directionMerge;

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

    public int getDirectionMerge() {
        return directionMerge;
    }

    public void setDirectionMerge(int directionMerge) {
        this.directionMerge = directionMerge;
    }

    public String toString() {
        return this.name;
    }
}
