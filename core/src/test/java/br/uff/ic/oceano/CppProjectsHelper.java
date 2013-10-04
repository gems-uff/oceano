/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano;

import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import java.util.Calendar;
import static org.testng.Assert.assertNotNull;

/**
 *
 * @author Daniel
 */
public class CppProjectsHelper {

    public static final String EASYCOUNT_C = "/src/easycount.c";
    private Revision easyCountRevision;
    private Revision neopzRevision;

    public CppProjectsHelper() {
        SoftwareProject softwareProject = new SoftwareProject();
        softwareProject.setMavenProject(false);
        softwareProject.setLanguage(Language.CPP);

        //Where tests are running
        String basePath = "./target/test-classes/CPP/";
        basePath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(basePath);

        this.easyCountRevision = new Revision();
        this.easyCountRevision.setLocalPath(basePath + "EasyCount/");
        this.easyCountRevision.setProject(softwareProject);
        this.easyCountRevision.setNumber(1L);
        this.easyCountRevision.setCommitDate(Calendar.getInstance());

        softwareProject = new SoftwareProject();
        softwareProject.setMavenProject(false);
        softwareProject.setLanguage(Language.CPP);
        softwareProject.setRepositoryUrl("http://neopz.googlecode.com/svn/trunk");

        this.neopzRevision = new Revision();
        this.neopzRevision.setLocalPath(basePath + "neopz/");
        this.neopzRevision.setProject(softwareProject);
        this.neopzRevision.setNumber(1L);
        this.neopzRevision.setCommitDate(Calendar.getInstance());
    }

    /**
     * @return the revision
     */
    public Revision getEasyCountRevision() {
        return easyCountRevision;
    }

    /**
     * @param revision the revision to set
     */
    public void setEasyCountRevision(Revision revision) {
        this.easyCountRevision = revision;
    }

    /**
     * @return the neopzRevision
     */
    public Revision getNeopzRevision() {
        return neopzRevision;
    }

    /**
     * @param neopzRevision the neopzRevision to set
     */
    public void setNeopzRevision(Revision neopzRevision) {
        this.neopzRevision = neopzRevision;
    }

    /**
     *
     * @return NeoPZ project
     * @throws ObjetoNaoEncontradoException
     */
    public SoftwareProject getDBNeoPZProject() throws ObjetoNaoEncontradoException {
        SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        assertNotNull(projectService);
        SoftwareProject neopz = null;
        try {
            //neoPZ local
            neopz = projectService.getById(12l);            
        } catch (ObjetoNaoEncontradoException ex) {
            neopz = projectService.getById(11l);  //neopz web
        }       
        return neopz;
    }
    
    public Revision checkoutNeoPzRevision(long revNumber) throws Exception {
        OceanoUserDao oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);
        assertNotNull(oceanoUserDao);

        OceanoUser oceanoUser = oceanoUserDao.getByLogin("dheraclio");
        assertNotNull(oceanoUser);

        ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
        assertNotNull(projectUserService);

        SoftwareProject project = getDBNeoPZProject();
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(project, oceanoUser);

        String tempPath = SystemUtil.getTempDirectory();
        String fixedDir = PathUtil.getWellFormedPath(tempPath, "DependometerService", "revisionTest");
        FileUtils.deleteDirectory(fixedDir);

        VCSService vcsService = ObjectFactory.getObjectWithDataBaseDependencies(VCSService.class);
        assertNotNull(vcsService);
            
        Revision revision = vcsService.getRevision(project, projectUser, revNumber);
        assertNotNull(revision);

        revision.setLocalPath(fixedDir);
        vcsService.doCheckout(revision, projectUser, false);

        return revision;
    }
}
