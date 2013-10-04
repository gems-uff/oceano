/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.service;

import br.uff.ic.oceano.core.dao.MetricExtractorDao;
import br.uff.ic.oceano.core.dao.impl.MetricExtractorDaoImpl;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author dheraclio
 */
public class MetricExtractorService implements PersistenceService {

    private MetricExtractorDao metricExtractorDao;

    public void setup() {
        metricExtractorDao = ObjectFactory.getObjectWithDataBaseDependencies(MetricExtractorDaoImpl.class);
    }

    public List<MetricExtractor> getMetricExtractorsByMetric(Metric metric) {
        return metricExtractorDao.getMetricExtractorsByMetric(metric);
    }

    public List<MetricExtractor> getAll() {
        return metricExtractorDao.getAll();
    }

    @Transacional
    public void save(MetricExtractor extractor) {
        metricExtractorDao.inclui(extractor);
    }
}
