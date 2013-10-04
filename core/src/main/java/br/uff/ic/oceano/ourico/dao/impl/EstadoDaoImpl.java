/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ourico.dao.EstadoDao;
import br.uff.ic.oceano.ourico.model.Estado;
import java.util.List;

/**
 *
 * @author marapao
 *
 */
public class EstadoDaoImpl extends JPADaoGenerico<Estado, Long> implements EstadoDao {

    public EstadoDaoImpl() {
        super(Estado.class);
    }

    @MetodoRecuperaLista
    public List<Estado> getByAutobranch(Long autobranch) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @MetodoRecuperaUnico
    public Estado getByAutobranchDescricao(Long autobranch, String descricao) throws ObjetoNaoEncontradoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
