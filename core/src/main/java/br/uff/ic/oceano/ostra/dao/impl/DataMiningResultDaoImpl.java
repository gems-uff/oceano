/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.ostra.dao.DataMiningResultDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author DanCastellani
 */
public class DataMiningResultDaoImpl extends JPADaoGenerico<DataMiningResult, Long> implements DataMiningResultDao {

    public DataMiningResultDaoImpl() {
        super(DataMiningResult.class);
    }

    @MetodoRecuperaLista
    public List<DataMiningResult> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public DataMiningResult getToDetailById(Long currentDataMiningResultsId) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    public void updateDataBase() {
    }

    public void updateDataMiningResultResult() {
        final String update1 = "ALTER TABLE ostra_dataminingresult ALTER dataResult TYPE character varying;";
        final String update2 = "ALTER TABLE ostra_dataminingresult ALTER arff TYPE character varying;";

        EntityManager em = JPAUtil.getEntityManager();
        Query query1 = em.createNativeQuery(update1);
        Query query2 = em.createNativeQuery(update2);

        query1.executeUpdate();
        query2.executeUpdate();
    }

    public void deleteBySql(DataMiningResult dataMiningResult) {
//        final String delete0 = "select ostra_dataminingpattern where iddataminingresult = " + dataMiningResult.getId() + " for update;";
        final String delete1 = "delete from ostra_dataminingpattern where iddataminingresult = " + dataMiningResult.getId() + ";";
        final String delete2 = "delete from ostra_dataminingresult where id = " + dataMiningResult.getId() + ";";

        EntityManager em = JPAUtil.getEntityManager();
//        Query query0 = em.createNativeQuery(delete0);
        Query query1 = em.createNativeQuery(delete1);
        Query query2 = em.createNativeQuery(delete2);

//        query0.getSingleResult();
        query1.executeUpdate();
        query2.executeUpdate();
    }
}
