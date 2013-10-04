/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.verificacao.politicas;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.ourico.util.Casting;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import br.uff.ic.oceano.ourico.verificacao.controller.ConstantesPoliticas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 *
 * @author marapao
 */
public abstract class PoliticaBase {

    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public StringBuffer preencheLog(String descricaoSucesso, String descricaoFalha, List<Throwable> erros) {



        StringBuffer stringBuffer = new StringBuffer();

        if (erros != null && !erros.isEmpty()) {
            stringBuffer.append(descricaoFalha).append("\n");
            StringBuffer detalheFalha = Casting.ListTrowableToString(erros);
            stringBuffer.append(detalheFalha);

        } else {
            stringBuffer.append(descricaoSucesso).append("\n");
        }
        return stringBuffer;


    }

    public void preProcessamento(String repAutobranch, String wsAutobranch, Subversion svn) throws SVNException {
        boolean log = true;

        if (log) {
            System.out.println("Realizando check-out da " + repAutobranch + " para " + wsAutobranch);
        }
        svn.checkout(repAutobranch, wsAutobranch);

    }

    public void preProcessamento(String repAutobranch, String wsAutobranch, Subversion svn, boolean update) throws SVNException {
        boolean log = true;

        if (log) {
            System.out.println("Realizando Pre-processamento da " + repAutobranch + " para " + wsAutobranch);
        }

        if (!update) {
            svn.checkout(repAutobranch, wsAutobranch);
        } else {
            svn.update(wsAutobranch, SVNRevision.HEAD);
        }

    }

    public boolean posProcessamento(Subversion svn, String wsProtegido, String wsAutobranch, String repProtegido, String repAutobranch, StringBuffer log) throws SVNException {
        boolean result = false;
        List<String> conflitosFisicos = new ArrayList<String>();


        svn.checkin(wsAutobranch, ConstantesOurico.MENSAGEM_SINCRONIZACAO_AUTOBRANCH_COM_CONTEUDO_TRUNK);

        svn.checkout(repProtegido, wsProtegido);

        conflitosFisicos = svn.mergePathReintegrate(repAutobranch, wsProtegido);
        if (conflitosFisicos.size() > 0) {
            log.append(ConstantesOurico.ANALISE_FISICA_INTEGRACAO_FAIL);
            log.append(ConstantesOurico.DIRETORIOS_CONFLITO);
            for (String conflito : conflitosFisicos) {
                log.append("\n    ").append(conflito);
            }
            return false;
        } else {
            log.append(ConstantesOurico.ANALISE_FISICA_INTEGRACAO_OK);
        }

        svn.checkin(wsProtegido, ConstantesOurico.MENSAGEM_SINCRONIZACAO_TRUNK_COM_CONTEUDO_AUTOBRANCH);


        return true;
    }

//    public abstract boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log);
    public abstract boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log, Long autobranch);

//    public boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log, int politica) {
//        boolean sucesso;
//
//
//        if (politica >= ConstantesPoliticas.POLITICA_MODERADA) {
//            sucesso = analiseSintatica_1(mvn, wsVerificado, log);
//
//            if (!sucesso) {
//                return false;
//            }
//        }
//
//        if (politica >= ConstantesPoliticas.POLITICA_RESTRITIVA) {
//            sucesso = analiseSemantica_1(mvn, wsVerificado, log);
//            if (!sucesso) {
//                return false;
//            }
//        }
//
//        try {
//            sucesso = analiseFisica(svn, wsVerificado, repProtegido, log);
//            if (!sucesso) {
//                return false;
//            }
//        } catch (SVNException ex) {
//            ex.printStackTrace();
//            return false;
//        }
//
//        if (politica >= ConstantesPoliticas.POLITICA_MODERADA) {
//
//            sucesso = analiseSintatica_2(mvn, wsVerificado, log);
//            if (!sucesso) {
//                return false;
//            }
//        }
//
//        if (politica >= ConstantesPoliticas.POLITICA_RESTRITIVA) {
//
//            sucesso = analiseSemantica_2(mvn, wsVerificado, log);
//            if (!sucesso) {
//                return false;
//            }
//        }
//
//
//        return true;
//    }
    public boolean verificacao(Maven mvn, Subversion svn, String urlRepositoryProtected, String wsVerificado, StringBuffer log, int politica, Long autobranch) {
        boolean sucesso;


        if (politica >= ConstantesPoliticas.POLITICA_MODERADA) {
            Date inicio = new Date();
            StringBuffer logAux = new StringBuffer();
            String descricao = ConstantesOurico.ANALISE_SINTATICA_1_OK;
            String descricaoFalha = ConstantesOurico.ANALISE_SINTATICA_1_FAIL+"\n";



            sucesso = analiseSintatica_1(mvn, wsVerificado, logAux);

            String aux = logAux.toString();
            aux = aux.replaceAll(wsVerificado, "");
            log.append(aux);
            if (!sucesso) {
                estadoService.saveEstado(inicio, new Date(), descricaoFalha, aux, autobranch.toString());
                return false;
            }

            estadoService.saveEstado(inicio, new Date(), descricao, null, autobranch.toString());

        }

        if (politica >= ConstantesPoliticas.POLITICA_RESTRITIVA) {
            StringBuffer logAux = new StringBuffer();
            Date inicio = new Date();
            String descricao = ConstantesOurico.ANALISE_SEMANTICA_1_OK;
            String descricaoFalha = ConstantesOurico.ANALISE_SEMANTICA_1_FAIL+"\n";




            sucesso = analiseSemantica_1(mvn, wsVerificado, logAux);
            String aux = logAux.toString();
            aux = aux.replaceAll(wsVerificado, "");
            log.append(aux);


            if (!sucesso) {
                estadoService.saveEstado(inicio, new Date(), descricaoFalha, aux, autobranch.toString());
                return false;
            }

            estadoService.saveEstado(inicio, new Date(), descricao, null, autobranch.toString());
        }

        Date inicioFisico = new Date();
        String descricaoFisica = ConstantesOurico.ANALISE_FISICA_2_OK;
        String descricaoFalhaFisica = ConstantesOurico.ANALISE_FISICA_2_FAIL+"\n";
        try {
            StringBuffer logAux = new StringBuffer();


            sucesso = analiseFisica(svn, wsVerificado, urlRepositoryProtected, logAux);
            log.append(logAux);


            if (!sucesso) {
                estadoService.saveEstado(inicioFisico, new Date(), descricaoFalhaFisica, logAux.toString(), autobranch.toString());
                return false;
            }

            estadoService.saveEstado(inicioFisico, new Date(), descricaoFisica, null, autobranch.toString());
        } catch (SVNException ex) {
            estadoService.saveEstado(inicioFisico, new Date(), descricaoFalhaFisica, ex.getMessage(), autobranch.toString());
            return false;
        }

        if (politica >= ConstantesPoliticas.POLITICA_MODERADA) {

            StringBuffer logAux = new StringBuffer();
            Date inicio = new Date();
            String descricao = ConstantesOurico.ANALISE_SINTATICA_2_OK;
            String descricaoFalha = ConstantesOurico.ANALISE_SINTATICA_2_FAIL+"\n";


            sucesso = analiseSintatica_2(mvn, wsVerificado, logAux);
            String aux = logAux.toString();
            aux = aux.replaceAll(wsVerificado, "");
            log.append(aux);

            if (!sucesso) {
                estadoService.saveEstado(inicio, new Date(), descricaoFalha, aux, autobranch.toString());
                return false;
            }

            estadoService.saveEstado(inicio, new Date(), descricao, null, autobranch.toString());
        }

        if (politica >= ConstantesPoliticas.POLITICA_RESTRITIVA) {

            StringBuffer logAux = new StringBuffer();
            Date inicio = new Date();
            String descricao = ConstantesOurico.ANALISE_SEMANTICA_2_OK;
            String descricaoFalha = ConstantesOurico.ANALISE_SEMANTICA_2_FAIL;



            sucesso = analiseSemantica_2(mvn, wsVerificado, logAux);
            String aux = logAux.toString();
            aux = aux.replaceAll(wsVerificado, "");
            log.append(aux);

            if (!sucesso) {
                estadoService.saveEstado(inicio, new Date(), descricaoFalha, aux, autobranch.toString());
                return false;
            }

            estadoService.saveEstado(inicio, new Date(), descricao, null, autobranch.toString());
        }


        return true;
    }

    protected abstract boolean analiseSintatica_1(Maven mvn, String wsVerificado, StringBuffer log);

    protected abstract boolean analiseSintatica_2(Maven mvn, String wsVerificado, StringBuffer log);

    protected abstract boolean analiseSemantica_1(Maven mvn, String wsVerificado, StringBuffer log);

    protected abstract boolean analiseSemantica_2(Maven mvn, String wsVerificado, StringBuffer log);

    protected abstract boolean analiseFisica(Subversion svn, String wsVerificado, String repProtegido, StringBuffer log) throws SVNException;
}
