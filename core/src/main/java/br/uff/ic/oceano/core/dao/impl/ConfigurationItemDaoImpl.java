/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.ConfigurationItemDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import java.util.List;

/**
 *
 * @author marapao
 */
public class ConfigurationItemDaoImpl extends JPADaoGenerico<ConfigurationItem, Long> implements ConfigurationItemDao {

    public ConfigurationItemDaoImpl(){
        super(ConfigurationItem.class);
    }

    @MetodoRecuperaLista
    public List<ConfigurationItem> getAll() {
        throw new MetodoInterceptadoException();
    }

}
