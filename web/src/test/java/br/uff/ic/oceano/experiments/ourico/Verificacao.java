/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import br.uff.ic.oceano.ourico.service.ConfiguracaoVerificacaoService;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class Verificacao {

    private ConfiguracaoVerificacaoService configuracaoVerificacaoService;

    @BeforeClass
    public void before(){
    
        configuracaoVerificacaoService = ObjectFactory.getObjectWithDataBaseDependencies(ConfiguracaoVerificacaoService.class);
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_MEMORY);
        JPAUtil.startUp();
    }

    @AfterClass
    public void after(){

    }

    @Test
    public void adicionaDados(){

        ConfiguracaoVerificacao configuracaoVerificacao;

        configuracaoVerificacao = new ConfiguracaoVerificacao();

        configuracaoVerificacao.setEmail("projmangue@gmail.com");
        configuracaoVerificacao.setSenhaEmail("senhafraca");
        configuracaoVerificacao.setMvnRepository("/home/marapao/repositorio/mvn");
        configuracaoVerificacao.setMvnSettings("/home/marapao/.m2/settings.xml");
        configuracaoVerificacao.setWorkspaceAutobranchDir("autobranch/");
        configuracaoVerificacao.setWorkspaceProtectedDir("protected/");

        configuracaoVerificacaoService.save(configuracaoVerificacao);
    }

    @Test(dependsOnMethods="adicionaDados")
    public void get(){
        List<ConfiguracaoVerificacao> get = configuracaoVerificacaoService.get();

        for (ConfiguracaoVerificacao configuracaoVerificacao : get) {
            System.out.println(configuracaoVerificacao.getEmail());
        }
    }

}
