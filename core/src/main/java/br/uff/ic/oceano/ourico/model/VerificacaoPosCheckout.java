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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author marapao
 */

@NamedQueries({
    @NamedQuery(name="VerificacaoPosCheckout.getNaoVerificado", query="select vpc from VerificacaoPosCheckout vpc where vpc.verificando=false")
})
@Entity
@Table(name="VerificacaoPosCheckout")
@SequenceGenerator(name="seq_VerificacaoPosCheckout", sequenceName= "seq_VerificacaoPosCheckout")
public class VerificacaoPosCheckout implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="seq_VerificacaoPosCheckout")
    private Long id;
    private Boolean sintatica;
    private Boolean semantica;
    private Boolean verificado;
    @Column(nullable=false)
    private Boolean verificando;
    @OneToOne(optional=false)
    @JoinColumn(name="idCheckout")
    private CheckOut checkOut;

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
     * @return the sintatica
     */
    public Boolean getSintatica() {
        return sintatica;
    }

    /**
     * @param sintatica the sintatica to set
     */
    public void setSintatica(Boolean sintatica) {
        this.sintatica = sintatica;
    }

    /**
     * @return the semantica
     */
    public Boolean getSemantica() {
        return semantica;
    }

    /**
     * @param semantica the semantica to set
     */
    public void setSemantica(Boolean semantica) {
        this.semantica = semantica;
    }

    /**
     * @return the verificado
     */
    public Boolean getVerificado() {
        return verificado;
    }

    /**
     * @param verificado the verificado to set
     */
    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
    }

    /**
     * @return the verificando
     */
    public Boolean getVerificando() {
        return verificando;
    }

    /**
     * @param verificando the verificando to set
     */
    public void setVerificando(Boolean verificando) {
        this.verificando = verificando;
    }

    /**
     * @return the checkOut
     */
    public CheckOut getCheckOut() {
        return checkOut;
    }

    /**
     * @param checkOut the checkOut to set
     */
    public void setCheckOut(CheckOut checkOut) {
        this.checkOut = checkOut;
    }

}
