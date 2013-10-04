/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.BranchDao;
import br.uff.ic.oceano.core.dao.impl.BranchDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Branch;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public class BranchService implements PersistenceService{

    private BranchDao branchDao;

     public void setup() {
        branchDao = ObjectFactory.getObjectWithDataBaseDependencies(BranchDaoImpl.class);
    }

    public BranchService() {
    }

    @Transacional
    public void save(Branch configuracao) {
        if (configuracao.getId() == null) {
            branchDao.inclui(configuracao);
        } else {
            branchDao.altera(configuracao);
        }
    }

    public Branch getById(Long id) throws ObjetoNaoEncontradoException {
        return branchDao.getPorId(id);
    }

    public List<Branch> getAll() {
        return branchDao.getAll();
    }

    public List<Branch> getbyProject(Long id) {
        return branchDao.getbyProject(id);
    }

    public Branch getbyProjectName(Long idProject, String nameBranch) throws ObjetoNaoEncontradoException {
        return branchDao.getbyProjectName(idProject, nameBranch);
    }


}

