/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Rafael Santos
 */
@NamedQueries({
    @NamedQuery(name = "Branch.getAll", query = "select b from Branch b order by b.name "),
    @NamedQuery(name = "Branch.getByName", query = "select b from Branch b where b.name = ?"),
    @NamedQuery(name = "Branch.getbyProject", query = "select b from Branch b where b.project.id = ?"),
    @NamedQuery(name = "Branch.getbyProjectName", query = "select b from Branch b where b.project.id = ? and b.name = ?")
})
@Entity
@Table(name = "polvo_Branch")
@SequenceGenerator(name = "Branch_seq", sequenceName = "Branch_seq")
public class Branch implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Branch_seq")
    private Long id;
    @Column(nullable = false, unique = false)
    private String name;
    @OneToOne(fetch = FetchType.EAGER)
    private BranchingModel branchingModel;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idProject")
    private SoftwareProject project;

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

    public SoftwareProject getProject() {
        return project;
    }

    public void setProject(SoftwareProject project) {
        this.project = project;
    }

    public BranchingModel getBranchingModel() {
        return branchingModel;
    }

    public void setBranchingModel(BranchingModel branchingModel) {
        this.branchingModel = branchingModel;
    }

    public String toString() {
        return this.name;
    }
}
