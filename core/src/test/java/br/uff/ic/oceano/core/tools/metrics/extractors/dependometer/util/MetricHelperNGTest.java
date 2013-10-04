/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import java.util.LinkedList;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author pabla
 */
public class MetricHelperNGTest extends AbstractNGTest {

    /**
     * Checks if all dependometer metrics are classified
     */
    @Test
    public void testMetricsClassification() throws Exception {
        println("test MetricsClassification");

        List<MetricEnum> unclassified = new LinkedList<MetricEnum>();
        for (MetricEnum metric : MetricEnum.values()) {
            if (MetricHelper.isCompilationUnitMetric(metric)
                    || MetricHelper.isTypeMetric(metric)
                    || MetricHelper.isLayerMetric(metric)
                    || MetricHelper.isPackageMetric(metric)
                    || MetricHelper.isProjectMetric(metric)
                    || MetricHelper.isSubsystemMetric(metric)
                    || MetricHelper.isVerticalSliceMetric(metric)
                    || !MetricHelper.isImplemented(metric)) {
                continue;
            }
            unclassified.add(metric);
        }
        if (!unclassified.isEmpty()) {
            println("Found unclassified metrics: ");
            for (MetricEnum metricEnum : unclassified) {
                println(metricEnum.name());
            }
        }

        assertTrue(unclassified.isEmpty(), "Found unclassified metrics");
    }
}