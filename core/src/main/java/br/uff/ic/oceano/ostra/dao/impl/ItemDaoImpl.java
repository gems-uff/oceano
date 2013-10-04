/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.dao.ItemDao;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.ostra.model.Item;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class ItemDaoImpl extends JPADaoGenerico<Item, Long> implements ItemDao {

    public ItemDaoImpl() {
        super(Item.class);
    }

    @MetodoRecuperaUnico
    public Item getByPathAndProject(String path, SoftwareProject itemProject) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Item> getByProject(SoftwareProject project) {
        throw new MetodoInterceptadoException();
    }
}
