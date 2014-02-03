package br.uff.ic.oceano.core.util;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.util.file.CSVUtils;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.io.File;
import java.util.List;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
/**
 *
 * @author Dancastellani
 */
public class DefaultDatabaseLoaderNGTest extends AbstractNGTest{    

    @BeforeClass
    public static void setUpClass() throws Exception {
        JPAUtil.startUp();
    }

    @Test
    public void insertDefaultData() {
        println("Inserting default data into DB");
        try {
            if (!DefaultDatabaseLoader.isDefaultDataInserted()) {
                DefaultDatabaseLoader.insertDefaultData();
            }
        } catch (Exception ex) {
            fail(ex.getMessage(),ex);
        }
    }
    
    /**
     * Check if metrics are created at the same order. 
     * The database initialization must be a stable process.
     * WARNING: This does not means that hard coded ID values should be used.
     */
    @Test
    public void metricsTableCOnsistency() {
        
        try {
            final File file = getTestFile("CSV/TABLE metric values.csv");
            final String expectedResultsPath = file.getAbsolutePath();
            
            final char delimiter = ';';
            final List<Metric> expectedMetrics = CSVUtils.getAll(expectedResultsPath, delimiter, new CSVUtils.Builder<Metric>() {
                public Metric newInstance(String[] line) {
                    Metric metric = new Metric();
                    //order is fixed
                    int i = 0;
                    metric.setId(Long.decode(line[0]));
                    metric.setAcronym(line[1]);                  
                    metric.setName(line[7]);                  
                                                          
                    return metric;
                }
            });
            
            MetricService service = new MetricService();
            service.setup();
            List<Metric> resultMetrics = service.getAll();
            
            for (Metric resultMetric : resultMetrics) {
                for (Metric expectedMetric : expectedMetrics) {
                    if(resultMetric.getId() != expectedMetric.getId()){
                        continue;
                    }                    
                    assertEquals(resultMetric.getName(), expectedMetric.getName());
                }
            }
            
        } catch (Exception ex) {
            
        }
    }
}
