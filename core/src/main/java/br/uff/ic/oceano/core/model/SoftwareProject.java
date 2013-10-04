/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import br.uff.ic.oceano.core.model.transiente.Language;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Kann
 */
@NamedQueries({
    @NamedQuery(name = "SoftwareProject.getByRepositoryUrl", query = "select p from SoftwareProject p where p.repositoryUrl = ?"),
    @NamedQuery(name = "SoftwareProject.getByName", query = "select p from SoftwareProject p where p.configurationItem.name = ?"),
    @NamedQuery(name = "SoftwareProject.getProjectsByOceanoUser", query = "select pu.project from ProjectUser pu where pu.oceanoUser = ? order by pu.project.configurationItem.name"),
    @NamedQuery(name = "SoftwareProject.getProjectToDetailById", query = "select distinct p from SoftwareProject p left outer join fetch p.revisions r left outer join fetch r.changedFiles vi where p.id = ?"),
    @NamedQuery(name = "SoftwareProject.getMavenProjectsByUser", query = "select pu.project from ProjectUser pu where pu.oceanoUser = ? and pu.project.mavenProject = true  order by pu.project.configurationItem.name")
})
@Entity
@Table(name = "SoftwareProject")
@SequenceGenerator(name = "SoftwareProject_seq", sequenceName = "SoftwareProject_seq")
public class SoftwareProject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SoftwareProject_seq")
    private Long id;
    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private Set<Revision> revisions;
    private boolean mavenProject = false;
    @Column(nullable = false, unique = true)
    private String repositoryUrl;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private Set<ProjectUser> projectUsers;
    @ManyToOne(optional = false)
    @JoinColumn(name = "idConfigurationItem")
    private ConfigurationItem configurationItem;
    @Column(nullable = false)
    private Language lang = Language.JAVA;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SoftwareProject other = (SoftwareProject) obj;
        if ((this.repositoryUrl == null) ? (other.repositoryUrl != null) : !this.repositoryUrl.equals(other.repositoryUrl)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.repositoryUrl != null ? this.repositoryUrl.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        String result = "";
        result +=    this.configurationItem != null ? this.configurationItem.getName() : "null item";
        result += " - ";
        result +=    this.repositoryUrl != null ? this.repositoryUrl.hashCode() : "null url";
        return result;
    }

    public int getNumberOfRevisionsMeasured() {
        if (this.revisions == null) {
            return 0;
        }
        return this.revisions.size();
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
     * @return the revisions
     */
    public Set<Revision> getRevisions() {
        return revisions;
    }

    public Integer getUncompiledRevisionCount() {
        Integer count = 0;
        if (revisions != null && !revisions.isEmpty()) {
            for (Revision revision : revisions) {
                if (revision.getCannotCompile() != null && revision.getCannotCompile() == true) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return the revisions
     */
    public List<Revision> getRevisionsAsList() {
        List<Revision> returnList = new ArrayList<Revision>(revisions);
        Collections.sort(returnList);
        return returnList;
    }

    /**
     * @param revisions the revisions to set
     */
    public void setRevisions(Set<Revision> revisions) {
        this.revisions = revisions;
    }

    /**
     * @return the mavenProject
     */
    public boolean isMavenProject() {
        return mavenProject;
    }

    /**
     * @param mavenProject the mavenProject to set
     */
    public void setMavenProject(boolean mavenProject) {
        this.mavenProject = mavenProject;
    }

    /**
     * @return the repositoryUrl
     */
    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    /**
     * @param repositoryUrl the repositoryUrl to set
     */
    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    /**
     * @return the projectUser
     */
    public Set<ProjectUser> getProjectUser() {
        return projectUsers;
    }

    /**
     * @param projectUser the projectUser to set
     */
    public void setProjectUser(Set<ProjectUser> projectUsers) {
        this.projectUsers = projectUsers;
    }

    /**
     * @return the configurationItem
     */
    public ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    /**
     * @param configurationItem the configurationItem to set
     */
    public void setConfigurationItem(ConfigurationItem configurationItem) {
        this.configurationItem = configurationItem;
    }

    /**
     * @param language the lanaguage to set
     */
    public Language getLanguage() {
        return this.lang;
    }

    /**
     * @param language the lanaguage to set
     */
    public void setLanguage(Language language) {
        this.lang = language;
    }
}
