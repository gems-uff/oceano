/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javancss.FunctionMetric;
import javancss.Javancss;

/**
 *
 * @author DHeraclio
 */
public class CyclomaticComplexityExtractorJava extends AbstractMetricExtractor {

    public CyclomaticComplexityExtractorJava() {

    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        final Javancss javancss = new Javancss(new File(path));
        int ccn = 0;
        int count = 0;
        float avrgCcn = 0;
        List functions;
        FunctionMetric function;
        Iterator i;
        functions = javancss.getFunctionMetrics();
        i = functions.iterator();
        while (i.hasNext()) {
            function = (FunctionMetric) i.next();
            ccn = function.ccn;
            avrgCcn = avrgCcn + ccn;
            count++;
        }
        if (avrgCcn == 0) {
            avrgCcn = 1;
        }

        if (count != 0) {
            avrgCcn /= count;
        }
        
        if (javancss.getLastError() != null)        
            throw new MetricException("It has not been possible to measure " + path);

        return createMetricValue(revision, avrgCcn);
    }
}
