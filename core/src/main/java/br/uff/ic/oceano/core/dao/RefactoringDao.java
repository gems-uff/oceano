/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import java.util.List;

/**
 *
 * @author heliokann
 */
public interface RefactoringDao extends DaoGenerico<Refactoring, Long> {

    public Refactoring getByName(String nome) throws ObjetoNaoEncontradoException;

    public List<Refactoring> getAll();

    public List<Refactoring> getByType(int tipo);

    public List<Refactoring> getByPopularType(int tipo, Integer quantidade);
}
