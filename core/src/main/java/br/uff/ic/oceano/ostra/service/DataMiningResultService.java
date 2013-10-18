/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ostra.dao.DataMiningResultDao;
import br.uff.ic.oceano.ostra.dao.impl.DataMiningResultDaoImpl;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class DataMiningResultService implements PersistenceService {

    public static final String SEPARATOR_ANTECESSOR_SUCESSOR_RULE = "==>";
    //
    private DataMiningResultDao dataMiningResultDao;

    public void setup() {
        dataMiningResultDao = ObjectFactory.getObjectWithDataBaseDependencies(DataMiningResultDaoImpl.class);
    }

    public DataMiningResultService() {
    }

    @Transacional
    public void delete(DataMiningResult dataMiningResult) throws ObjetoNaoEncontradoException {
//        dataMiningResult = dataMiningResultDao.getPorIdComLock(dataMiningResult.getId());
        dataMiningResultDao.deleteBySql(dataMiningResult);
    }

    @Transacional
    public void save(DataMiningResult dataMiningResult) {
        dataMiningResult.prepare();

        if (dataMiningResult.getId() == null) {
            dataMiningResultDao.inclui(dataMiningResult);
        } else {
            dataMiningResultDao.altera(dataMiningResult);
        }
    }

    @Transacional
    public void updateDataBase() {
        dataMiningResultDao.updateDataMiningResultResult();
    }

    public DataMiningResult getToDetailById(Long id) throws ObjetoNaoEncontradoException {
        DataMiningResult dmr = dataMiningResultDao.getToDetailById(id);
        dmr.setPrepared(true);
        return dmr;
    }

    /**
     * Removes the mined rules from the resultString.
     * It is needed to save the result because most of the times, the result String is too long to be stored.
     * @param dataMiningResult
     */
    public static void cleanDataMiningResult(DataMiningResult dataMiningResult) {
        //updates the data mining result so that it can be saved.
        final String originalResult = dataMiningResult.getResultData();
        final int indexOfRulesDeclaration = originalResult.indexOf("Best rules found:");
        if (indexOfRulesDeclaration != -1) {
            dataMiningResult.setResultData(originalResult.substring(0, indexOfRulesDeclaration).trim());
        }
    }

    /**
     * 
     * @return 
     */
    @Transacional
    public List<DataMiningResult> getAll() {
        return dataMiningResultDao.getAll();
    }
}
