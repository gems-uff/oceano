/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;

/**
 *
 * @author wallace
 */
public class Sqrt extends UnaryExpression {

    public Sqrt(MetricExpression metricExpression) {
        super(metricExpression);
    }

    public double getDoubleValue(Revision revision, String path) throws MetricException {
        return Math.sqrt(getMetricExpression().getDoubleValue(revision, path));
    }

    @Override
    public double getDoubleValue(Revision revision) throws MetricException {
        return Math.sqrt(getMetricExpression().getDoubleValue(revision));
    }
}
