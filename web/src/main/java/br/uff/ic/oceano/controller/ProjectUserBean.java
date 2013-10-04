/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.controller;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.view.SelectOneDataModel;
import br.uff.ic.oceano.view.SelectableItem;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author marapao
 */
public class ProjectUserBean extends BaseBean {

    private SelectOneDataModel<OceanoUser> selectOneOceanoUser;
    private SelectOneDataModel<SoftwareProject> selectOneProject;
    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
    private String loginSVN;
    private String senhaSVN;
    private String confirmacaoSenhaSVN;
    private DataModel tabelaProjetosDecorados;
    private OceanoUser selectedOceanoUser;
    private ProjectUser currentProjectUser;

    public ProjectUser getCurrentProjectUser() {
        return currentProjectUser;
    }

    public void setCurrentProjectUser(ProjectUser currentProjectUser) {
        this.currentProjectUser = currentProjectUser;
    }

    public ProjectUserBean() {
        super("ProjectUserBean");

    }
    private String PAGINA_CADASTRO = "def:/privado/oceano/projectUser/projectUserManeger";

    public String doCadastrar() {
        return PAGINA_CADASTRO;
    }

    public void preencheTabelaProjetosDecorados() {
        selectedOceanoUser = selectOneOceanoUser.getObjetoSelecionado();


        List<SoftwareProject> listaProjetos = projectService.getAll();
        List<ProjectUser> listaUsuariosProjetos = projectUserService.getByOceanoUser(selectedOceanoUser);

        List<SelectableItem<ProjectUser>> listaDecorada = new ArrayList<SelectableItem<ProjectUser>>(listaProjetos.size());

        for (SoftwareProject softwareProject : listaProjetos) {

            ProjectUser projectUser = new ProjectUser();
            projectUser.setOceanoUser(selectedOceanoUser);
            projectUser.setProject(softwareProject);

            SelectableItem selectableItem = new SelectableItem(projectUser);

            boolean selecionado = false;
            for (ProjectUser projectUserInterno : listaUsuariosProjetos) {
                if (projectUserInterno.getProject().equals(softwareProject)) {
                    selecionado = true;
                    selectableItem.setItem(projectUserInterno);
                    break;
                }
            }
            selectableItem.setSelected(selecionado);
            listaDecorada.add(selectableItem);
        }

        tabelaProjetosDecorados = new ListDataModel(listaDecorada);
    }

    public void incluirOuAlterar() {
        currentProjectUser = ((SelectableItem<ProjectUser>) tabelaProjetosDecorados.getRowData()).getItem();
    }

    public void excluir() {
        SelectableItem<ProjectUser> itemSelecionado = (SelectableItem<ProjectUser>) tabelaProjetosDecorados.getRowData();
        ProjectUser projectUser = itemSelecionado.getItem();
        projectUserService.exclude(projectUser);
        itemSelecionado.setSelected(false);
        projectUser.setId(null);
        projectUser.setLogin(null);
        projectUser.setPassword(null);
        currentProjectUser = null;
        info("Vínculo removido com sucesso");

    }

    public String updateAnonymousAccess() {
        currentProjectUser.setAnonymous(!currentProjectUser.isAnonymous());
        return null;
    }

    public String doSave() {
        if (!currentProjectUser.isAnonymous()) {
            if (currentProjectUser.getLogin().isEmpty() || currentProjectUser.getPassword().isEmpty()) {
                error("O login ou a senha não pode ser nula.");
            }

            if (!currentProjectUser.getPassword().equals(confirmacaoSenhaSVN)) {
                error("A senha e a confirmação são diferentes.");
            }
        }

        try {
            projectUserService.save(currentProjectUser);
            info("Cadastro Realizado com Sucesso");
            currentProjectUser = null;
        } catch (ServiceException ex) {
            error(ex.getMessage());
        }

        return null;
    }

    public String doCancel() {
        currentProjectUser = null;
        return null;
    }

    /**
     * @return the selectOneOceanoUser
     */
    public SelectOneDataModel<OceanoUser> getSelectOneOceanoUser() throws ObjetoNaoEncontradoException {
        if (selectOneOceanoUser == null) {

            List<OceanoUser> all = oceanoUserService.getAll();
            selectOneOceanoUser = SelectOneDataModel.criaComTextoInicialPersonalizado(all, "Selecione Usuário");

        }

        return selectOneOceanoUser;
    }

    /**
     * @param selectOneOceanoUser the selectOneOceanoUser to set
     */
    public void setSelectOneOceanoUser(SelectOneDataModel<OceanoUser> selectOneOceanoUser) {
        this.selectOneOceanoUser = selectOneOceanoUser;
    }

    /**
     * @return the selectOneProject
     */
    public SelectOneDataModel<SoftwareProject> getSelectOneProject() {
        if (selectOneProject == null) {
            selectOneProject = SelectOneDataModel.criaComTextoInicialPersonalizado(projectService.getAll(), "Selecione Projeto");

        }


        return selectOneProject;
    }

    /**
     * @param selectOneProject the selectOneProject to set
     */
    public void setSelectOneProject(SelectOneDataModel<SoftwareProject> selectOneProject) {
        this.selectOneProject = selectOneProject;
    }

    /**
     * @return the loginSVN
     */
    public String getLoginSVN() {
        return loginSVN;
    }

    /**
     * @param loginSVN the loginSVN to set
     */
    public void setLoginSVN(String loginSVN) {
        this.loginSVN = loginSVN;
    }

    /**
     * @return the senhaSVN
     */
    public String getSenhaSVN() {
        return senhaSVN;
    }

    /**
     * @param senhaSVN the senhaSVN to set
     */
    public void setSenhaSVN(String senhaSVN) {
        this.senhaSVN = senhaSVN;
    }

    /**
     * @return the confirmacaoSenhaSVN
     */
    public String getConfirmacaoSenhaSVN() {
        return confirmacaoSenhaSVN;
    }

    /**
     * @param confirmacaoSenhaSVN the confirmacaoSenhaSVN to set
     */
    public void setConfirmacaoSenhaSVN(String confirmacaoSenhaSVN) {
        this.confirmacaoSenhaSVN = confirmacaoSenhaSVN;
    }

    /**
     * @return the tabelaProjetosDecorados
     */
    public DataModel getTabelaProjetosDecorados() {
        return tabelaProjetosDecorados;
    }

    /**
     * @param tabelaProjetosDecorados the tabelaProjetosDecorados to set
     */
    public void setTabelaProjetosDecorados(DataModel tabelaProjetosDecorados) {
        this.tabelaProjetosDecorados = tabelaProjetosDecorados;
    }
}
