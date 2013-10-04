/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.ConfigurationItemDao;
import br.uff.ic.oceano.core.dao.impl.ConfigurationItemDaoImpl;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author marapao
 */
public class ConfigurationItemService implements PersistenceService{

    private ConfigurationItemDao configurationItemDao;

    public void setup() {
        configurationItemDao = ObjectFactory.getObjectWithDataBaseDependencies(ConfigurationItemDaoImpl.class);
    }

    public ConfigurationItemService(){
    }

    @Transacional
    public void save(ConfigurationItem configurationItem){
        if(configurationItem.getId() == null)
            configurationItemDao.inclui(configurationItem);
        else
            configurationItemDao.altera(configurationItem);
    }

    public List<ConfigurationItem> getAll(){
        return configurationItemDao.getAll();
    }
}
