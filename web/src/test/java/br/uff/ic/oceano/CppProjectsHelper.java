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
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import static org.testng.Assert.assertNotNull;

/**
 *
 * @author Daniel
 */
public class CppProjectsHelper {

    public CppProjectsHelper() {        
    }

    /**
     *
     * @return NeoPZ project
     * @throws ObjetoNaoEncontradoException
     */
    public static SoftwareProject getDBNeoPZProject() throws ObjetoNaoEncontradoException {
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
