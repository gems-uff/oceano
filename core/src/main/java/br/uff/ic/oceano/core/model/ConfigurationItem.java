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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author marapao
 */
@NamedQueries({
    @NamedQuery(name = "ConfigurationItem.getAll", query = "select ci from ConfigurationItem ci order by ci.name ")
})
@Entity
@Table(name = "ConfigurationItem")
@SequenceGenerator(name = "ConfigurationItem_seq", sequenceName = "ConfigurationItem_seq")
public class ConfigurationItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ConfigurationItem_seq")
    private Long id;
    @Column(nullable=false)
    private String trunkPath;
    @Column(nullable = false)
    private String branchPath;
    @Column(nullable = false)
    private String baseUrl;
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional=false)
    @JoinColumn(name="idrepository")
     private Repository repository;

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
     * @return the trunkPath
     */
    public String getTrunkPath() {
        return trunkPath;
    }

    /**
     * @param trunkPath the trunkPath to set
     */
    public void setTrunkPath(String trunkPath) {
        this.trunkPath = trunkPath;
    }

    /**
     * @return the branchPath
     */
    public String getBranchPath() {
        return branchPath;
    }

    /**
     * @param branchPath the branchPath to set
     */
    public void setBranchPath(String branchPath) {
        this.branchPath = branchPath;
    }

    /**
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
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
     * @return the repository
     */
    public Repository getRepository() {
        return repository;
    }

    /**
     * @param repository the repository to set
     */
    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    @Override
    public String toString() {
        return name + " - " + baseUrl;
    }


    
}
