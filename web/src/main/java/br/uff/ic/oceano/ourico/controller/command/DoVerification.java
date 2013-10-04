/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.ourico.verificacao.politicas.Politica;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.ourico.service.ProjectConfigurationService;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import br.uff.ic.oceano.ourico.verificacao.mail.MailService;
import br.uff.ic.oceano.ourico.verificacao.politicas.Moderada;
import br.uff.ic.oceano.ourico.verificacao.politicas.Permissiva;
import br.uff.ic.oceano.ourico.verificacao.politicas.Restritiva;
import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ourico.contexto.Configuracao;
import br.uff.ic.oceano.ourico.controller.FlagVerification;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import br.uff.ic.oceano.ourico.service.ConfiguracaoVerificacaoService;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.ourico.verificacao.informativa.FiltrosInformativos;
import java.io.File;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tmatesoft.svn.core.SVNException;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;
import static br.uff.ic.oceano.ourico.controle.ConstantesOurico.*;

/**
 *
 * @author marapao
 */
public class DoVerification implements Command {

    private CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    private SoftwareProjectService softwareProjectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private ProjectConfigurationService projectConfigurationService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectConfigurationService.class);
    private ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
    private ConfiguracaoVerificacaoService configuracaoVerificacaoService = ObjectFactory.getObjectWithDataBaseDependencies(ConfiguracaoVerificacaoService.class);
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Dados recebidos do cliente
        String autobranchStr = request.getParameter(AUTOBRANCH);

       


        //Manipulação do email
        MailService mail = new MailService();

        //parte gerencial
        ConfiguracaoVerificacao configuracaoVerificacao = configuracaoVerificacaoService.get().get(0);

        if (configuracaoVerificacao == null) {
            System.out.println("Configurar dados de verificação");
        }

//        String workspaceAutobranchDir = configuracaoVerificacao.getWorkspaceAutobranchDir();
//        if (!workspaceAutobranchDir.endsWith("/")) {
//            workspaceAutobranchDir += "/";
//        }
        String workspaceProtectedDir = configuracaoVerificacao.getWorkspaceProtectedDir();

        if (!workspaceProtectedDir.endsWith("/")) {
            workspaceProtectedDir += "/";
        }

        String email = configuracaoVerificacao.getEmail();
        String senhaEmail = configuracaoVerificacao.getSenhaEmail();
        String mvnSettings = configuracaoVerificacao.getMvnSettings();
        String mvnRepossitory = configuracaoVerificacao.getMvnRepository();
        String workspace = configuracaoVerificacao.getWorkspacePath();
        if (!workspace.endsWith("/")) {
            workspace += "/";
        }

        //manipulação de dados do bd
        CheckOut checkOut = checkOutService.getbyAutobranch(Long.parseLong(autobranchStr));
        SoftwareProject softwareProject = softwareProjectService.getByUrl(checkOut.getUrlCheckedOut());
        if (softwareProject == null && Configuracao.sendEmail) {
            mail.sendMail(email, email, "Projeto", "Favor cadastrar o projeto", email, senhaEmail);
            return;
        }

        ProjectConfiguration projectConfiguration = projectConfigurationService.getByProject(softwareProject);
        if (projectConfiguration == null && Configuracao.sendEmail) {
            mail.sendMail(email, email, "Dados ouriço", "Favor cadastrar dados do ouriço", email, senhaEmail);
            return;
        }

        OceanoUser responsavel = checkOut.getResponsavel();
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(softwareProject, responsavel);
        if (projectUser == null && Configuracao.sendEmail) {
            mail.sendMail(email, email, "Usuário do projeto.", "Favor cadastrar este usuário ao projeto.", email, senhaEmail);
            return;
        }



        //emailResponsável
        String emailResponsavel = responsavel.getEmail();

        //usar projectUser(refatorar)
        String loginSVN = projectUser.getLogin();
        String senhaSVN = projectUser.getPassword();


        if (checkOut.isCicloFinalizado()) {
            return;
        }

//        OuricoUser responsavel = checkOut.getResponsavel();
//        SoftwareProject project = responsavel.getProject();

        String urlRepository = softwareProject.getConfigurationItem().getBaseUrl();
        if (!urlRepository.endsWith("/")) {
            urlRepository += "/";
        }



        String dirAutobranch = projectConfiguration.getDirAutobranch();
        if (!dirAutobranch.endsWith("/")) {
            dirAutobranch += "/";
        }



        //dados instanciação politica

        String rotuloPolitica = projectConfiguration.getPolitica();
        if (rotuloPolitica.equals(AUTOMATICA)) {
            rotuloPolitica = projectConfiguration.getPoliticaAutomatica();
        }

        Boolean verificacaoSintaticaInformativa = projectConfiguration.getVerificacaoSintaticaInformativa();
        Boolean verificacaoSemanticaInformativa = projectConfiguration.getVerificacaoSemanticaInformativa();


        //Dados do pre-processamento
        String urlAutobranch = urlRepository + dirAutobranch + autobranchStr + "/";
//        String urlAutobranch = PathUtil.getWellFormedURL(urlRepository, dirAutobranch, autobranchStr);
//        String wsAutobranch = workspace + softwareProject.getConfigurationItem().getName() + "/" + workspaceAutobranchDir + autobranchStr;
        Subversion svn = new Subversion(urlAutobranch, loginSVN, senhaSVN);

        //verificação de o workspace existe
        String wsAutobranch = checkOut.getWorkspace();
        File dirAutobranchFile = new File(wsAutobranch);

        //Dados de verificação
        Maven mvn = new Maven();

        mvn.setPathSettings(mvnSettings);
        mvn.setRepositorioLocal(mvnRepossitory);
        mvn.setUrlProjeto(wsAutobranch);

        String urlRepositoryProtected = checkOut.getUrlCheckedOut();
        StringBuffer log = new StringBuffer();



        Politica politica = null;

        if (rotuloPolitica.equals(RESTRITIVA)) {
            politica = new Restritiva();
        } else if (rotuloPolitica.equals(MODERADA)) {
            politica = new Moderada();
        } else {
            politica = new Permissiva();
        }

        //Dados Pos-Processamento
        String wsProtegido = workspace + softwareProject.getConfigurationItem().getName().replace(" ", "_") + "/" + workspaceProtectedDir + autobranchStr;
//        String wsProtegido = PathUtil.getWellFormedPath(workspace, softwareProject.getConfigurationItem().getName().replace(" ", "_"), workspaceProtectedDir, autobranchStr);






        //fim verificação de o workspace existe

        try {

            politica.preProcessamento(urlAutobranch, wsAutobranch, svn, dirAutobranchFile.isDirectory());

        } catch (SVNException sVNException) {
            sVNException.printStackTrace();
            System.out.println(sVNException.getMessage());
            if (Configuracao.sendEmail) {
                mail.sendMail(email, emailResponsavel, "Problema no pré-processamento", sVNException.getMessage(), email, senhaEmail);
            }
            return;
        }


        boolean verificacao = politica.verificacao(mvn, svn, urlRepositoryProtected, wsAutobranch, log, Long.parseLong(autobranchStr));

        if (!verificacao) {
            if (Configuracao.sendEmail) {
                mail.sendMail(email, emailResponsavel, "Problema na verificação", log.toString(), email, senhaEmail);
            }
            return;
        }

        Date inicio = new Date();
        boolean posProcessamento = politica.posProcessamento(svn, wsProtegido, wsAutobranch, urlRepositoryProtected, urlAutobranch, log);

        if (!posProcessamento) {
            if (Configuracao.sendEmail) {
                mail.sendMail(email, emailResponsavel, "Problema no pos-processamento", log.toString(), email, senhaEmail);
            }
            estadoService.saveEstado(inicio, new Date(), INTEGRACAO_FAIL, null, autobranchStr);
            return;
        } else {

            checkOut.setCicloFinalizado(true);
            checkOutService.save(checkOut);

            //tudo correto
            estadoService.saveEstado(inicio, new Date(), INTEGRACAO_OK, null, autobranchStr);

            if (verificacaoSintaticaInformativa) {
                log.append("\n" + "Obs.: O resultado do filtro informativo será enviado em outro e-mail.");
            } else if (verificacaoSemanticaInformativa) {
                log.append("\n" + "Obs.: Os resultados dos filtros informativos serão enviados em outro e-mail.");
            } else {
                log.append("\n" + "Obs.: Ciclo Finalizado nenhuma verificação informativa foi requisitada.");

            }
            if (Configuracao.sendEmail) {
                mail.sendMail(email, emailResponsavel, "Ciclo Completo", log.toString(), email, senhaEmail);
            }
        }

        //Inicio da verificação informativa
        //Dados: workspaceProtegido, objeto maven, objeto para colocar o log

        FiltrosInformativos filtrosInformativos = new FiltrosInformativos();
        StringBuffer verifica = filtrosInformativos.verifica(mvn, wsProtegido, verificacaoSintaticaInformativa, verificacaoSemanticaInformativa, autobranchStr);

        if (Configuracao.sendEmail) {
            mail.sendMail(email, emailResponsavel, "Verificação Informativa", verifica.toString(), email, senhaEmail);
        }

        //Fim da verificação informativa

        
    }
}
