/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.BranchingMetricDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.model.BranchingMetric;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public class BranchingMetricDaoImpl extends JPADaoGenerico<BranchingMetric, Long> implements BranchingMetricDao {

    public BranchingMetricDaoImpl() {
        super(BranchingMetric.class);
    }
    
    @MetodoRecuperaLista
    public List<BranchingMetric> getAll() {
        throw new MetodoInterceptadoException();
    }

}
