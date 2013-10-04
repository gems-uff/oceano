/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wallace
 */
public class MetricManagerExpression extends MetricExpression {

    private MetricManager metricManager;

    public MetricManagerExpression(MetricManager metricManager) {
        this.metricManager = metricManager;
    }

    @Override
    public double getDoubleValue(Revision revision) throws MetricException {
        MetricValue metricValue = null;
        try {
            //verify if this metric is already measured for this revision
            if (revision.getId() != null) {
                final MetricValueService metricValueService = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueService.class);

                metricValue = metricValueService.getByRevisionMetricAndDelta(revision, metricManager.getMetric(), false);
            }
        } catch (ObjetoNaoEncontradoException ex) {
            //if it is not yet measured, than measure it.
        }

        if (metricValue == null) {
            return metricManager.extractMetric(revision).getDoubleValue();
        } else {
            return metricValue.getDoubleValue();
        }
    }

    @Override
    public double getDoubleValue(Revision revision, String path) throws MetricException {
        return metricManager.extractMetric(revision, path).getDoubleValue();
    }

    @Override
    public int getExtratcsFrom() {
        Metric metric = metricManager.getMetric();
        return metric.getExtratcsFrom();
    }

    @Override
    public boolean isLanguageSupported(Language language) {
        return metricManager.isLanguageSupported(language);
    }


}
