/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.model;

import br.uff.ic.oceano.core.model.OceanoUser;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author marapao
 */
@NamedQueries({
    @NamedQuery(name = "DadosPolitica.getAll", query = "select d from DadosPolitica d")
})
@Entity
@Table(name = "DadosPolitica")
@SequenceGenerator(name = "sequencia_dp", sequenceName = "seq_estado")
public class DadosPolitica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequencia_dp")
    private Long id;
    @Column(nullable = false)
    private String tipo;
    @ManyToOne(fetch = FetchType.EAGER)
    private OceanoUser responsavel;

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
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
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
}
