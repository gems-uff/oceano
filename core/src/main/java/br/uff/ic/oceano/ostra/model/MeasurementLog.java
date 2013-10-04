/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.core.model.SoftwareProject;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author DanCastellani
 */
@NamedQueries({
    @NamedQuery(name = "MeasurementLog.getByProject", query = "select ml from MeasurementLog ml where ml.softwareProject = ?")
})
@Entity
@Table(name = "ostra_MeasurementLog")
@SequenceGenerator(name = "MeasurementLog_seq", sequenceName = "MeasurementLog_seq")
public class MeasurementLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MeasurementLog_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idSoftawreProject")
    private SoftwareProject softwareProject;

    @Column(nullable=false)
    private Long revisionNumber;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;

    @Lob
    private String log;

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
     * @return the softwareProject
     */
    public SoftwareProject getSoftwareProject() {
        return softwareProject;
    }

    /**
     * @param softwareProject the softwareProject to set
     */
    public void setSoftwareProject(SoftwareProject softwareProject) {
        this.softwareProject = softwareProject;
    }

    /**
     * @return the revisionNumber
     */
    public Long getRevisionNumber() {
        return revisionNumber;
    }

    /**
     * @param revisionNumber the revisionNumber to set
     */
    public void setRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the log
     */
    public String getLog() {
        return log;
    }

    /**
     * @param log the log to set
     */
    public void setLog(String log) {
        this.log = log;
    }
}
