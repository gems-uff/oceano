/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;

/**
 *
 * @author wallace
 */
public class Div extends BinaryExpression {

    public Div(MetricExpression left, MetricExpression right) {
        super(left, right);
    }

    public double getDoubleValue(Revision revision, String path) throws MetricException {
        return getLeft().getDoubleValue(revision, path) / getRight().getDoubleValue(revision, path);
    }

    @Override
    public double getDoubleValue(Revision revision) throws MetricException {
        return getLeft().getDoubleValue(revision) / getRight().getDoubleValue(revision);
    }
}
