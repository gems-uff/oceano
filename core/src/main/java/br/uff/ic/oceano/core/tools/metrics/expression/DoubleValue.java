/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;

/**
 *
 * @author wallace
 */
public class DoubleValue extends MetricExpression {

    private double doubleValue;

    public DoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public double getDoubleValue(Revision revision, String path) {
        return doubleValue;
    }

    @Override
    public double getDoubleValue(Revision revision) throws MetricException {
        return doubleValue;
    }

    public int getExtratcsFrom() {
        return Metric.EXTRACTS_FROM_PROJECT;
    }

    @Override
    public boolean isLanguageSupported(Language language) {
        return true;
    }

}
