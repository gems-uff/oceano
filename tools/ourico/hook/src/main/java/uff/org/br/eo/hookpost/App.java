package uff.org.br.eo.hookpost;

import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.service.ClientService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uff.org.br.eo.autobranch.TrataAutobranch;
import uff.org.br.eo.gerencial.ConfigurationData;
import uff.org.br.eo.scv.SvnInformation;

public class App {

    public static void main(String[] args) throws IOException {
        try {







            //fim temporario de implantaç?o

//            String arqConf = "teste.conf";
//            String rep = "/var/lib/submin/svn/testeourico";
//            String rev = "10";
            String arqConf = args[0];
            String rep = args[1];
            String rev = args[2];
            System.out.println("Repositório " + rep + " revis?o " + rev);
            ConfigurationData configurationData = ConfigurationData.getConfiguration(arqConf);
            //login e senha svn
//            String senhaSVN = SENHA_SVN;
            String urlOceano = configurationData.getUrlOceano();
            String dirAutobranch = configurationData.getDiretoryAutobranch();

            System.out.println("urlOceano = " + urlOceano);
            System.out.println("dirAutobranch = " + dirAutobranch);
            //informa??o que vir? da configuraç?o o projeto
            String pathAutobranch = configurationData.getDiretoryAutobranch();
            //lista que guarda diretorios que foram alterados
            List<String> directories = null;
            //objeto respons?vel pela coleta de dados do SVN
            SvnInformation svnInfo = new SvnInformation(rep, rev);
            //inicializada com os diretorios que foram alterados na revis?o
            directories = svnInfo.returnChangedDirectories();
            System.out.println("ponto 1");
            if (directories.isEmpty()) {
                System.out.println("VAZIO");
                return;
            } else if (!directories.get(0).contains(pathAutobranch)) {
                System.out.println("PATH AUTOBRANCH");
                return;
            }
            System.out.println("?onto 2");
            //descobre o autobranch que foi alterado
            Long autobranch = TrataAutobranch.autobranch(directories.get(0), dirAutobranch);
            System.out.println("autobranch = " + autobranch);

            //verifica??o de o commit feito no autobranch foi feito pelo usu?rio ou pelo ouri?o
            if (svnInfo.retornaMensagem().equals(ConstantesOurico.MENSAGEM_SINCRONIZACAO_AUTOBRANCH_COM_CONTEUDO_TRUNK)) {
                return;
            }





            ClientService clientService = new ClientService();
            System.out.println("Serviço web");
            clientService.verification(autobranch.toString(), urlOceano);






        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
