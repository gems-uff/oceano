/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.model.Item;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface ItemDao extends DaoGenerico<Item, Long> {

    public Item getByPathAndProject(String path, SoftwareProject itemProject) throws ObjetoNaoEncontradoException;

    public List<Item> getByProject(SoftwareProject project);

}
