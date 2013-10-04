/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.RepositoryDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.Repository;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class RepositoryDaoImpl extends JPADaoGenerico<Repository, Long> implements RepositoryDao {

    public RepositoryDaoImpl() {
        super(Repository.class);
    }

    @MetodoRecuperaUnico
    public Repository getByName(String nome) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Repository> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Repository getByProject(SoftwareProject project) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }
}
