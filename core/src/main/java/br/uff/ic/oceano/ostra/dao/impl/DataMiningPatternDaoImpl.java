/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.ostra.dao.DataMiningPatternDao;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class DataMiningPatternDaoImpl extends JPADaoGenerico<DataMiningPattern, Long> implements DataMiningPatternDao {

    public DataMiningPatternDaoImpl() {
        super(DataMiningPattern.class);
    }

    public List<DataMiningPattern> getAll() {
        throw new MetodoInterceptadoException();
    }
}
