/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.model;

import br.uff.ic.oceano.core.model.QualityAttribute;
import java.io.Serializable;
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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Heliomar
 */
@NamedQueries({

    @NamedQuery(name="Knowledge.getByQualitiatributteAndRefactoring",query="select c from Knowledge c where c.qualityAttribute =? and c.refactring= ?"),
    @NamedQuery(name="Knowledge.getIdByQualitiatributteAndRefactoring",query="select c.id from Knowledge c where c.qualityAttribute =? and c.refactring= ?"),
    @NamedQuery(name="Knowledge.getAll",query="select c from Knowledge c order by c.totalSucess/c.totalUsed ")

})

@Entity
@Table(name="espada_knowledge", uniqueConstraints={@UniqueConstraint(columnNames={"id","idQualityAttribute"})})
@SequenceGenerator(name = "espada_knowledge_seq", sequenceName = "espada_knowledge_seq")
public class Knowledge implements Serializable{
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO, generator="espada_knowledge_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name="idRefactoring")
    private Refactoring refactring;
    @ManyToOne
    @JoinColumn(name="idQualityAttribute")
    private QualityAttribute qualityAttribute;
    private int totalSucess = 0;
    private int totalUsed = 0;
    private int totalWorsen = 0;
    private int totalNotImproveNorWorsen = 0;


    /**
     * @return the transformacao
     */
    public Refactoring getRefactoring() {
        return refactring;
    }

    /**
     * @param transformacao the transformacao to set
     */
    public void setRefactoring(Refactoring transformacao) {
        this.refactring = transformacao;
    }

    /**
     * @return the totalSucesso
     */
    public int getTotalSuccess() {
        return totalSucess;
    }

    /**
     * @param totalSucesso the totalSucesso to set
     */
    public void setTotalSuccess(int totalSucesso) {
        this.totalSucess = totalSucesso;
    }

    /**
     * @return the totalUtilizado
     */
    public int getTotalUsed() {
        return totalUsed;
    }

    /**
     * @param totalUtilizado the totalUtilizado to set
     */
    public void setTotalUsed(int totalUtilizado) {
        this.totalUsed = totalUtilizado;
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

    /**
     * @return the idKnowledge
     */
    public Long getIdKnowledge() {
        return id;
    }

    /**
     * @param idKnowledge the idKnowledge to set
     */
    public void setIdKnowledge(Long idKnowledge) {
        this.id = idKnowledge;
    }

    /**
     * @return the totalWorsen
     */
    public int getTotalWorsen() {
        return totalWorsen;
    }

    /**
     * @param totalWorsen the totalWorsen to set
     */
    public void setTotalWorsen(int totalWorsen) {
        this.totalWorsen = totalWorsen;
    }

    /**
     * @return the totalNotImproveNorWorsen
     */
    public int getTotalNotImproveNorWorsen() {
        return totalNotImproveNorWorsen;
    }

    /**
     * @param totalNotImproveNorWorsen the totalNotImproveNorWorsen to set
     */
    public void setTotalNotImproveNorWorsen(int totalNotImproveNorWorsen) {
        this.totalNotImproveNorWorsen = totalNotImproveNorWorsen;
    }




}
