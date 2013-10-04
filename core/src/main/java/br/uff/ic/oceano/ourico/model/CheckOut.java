/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.model;

import br.uff.ic.oceano.core.model.OceanoUser;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author marapao
 */
@NamedQueries({
    @NamedQuery(name = "CheckOut.getByAutobranch", query = "select c from CheckOut c where c.autobranch=?"),
    @NamedQuery(name = "CheckOut.getAll", query = "select c from CheckOut c order by c.autobranch"),
    @NamedQuery(name = "CheckOut.getMaxAutobranch", query = "select max(co.autobranch) from CheckOut co"),
    @NamedQuery(name = "CheckOut.getCheckOutwithMaxAutobranch", query = "select c from CheckOut c where c.autobranch=(select max(co.autobranch) from CheckOut co)")
})
@Entity
@Table(name = "ourico_checkOut")
@SequenceGenerator(name = "seq_checkout", sequenceName = "seq_checkout")
public class CheckOut implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_checkout")
    private Long idCheckout;
    @Column(nullable = false, unique = true)
    private Long autobranch;
    @Column(nullable = false)
    private Long revisao;
    @Column(nullable = false)
    private String politica;
    private String workspace;
    @Column(nullable = false)
    private String urlCheckedOut;
    @Column(nullable = false)
    private boolean cicloFinalizado;
    @OneToOne
    @JoinColumn(name="id")
    private OceanoUser responsavel;

    @Transient
    private Estado currentState;

    /**
     * @return the idCheckout
     */
    public Long getIdCheckout() {
        return idCheckout;
    }

    /**
     * @param idCheckout the idCheckout to set
     */
    public void setIdCheckout(Long idCheckout) {
        this.idCheckout = idCheckout;

    }

    /**
     * @return the autobranch
     */
    public Long getAutobranch() {
        return autobranch;
    }

    /**
     * @param autobranch the autobranch to set
     */
    public void setAutobranch(Long autobranch) {
        this.autobranch = autobranch;
    }

    /**
     * @return the revisao
     */
    public long getRevisao() {
        return revisao;
    }

    /**
     * @param revisao the revisao to set
     */
    public void setRevisao(long revisao) {
        this.revisao = revisao;
    }

    /**
     * @return the politica
     */
    public String getPolitica() {
        return politica;
    }

    /**
     * @param politica the politica to set
     */
    public void setPolitica(String politica) {
        this.politica = politica;
    }

    /**
     * @return the workspace
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * @return the urlCheckedOut
     */
    public String getUrlCheckedOut() {
        return urlCheckedOut;
    }

    /**
     * @param urlCheckedOut the urlCheckedOut to set
     */
    public void setUrlCheckedOut(String urlCheckedOut) {
        this.urlCheckedOut = urlCheckedOut;
    }

    /**
     * @return the cicloFinalizado
     */
    public boolean isCicloFinalizado() {
        return cicloFinalizado;
    }

    /**
     * @param cicloFinalizado the cicloFinalizado to set
     */
    public void setCicloFinalizado(boolean cicloFinalizado) {
        this.cicloFinalizado = cicloFinalizado;
    }

    /**
     * @return the responsavel
     */
    public OceanoUser getResponsavel() {
        return responsavel;
    }

    /**
     * @param responsavel the responsavel to set
     */
    public void setResponsavel(OceanoUser responsavel) {
        this.responsavel = responsavel;
    }

    /**
     * @return the currentState
     */
    public Estado getCurrentState() {
        return currentState;
    }

    /**
     * @param currentState the currentState to set
     */
    public void setCurrentState(Estado currentState) {
        this.currentState = currentState;
    }
}
