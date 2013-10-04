/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.ostra.exception.CompilerException;

/**
 * //TODO split between extractor for serializing and abstract class
 * @author DHeraclio
 *
 */
public abstract class AbstractMetricExtractor implements IMetricExtractor {

    private Metric metric;

    private Language language;

    private MetricManager metricManager;

    /**
     *
     * @param revision
     * @return
     * @throws MetricException
     */
    protected MetricValue extractMetricWithMetricService(Revision revision) throws MetricException {
        try {
            return MeasurementService.extractMetric(getMetricManager(), revision);
        } catch (CompilerException ex) {
            throw new MetricException(ex);
        } catch (ServiceException ex) {
            throw new MetricException(ex);
        }
    }
    /**
     * Return new instance of MetricValue without the metric attribute set.
     *
     * @param revision
     * @param value
     * @return
     */
    public MetricValue createMetricValue(Revision revision, Double value){
        //Do not create metric for null values.
        if( value == null){
            return null;            
        }
        
        MetricValue metricValue = new MetricValue();
        metricValue.setRevision(revision);
        metricValue.setMetric(getMetric());
        metricValue.setDoubleValue(NumberUtil.roundDecimal(value));

        return metricValue;
    }

    public MetricValue createMetricValue(Revision revision,float value){
        return createMetricValue(revision, Double.valueOf(value));
    }

    /**
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * @return the metric
     */
    public Metric getMetric() {
        return metric;
    }

    /**
     * @param metric the metric to set
     */
    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    /**
     * @return the metricManager
     */
    public MetricManager getMetricManager() {
        return metricManager;
    }

    /**
     * @param metricManager the metricManager to set
     */
    public void setMetricManager(MetricManager metricManager) {
        this.metricManager = metricManager;
    }
}
