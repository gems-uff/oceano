/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "ProjectUser.getByProjectAndOceanoUser", query = "select pu from ProjectUser pu where pu.project = ? and pu.oceanoUser = ?"),
    @NamedQuery(name = "ProjectUser.getByOceanoUser", query = "select pu from ProjectUser pu where pu.oceanoUser = ?"),
    @NamedQuery(name = "ProjectUser.getByProjectAndLogin", query = "select pu from ProjectUser pu where pu.project = ? and pu.login = ?")
})
@Entity
@Table(name = "ProjectUser", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"idProject", "idOceanoUser"})})
@SequenceGenerator(name = "ProjectUser_seq", sequenceName = "ProjectUser_seq")
public class ProjectUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ProjectUser_seq")
    private Long id;
    private String login;
    private String password;
    private boolean anonymous = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProject")
    private SoftwareProject project;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idOceanoUser")
    private OceanoUser oceanoUser;

    public ProjectUser(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public ProjectUser(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public ProjectUser(String login, String password, SoftwareProject project, OceanoUser oceanoUser) {
        this.login = login;
        this.password = password;
        this.project = project;
        this.oceanoUser = oceanoUser;
    }

    public ProjectUser() {
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

    /**
     * @return the project
     */
    public SoftwareProject getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(SoftwareProject project) {
        this.project = project;
    }

    /**
     * @return the user
     */
    public OceanoUser getOceanoUser() {
        return oceanoUser;
    }

    /**
     * @param user the user to set
     */
    public void setOceanoUser(OceanoUser oceanoUser) {
        this.oceanoUser = oceanoUser;
    }

    @Override
    public String toString() {
        return "User: " + oceanoUser + " , Project: " + project;
    }

    /**
     * @return the anonymous
     */
    public boolean isAnonymous() {
        return anonymous;
    }

    /**
     * @param anonymous the anonymous to set
     */
    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
