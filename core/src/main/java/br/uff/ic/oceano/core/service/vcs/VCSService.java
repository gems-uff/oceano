/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service.vcs;

import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_CommandLineInterface;
import br.uff.ic.oceano.core.tools.vcs.VCS;
import br.uff.ic.oceano.core.tools.vcs.VCSUtil;
import java.io.File;
import java.util.Calendar;
import java.util.Set;

/**
 *
 * @author Dancastellani
 * este service não é um PersistenceService pois nao depende do BD
 */
public class VCSService {

    private VCS svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_CommandLineInterface.class);

    public Revision doCheckout(Revision revision, ProjectUser projectUser, boolean createDirectory) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(revision.getProject());
        return propperVersionControlSystem.doCheckout(revision, projectUser, createDirectory);
    }

    public Long doCheckout(File createdFileToworkspace, ProjectUser projectUser) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(projectUser.getProject());
        return propperVersionControlSystem.doCheckout(createdFileToworkspace, projectUser);
    }

    public Long doCommit(String pathWorkspace, ProjectUser projectUserChanged, String commitMessage) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(projectUserChanged.getProject());
        return propperVersionControlSystem.doCommit(pathWorkspace, projectUserChanged, commitMessage);
    }

    public Long doSwitchTo(String pathWorkspace, ProjectUser projectUserChanged, String commitMessage) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(projectUserChanged.getProject());
        return propperVersionControlSystem.doSwitchTo(pathWorkspace, projectUserChanged, commitMessage);
    }

    public void doCopyTo(String pathWorkspace, ProjectUser projectUserChanged, String urlDestine) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(projectUserChanged.getProject());
        propperVersionControlSystem.doCopyTo(pathWorkspace, projectUserChanged, urlDestine);
    }

    public Long getNumberOfHEADRevision(ProjectUser projectUser) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(projectUser.getProject());
        return propperVersionControlSystem.getNumberOfHEADRevision(projectUser);
    }

    public Set<Revision> getRevisions(SoftwareProject project, ProjectUser projectUser) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(project);
        return propperVersionControlSystem.getRevisions(project, projectUser);
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Calendar calendar) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(project);
        return propperVersionControlSystem.getRevision(project, projectUser, calendar);
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Long revisionNumber) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(project);
        return propperVersionControlSystem.getRevision(project, projectUser, revisionNumber);
    }

    public Revision doUpdate(Revision revision, ProjectUser projectUser, boolean updatePathName) throws VCSException {
        final VCS propperVersionControlSystem = getVCSForProject(revision.getProject());
        return propperVersionControlSystem.doUpdate(revision, projectUser, updatePathName);
    }

    private VCS getVCSForProject(SoftwareProject project) throws VCSException {
        if(project == null){
            throw new VCSException("Software project is null");
        }
        if(project.getConfigurationItem() == null){
            throw new VCSException("Software project configuration item not set in :"+ project);
        }
        if(project.getConfigurationItem().getRepository() == null){
            throw new VCSException("Software project configuration item repository not set in :"+ project);
        }

        String repositoryName = project.getConfigurationItem().getRepository().getName();

        if (repositoryName.equalsIgnoreCase(svn.getName())) {
            return svn;
        }

        throw new VCSException("The repository " + repositoryName + " is not supported yet.\n Please contact Oceano support for more details.");
    }


    public String getMD5(Revision revision) throws VCSException{
        return VCSUtil.getMD5(revision);
    }
}
