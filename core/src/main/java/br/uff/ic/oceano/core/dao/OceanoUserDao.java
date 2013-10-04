/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.OceanoUser;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public interface OceanoUserDao extends DaoGenerico<OceanoUser, Long>{

    public OceanoUser getByLogin(String login) throws ObjetoNaoEncontradoException;
    public List<OceanoUser> getAll();

}
