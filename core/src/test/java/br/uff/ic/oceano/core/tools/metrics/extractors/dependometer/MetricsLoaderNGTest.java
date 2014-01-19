/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.MetricHelper;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Heraclio
 */
public class MetricsLoaderNGTest extends AbstractNGTest {

    @Test
    public void testGetSelf() {
        println("getSelf");
        assertNotNull(MetricsLoader.getSelf(), "Null metric loader");
    }

    @Test
    public void testMetricSetChanged() {
        println("checkIfMetricSetHasChanged");
        assertEquals(MetricEnum.values().length, 103, "Metric set changed");

        //count undefined metrics
        List<MetricEnum> undefined = new LinkedList<MetricEnum>();
        for (MetricEnum mEnum : MetricEnum.values()) {
            try {
                MetricHelper.isNumberMetric(mEnum);
            } catch (DependometerException ex) {
                undefined.add(mEnum);
            }
        }
        assertEquals(undefined.size(),4, "Undefined metrics set changed");

        //count numeric metrics
        try {
            int count = 0;
            for (MetricEnum mEnum : MetricEnum.values()) {
                //ignore undefined
                if (undefined.contains(mEnum)) {
                    continue;
                }
                count += MetricHelper.isNumberMetric(mEnum) ? 1 : 0;
            }
            assertEquals(count, 75, "Metric set changed");
        } catch (DependometerException ex) {
            fail(ex.getMessage(), ex);
        }
                
        try {
            Set<MetricEnum> valids = new HashSet<MetricEnum>();
            
            List<MetricEnum> metrics = MetricHelper.getValidProjectMetrics();
            assertEquals(metrics.size(), 53, "Valid Project metrics set changed");
            valids.addAll(metrics);
            
            metrics = MetricHelper.getValidPackageMetrics();
            assertEquals(metrics.size(), 19, "Valid Package Metrics set changed");
            valids.addAll(metrics);
            
            metrics = MetricHelper.getValidCompilationUnitMetrics();
            assertEquals(metrics.size(), 12, "Valid CompilationUnit Metrics set changed");
            valids.addAll(metrics);
            
            metrics = MetricHelper.getValidTypeMetrics();
            assertEquals(metrics.size(), 10, "Valid Type metrics set changed");
            valids.addAll(metrics);
            
            assertEquals(valids.size(), 72, "Valid metric set changed");
        } catch (DependometerException ex) {
            fail(ex.getMessage(),ex);
        }
        
    }
}