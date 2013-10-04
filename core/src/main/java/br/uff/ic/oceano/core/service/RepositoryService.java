/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.RepositoryDao;
import br.uff.ic.oceano.core.dao.impl.RepositoryDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Repository;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class RepositoryService implements PersistenceService{

    private RepositoryDao repositoryDao;

    public void setup() {
        repositoryDao = ObjectFactory.getObjectWithDataBaseDependencies(RepositoryDaoImpl.class);
    }

    public RepositoryService() {
    }

    public List<Repository> getAll() {
        return repositoryDao.getAll();
    }

    public Repository getByName(String name) throws ObjetoNaoEncontradoException {
        return repositoryDao.getByName(name);
    }

    @Transacional
    public void save(Repository repository) throws ServiceException {
        try {
            repositoryDao.getByName(repository.getName());

            throw new ServiceException("Não podem haver dois repositórios com mesmo nome.");
        } catch (ObjetoNaoEncontradoException ex) {
            // it is OK
        }

        if (repository.getId() == null) {
            repositoryDao.inclui(repository);
        } else {
            repositoryDao.altera(repository);
        }
    }

}

