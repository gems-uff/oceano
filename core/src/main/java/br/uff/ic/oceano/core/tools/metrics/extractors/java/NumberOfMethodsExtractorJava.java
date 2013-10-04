/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import org.apache.bcel.classfile.ClassParser;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class NumberOfMethodsExtractorJava extends AbstractMetricExtractor {

    public NumberOfMethodsExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            org.apache.bcel.classfile.Method met[] = new ClassParser(path).parse().getMethods();
            return createMetricValue(revision, met.length);
        } catch (Exception e) {
            throw new MetricException(e);
        }
    }
}
