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
 * @author Heliomar
 */
@NamedQueries({
    @NamedQuery(name="OceanoUser.getByLogin",query="select u from OceanoUser u where u.login= ?"),
    @NamedQuery(name="OceanoUser.getAll",query="select u from OceanoUser u order by u.login")
})

@Entity
@Table(name="OceanoUser")
@SequenceGenerator(name = "OceanoUser_seq", sequenceName = "OceanoUser_seq")
public class OceanoUser implements Serializable{

    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="OceanoUser_seq")
    private Long id;
    private String name;
    @Column(unique=true)
    private String login;
    private String password;
    private String email;

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return login;
    }


}
