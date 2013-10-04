/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import java.io.File;
import java.util.Iterator;
import javancss.FunctionMetric;
import javancss.Javancss;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class TotalCyclomaticComplexityExtractorJava extends AbstractMetricExtractor {

    public TotalCyclomaticComplexityExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        final Javancss javancss = new Javancss(new File(path));
        int sumccn = 0;
        Iterator i = javancss.getFunctionMetrics().iterator();

        while (i.hasNext()) {
            FunctionMetric function = (FunctionMetric) i.next();
            sumccn += function.ccn;
        }
        if (sumccn == 0) {
            sumccn = 1;
        }
        
        if (javancss.getLastError() != null)        
            throw new MetricException("It has not been possible to measure " + path);

        return createMetricValue(revision, sumccn);
    }
}
