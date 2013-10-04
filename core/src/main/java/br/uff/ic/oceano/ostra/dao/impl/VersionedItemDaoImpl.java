/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.dao.VersionedItemDao;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;

/**
 *
 * @author DanCastellani
 */
public class VersionedItemDaoImpl extends JPADaoGenerico<VersionedItem, Long> implements VersionedItemDao {

    public VersionedItemDaoImpl() {
        super(VersionedItem.class);
    }

    @MetodoRecuperaUnico
    public VersionedItem getByItemAndRevision(Item i, Revision r) throws ObjetoNaoEncontradoException{
        throw new MetodoInterceptadoException();
    }

}
