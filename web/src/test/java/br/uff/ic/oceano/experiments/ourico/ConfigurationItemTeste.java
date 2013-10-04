/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.service.ConfigurationItemService;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class ConfigurationItemTeste {

    private ConfigurationItemService configurationItemService = ObjectFactory.getObjectWithDataBaseDependencies(ConfigurationItemService.class);

    @BeforeClass
    public void antes() {        
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_MEMORY);
        JPAUtil.startUp();

    }

    @AfterClass
    public void depois() {
    }

    @Test
    public void insere() {

        ConfigurationItem ci = new ConfigurationItem();
        ci.setBaseUrl("https://gems.ic.uff.br/svn/oceano");
        ci.setBranchPath("/branch");
        ci.setName("Oceano");
        ci.setTrunkPath("/trunk");

        configurationItemService.save(ci);


    }

    @Test(dependsOnMethods="insere")
    public void recupera(){
        List<ConfigurationItem> all = configurationItemService.getAll();

        for (ConfigurationItem configurationItem : all) {
            System.out.println(configurationItem.getName());
        }
    }
}
