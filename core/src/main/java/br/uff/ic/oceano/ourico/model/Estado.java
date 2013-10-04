/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author marapao
 */

@NamedQueries({
    @NamedQuery(name = "Estado.getByAutobranch", query = "select e from Estado e where e.autobranch=? order by e.inicio"),
    @NamedQuery(name = "Estado.getByAutobranchDescricao", query = "select e from Estado e where e.autobranch=? and e.descricao=?")

})
@Entity
@Table(name = "ourico_Estado")
@SequenceGenerator(name = "sequencia_es", sequenceName = "seq_estado")
public class Estado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequencia_es")
    @Column(nullable = false)
    private Long idEstado;
    private Long autobranch;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date inicio;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date fim;
    @Column(nullable = false)
    private String descricao;
    @Lob
    private String detalhe;

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
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }
    
    public String getStringInicio() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        return df.format(inicio);
    }

    
    /**
     * @return the fim
     */
    public Date getFim() {
        return fim;
    }

    public String getStringFim() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss");
        return df.format(fim);
    }

    /**
     * @param fim the fim to set
     */
    public void setFim(Date fim) {
        this.fim = fim;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the idEstado
     */
    public Long getIdEstado() {
        return idEstado;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdEstado(Long idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @return the detalhe
     */
    public String getDetalhe() {
        return detalhe;
    }

    /**
     * @param detalhe the detalhe to set
     */
    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }

   
}
