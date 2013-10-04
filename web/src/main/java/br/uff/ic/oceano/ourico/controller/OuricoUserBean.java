///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.uff.ic.oceano.ourico.controller;
//
//import br.uff.ic.oceano.controller.BaseBean;
//import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
//import br.uff.ic.oceano.core.factory.ObjectFactory;
//import br.uff.ic.oceano.core.model.OceanoUser;
//import br.uff.ic.oceano.core.model.SoftwareProject;
//import br.uff.ic.oceano.core.service.OceanoUserService;
//import br.uff.ic.oceano.core.service.SoftwareProjectService;
////import br.uff.ic.oceano.ourico.model.OuricoUser;
////import br.uff.ic.oceano.ourico.service.OuricoUserService;
//import br.uff.ic.oceano.view.SelectOneDataModel;
//import java.util.List;
//
///**
// *
// * @author marapao
// */
//public class OuricoUserBean extends BaseBean {
//
//    private SelectOneDataModel<OceanoUser> selectOneOceanoUser;
//    private SelectOneDataModel<SoftwareProject> selectOneProject;
//    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
//    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
////    private OuricoUserService ouricoUserService = ObjectFactory.getObj(OuricoUserService.class);
//    private String loginSVN;
//    private String senhaSVN;
//    private String confirmacaoSenhaSVN;
//
//    public OuricoUserBean() {
//        super("OuricoUserBean");
//
//    }
//    private String PAGINA_CADASTRO = "def:/privado/ourico/ouricoUser/form";
//
//    public String doCadastrar() {
//        return PAGINA_CADASTRO;
//    }
//
//    public String doSave() {
//
//        if (senhaSVN.equals(confirmacaoSenhaSVN)) {
//
//            OceanoUser oceanoUser = selectOneOceanoUser.getObjetoSelecionado();
//            SoftwareProject project = selectOneProject.getObjetoSelecionado();
//
////            if (!project.isOuricoProject()) {
////                return null;
////            //projeto não é compativel com ouriço.
////            }
//
////            OuricoUser ouricoUser = new OuricoUser();
//
////            ouricoUser.setLogin(loginSVN);
////            ouricoUser.setOceanoUser(oceanoUser);
////            ouricoUser.setPassword(senhaSVN);
////            ouricoUser.setProject(project);
////            try {
////                ouricoUserService.save(ouricoUser);
////            } catch (ServiceException ex) {
////                ex.printStackTrace();
////                return null;
////            }
//        }
//
//        return null;
//
//    }
//
//    public void doCancel() {
//
//        loginSVN = null;
//        senhaSVN = null;
//        confirmacaoSenhaSVN = null;
//
////        return null;
//    }
//
//    /**
//     * @return the selectOneOceanoUser
//     */
//    public SelectOneDataModel<OceanoUser> getSelectOneOceanoUser() throws ObjetoNaoEncontradoException {
//        if (selectOneOceanoUser == null) {
//
//            List<OceanoUser> all = oceanoUserService.getAll();
//            selectOneOceanoUser = SelectOneDataModel.criaComTextoInicialPersonalizado(all, "Selecione Usuário");
//
//        }
//
//        return selectOneOceanoUser;
//    }
//
//    /**
//     * @param selectOneOceanoUser the selectOneOceanoUser to set
//     */
//    public void setSelectOneOceanoUser(SelectOneDataModel<OceanoUser> selectOneOceanoUser) {
//        this.selectOneOceanoUser = selectOneOceanoUser;
//    }
//
//    /**
//     * @return the selectOneProject
//     */
//    public SelectOneDataModel<SoftwareProject> getSelectOneProject() {
//        if (selectOneProject == null) {
//            selectOneProject = SelectOneDataModel.criaComTextoInicialPersonalizado(projectService.getAll(), "Selecione Projeto");
//
//        }
//
//
//        return selectOneProject;
//    }
//
//    /**
//     * @param selectOneProject the selectOneProject to set
//     */
//    public void setSelectOneProject(SelectOneDataModel<SoftwareProject> selectOneProject) {
//        this.selectOneProject = selectOneProject;
//    }
//
//    /**
//     * @return the loginSVN
//     */
//    public String getLoginSVN() {
//        return loginSVN;
//    }
//
//    /**
//     * @param loginSVN the loginSVN to set
//     */
//    public void setLoginSVN(String loginSVN) {
//        this.loginSVN = loginSVN;
//    }
//
//    /**
//     * @return the senhaSVN
//     */
//    public String getSenhaSVN() {
//        return senhaSVN;
//    }
//
//    /**
//     * @param senhaSVN the senhaSVN to set
//     */
//    public void setSenhaSVN(String senhaSVN) {
//        this.senhaSVN = senhaSVN;
//    }
//
//    /**
//     * @return the confirmacaoSenhaSVN
//     */
//    public String getConfirmacaoSenhaSVN() {
//        return confirmacaoSenhaSVN;
//    }
//
//    /**
//     * @param confirmacaoSenhaSVN the confirmacaoSenhaSVN to set
//     */
//    public void setConfirmacaoSenhaSVN(String confirmacaoSenhaSVN) {
//        this.confirmacaoSenhaSVN = confirmacaoSenhaSVN;
//    }
//}
