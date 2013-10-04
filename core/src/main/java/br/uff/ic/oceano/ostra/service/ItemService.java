/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.ostra.dao.ItemDao;
import br.uff.ic.oceano.ostra.dao.impl.ItemDaoImpl;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class ItemService implements PersistenceService{

    private ItemDao itemDao;

    public void setup() {
        itemDao = ObjectFactory.getObjectWithDataBaseDependencies(ItemDaoImpl.class);
    }

    public ItemService() {
    }

    @Transacional
    public void save(Item itemToSave, SoftwareProject project) throws ServiceException {
        try {

            Long id = itemDao.getByPathAndProject(itemToSave.getPath(), project).getId();

            itemToSave.setId(id);

//            throw new ServiceException("Can't exists two items to the same project with the same path.");
        } catch (ObjetoNaoEncontradoException ex) {
            // it is OK
        }

        if (itemToSave.getId() == null) {
            itemDao.inclui(itemToSave);

        } else {

            itemDao.altera(itemToSave);
        }
    }

    public List<Item> getByProject(SoftwareProject project) {
        return itemDao.getByProject(project);
    }
}

