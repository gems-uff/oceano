/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;

/**
 *
 * @author DanCastellani
 */
public interface VersionedItemDao extends DaoGenerico<VersionedItem, Long> {

    public VersionedItem getByItemAndRevision(Item i, Revision r)throws ObjetoNaoEncontradoException;

}
