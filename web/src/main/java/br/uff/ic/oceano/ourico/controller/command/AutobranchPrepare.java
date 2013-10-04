/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import br.uff.ic.oceano.ourico.service.VerificacaoPosCheckoutService;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.ourico.service.ProjectConfigurationService;
import br.uff.ic.oceano.ourico.service.OuricoSVNService;
import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ourico.model.VerificacaoPosCheckout;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.service.ConfiguracaoVerificacaoService;
import br.uff.ic.oceano.ourico.service.EstadoService;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;
import static br.uff.ic.oceano.ourico.controle.ConstantesOurico.*;
import org.tmatesoft.svn.core.SVNException;

/**
 *
 * @author marapao
 */
public class AutobranchPrepare implements Command {

    private SoftwareProjectService softwareProjectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private ProjectConfigurationService projectConfigurationService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectConfigurationService.class);
    private CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    private ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
    private VerificacaoPosCheckoutService verificacaoPosCheckoutService = ObjectFactory.getObjectWithDataBaseDependencies(VerificacaoPosCheckoutService.class);
    private ConfiguracaoVerificacaoService configuracaoVerificacaoService = ObjectFactory.getObjectWithDataBaseDependencies(ConfiguracaoVerificacaoService.class);
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {



        Date inicio = new Date();
        CheckOut checkOut;
        OceanoUser cliente = null;
        ProjectUser projectUser;
        SoftwareProject project = null;
        ProjectConfiguration projectConfiguration = null;
        String autobranch;
        ConfiguracaoVerificacao dadosConfiguracao = null;

//        String pathRepositorio = request.getParameter(PATH_REPOSITORIO);
//        String dirProtegido = request.getParameter(DIRETORIO_PROTEGIDO);
//        String dirAutobranch = request.getParameter(DIRETORIO_AUTOBRANCH);
        String senhaSVN = request.getParameter(SENHA_SVN);
        String loginSVN = request.getParameter(LOGIN_SVN);
        String urlCheckedOut = request.getParameter(URL_CHECKED_OUT);
        try {
            //recuperando dados do BD
            //alertar para cadastrar o projeto

            if(urlCheckedOut.endsWith("/")){
                urlCheckedOut = urlCheckedOut.subSequence(0, urlCheckedOut.length() -1).toString();

            }
            project = softwareProjectService.getByRepositoryUrl(urlCheckedOut);
            projectUser = projectUserService.getByProjectAndLogin(project, loginSVN);
            projectConfiguration = projectConfigurationService.getByProject(project);
            cliente = projectUser.getOceanoUser();
            dadosConfiguracao = configuracaoVerificacaoService.get().get(0);
        } catch (ServiceException ex) {
            estadoService.saveEstado(inicio, new Date(), CHECKOUT_FALHA, ex.getMessage(), null);
            Logger.getLogger(AutobranchPrepare.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } catch (ObjetoNaoEncontradoException ex) {
            estadoService.saveEstado(inicio, new Date(), CHECKOUT_FALHA, ex.getMessage(), null);
            Logger.getLogger(AutobranchPrepare.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }



        //manipulação de dados
        String pathRepository = project.getConfigurationItem().getBaseUrl();
        if (!pathRepository.endsWith("/")) {
            pathRepository = pathRepository.concat("/");
        }

        String dirAutobranch = projectConfiguration.getDirAutobranch();
        if (!dirAutobranch.endsWith("/")) {
            dirAutobranch = dirAutobranch.concat("/");
        }


        //dados necessarios
        String pathVerificado = urlCheckedOut;
        String pathAutobranch = pathRepository + dirAutobranch;
        String politica = projectConfiguration.getPolitica();
        String workspace = dadosConfiguracao.getWorkspacePath();
        if (!workspace.endsWith("/")) {
            workspace += "/";
        }

        String workspaceAutobranchDir = dadosConfiguracao.getWorkspaceAutobranchDir();
        if (!workspaceAutobranchDir.endsWith("/")) {
            workspaceAutobranchDir += "/";
        }
        //autenticar usuário
        //        OceanoUser oceanoUser = oceanoUserService.autenticarUsuario(loginOceano, senhaOceano);
        //executa acao
        OuricoSVNService ouricoSVNService = ObjectFactory.getObjectWithDataBaseDependencies(OuricoSVNService.class);
        checkOut = ouricoSVNService.preparaAutobranch(pathVerificado, pathAutobranch, loginSVN, senhaSVN);

       


        //completa dados do checkOut
        checkOut.setCicloFinalizado(false);
        checkOut.setPolitica(politica);
        checkOut.setUrlCheckedOut(urlCheckedOut);
        checkOut.setWorkspace(workspace);
        checkOut.setResponsavel(cliente);



        checkOutService.save(checkOut);


        String project_name = project.getConfigurationItem().getName();
        project_name = project_name.replaceAll(" ", "_");
        if (!project_name.endsWith("/")) {
            project_name += "/";
        }

        String wsAutobranch = workspace + project_name + workspaceAutobranchDir + checkOut.getAutobranch();
        //Registrando verificação do projeto
        CheckOut checkOut1 = null;
        try {
            checkOut1 = checkOutService.getbyAutobranch(checkOut.getAutobranch());
        } catch (ObjetoNaoEncontradoException ex) {
            estadoService.saveEstado(inicio, new Date(), CHECKOUT_FALHA, ex.getMessage(), checkOut.getAutobranch().toString());
            Logger.getLogger(AutobranchPrepare.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }



        VerificacaoPosCheckout verificacaoPosCheckout = new VerificacaoPosCheckout();
        verificacaoPosCheckout.setCheckOut(checkOut);
        verificacaoPosCheckout.setSemantica(false);
        verificacaoPosCheckout.setSintatica(false);
        verificacaoPosCheckout.setVerificado(false);
        verificacaoPosCheckout.setVerificando(false);
        checkOut1.setWorkspace(wsAutobranch);
        verificacaoPosCheckoutService.save(verificacaoPosCheckout);
        checkOutService.save(checkOut1);
        //Realizando check-out no servidor


        autobranch = pathAutobranch + checkOut1.getAutobranch() + "/";

        Subversion svn = new Subversion(autobranch, loginSVN, senhaSVN);
        try {
            Long checkout = svn.checkout(autobranch, wsAutobranch);
        } catch (SVNException ex) {
            estadoService.saveEstado(inicio, new Date(), CHECKOUT_FALHA, ex.getMessage(), checkOut.getAutobranch().toString());
            Logger.getLogger(AutobranchPrepare.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        try {
            response.getWriter().print(autobranch);
        } catch (IOException ex) {
            estadoService.saveEstado(inicio, new Date(), CHECKOUT_FALHA, ex.getMessage(), checkOut.getAutobranch().toString());
            Logger.getLogger(AutobranchPrepare.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        estadoService.saveEstado(inicio, new Date(), CHECKOUT_SUCESSO, null, checkOut.getAutobranch().toString());

    }
}
