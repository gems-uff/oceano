/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.BranchingModelDao;
import br.uff.ic.oceano.core.dao.impl.BranchingModelDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.BranchingModel;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public class BranchingModelService implements PersistenceService{

    private BranchingModelDao branchingModelDao;

    public void setup() {
        branchingModelDao = ObjectFactory.getObjectWithDataBaseDependencies(BranchingModelDaoImpl.class);
    }

    public BranchingModelService() {
    }

    @Transacional
    public void save(BranchingModel configuracao) {
        if (configuracao.getId() == null) {
            branchingModelDao.inclui(configuracao);
        } else {
            branchingModelDao.altera(configuracao);
        }
    }

    public BranchingModel getById(Long id) throws ObjetoNaoEncontradoException {
        return branchingModelDao.getPorId(id);
    }

    public List<BranchingModel> getAll() {
        return branchingModelDao.getAll();
    }

}

