/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.model;

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
 * @author marapao
 */
@NamedQueries({
    @NamedQuery(name = "ConfiguracaoVerificacao.get", query = "select c from ConfiguracaoVerificacao c")
})
@Entity
@Table(name = "ourico_ConfiguracaoVerificacao")
@SequenceGenerator(name = "seq_ConfiguracaoVerificacao", sequenceName = "seq_ConfiguracaoVerificacao")
public class ConfiguracaoVerificacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_ConfiguracaoVerificacao")
    private Long id;
    @Column(nullable=false)
    private String workspacePath;
    @Column(nullable=false)
    private String workspaceAutobranchDir;
    @Column(nullable=false)
    private String workspaceProtectedDir;
    @Column(nullable=false)
    private String email;
    @Column(nullable=false)
    private String senhaEmail;
    @Column(nullable=false)
    private String mvnSettings;
    @Column(nullable=false)
    private String mvnRepository;

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
     * @return the workspacePath
     */
    public String getWorkspacePath() {
        return workspacePath;
    }

    /**
     * @param workspacePath the workspacePath to set
     */
    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    /**
     * @return the workspaceAutobranchDir
     */
    public String getWorkspaceAutobranchDir() {
        return workspaceAutobranchDir;
    }

    /**
     * @param workspaceAutobranchDir the workspaceAutobranchDir to set
     */
    public void setWorkspaceAutobranchDir(String workspaceAutobranchDir) {
        this.workspaceAutobranchDir = workspaceAutobranchDir;
    }

    /**
     * @return the workspaceProtectedDir
     */
    public String getWorkspaceProtectedDir() {
        return workspaceProtectedDir;
    }

    /**
     * @param workspaceProtectedDir the workspaceProtectedDir to set
     */
    public void setWorkspaceProtectedDir(String workspaceProtectedDir) {
        this.workspaceProtectedDir = workspaceProtectedDir;
    }

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
     * @return the senhaEmail
     */
    public String getSenhaEmail() {
        return senhaEmail;
    }

    /**
     * @param senhaEmail the senhaEmail to set
     */
    public void setSenhaEmail(String senhaEmail) {
        this.senhaEmail = senhaEmail;
    }

    /**
     * @return the mvnSettings
     */
    public String getMvnSettings() {
        return mvnSettings;
    }

    /**
     * @param mvnSettings the mvnSettings to set
     */
    public void setMvnSettings(String mvnSettings) {
        this.mvnSettings = mvnSettings;
    }

    /**
     * @return the mvnRepository
     */
    public String getMvnRepository() {
        return mvnRepository;
    }

    /**
     * @param mvnRepository the mvnRepository to set
     */
    public void setMvnRepository(String mvnRepository) {
        this.mvnRepository = mvnRepository;
    }

   
    
}
