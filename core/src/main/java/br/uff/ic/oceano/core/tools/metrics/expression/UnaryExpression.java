/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.model.transiente.Language;

/**
 *
 * @author wallace
 */
public abstract class UnaryExpression extends MetricExpression {

    private MetricExpression metricExpression;

    protected UnaryExpression(MetricExpression metricExpression) {
        this.metricExpression = metricExpression;
    }

    public MetricExpression getMetricExpression() {
        return metricExpression;
    }

    void setMetricExpression(MetricExpression metricExpression) {
        this.metricExpression = metricExpression;
    }

    @Override
    public int getExtratcsFrom() {
        return metricExpression.getExtratcsFrom();
    }

    @Override
    public boolean isLanguageSupported(Language language) {
        return metricExpression.isLanguageSupported(language);
    }


}
