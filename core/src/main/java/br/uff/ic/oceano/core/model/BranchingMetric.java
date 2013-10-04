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
    @NamedQuery(name = "BranchingMetric.getAll", query = "select b from BranchingMetric b order by b.name "),
    @NamedQuery(name = "BranchingMetric.getByName", query = "select b from BranchingMetric b where b.name = ?")
})
@Entity
@Table(name = "polvo_BranchingMetric")
@SequenceGenerator(name = "BranchingMetric_seq", sequenceName = "BranchingMetric_seq")
public class BranchingMetric implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "BranchingMetric_seq")
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;


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

    public String toString() {
        return this.name;
    }
}
