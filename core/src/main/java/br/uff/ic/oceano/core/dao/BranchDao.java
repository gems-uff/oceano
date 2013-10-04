/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Branch;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public interface BranchDao extends DaoGenerico<Branch, Long>{
    public List<Branch> getAll();
    public List<Branch> getbyProject(Long id);
    public Branch getbyProjectName(Long idProject, String nameBranch) throws ObjetoNaoEncontradoException;
}
