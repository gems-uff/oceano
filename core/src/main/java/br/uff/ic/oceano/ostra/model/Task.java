/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.model;

import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.util.DateUtil;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;

/**
 *
 * @author daniel
 */
@NamedQueries({
    @NamedQuery(name = "Task.getAll", query = "select t from Task t"),
    @NamedQuery(name = "Task.getScheduledTasks", query = "select t from Task t where t.scheduleTime is not null and t.startTime is null")
})
@Entity
@Table(name = "ostra_Task")
@SequenceGenerator(name = "ostra_Task_seq", sequenceName = "ostra_Task_seq")
public class Task implements Serializable {

    public static final String STATUS_CREATED = "Criada";
    public static final String STATUS_RUNNING = "Rodando";
    public static final String STATUS_DONE = "Pronta";
    public static final int TYPE_EXTRACT_METRIC = 0;
    public static final String NAME_EXTRACT_METRIC = "Medição";
    public static final int TYPE_MINE_DATABASE = 2;
    public static final String NAME_MINE_DATABASE = "Mineraração";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ostra_Task_seq")
    private Long id;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date scheduleTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date finishTime;
    private Integer type;
    private String description;
    @ManyToOne
    @JoinColumn(name = "idProject")
    private SoftwareProject project;
    private String status = STATUS_CREATED;

    public String getTarget() {
        if (this.type == null) {
            return null;
        }
        switch (this.type) {
            case TYPE_EXTRACT_METRIC: {
                return this.project.toString();
            }
            case TYPE_MINE_DATABASE: {
                return "Base de dados";
            }
        }
        return null;
    }

    public String getTypeAsString() {
        if (type == null) {
            return null;
        }
        switch (type) {
            case TYPE_EXTRACT_METRIC: {
                return NAME_EXTRACT_METRIC;
            }

            case TYPE_MINE_DATABASE: {
                return NAME_MINE_DATABASE;
            }

        }
        return null;
    }

    @Override
    public String toString() {
        return "Task: " + this.getFormatedScheduleTime() + ". Project: " + this.project.toString();
    }

    public String getFormatedScheduleTime() {
        return DateUtil.format(scheduleTime);
    }

    public String getFormatedStartTime() {
        return DateUtil.format(startTime);
    }

    public String getFormatedFinishTime() {
        return DateUtil.format(finishTime);
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the scheduleTime
     */
    public Date getScheduleTime() {
        return scheduleTime;
    }

    /**
     * @param scheduleTime the scheduleTime to set
     */
    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the finishTime
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * @param finishTime the finishTime to set
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
