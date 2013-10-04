/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.model;

import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.util.DateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Heliomar
 */
@NamedQueries({
    @NamedQuery(name = "Revision.getWithChangedFiles", query = "select r from Revision r left outer join fetch r.changedFiles cf where r = ?"),
    @NamedQuery(name = "Revision.getWithRevisionedItemsAndItemsAndMetricValues", query = "select r from Revision r left outer join fetch r.changedFiles cf left outer join fetch cf.metricValues mv where r = ?"),
    @NamedQuery(name = "Revision.getByNumberAndProject", query = "select r from Revision r where r.number = ? and r.project = ?"),
    @NamedQuery(name = "Revision.getByUsuario", query = "select r from Revision r where r.project.id in (select pu.project.id from ProjectUser pu where pu.oceanoUser = ?)"),
    @NamedQuery(name = "Revision.getByProject", query = "select r from Revision r where r.project = ? order by r.number"),
    @NamedQuery(name = "Revision.getByCaminhoLocal", query = "select r from Revision r where r.localPath = ?")
})
@Entity
@Table(name = "Revision")
@SequenceGenerator(name = "Revision_seq", sequenceName = "Revision_seq")
public class Revision implements Serializable, Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "Revision_seq")
    protected Long id;
//    @Transient
    protected String localPath;
    protected String message;
    protected String commiter;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "revision")
    protected Set<VersionedItem> changedFiles;
    protected Long number;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Calendar commitDate;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "idProject")
    protected SoftwareProject project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idOriginalRevision")
    protected Revision revision;
    /**
     * This variable can have 3 values: 
     *  - Null: we did not try to compile t yet
     *  - False: it compiles!
     *  - True: the compilation breaks.
     */
    protected Boolean cannotCompile;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Revision other = (Revision) obj;
        if (this.number != other.number && (this.number == null || !this.number.equals(other.number))) {
            return false;
        }
        if (this.project != other.project && (this.project == null || !this.project.equals(other.project))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.number != null ? this.number.hashCode() : 0);
        hash = 19 * hash + (this.project != null ? this.project.hashCode() : 0);
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
     * @return the localPath
     */
    public String getLocalPath() {
        return localPath;
    }

    /**
     * @param localPath the localPath to set
     */
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    /**
     * @return the commiter
     */
    public String getCommiter() {
        return commiter;
    }

    /**
     * @param commiter the commiter to set
     */
    public void setCommiter(String commiter) {
        this.commiter = commiter;
    }

    public List<VersionedItem> getChangedFilesAsList() {
        if (changedFiles != null) {
            return new ArrayList<VersionedItem>(changedFiles);
        } else {
            return null;
        }
    }

    public int getNumberOfComittedFiles() {
        if (changedFiles == null) {
            return 0;
        }
        return changedFiles.size();
    }

    /**
     * @return the changedFiles
     */
    public void setChangedFiles(Set<VersionedItem> changedFiles) {
        this.changedFiles = changedFiles;
    }

    /**
     * @return the set of changedFiles
     */
    public Set<VersionedItem> getChangedFiles() {
        return changedFiles;
    }

    /**
     * @return the number
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(Long number) {
        this.number = number;
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
     * @return the revision
     */
    public Revision getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    /**
     * @return the commitDate
     */
    public Calendar getCommitDate() {
        return commitDate;
    }

    /**
     * @return the commitDate formated by the DateUtil util tool
     */
    public String getFormattedCommitDate() {
        return DateUtil.format(commitDate);
    }

    /**
     * @param commitDate the commitDate to set
     */
    public void setCommitDate(Calendar commitDate) {
        this.commitDate = commitDate;
    }

    @Override
    public String toString() {
        return "#" + String.valueOf(this.number)
                + " @" + getFormattedCommitDate()!=null? getFormattedCommitDate():"" +
                " :" + project.getConfigurationItem() != null ? project.getConfigurationItem().getName() : "null";
    }

    public int compareTo(Object o) {
        return this.number.intValue() - ((Revision) o).number.intValue();
    }

    /**
     * @return the cannotCompile
     */
    public Boolean getCannotCompile() {
        return cannotCompile;
    }

    /**
     * @param cannotCompile the cannotCompile to set
     */
    public void setCannotCompile(Boolean cannotCompile) {
        this.cannotCompile = cannotCompile;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
