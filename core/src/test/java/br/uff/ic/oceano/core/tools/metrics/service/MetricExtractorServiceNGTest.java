/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.service;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Heraclio
 */
public class MetricExtractorServiceNGTest extends AbstractNGTest{
    
   private MetricExtractorService service;

    public MetricExtractorServiceNGTest() {
        super();
        service = new MetricExtractorService();
        service.setup();
    }
      
    @Test
    public void testGetMetricExtractorsByMetric() {        
        println("getMetricExtractorsByMetric");
        MetricService mService = new MetricService();
        mService.setup();
        for (Metric metric : mService.getAll()) {
            if(metric.isDerived()){
                //Derived metrics have no metric extractors
                continue;
            }
            
            List<MetricExtractor> mext = service.getMetricExtractorsByMetric(metric);
            assertNotNull(mext);
            if(mext.isEmpty()){
               println("Empty metric extractor for "+metric);
            }
            assertTrue(!mext.isEmpty(),"Need at least one metricextractor");            
        }

    }
    
    @Test
    public void testGetAll() {
        println("getAll");
        for (MetricExtractor mext : service.getAll()) {
            assertTrue(mext.getLanguage() != null);
            assertTrue(mext.getMetric() != null);
            assertTrue(mext.getMetricExtractorClass() != null);
        }       
    }

    
}