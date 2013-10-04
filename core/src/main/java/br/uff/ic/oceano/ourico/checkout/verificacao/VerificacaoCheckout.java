/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.checkout.verificacao;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;
import br.uff.ic.oceano.ourico.model.VerificacaoPosCheckout;
import br.uff.ic.oceano.ourico.service.ConfiguracaoVerificacaoService;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.ourico.service.ProjectConfigurationService;
import br.uff.ic.oceano.ourico.service.VerificacaoPosCheckoutService;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;

/**
 *
 * @author marapao
 */
public class VerificacaoCheckout extends Thread {

    private VerificacaoPosCheckoutService verificacaoPosCheckoutService = ObjectFactory.getObjectWithDataBaseDependencies(VerificacaoPosCheckoutService.class);
    private ConfiguracaoVerificacaoService configuracaoVerificacaoService = ObjectFactory.getObjectWithDataBaseDependencies(ConfiguracaoVerificacaoService.class);
    EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);
    SoftwareProjectService softwareProjectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    ProjectConfigurationService projectConfigurationService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectConfigurationService.class);
    private Maven mvn;
    String politica;

    public VerificacaoCheckout() {
        mvn = new Maven();
    }

    private boolean verificacaoSintatica(Maven mvn) throws Exception {

        List<Throwable> result = mvn.compila();

        if (result != null && !result.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean verificacaoSemantica(Maven mvn) throws Exception {

        List<Throwable> result = mvn.testa();

        if (result != null && !result.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public void run() {



        ConfiguracaoVerificacao config = configuracaoVerificacaoService.get().get(0);
        mvn.setPathSettings(config.getMvnSettings());
        mvn.setRepositorioLocal(config.getMvnRepository());

        List<VerificacaoPosCheckout> naoVerificados = verificacaoPosCheckoutService.getNaoVerificado();

        for (VerificacaoPosCheckout naoVerificado : naoVerificados) {

            try {

                naoVerificado.setVerificando(true);
                mvn.setUrlProjeto(naoVerificado.getCheckOut().getWorkspace());

                boolean sintatica = verificacaoSintatica(mvn);
                naoVerificado.setSintatica(sintatica);

                boolean semantica = false;

                if (sintatica) {
                    semantica = verificacaoSemantica(mvn);
                    naoVerificado.setSemantica(semantica);
                } else {
                    naoVerificado.setSemantica(false);
                }


                naoVerificado.setVerificado(true);

                verificacaoPosCheckoutService.save(naoVerificado);

                if (sintatica && semantica) {
                    politica = RESTRITIVA;
                } else if (sintatica) {
                    politica = MODERADA;
                } else {
                    politica = PERMISSIVA;
                }

                CheckOut checkOut = naoVerificado.getCheckOut();
                SoftwareProject project = softwareProjectService.getByRepositoryUrl(checkOut.getUrlCheckedOut());
                ProjectConfiguration projectConfiguration = projectConfigurationService.getByProject(project);

                if(projectConfiguration.getPolitica().equals(AUTOMATICA)){
                    projectConfiguration.setPoliticaAutomatica(politica);
                    projectConfigurationService.save(projectConfiguration);
                }

            } catch (Exception ex) {
                Logger.getLogger(VerificacaoCheckout.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }
}
