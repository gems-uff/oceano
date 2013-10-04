/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ourico.model.Estado;
import java.util.List;

/**
 *
 * @author marapao
 */
public interface EstadoDao extends DaoGenerico<Estado, Long> {

    public List<Estado> getByAutobranch(Long autobranch);
    public Estado getByAutobranchDescricao(Long autobranch, String Descricao) throws ObjetoNaoEncontradoException;
}
