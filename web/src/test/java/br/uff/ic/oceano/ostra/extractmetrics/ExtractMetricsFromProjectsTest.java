package br.uff.ic.oceano.ostra.extractmetrics;


import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.tools.metrics.DerivedMetricManager;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.ostra.service.metric.Request;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 */
public class ExtractMetricsFromProjectsTest extends AbstractNGTest{
          

    @Test
    public void extractMetrics() throws Throwable {
        if(!JPAUtil.isRunningOnMemoryDB()){
            return;
        }       
        
    }
    
    private List<SoftwareProject> getProjectList() throws ServiceException {
        final SoftwareProjectService service = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        List<SoftwareProject> projects = new ArrayList<SoftwareProject>();        
        projects.add(service.getProjectToDetailById(12L)); //NeoPz Local        
        return projects;
    }

    /**
     * Filter metrics to be extracted
     * @return
     * @throws ServiceException 
     */
    private List<String> getMetricsList() throws ServiceException {
        List<String> metrics = new ArrayList<String>();
        
        MetricManagerFactory fact = MetricManagerFactory.getInstance();
        Collection<MetricManager> metricManagrs = fact.getMetricManagers();
        for (MetricManager mng : metricManagrs){           
            if(!mng.isLanguageSupported(Language.CPP)){
                continue;
            }
            metrics.add(mng.getName());
        }        
        
        return metrics;
    }
}
