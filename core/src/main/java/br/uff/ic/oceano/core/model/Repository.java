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
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "Repository.getAll", query = "select r from Repository r order by r.name "),
    @NamedQuery(name = "Repository.getByName", query = "select r from Repository r where r.name = ?"),
    @NamedQuery(name = "Repository.getByProject", query = "select p.configurationItem.repository from SoftwareProject p where p = ? ")
})
@Entity
@Table(name = "Repository")
@SequenceGenerator(name = "Repository_seq", sequenceName = "Repository_seq")
public class Repository implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Repository_seq")
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
