package br.uff.ic.gems.peixeespadacliente.vcs;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import java.io.File;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class TesteVCS {
    /*
     * To change this template, choose Too7ls | Templates
     * and open the template in the editor.
     */

    private SVN_By_SVNKit svn = ObjectFactory.getObjectWithoutDataBaseDependencies(SVN_By_SVNKit.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

//    @Test
    public void testCheckoutLocal() throws Throwable {
        File workspace = new File("G:\\Users\\Heliomar\\AppData\\Local\\Temp\\peixeespada_workspaces\\Oceano-web_0");
        SoftwareProject sp = new SoftwareProject();
        sp.setRepositoryUrl("file:///d:/repiduff/trunk");
        ProjectUser pu = new ProjectUser();
        pu.setPassword("kann");
        pu.setLogin("kann");
        pu.setProject(sp);

        svn.doCheckout(workspace, pu);

    }

    @Test
    public void testCheckoutRemoto() throws Throwable {
        File workspace = new File("G:\\Users\\Heliomar\\AppData\\Local\\Temp\\peixeespada_workspaces\\Oceano-web_1");
        workspace.mkdir();
        SoftwareProject sp = new SoftwareProject();
        sp.setRepositoryUrl("https://gems.ic.uff.br/svn/oceano/oceano-web/trunk");
        ProjectUser pu = new ProjectUser();
        pu.setLogin("kann");
        pu.setPassword("xxxx");
        pu.setProject(sp);
        svn.doCheckout(workspace, pu);
    }

}
