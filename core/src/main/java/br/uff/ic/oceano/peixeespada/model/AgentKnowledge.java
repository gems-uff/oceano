/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Heliomar
 */
@NamedQueries({
    @NamedQuery(name="Knowledge.getByAgent",query="select ak from AgentKnowledge ak where ak.agent=?")
})

@Entity
@Table(name="espada_agentknowledge", uniqueConstraints={@UniqueConstraint(columnNames={"idAgent","idKnowledge"})})
@SequenceGenerator(name = "espada_agentknowledge_seq", sequenceName = "espada_agentknowledge_seq")
public class AgentKnowledge implements Serializable{

    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="espada_agentknowledge_seq")
    private Long idAgentKnowledge;

    @ManyToOne
    @JoinColumn(name="idAgent")
    private Agent agent;
    @ManyToOne
    @JoinColumn(name="idKnowledge")
    private Knowledge knowledge;

    /**
     * @return the idAgentKnowledge
     */
    public Long getIdAgentKnowledge() {
        return idAgentKnowledge;
    }

    /**
     * @param idAgentKnowledge the idAgentKnowledge to set
     */
    public void setIdAgentKnowledge(Long idAgentKnowledge) {
        this.idAgentKnowledge = idAgentKnowledge;
    }

    /**
     * @return the agent
     */
    public Agent getAgent() {
        return agent;
    }

    /**
     * @param agent the agent to set
     */
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    /**
     * @return the knowledge
     */
    public Knowledge getKnowledge() {
        return knowledge;
    }

    /**
     * @param knowledge the knowledge to set
     */
    public void setKnowledge(Knowledge knowledge) {
        this.knowledge = knowledge;
    }

}
