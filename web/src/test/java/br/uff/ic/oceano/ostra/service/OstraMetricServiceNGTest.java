/*
 * 
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
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
public class OstraMetricServiceNGTest extends AbstractNGTest{
        
    private static boolean isRunningOnMemoryDb;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        isRunningOnMemoryDb = JPAUtil.isRunningOnMemoryDB();
    }

    @Test
    public void testExtractAndSaveMetricsFromAllFilesInProjectRevisions() throws Exception {
        println("extractAndSaveMetricsFromAllFilesInProjectRevisions");
        
        if(!isRunningOnMemoryDb){
            println("Test ignored. Not running memory database");
            return;
        }
        
        SoftwareProject project = CppProjectsHelper.getDBNeoPZProject();
        assertNotNull(project);
        
        OceanoUserDao oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);
        OceanoUser oceanoUser = oceanoUserDao.getByLogin("dheraclio");
        assertNotNull(oceanoUser);
        
        List<MetricManager> metricsToExtract = getMetrics();
        
        OstraMetricService instance = new OstraMetricService();
        instance.extractAndSaveMetricsFromAllFilesInProjectRevisions(project, oceanoUser, metricsToExtract);
        
    }
    
    private List<MetricManager> getMetrics() throws ServiceException {
        List<MetricManager> metrics = new ArrayList();
        
        MetricManagerFactory fact = MetricManagerFactory.getInstance();
        
        final MetricManager mng = fact.getMetricByName(MetricEnumeration.TLOC.getName());
        Assert.assertNotNull(mng);        
        metrics.add(mng);        
        
//        for (MetricManager mngTemp : fact.getMetricManagers()) {
//            if (!mngTemp.isLanguageSupported(Language.CPP)) {
//                continue;
//            }
//            metrics.add(mngTemp);
//        }
        return metrics;
    }
}