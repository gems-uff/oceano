/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.service;

import br.uff.ic.oceano.core.dao.MetricDao;
import br.uff.ic.oceano.core.dao.impl.MetricDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class MetricService implements PersistenceService {

    private MetricDao metricDao;

    public void setup() {
        metricDao = ObjectFactory.getObjectWithDataBaseDependencies(MetricDaoImpl.class);
    }

    public MetricService() {
    }

    public static Metric getMetricByName(String metricName) throws ServiceException {
        MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
        return metricService.getMetric(metricName);
    }

    public Metric getMetric(String nome) throws ServiceException {
        try {
            return metricDao.getByNome(nome);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<Metric> getAll() {
        return metricDao.getAll();
    }

    @Transacional
    public void save(Metric m) {
        metricDao.inclui(m);
    }

    public List<Metric> getMetricsByProject(SoftwareProject project) {
        //From metric value
        List<Metric> metricsList = metricDao.getMetricsByProjectRevisions(project);

        //And also from versioned item metric value
        for (Metric metric : metricDao.getMetricsByProjectVersionedItems(project)) {
            if (!metricsList.contains(metric)) {
                metricsList.add(metric);
            }
        }
        return metricsList;
    }

    /**
     *
     * @param acronym
     * @return
     * @throws ObjetoNaoEncontradoException when there is no metric with the acronym
     */
    public Metric getByAcronym(final String acronym) throws ObjetoNaoEncontradoException {
        return metricDao.getByAcronym(acronym);
    }
    
    public static boolean isLanguageAvailable(final Language language, final Metric metric) {
        MetricExtractorService ms = new MetricExtractorService();
        ms.setup();
        
        List<MetricExtractor> mextractors = ms.getMetricExtractorsByMetric(metric);
        Iterator<MetricExtractor> it = mextractors.iterator();
        while(it.hasNext()){
            final MetricExtractor ext = it.next();
            if (language.isSame(ext.getLanguage())){
                return true;
            }
        }
        return false;
    
    }
}
