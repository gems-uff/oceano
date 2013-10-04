/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.OceanoUser;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class OceanoUserDaoImpl extends JPADaoGenerico<OceanoUser, Long> implements OceanoUserDao {

    public OceanoUserDaoImpl(){
        super(OceanoUser.class);
    }

    @MetodoRecuperaUnico
    public OceanoUser getByLogin(String login) throws ObjetoNaoEncontradoException{
        throw new MetodoInterceptadoException();
//        return mapaUsuarios.get(login);
    }

    @MetodoRecuperaLista
    public List<OceanoUser> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
