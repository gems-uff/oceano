/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.expression;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.transiente.Language;

/**
 *
 * @author wallace
 */
public abstract class BinaryExpression extends MetricExpression {

    private MetricExpression left;
    private MetricExpression right;

    protected BinaryExpression(MetricExpression left, MetricExpression right) {
        this.left = left;
        this.right = right;
    }

    public MetricExpression getLeft() {
        return left;
    }

    public MetricExpression getRight() {
        return right;
    }

    void setLeft(MetricExpression left) {
        this.left = left;
    }

    void setRight(MetricExpression right) {
        this.right = right;
    }

    @Override
    public boolean isLanguageSupported(Language language) {
        return left.isLanguageSupported(language) && right.isLanguageSupported(language);

    }

    public int getExtratcsFrom() {
        int leftExtratcsFrom, rightExtratcsFrom;
        leftExtratcsFrom = left.getExtratcsFrom();
        if (leftExtratcsFrom == Metric.EXTRACTS_FROM_FILE) {
            return leftExtratcsFrom;
        }
        rightExtratcsFrom = right.getExtratcsFrom();
        if (leftExtratcsFrom < rightExtratcsFrom) {
            return leftExtratcsFrom;
        }
        return rightExtratcsFrom;
    }
}
