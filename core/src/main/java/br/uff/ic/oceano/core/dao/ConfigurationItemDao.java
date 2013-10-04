/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import java.util.List;

/**
 *
 * @author marapao
 */
public interface ConfigurationItemDao extends DaoGenerico<ConfigurationItem, Long>{

    public List<ConfigurationItem> getAll();
}
