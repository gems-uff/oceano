/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface DataMiningResultDao extends DaoGenerico<DataMiningResult, Long> {

    public List<DataMiningResult> getAll();

    public DataMiningResult getToDetailById(Long currentDataMiningResultsId) throws ObjetoNaoEncontradoException;

    public void updateDataMiningResultResult();

    public void deleteBySql(DataMiningResult dataMiningResult);

}
