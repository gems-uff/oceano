/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.verificacao.politicas;

import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import br.uff.ic.oceano.ourico.verificacao.controller.ConstantesPoliticas;
import java.util.ArrayList;
import java.util.List;
import org.tmatesoft.svn.core.SVNException;

/**
 *
 * @author marapao
 */
public class Permissiva extends PoliticaBase implements Politica {

    @Override
    public boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log, Long autobranch) {
        return verificacao(mvn, svn, repProtegido, wsVerificado, log, ConstantesPoliticas.POLITICA_PERMISSIVA, autobranch);
    }

//    @Override
//    public boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log) {
//        return verificacao(mvn, svn, repProtegido, wsVerificado, log, ConstantesPoliticas.POLITICA_PERMISSIVA);
//    }

    @Override
    protected boolean analiseSintatica_1(Maven mvn, String wsVerificado, StringBuffer log) {
        return true;
    }

    @Override
    protected boolean analiseSintatica_2(Maven mvn, String wsVerificado, StringBuffer log) {
       return true;
    }

    @Override
    protected boolean analiseSemantica_1(Maven mvn, String wsVerificado, StringBuffer log) {
        return true;
    }

    @Override
    protected boolean analiseSemantica_2(Maven mvn, String wsVerificado, StringBuffer log) {
        return true;
    }

    @Override
    protected boolean analiseFisica(Subversion svn, String wsVerificado, String repProtegido, StringBuffer log) throws SVNException {
        boolean result;
        List<String> conflitosFisicos = new ArrayList<String>();


        conflitosFisicos = svn.mergePath(repProtegido, wsVerificado);

        if (conflitosFisicos.size() > 0) {

            log.append(ConstantesOurico.ANALISE_FISICA_2_FAIL).append("\n");
            log.append(ConstantesOurico.DIRETORIOS_CONFLITO);
            for (String conflito : conflitosFisicos) {
                log.append("\n    ").append(conflito);
            }
            result = false;
        } else {
            log.append(ConstantesOurico.ANALISE_FISICA_2_OK).append("\n");
            result = true;
        }
        return result;
    }
}
