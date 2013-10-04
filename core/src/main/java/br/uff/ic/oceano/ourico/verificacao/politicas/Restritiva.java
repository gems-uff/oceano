/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.verificacao.politicas;

import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
import br.uff.ic.oceano.ourico.rcs.Subversion;
//import br.uff.ic.oceano.ourico.verificacao.build.LogMaven;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import br.uff.ic.oceano.ourico.verificacao.controller.ConstantesPoliticas;
import java.util.ArrayList;
import java.util.List;
import org.tmatesoft.svn.core.SVNException;



/**
 *
 * @author marapao
 */
public class Restritiva extends PoliticaBase implements Politica{

    
    @Override
    public boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log, Long autobranch) {
        return verificacao(mvn, svn, repProtegido, wsVerificado, log, ConstantesPoliticas.POLITICA_RESTRITIVA, autobranch);
    }

//    @Override
//    public boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log) {
//        return verificacao(mvn, svn, repProtegido, wsVerificado, log, ConstantesPoliticas.POLITICA_RESTRITIVA);
//    }

    @Override
    protected boolean analiseSintatica_1(Maven mvn, String wsVerificado, StringBuffer log) {
        List<Throwable> erros = null;
         mvn.setUrlProjeto(wsVerificado);
        try {
            erros = mvn.compila();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         log.append(preencheLog(ConstantesOurico.ANALISE_SINTATICA_1_OK,ConstantesOurico.ANALISE_SINTATICA_1_FAIL, erros));

        return (erros == null);
    }

    @Override
    protected boolean analiseSintatica_2(Maven mvn, String wsVerificado, StringBuffer log) {
         List<Throwable> erros = null;
         mvn.setUrlProjeto(wsVerificado);
        try {
            erros = mvn.compila();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         log.append(preencheLog(ConstantesOurico.ANALISE_SINTATICA_2_OK,ConstantesOurico.ANALISE_SINTATICA_2_FAIL, erros));

        return (erros == null);
    }

    @Override
    protected boolean analiseSemantica_1(Maven mvn, String wsVerificado, StringBuffer log) {
         List<Throwable> erros = null;
         mvn.setUrlProjeto(wsVerificado);
        try {
            erros = mvn.testa();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         log.append(preencheLog(ConstantesOurico.ANALISE_SINTATICA_1_OK,ConstantesOurico.ANALISE_SINTATICA_1_FAIL, erros));

        return (erros == null);
    }

    @Override
    protected boolean analiseSemantica_2(Maven mvn, String wsVerificado, StringBuffer log) {
         List<Throwable> erros = null;
         mvn.setUrlProjeto(wsVerificado);
        try {
            erros = mvn.testa();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        log.append(preencheLog(ConstantesOurico.ANALISE_SEMANTICA_2_OK,ConstantesOurico.ANALISE_SEMANTICA_2_FAIL, erros));

        return (erros == null);
    }

    @Override
    protected boolean analiseFisica(Subversion svn, String wsVerificado, String repProtegido, StringBuffer log) throws SVNException {
        boolean result;
        List<String> conflitosFisicos = new ArrayList<String>();


        conflitosFisicos = svn.mergePath(repProtegido, wsVerificado);

        if (conflitosFisicos.size() > 0) {

            log.append(ConstantesOurico.ANALISE_FISICA_2_FAIL);
            log.append(ConstantesOurico.DIRETORIOS_CONFLITO);
            for (String conflito : conflitosFisicos) {
                log.append("\n    ").append(conflito);
            }
            result = false;
        } else {
            log.append(ConstantesOurico.ANALISE_FISICA_2_OK);
            result = true;
        }
        return result;
    }

}
