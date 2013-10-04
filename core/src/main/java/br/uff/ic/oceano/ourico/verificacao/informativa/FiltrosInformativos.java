/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.verificacao.informativa;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.ourico.util.Casting;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.ProjectBuildFailureException;
import org.apache.maven.plugin.MojoFailureException;
import static br.uff.ic.oceano.ourico.controle.ConstantesOurico.*;

/**
 *
 * @author marapao
 */
public class FiltrosInformativos {

    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    

    private String analiseSintatica(Maven mvn, String workspace, String autobranch) throws Exception {
        Date inicio = new Date();

        mvn.setUrlProjeto(workspace);
        List<Throwable> resultado = mvn.compila();

        String sucesso = FILTRO_INFORMATIVO_SINTATICO_OK;
        String falha = FILTRO_INFORMATIVO_SINTATICO_FAIL;
        String detalhe = null;

        try{
        detalhe = Casting.ListTrowableToString(resultado).toString();
        }catch(NullPointerException ne){
            detalhe = null;
        }
        if (detalhe == null) {
            estadoService.saveEstado(inicio, new Date(), sucesso, null, autobranch);
            return  "\n"+sucesso;
        } else {
            estadoService.saveEstado(inicio, new Date(), falha, detalhe, autobranch);
            return "\n"+falha+"\n"+detalhe;
        }


        
    }

    private String analiseSemantica(Maven mvn, String workspace, String autobranch) throws Exception {
        Date inicio = new Date();

        mvn.setUrlProjeto(workspace);
        List<Throwable> resultado = mvn.testa();

        String sucesso = FILTRO_INFORMATIVO_SEMANTICO_OK;
        String falha = FILTRO_INFORMATIVO_SEMANTICO_FAIL;
        String detalhe = null;

        try{
        detalhe = Casting.ListTrowableToString(resultado).toString();
        }catch(NullPointerException ne){
            detalhe = null;
        }
        
        if (detalhe == null) {
            estadoService.saveEstado(inicio, new Date(), sucesso, null, autobranch);
            return "\n"+sucesso;
        } else {
            estadoService.saveEstado(inicio, new Date(), falha, detalhe, autobranch);
            return "\n"+falha+"\n"+detalhe;
        }

        
    }

    public StringBuffer verifica(Maven mvn, String workspace, Boolean verificacaoSintatica, Boolean verificacaoSemantica, String autobranch) {

        StringBuffer log = new StringBuffer();


        if (!(verificacaoSemantica || verificacaoSintatica)) {
            return null;
        } else if (verificacaoSemantica) {

            try {


                String analiseSintatica = analiseSintatica(mvn, workspace, autobranch);
                log.append(analiseSintatica);

                String analiseSemantica = analiseSemantica(mvn, workspace, autobranch);
                log.append(analiseSemantica);


            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(FiltrosInformativos.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (verificacaoSintatica) {
            try {
                String analiseSintatica = analiseSintatica(mvn, workspace, autobranch);
                log.append(analiseSintatica);

            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(FiltrosInformativos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return log;
    }
}
