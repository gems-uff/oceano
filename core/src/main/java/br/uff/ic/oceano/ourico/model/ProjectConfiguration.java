/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.model;

/**
 *
 * @author marapao
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.core.model.SoftwareProject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @NamedQuery(name = "ProjectConfiguration.getByProject", query = "select d from ProjectConfiguration d where d.project=?")
})
@Entity
@Table(name = "ProjectConfiguration")
@SequenceGenerator(name = "seq_ProjectConfiguration", sequenceName = "seq_ProjectConfiguration")
public class ProjectConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_ProjectConfiguration")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSoftwareProject")
    private SoftwareProject project;
    @Column(nullable = false)
    private String dirAutobranch;
    @Column(nullable = false)
    private String politica;
    @Column(nullable = false)
    private Boolean verificacaoSintaticaInformativa;
    @Column(nullable = false)
    private Boolean verificacaoSemanticaInformativa;
    private String politicaAutomatica;

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
     * @return the dirAutobranch
     */
    public String getDirAutobranch() {
        return dirAutobranch;
    }

    /**
     * @param dirAutobranch the dirAutobranch to set
     */
    public void setDirAutobranch(String dirAutobranch) {
        this.dirAutobranch = dirAutobranch;
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
     * @return the verificacaoSintaticaInformativa
     */
    public Boolean getVerificacaoSintaticaInformativa() {
        return verificacaoSintaticaInformativa;
    }

    /**
     * @param verificacaoSintaticaInformativa the verificacaoSintaticaInformativa to set
     */
    public void setVerificacaoSintaticaInformativa(Boolean verificacaoSintaticaInformativa) {
        this.verificacaoSintaticaInformativa = verificacaoSintaticaInformativa;
    }

    /**
     * @return the verificacaoSemanticaInformativa
     */
    public Boolean getVerificacaoSemanticaInformativa() {
        return verificacaoSemanticaInformativa;
    }

    /**
     * @param verificacaoSemanticaInformativa the verificacaoSemanticaInformativa to set
     */
    public void setVerificacaoSemanticaInformativa(Boolean verificacaoSemanticaInformativa) {
        this.verificacaoSemanticaInformativa = verificacaoSemanticaInformativa;
    }

    /**
     * @return the politicaAutomatica
     */
    public String getPoliticaAutomatica() {
        return politicaAutomatica;
    }

    /**
     * @param politicaAutomatica the politicaAutomatica to set
     */
    public void setPoliticaAutomatica(String politicaAutomatica) {
        this.politicaAutomatica = politicaAutomatica;
    }


}
