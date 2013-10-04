/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.teste;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import java.util.Set;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
//import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
//import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;

/**
 *
 * @author wallace
 */
public class TesteSVN {

    public TesteSVN() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
//        FSRepositoryFactory.setup();
//        System.out.println("FSRepositoryFactory.setup();");
//        DAVRepositoryFactory.setup();
//        System.out.println("DAVRepositoryFactory.setup();");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

//    @Test
    public void checkout() throws VCSException {
        System.out.println("---------------------------- teste checkout -----------------------------");
        SoftwareProject project = new SoftwareProject();
//        project.setName("TesteCheckout");
        project.setRepositoryUrl("file:///d:/svn/oceano-mirror/trunk/");

        ProjectUser projectuser = new ProjectUser();
        projectuser.setPassword("pass");
        projectuser.setLogin("user");

        Revision r = new Revision();
        r.setProject(project);

        SVN_By_SVNKit svn = new SVN_By_SVNKit();
        svn.doCheckout(r, projectuser, true);
        System.out.println("---------------------------------------------------------");
    }

    @Test
    public void checkoutComProjetoPersistente() throws VCSException, ObjetoNaoEncontradoException, ServiceException {
        System.out.println("---------------------------- teste checkout com projeto persistente -----------------------------");
        SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        SoftwareProject project = projectService.getById(2L);

        OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
        OceanoUser user = oceanoUserService.getByLogin("xan");

        ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(project, user);

        Revision r = new Revision();
        r.setNumber(2469L);
        r.setProject(project);

        VCSService vcsService = ObjectFactory.getObjectWithoutDataBaseDependencies(VCSService.class);
        vcsService.doCheckout(r, projectUser, true);
        System.out.println("---------------------------------------------------------");
    }

//    @Test
    public void getRevisions() throws VCSException {
        System.out.println("---------------------------- teste getRevisions -----------------------------");
        SoftwareProject project = new SoftwareProject();
//        project.setName("TesteCheckout");
//        project.setRepositoryUrl("https://gems.ic.uff.br/svn/oceano/branches/metricas-desenvolvimento/");
        project.setRepositoryUrl("file:///d:/svn/oceano-mirror/trunk/");

        ProjectUser projectuser = new ProjectUser();
        projectuser.setLogin("user");
        projectuser.setPassword("pass");

        Revision r = new Revision();
        r.setProject(project);

        SVN_By_SVNKit svn = new SVN_By_SVNKit();
        Set<Revision> set = svn.getRevisions(project, projectuser);

        System.out.println("oceano-mirror/trunk=" + svn.getRevisions(project, projectuser));
        for (Revision revision : set) {
            System.out.println("revision = " + revision);
            for (VersionedItem changedFile : revision.getChangedFiles()) {
                System.out.println("    changedFile = " + changedFile);
            }
        }

        System.out.println("---------------------------------------------------------");
    }
}
