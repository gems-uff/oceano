/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.ostra.dao.VersionedItemDao;
import br.uff.ic.oceano.ostra.dao.impl.VersionedItemDaoImpl;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;

/**
 *
 * @author Heliomar
 */
public class VersionedItemService implements PersistenceService{

    private VersionedItemDao versionedItemDao;

    public void setup() {
        versionedItemDao = ObjectFactory.getObjectWithDataBaseDependencies(VersionedItemDaoImpl.class);
    }

    public VersionedItemService() {
    }

    @Transacional
    public void save(VersionedItem versionedItemToSave) throws ServiceException {
        try {
            final Revision r = versionedItemToSave.getRevision();
            final Item i = versionedItemToSave.getItem();
            final Long id = versionedItemDao.getByItemAndRevision(i, r).getId();
            versionedItemToSave.setId(id);
            
        } catch (ObjetoNaoEncontradoException ex) {
            //ok, its new.
//            Logger.getLogger(VersionedItemService.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (versionedItemToSave.getId() == null) {
            versionedItemDao.inclui(versionedItemToSave);

        } else {
            versionedItemDao.altera(versionedItemToSave);
        }

    }
}

