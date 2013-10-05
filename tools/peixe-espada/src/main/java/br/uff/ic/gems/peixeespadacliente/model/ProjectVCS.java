package br.uff.ic.gems.peixeespadacliente.model;

import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import java.io.File;

/**
 *
 * @author Heliomar, João Felipe
 */
public class ProjectVCS {

    protected String name;
    protected String repositoryUrl;
    protected String localPath;
    protected ProjectUser projectUser;
    protected VCSService versionControlSystemService = ObjectFactory.getObjectWithoutDataBaseDependencies(VCSService.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " - " + repositoryUrl + " - ";//+VCS;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        if (localPath != null) {
            String separetor = System.getProperty("file.separator");
            if (!localPath.endsWith(separetor)) {
                localPath = localPath.concat(separetor);
            }
        }
        this.localPath = localPath;
    }

    public ProjectUser getProjectUser() {
        return projectUser;
    }

    public void setProjectUser(ProjectUser projectUser) {
        this.projectUser = projectUser;
    }

    public void doReset() throws VCSException {
        try {
            SVN_By_SVNKit vcs = new SVN_By_SVNKit();
            File[] paths = new File[1];
            paths[0] = new File(localPath + "");
            vcs.doFullRevert(paths);
        } catch (Exception e) {
            throw new VCSException(e);
        }
    }

    public Long doCommit(String message) throws VCSException {
        return versionControlSystemService.doCommit(this.localPath, projectUser, message);
    }

    public Long doCheckout(File workspace) throws VCSException {
        return versionControlSystemService.doCheckout(workspace, projectUser);
    }

    public void doCopyTo(String branch) throws VCSException {
        versionControlSystemService.doCopyTo(this.localPath, projectUser, branch);
    }

    public Long doSwitchTo(String branch) throws VCSException {
        return versionControlSystemService.doSwitchTo(this.localPath, projectUser, branch);
    }
}
