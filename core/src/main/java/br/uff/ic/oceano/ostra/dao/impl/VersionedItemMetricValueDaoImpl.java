/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.ostra.dao.VersionedItemMetricValueDao;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author DanCastellani
 */
public class VersionedItemMetricValueDaoImpl extends JPADaoGenerico<VersionedItemMetricValue, Long> implements VersionedItemMetricValueDao {

    public VersionedItemMetricValueDaoImpl() {
        super(VersionedItemMetricValue.class);
    }

    public long countByRevisionAndMetric(Revision revision, Metric metric) {
        final String strQuery = "select count( distinct vimv.id ) "
                + "from ostra_VersionedItemMetricValue vimv "
                + "join ostra_VersionedItem vi on vi.id = vimv.idVersionedItem "
                + "join revision r on r.id = vi.idRevision "
                + "where r.idproject = ? and r.number = ? and vimv.idMetric = ?";

        Query query = JPAUtil.getEntityManager().createNativeQuery(strQuery);
        query.setParameter(1, revision.getProject().getId());
        query.setParameter(2, revision.getNumber());
        query.setParameter(3, metric.getId());

        return NumberUtil.longValueOf(query.getSingleResult());
    }

    @MetodoRecuperaLista
    public List<VersionedItemMetricValue> getByRevisionAndMetric(Revision revision, Metric metric) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<VersionedItemMetricValue> getByRevision(Revision revision) {
        throw new MetodoInterceptadoException();
    }

//    @MetodoRecuperaPrimeiro
    public VersionedItemMetricValue getNewestByItemAndMetricUntilRevision(Item item, Metric metric, Long revisionNumber) {
//        throw new MetodoInterceptadoException();
        final String strQuery = "select vimv "
                + " from VersionedItemMetricValue vimv "
                + " where vimv.versionedItem.item = ? "
                + "     and vimv.metric = ? "
                + "     and vimv.versionedItem.revision.number <= ? "
                + " order by vimv.versionedItem.revision.number desc";

        Query query = JPAUtil.getEntityManager().createQuery(strQuery);
        query.setParameter(1, item);
        query.setParameter(2, metric);
        query.setParameter(3, revisionNumber);

        List resultList = query.getResultList();

        return (VersionedItemMetricValue) (resultList == null || resultList.isEmpty() ? null : resultList.get(0));
    }
}
