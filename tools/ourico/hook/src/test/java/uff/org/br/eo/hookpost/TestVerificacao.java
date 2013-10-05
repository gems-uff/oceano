/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uff.org.br.eo.hookpost;

//import br.uff.ic.oceano.core.control.ApplicationConstants;
//import br.uff.ic.oceano.core.dao.controle.JPAUtil;
//import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
//import br.uff.ic.oceano.core.factory.ObjectFactory;
//import br.uff.ic.oceano.ostra.controle.Constantes;
//import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
//import br.uff.ic.oceano.ourico.model.CheckOut;
//import br.uff.ic.oceano.ourico.rcs.Subversion;
//import br.uff.ic.oceano.ourico.service.CheckOutService;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JOptionPane;
//import org.testng.annotations.Test;
//import org.tmatesoft.svn.core.SVNException;
//import uff.org.br.eo.autobranch.TrataAutobranch;
//import uff.org.br.eo.email.SendMail;
//import uff.org.br.eo.gerencial.Politicas;
//import uff.org.br.eo.scv.svnInformation;
//import uff.org.br.eo.politicas.ConfiguracaoPolitica;
//import uff.org.br.eo.politicas.Restritiva;
//import uff.org.br.eo.util.DadosEmail;
//import uff.org.br.eo.util.DadosMvn;
//import uff.org.br.eo.util.DadosServidor;
//import uff.org.br.eo.util.LSenha;

/**
 *
 * @author marapao
 */
public class TestVerificacao {

//    @Test
    public void testandoCiclo() {
////args[1] reposit?rio
//        //args[2] revis?o
//
//
//        //Caso teste
//        //Repositorio: file:///home/marapao/sandbox/teste/
//        //path autobranch: autobranch
//        //path protegido: trunk
//
//        //inicializando BD
//        ApplicationConstants.CURRENT_PERSISTENCE_UNIT = ApplicationConstants.PERSISTENCE_UNIT_LOCAL;
//        JPAUtil.startUp();
//
////        JOptionPane.showMessageDialog(null, "Executando Hook Post");
//        //inicializa as vari?veis com o reposit?rio que foi alterado e a vers?o gerada
//        String rep = "/home/svn";
//        String rev = "130";
//        System.out.println("Repositório " + rep + " revis?o " + rev);
//
////        JOptionPane.showMessageDialog(null, "Repositorio = "+ rep +" Revis?o = "+rev);
//        //login e senha svn
//        String loginSVN = "marapao";
//        String senhaSVN = "marapa";
//
//
//        String politicaTipo = "restritiva";
//        String workspace = "/home/marapao/workspace";
//
//        //informa??o que vir? da configura??o o projeto
//        String pathAutobranch = "autobranch";
//
//
//
//
//
//        CheckOutService coService = ObjectFactory.getObj(CheckOutService.class);
//        CheckOut coModel = new CheckOut();
//
//        //lista que guarda diretorios que foram alterados
//        List<String> directories = null;
//
//        //objeto respons?vel pela coleta de dados do SVN
//        svnInformation svnInfo = new svnInformation(rep, rev, loginSVN, senhaSVN);
//
//        //inicializada com os diretorios que foram alterados na revis?o
//        directories = svnInfo.returnChangedDirectories();
//
//
//
//        if (directories.isEmpty()) {
//            JOptionPane.showMessageDialog(null, "VAZIO! ");
//            return;
//        } else if (!directories.get(0).contains(pathAutobranch)) {
//            return;
//        }
//
//
//
//
//        //descobre o autobranch que foi alterado
//        Long autobranch = TrataAutobranch.autobranch(directories.get(0), pathAutobranch);
//
//        //verifica??o de o autobranch deveria ser verificado
//        System.out.println("autobranch " + autobranch);
//        try {
//            //manipula??o de dados do bd
//            coModel = coService.getbyAutobranch(autobranch);
//        } catch (ObjetoNaoEncontradoException ex) {
//            //se nao existe o autobranch n?o continua a verifica??o
//
//            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
//            return;
//        }
//
//        //verifica??o de o commit feito no autobranch foi feito pelo usu?rio ou pelo ouri?o
//        if (svnInfo.retornaMensagem().equals(ConstantesOurico.MENSAGEM_SINCRONIZACAO_AUTOBRANCH_COM_CONTEUDO_TRUNK)) {
//            return;
//        }
//
//        //fim manipula??o de dados do bd
//
//        Politicas politicas = new Politicas();
//
//        LSenha emailLS = new LSenha();
//        emailLS.setLogin("projmangue@gmail.com");
//        emailLS.setSenha("senhafraca");
//
//        DadosEmail emailDados = new DadosEmail();
//        emailDados.setFrom(emailLS.getLogin());
//        emailDados.setMessage(new StringBuffer());
//        emailDados.setSubject("Relat?rio");
//        emailDados.setTo(emailLS.getLogin());
//
//        LSenha svnLS = new LSenha();
//        svnLS.setLogin(loginSVN);
//        svnLS.setSenha(senhaSVN);
//
//
//
//
//
////        //pc linux
////        DadosMvn mvnDados = new DadosMvn();
////        mvnDados.setRepositorioLocal("/home/marapao/repositorio/mvn");
////        mvnDados.setSettings("/home/marapao/.m2/settings.xml");
////
////        DadosServidor servidorDados = new DadosServidor();
////        servidorDados.setPathRepAutobranch("file:///home/marapao/repositorio/svn/testando/autobranch/01/trunk");
////        servidorDados.setPathRepTrunk("file:///home/marapao/repositorio/svn/testando/trunk");
////        servidorDados.setPathWsAutobranch("/home/marapao/workspace/autobranch");
////        servidorDados.setPathWsTrunk("/home/marapao/workspace/mainLine");
////        //fim pc linux
//        //BD
//        DadosMvn mvnDados = new DadosMvn();
//        mvnDados.setRepositorioLocal("/home/marapao/repositorio/mvn");
//        mvnDados.setSettings("/home/marapao/.m2/settings.xml");
//
//        DadosServidor servidorDados = new DadosServidor();
//        //depois ser? auterado por coModel.getUrlRepositorio()+coModel.getPathAutobranch()+autobranch
////        servidorDados.setPathRepAutobranch("file:///home/marapao/repositorio/svn/testando/autobranch/"+autobranch);
//
//        System.out.println("1 = "+coModel.getUrlRepositorio());
//        System.out.println("2 = "+coModel.getDiretorioAutobranch());
//        System.out.println("3 = "+coModel.getAutobranch());
//        servidorDados.setPathRepAutobranch(coModel.getUrlRepositorio() + coModel.getDiretorioAutobranch() + coModel.getAutobranch());
//        //depois ser? auterado por coModel.getUrlRepositorio()+coModel.getPathProtegido()
//        servidorDados.setPathRepTrunk(coModel.getUrlRepositorio() + coModel.getDiretorioProtegido());
//        servidorDados.setPathWsAutobranch(workspace + "/autobranch" + Constantes.OS_SLASH + coModel.getAutobranch());
//        servidorDados.setPathWsTrunk(workspace + "/mainLine" + Constantes.OS_SLASH + coModel.getAutobranch());
//        //fim pc linux
//
//        System.out.println("check point 1");
//        ConfiguracaoPolitica politicasConfi = new ConfiguracaoPolitica();
//        politicasConfi.setLSSvn(svnLS);
//        politicasConfi.setTipo(politicaTipo);
//        politicasConfi.setdMvn(mvnDados);
//        politicasConfi.setdServidor(servidorDados);
//
//        System.out.println("check point 2");
//
//        String repProtegido = servidorDados.getPathRepTrunk();
//        String repAutobranch = servidorDados.getPathRepAutobranch();
//        String wsProtegido = servidorDados.getPathWsTrunk();
//        String wsAutobranch = servidorDados.getPathWsAutobranch();
//
//        StringBuffer saida = new StringBuffer();
//
//        System.out.println("check point 3-");
//
//        Restritiva politicaRestritiva = new Restritiva();
//        System.out.println("check point 3.1");
//
//        Subversion svn = null;
//
//        try{
//            svn = new Subversion(servidorDados.getPathRepAutobranch(), svnLS.getLogin(), svnLS.getSenha());
//        } catch(Exception e){
//            System.out.println(e.getMessage());
//        }
//
//        System.out.println("check point 3.2");
//
//
//        SendMail mail = new SendMail();
//
//        boolean sucesso = false;
//        System.out.println("check point 4");
//
//
//
//        sucesso = politicaRestritiva.preProcessamento(servidorDados.getPathRepAutobranch(), servidorDados.getPathWsAutobranch(), svn);
//        System.out.println("check point 5");
//
//        if (!sucesso) {
//
//            mail.SendMail(emailDados.getFrom(), emailDados.getTo(), emailDados.getSubject(), saida.toString(), emailLS.getLogin(), emailLS.getSenha());
//        System.out.println("check point 6");
//            return;
//        }
//
//
//        sucesso = politicaRestritiva.verificacao(politicasConfi.projetoAutobranch(), svn, repProtegido, wsAutobranch, saida, autobranch);
//
//
//        if (!sucesso) {
//
//            mail.SendMail(emailDados.getFrom(), emailDados.getTo(), emailDados.getSubject(), "problema verificaç?o\n" + saida.toString(), emailLS.getLogin(), emailLS.getSenha());
//
//            return;
//
//        }
//        System.out.println("check point 7");
//
//        try {
//
//            sucesso = politicaRestritiva.posProcessamento(svn, wsProtegido, wsAutobranch, repProtegido, repAutobranch, saida);
//            if (!sucesso) {
//                mail.SendMail(emailDados.getFrom(), emailDados.getTo(), emailDados.getSubject(), saida.toString(), emailLS.getLogin(), emailLS.getSenha());
//                return;
//            }
//        } catch (SVNException ex) {
//            ex.printStackTrace();
//            System.out.println("Problema no pos-processamento!");
//        }
//
//
//        mail.SendMail(emailDados.getFrom(), emailDados.getTo(), emailDados.getSubject(), saida.toString(), emailLS.getLogin(), emailLS.getSenha());
//        /*------------------------------------Fim Ciclo completo-------------------------------------------*/
    }
}
