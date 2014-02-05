/*
 * 
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.tools.datamining.util.DatabaseToArffService;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Heraclio <dheraclio@gmail.com>
 */
public class DeltaMetricsRevisionDataBaseServiceNGTest extends AbstractNGTest {

    private DeltaMetricsRevisionDataBaseService deltaMetricsRevisionDataBaseService;

    @BeforeClass
    public void setUpClass() throws Exception {
        this.deltaMetricsRevisionDataBaseService = ObjectFactory.getObjectWithDataBaseDependencies(DeltaMetricsRevisionDataBaseService.class);

        //clear deltas in database before run test
        //delete from metricvalue where delta=true
    }

    /**
     */
    @Test
    public void testBuildDeltaMetricsDataBase() throws Exception {
        final List<Discretizer> discretizers = DeltaMetricsRevisionDataBaseService.getDefaultDiscretizers();
        final List<Metric> metrics = getMetrics();
        final List<SoftwareProject> projects = getProjects();

        DataBaseSnapshot snapshot = deltaMetricsRevisionDataBaseService.buildDeltaMetricsDataBase(projects, discretizers, true, false, metrics, true);
        println("ARRF");
        println(DatabaseToArffService.dataBaseToARFF(snapshot, discretizers));
    }

    private List<SoftwareProject> getProjects() throws ServiceException {
        try {
            List<SoftwareProject> projects = new ArrayList<SoftwareProject>();

            SoftwareProject neopzLocal = CppProjectsHelper.getDBNeoPZProject();
            assertNotNull(neopzLocal);
            projects.add(neopzLocal);

            return projects;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    private List<Metric> getMetrics() throws ServiceException {
        List<Metric> metrics = new ArrayList();

        MetricManagerFactory fact = MetricManagerFactory.getInstance();

        final MetricManager mng = fact.getMetricByName(MetricEnumeration.TLOC.getName());
        Assert.assertNotNull(mng);
        metrics.add(mng.getMetric());

//        for (MetricManager mngTemp : fact.getMetricManagers()) {
//            if (!mngTemp.isLanguageSupported(Language.CPP)) {
//                continue;
//            }
//            metrics.add(mngTemp.getMetric());
//        }
        return metrics;
    }
}