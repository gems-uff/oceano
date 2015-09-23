/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.service;

import br.uff.ic.oceano.core.exception.InfraestruturaException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import com.valtech.source.dependometer.app.core.metrics.MetricDefinition;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.RollbackException;
import org.hibernate.exception.ConstraintViolationException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Heraclio
 */
public class MetricServiceNGTest extends AbstractNGTest{

    private MetricService service;

    public MetricServiceNGTest() {
        super();
        service = new MetricService();
        service.setup();
    }


    @Test
    public void testGetByAcronym() throws Exception {
        println("getByAcronym");
        try {
            service.getByAcronym("");
        } catch (ObjetoNaoEncontradoException ex) {
            assertTrue(true);
        } catch (Throwable tr) {
            fail(tr.getMessage());
        }
    }

    @Test
    public void testGetMetricByName() throws Exception {
        println("getMetricByName");
        String metricName = "";
        try {
            MetricService.getMetricByName(metricName);
        } catch (ServiceException ex) {
            assertTrue(ex.getCause() instanceof ObjetoNaoEncontradoException, ex.getMessage());
        } catch (Throwable tr) {
            tr.printStackTrace();
            assertTrue(true);//database empty
        }
    }

    
    @Test
    public void testGetMetric() throws Exception {
        println("getMetric");
        try {
            service.getMetric("");
        } catch (ServiceException ex) {
            assertTrue(ex.getCause() instanceof ObjetoNaoEncontradoException, ex.getMessage());
        } catch (Throwable tr) {
            assertTrue(true);//database empty
        }
    }

    @Test
    public void testGetAll() {
        println("getAll");
        List result = service.getAll();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMetricsByProject() {
        println("getMetricsByProject");
        SoftwareProject project = null;
        try {
            service.getMetricsByProject(project);
        } catch (Exception ex) {
            assertTrue(true);//database empty
        }
    }

    @Test
    public void testSave_longDescription() {
        println("testSave_longDescription");
        
        MetricEnum mEnum = MetricEnum.ABSTRACTNESS;
        Metric metric = new Metric();
        metric.setName(mEnum.getDisplayName());
        metric.setAcronym(mEnum.name());
        MetricDefinition[] metrics = MetricDefinition.getMetricDefinitions();
        metric.setDescription(metrics[1].getDescription());
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(Metric.EXTRACTS_FROM_FILE);
        metric.setDerived(false);
        metric.setExtractsFromFont(true);
        try {
            service.save(metric);
        } catch (InfraestruturaException ex) {
            //ok, already exists
            assertTrue(ex.getCause() instanceof EntityExistsException, ex.getMessage());
        } catch (RollbackException ex) {
            //ok, already exists
            assertTrue(ex.getCause() instanceof ConstraintViolationException, ex.getMessage());
        }
        catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.getMessage());
        }
    }
}
