/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.Repository;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface RepositoryDao extends DaoGenerico<Repository, Long> {

    public Repository getByName(String nome) throws ObjetoNaoEncontradoException;

    public List<Repository> getAll();

    public Repository getByProject(SoftwareProject project) throws ObjetoNaoEncontradoException;
}
