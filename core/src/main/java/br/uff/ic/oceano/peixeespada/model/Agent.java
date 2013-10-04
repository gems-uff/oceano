/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.model;

import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.QualityAttribute;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author Heliomar
 */
@NamedQueries({

    @NamedQuery(name="Agent.getByOceanoUserAndStateActive",query="select a from Agent a where a.project.id in (select sp.id from ProjectUser pu left outer join pu.project sp where pu.oceanoUser = ?) and a.active = ? "),
    @NamedQuery(name="Agent.getByQualityAttribute",query="select a from Agent a where a.qualityAttribute=?")

})

@Entity
@Table(name="espada_agent")
@SequenceGenerator(name = "espada_agent_seq", sequenceName = "espada_agent_seq")
public class Agent implements Serializable {

    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="espada_agent_seq")
    private Long idAgent;

    private String status;
    
    @Transient
    private String name;
    

    @ManyToOne
    @JoinColumn(name="idProject")
    private SoftwareProject project;
    @Transient
    private Date initDate;
    @Transient
    private Date endDate;
    private Integer cycles = 0;
    private Integer successCycles = 0;
    private Integer worsenCycles = 0;
    private Integer notImproveNorWorsenCycles = 0;
    private boolean active = true;
    @ManyToOne
    @JoinColumn(name="idQualityAttribute")
    private QualityAttribute qualityAttribute;

    /**
     * @return the idAgentKnowledge
     */
    public Long getIdAgent() {
        return idAgent;
    }

    /**
     * @param idAgentKnowledge the idAgentKnowledge to set
     */
    public void setIdAgent(Long idAgent) {
        this.idAgent = idAgent;
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
     * @return the initDate
     */
    public Date getInitDate() {
        return initDate;
    }

    /**
     * @param initDate the initDate to set
     */
    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the cycles
     */
    public Integer getCycles() {
        return cycles;
    }

    /**
     * @param cycles the cycles to set
     */
    public void setCycles(Integer cycles) {
        this.cycles = cycles;
    }

    /**
     * @return the sucessCycles
     */
    public Integer getSuccessCycles() {
        return successCycles;
    }

    /**
     * @param sucessCycles the sucessCycles to set
     */
    public void setSuccessCycles(Integer sucessCycles) {
        this.successCycles = sucessCycles;
    }

    /**
     * @return the qualityAttribute
     */
    public QualityAttribute getQualityAttribute() {
        return qualityAttribute;
    }

    /**
     * @param qualityAttribute the qualityAttribute to set
     */
    public void setQualityAttribute(QualityAttribute qualityAttribute) {
        this.qualityAttribute = qualityAttribute;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Agent)){
            return false;
        }
        Agent other = (Agent) obj;
        if(other.idAgent != null && this.idAgent != null){
            return other.idAgent.equals(this.idAgent);
        }
        if(other.name != null && this.name != null){
            return other.name.equals(this.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.idAgent != null ? this.idAgent.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        
        StringBuilder builder = new StringBuilder("Agent: ");
        if(qualityAttribute != null){
            builder.append(qualityAttribute);
            builder.append(" - ");
        }
        if(project != null){
            builder.append(project.getConfigurationItem().getName());
            builder.append(" - ");
            builder.append(project.getRepositoryUrl());
        }

        return builder.toString();
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
     * @return the worsenCycles
     */
    public Integer getWorsenCycles() {
        return worsenCycles;
    }

    /**
     * @param worsenCycles the worsenCycles to set
     */
    public void setWorsenCycles(Integer worsenCycles) {
        this.worsenCycles = worsenCycles;
    }

    /**
     * @return the notImproveNorWorsenCycles
     */
    public Integer getNotImproveNorWorsenCycles() {
        return notImproveNorWorsenCycles;
    }

    /**
     * @param notImproveNorWorsenCycles the notImproveNorWorsenCycles to set
     */
    public void setNotImproveNorWorsenCycles(Integer notImproveNorWorsenCycles) {
        this.notImproveNorWorsenCycles = notImproveNorWorsenCycles;
    }



}
