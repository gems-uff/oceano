/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.service.DerivedMetricService;
import br.uff.ic.oceano.core.tools.metrics.expression.MetricExpression;

/**
 *
 * @author wallace
 *
 * Revision by DHeraclio
 *  Renamed to DerivedMetricManager
 *  moved metricExpression initialization to constructor
 */
public class DerivedMetricManager extends MetricManager {

    private MetricExpression metricExpression;

    public DerivedMetricManager(Metric metric) {
        super(metric);

        if (getMetric() == null) {
            throw new InstantiationError("The metric for derived metric can't be null");
        }
        try {
            DerivedMetricService dms = new DerivedMetricService();
            this.metricExpression = dms.buildExpression(metric.getExpression());
        } catch (ServiceException ex) {
            throw new InstantiationError(ex.getMessage());
        }
    }

    @Override
    public MetricValue extractMetric(Revision revision) throws MetricException {
        double metricValue = metricExpression.getDoubleValue(revision);
        return new MetricValue(revision, getMetric(), metricValue);
    }

    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        double metricValue = Double.valueOf(metricExpression.getDoubleValue(revision, path));
        return new MetricValue(revision, getMetric(), metricValue);
    }

    /**
     *
     * @param language
     * @return
     */
    @Override
    public boolean isLanguageSupported(Language language) {
        //return Language.JAVA.equals(language)?true:false;
        return metricExpression.isLanguageSupported(language);
    }
}
