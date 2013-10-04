package br.uff.ic.oceano.experiments.peixeespada;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.peixeespada.service.AgentService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 *
 * @author Kann
 */
public class TestAgents {

    private AgentService agentService = ObjectFactory.getObjectWithDataBaseDependencies(AgentService.class);
    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);

    public TestAgents() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
    }

    @Test
    public void testQueries() throws Exception {
        OceanoUser oceanoUser =    oceanoUserService.autenticarUsuario("kann", "kann");
        System.out.println(projectUserService.getByOceanoUser(oceanoUser));
        System.out.println(agentService.getActiveByOceanoUser(oceanoUser));
    }
}
