/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.BranchingMetricDao;
import br.uff.ic.oceano.core.dao.impl.BranchingMetricDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.BranchingMetric;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public class BranchingMetricService implements PersistenceService{

    private BranchingMetricDao branchingMetricDao;

    public void setup() {
        branchingMetricDao = ObjectFactory.getObjectWithDataBaseDependencies(BranchingMetricDaoImpl.class);
    }

    public BranchingMetricService() {
    }

    @Transacional
    public void save(BranchingMetric configuracao) {
        if (configuracao.getId() == null) {
            branchingMetricDao.inclui(configuracao);
        } else {
            branchingMetricDao.altera(configuracao);
        }
    }

    public BranchingMetric getById(Long id) throws ObjetoNaoEncontradoException {
        return branchingMetricDao.getPorId(id);
    }

    public List<BranchingMetric> getAll() {
        return branchingMetricDao.getAll();
    }

}

