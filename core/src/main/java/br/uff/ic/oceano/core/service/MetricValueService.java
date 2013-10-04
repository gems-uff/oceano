/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.MetricValueDao;
import br.uff.ic.oceano.core.dao.impl.MetricValueDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ostra.service.VersionedItemMetricValueService;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class MetricValueService implements PersistenceService {

    protected MetricValueDao metricValueDao;
    
    public void setup() {
        metricValueDao = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueDaoImpl.class);    
    }

    public MetricValueService() {
    }

    @Transacional
    public void save(MetricValue metricValue) {
        try {
            long metricValueId = metricValueDao.getByRevisionMetricAndDelta(metricValue.getRevision(), metricValue.getMetric(), metricValue.isDelta()).getId();
            metricValue.setId(metricValueId);
            metricValueDao.altera(metricValue);

        } catch (ObjetoNaoEncontradoException ex) {
            metricValueDao.inclui(metricValue);
        }
    }

    public List<MetricValue> getAll() {
        return metricValueDao.getAll();
    }

    public Long getMetricValueId(MetricValue mv) throws ServiceException {
        try {
            return metricValueDao.getMetricValueId(mv.getRevision(), mv.getMetric(), mv.isDelta());
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    @Transacional
    public void saveAll(List<MetricValue> metricsExtracted) {
        for (MetricValue metricValue : metricsExtracted) {
            save(metricValue);
        }
    }

    public List<MetricValue> getByRevision(Revision revision) {
        return metricValueDao.getByRevision(revision);
    }

    public boolean isMeasured(Revision revision, Metric metric) throws ServiceException{
        try {
            return metricValueDao.countByRevisionAndMetric(revision, metric) > 0L;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    public List<MetricValue> getDeltaValuesByProjectAndMetric(SoftwareProject softwareProject, Metric metric) {
        return metricValueDao.getValuesByProjectAndMetric(softwareProject, metric, true);
    }

    public List<MetricValue> getAbsoluteValuesByProjectAndMetric(SoftwareProject softwareProject, Metric metric) {
        return metricValueDao.getValuesByProjectAndMetric(softwareProject, metric, false);
    }

    public MetricValue getByRevisionMetricAndDelta(Revision revision, Metric metric, boolean delta) throws ObjetoNaoEncontradoException {

        return metricValueDao.getByRevisionMetricAndDelta(revision, metric, delta);
    }
}
