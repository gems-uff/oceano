/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Revision;

/**
 *
 * @author wallace
 */
public class UnarySub extends UnaryExpression {

    public UnarySub(MetricExpression metricExpression) {
        super(metricExpression);
    }

    @Override
    public double getDoubleValue(Revision revision, String path) throws MetricException {
        return (-1) * getMetricExpression().getDoubleValue(revision, path);
    }

    @Override
    public double getDoubleValue(Revision revision) throws MetricException {
        return (-1) * getMetricExpression().getDoubleValue(revision);
    }
}
