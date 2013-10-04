/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface DataMiningPatternDao extends DaoGenerico<DataMiningPattern, Long> {

    public List<DataMiningPattern> getAll();
}
