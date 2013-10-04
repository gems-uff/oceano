/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import gr.spinellis.ckjm.ClassMetrics;
import gr.spinellis.ckjm.ClassMetricsContainer;
import gr.spinellis.ckjm.ClassVisitor;
import java.io.IOException;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class LackOfCohesionOfMethodsExtractorJava extends AbstractMetricExtractor {

    public LackOfCohesionOfMethodsExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            JavaClass jc = new ClassParser(path).parse();
            ClassMetricsContainer cm = new ClassMetricsContainer();
            
            ClassVisitor visitor = new ClassVisitor(jc, cm);
            visitor.start();
            visitor.end();

            ClassMetrics cmMetrics = cm.getMetrics(jc.getClassName());
            return createMetricValue(revision, cmMetrics.getLcom());
        } catch (IOException e) {
            throw new MetricException(e);
        }
    }
}
