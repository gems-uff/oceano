/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.model.BranchingModel;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public interface BranchingModelDao extends DaoGenerico<BranchingModel, Long>{
    public List<BranchingModel> getAll();

}
