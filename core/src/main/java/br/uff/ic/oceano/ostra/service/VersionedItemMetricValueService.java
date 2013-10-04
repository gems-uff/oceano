/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.ostra.dao.VersionedItemMetricValueDao;
import br.uff.ic.oceano.ostra.dao.impl.VersionedItemMetricValueDaoImpl;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ostra.model.Item;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class VersionedItemMetricValueService implements PersistenceService {

    private VersionedItemMetricValueDao versionedItemMetricValueDao;

    public void setup() {
        versionedItemMetricValueDao = ObjectFactory.getObjectWithDataBaseDependencies(VersionedItemMetricValueDaoImpl.class);
    }

    public VersionedItemMetricValueService() {
    }

    @Transacional
    public void save(VersionedItemMetricValue versionedItemMetricValueToSave) throws ServiceException {
        if (versionedItemMetricValueToSave.getId() == null) {
            versionedItemMetricValueDao.inclui(versionedItemMetricValueToSave);

        } else {
            versionedItemMetricValueDao.altera(versionedItemMetricValueToSave);
        }
    }

    /**
     * Used to deltas, were the value has not changed.
     * @param metricManager
     * @param versionedItem
     * @return 
     */
    public VersionedItemMetricValue createVersionedMetricWithZeroValue(MetricManager metricManager, VersionedItem versionedItem) {
        final Revision revision = versionedItem.getRevision();

        VersionedItemMetricValue vimv = new VersionedItemMetricValue();
        vimv.setDoubleValue(0D);
//        vimv.setValue("0");
        vimv.setMetric(metricManager.getMetric());
        vimv.setVersionedItem(versionedItem);
        versionedItem.setRevision(revision);
        return vimv;
    }

    public boolean isMeasured(Revision revision, Metric metric) {
        return versionedItemMetricValueDao.countByRevisionAndMetric(revision, metric) > 0L;
    }

    public List<VersionedItemMetricValue> getByRevisionAndMetric(Revision revision, Metric metric) {
        return versionedItemMetricValueDao.getByRevisionAndMetric(revision, metric);
    }

//    @Deprecated
//    public MetricValue getMetricValueFromVersionedItemsByRevisionAndMetric(Revision revision, Metric metric) {
//        MetricValue valueToReturn;
//        List<VersionedItemMetricValue> versionedItemMetricValues = versionedItemMetricValueDao.getByRevisionAndMetric(revision, metric);
//        Double doubleValue = 0D;
//        for (VersionedItemMetricValue versionedItemMetricValue : versionedItemMetricValues) {
//            doubleValue += versionedItemMetricValue.getDoubleValue();
//        }
//        if (!versionedItemMetricValues.isEmpty()) {
//            doubleValue /= versionedItemMetricValues.size();
//        }
//        valueToReturn = new MetricValue(revision, metric, doubleValue);
//        return valueToReturn;
//    }

    public List<VersionedItemMetricValue> getByRevision(Revision revision) {
        return versionedItemMetricValueDao.getByRevision(revision);
    }

    public VersionedItemMetricValue getNewestByItemAndMetricUntilRevision(Item item, Metric metric, Revision revision) {
        return versionedItemMetricValueDao.getNewestByItemAndMetricUntilRevision(item, metric, revision.getNumber());
    }
}
