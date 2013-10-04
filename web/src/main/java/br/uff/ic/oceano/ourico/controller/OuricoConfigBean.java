/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller;

import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;
import br.uff.ic.oceano.ourico.service.ProjectConfigurationService;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.util.ArrayList;
import java.util.List;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marapao
 */
public class OuricoConfigBean extends BaseBean {

    private String PAGINA_CADASTRO = "def:/privado/ourico/ouricoConfig/form";
    private SelectOneDataModel<SoftwareProject> selectOneProject;
    private SelectOneDataModel<String> selectOnePolitica;
    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private ProjectConfigurationService projectConfigurationService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectConfigurationService.class);
    private String dirAutobranch;
    private List<String> filtrosInformativos;
    private boolean filtroSintatico;
    private boolean filtroSemantico;
    private boolean chboxFiltroSintatico;
    private boolean chboxFiltroSemantico;
    private boolean projetoVerificado = false;
    private String politicaSelecionada;
    private ProjectConfiguration projectConfiguration;

    public OuricoConfigBean() {
        super("OuricoConfigBean");
    }

    /**
     * @return the selectOneProject
     */
    public SelectOneDataModel<SoftwareProject> getSelectOneProject() {
        if (selectOneProject == null) {
            selectOneProject = selectOneProject.criaComTextoInicialPersonalizado(projectService.getAll(), "Selecione um projeto");
        }

        return selectOneProject;
    }

    /**
     * @param selectOneProject the selectOneProject to set
     */
    public void setSelectOneProject(SelectOneDataModel<SoftwareProject> selectOneProject) {
        this.selectOneProject = selectOneProject;
    }

    public String doCadastrar() {
        return PAGINA_CADASTRO;
    }

    public String doSalvar() {

        if(projectConfiguration == null)
            projectConfiguration = new ProjectConfiguration();
        

        getProjectConfiguration().setProject(selectOneProject.getObjetoSelecionado());
        getProjectConfiguration().setDirAutobranch(dirAutobranch);
        getProjectConfiguration().setPolitica(selectOnePolitica.getObjetoSelecionado());
        getProjectConfiguration().setVerificacaoSintaticaInformativa(chboxFiltroSintatico);
        getProjectConfiguration().setVerificacaoSemanticaInformativa(chboxFiltroSemantico);

        projectConfigurationService.save(getProjectConfiguration());

        info("The configuration was sucefully stored.");
        return null;
    }

    public void OnClickSemantico() {
        if (chboxFiltroSemantico) {
            chboxFiltroSintatico = true;
        }
    }

    public void doCancelar() {
        dirAutobranch = null;
    }

    public String preencheDadosProjeto() {
        SoftwareProject projetoSelecionado = selectOneProject.getObjetoSelecionado();
        try {
            ProjectConfiguration projConfiguration = projectConfigurationService.getByProject(projetoSelecionado);
            projectConfiguration = projConfiguration;

            if (selectOnePolitica == null) {
                List<String> politicasLista = new ArrayList<String>();
                politicasLista.add(PERMISSIVA);
                politicasLista.add(MODERADA);
                politicasLista.add(RESTRITIVA);
                politicasLista.add(AUTOMATICA);

                selectOnePolitica = SelectOneDataModel.criaComTextoInicialPersonalizado(politicasLista, "selecione uma politica.");
            }

            dirAutobranch = projConfiguration.getDirAutobranch();
            politicaSelecionada = projConfiguration.getPolitica();

            selectOnePolitica.setSelecao(politicaSelecionada);
            chboxFiltroSintatico = projConfiguration.getVerificacaoSintaticaInformativa();
            chboxFiltroSemantico = projConfiguration.getVerificacaoSemanticaInformativa();

            if (politicaSelecionada.equals(PERMISSIVA)) {
                filtrosInformativos = new ArrayList<String>();
                filtrosInformativos.add(FILTRO_SINTATICO);
                filtrosInformativos.add(FILTRO_SEMANTICO);
            } else if (politicaSelecionada.equals(MODERADA)) {
                filtrosInformativos = new ArrayList<String>();
                filtrosInformativos.add(FILTRO_SEMANTICO);
            } else {
                filtrosInformativos = new ArrayList<String>();
            }

        } catch (ObjetoNaoEncontradoException ex) {
            Logger.getLogger(OuricoConfigBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            projetoVerificado = true;
        }

        return null;
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
     * @return the selectOnePolitica
     */
    public SelectOneDataModel<String> getSelectOnePolitica() {
        if (selectOnePolitica == null) {
            List<String> politicasLista = new ArrayList<String>();
            politicasLista.add(PERMISSIVA);
            politicasLista.add(MODERADA);
            politicasLista.add(RESTRITIVA);
            politicasLista.add(AUTOMATICA);

            selectOnePolitica = SelectOneDataModel.criaComTextoInicialPersonalizado(politicasLista, "selecione uma politica.");
        }

        return selectOnePolitica;
    }

    /**
     * @param selectOnePolitica the selectOnePolitica to set
     */
    public void setSelectOnePolitica(SelectOneDataModel<String> selectOnePolitica) {
        this.selectOnePolitica = selectOnePolitica;
    }

    public void preencheListaFiltrosInformativos() {
        politicaSelecionada = selectOnePolitica.getObjetoSelecionado();
        chboxFiltroSintatico = false;
        chboxFiltroSemantico = false;

        if (politicaSelecionada.equals(PERMISSIVA)) {
            filtrosInformativos = new ArrayList<String>();
            filtrosInformativos.add(FILTRO_SINTATICO);
            filtrosInformativos.add(FILTRO_SEMANTICO);
        } else if (politicaSelecionada.equals(MODERADA)) {
            filtrosInformativos = new ArrayList<String>();
            filtrosInformativos.add(FILTRO_SEMANTICO);
        } else {
            filtrosInformativos = new ArrayList<String>();
        }

    }

    /**
     * @return the filtrosInformativos
     */
    public List<String> getFiltrosInformativos() {
        return filtrosInformativos;
    }

    /**
     * @param filtrosInformativos the filtrosInformativos to set
     */
    public void setFiltrosInformativos(List<String> filtrosInformativos) {
        this.filtrosInformativos = filtrosInformativos;
    }

    /**
     * @return the filtroSintatico
     */
    public boolean getFiltroSintatico() {


        if (filtrosInformativos == null) {
            filtroSintatico = false;
        } else if (filtrosInformativos.contains(FILTRO_SINTATICO)) {
            filtroSintatico = true;
        } else {
            filtroSintatico = false;
        }

        return filtroSintatico;
    }

    /**
     * @param filtroSintatico the filtroSintatico to set
     */
    public void setFiltroSintatico(boolean filtroSintatico) {
        this.filtroSintatico = filtroSintatico;
    }

    /**
     * @return the filtroSemantico
     */
    public boolean getFiltroSemantico() {

        if (filtrosInformativos == null) {
            filtroSemantico = false;
        } else if (filtrosInformativos.contains(FILTRO_SEMANTICO)) {
            filtroSemantico = true;
        } else {
            filtroSemantico = false;
        }

        return filtroSemantico;
    }

    /**
     * @param filtroSemantico the filtroSemantico to set
     */
    public void setFiltroSemantico(boolean filtroSemantico) {
        this.filtroSemantico = filtroSemantico;
    }

    /**
     * @return the politicaSelecionada
     */
    public String getPoliticaSelecionada() {
        return politicaSelecionada;
    }

    /**
     * @param politicaSelecionada the politicaSelecionada to set
     */
    public void setPoliticaSelecionada(String politicaSelecionada) {
        this.politicaSelecionada = politicaSelecionada;
    }

    /**
     * @return the chboxFiltroSintatico
     */
    public boolean isChboxFiltroSintatico() {
        return chboxFiltroSintatico;
    }

    /**
     * @param chboxFiltroSintatico the chboxFiltroSintatico to set
     */
    public void setChboxFiltroSintatico(boolean chboxFiltroSintatico) {
        this.chboxFiltroSintatico = chboxFiltroSintatico;
    }

    /**
     * @return the chboxFiltroSemantico
     */
    public boolean isChboxFiltroSemantico() {
        return chboxFiltroSemantico;
    }

    /**
     * @param chboxFiltroSemantico the chboxFiltroSemantico to set
     */
    public void setChboxFiltroSemantico(boolean chboxFiltroSemantico) {
        this.chboxFiltroSemantico = chboxFiltroSemantico;
    }

    /**
     * @return the projetoVerificado
     */
    public boolean isProjetoVerificado() {
        return projetoVerificado;
    }

    /**
     * @param projetoVerificado the projetoVerificado to set
     */
    public void setProjetoVerificado(boolean projetoVerificado) {
        this.projetoVerificado = projetoVerificado;
    }

    /**
     * @return the projectConfiguration
     */
    public ProjectConfiguration getProjectConfiguration() {
        return projectConfiguration;
    }

    /**
     * @param projectConfiguration the projectConfiguration to set
     */
    public void setProjectConfiguration(ProjectConfiguration projectConfiguration) {
        this.projectConfiguration = projectConfiguration;
    }
}
