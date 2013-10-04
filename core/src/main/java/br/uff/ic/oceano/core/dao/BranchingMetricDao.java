/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.model.BranchingMetric;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public interface BranchingMetricDao extends DaoGenerico<BranchingMetric, Long>{
    public List<BranchingMetric> getAll();

}
