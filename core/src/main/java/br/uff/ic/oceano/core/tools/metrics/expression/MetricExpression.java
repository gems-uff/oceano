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
public abstract class MetricExpression {

    abstract public double getDoubleValue(Revision revision) throws MetricException;

    abstract public double getDoubleValue(Revision revision, String path) throws MetricException;

    abstract public int getExtratcsFrom();

    abstract public boolean isLanguageSupported(Language language);
}
