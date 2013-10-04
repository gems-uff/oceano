/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.vcs;

import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.tools.*;
import br.uff.ic.oceano.core.model.Revision;
import java.io.File;
import java.util.Calendar;
import java.util.Set;

/**
 *
 * @author DanCastellani
 */
public interface VCS extends Tool {

    public Revision doCheckout(Revision configuration, ProjectUser projectUser, boolean createDirectory) throws VCSException;

    public Long doCheckout(File createdFileToworkspace, ProjectUser projectUser) throws VCSException;

    public Revision doUpdate(Revision configuration, ProjectUser projectUser, boolean updateFilderName) throws VCSException;

    public void doCopyTo(String pathWorkspace, ProjectUser projectUser, String urlTarget) throws VCSException;

    public Long doCommit(String pathWorkspace, ProjectUser projectUserChanged, String commitMessage) throws VCSException;

    public Long doSwitchTo(String pathWorkspace, ProjectUser projectUserChanged, String urlTarget) throws VCSException;

    public void doFullRevert(File[] paths)throws VCSException;

    public Set<Revision> getRevisions(SoftwareProject project, ProjectUser projectUser) throws VCSException;

    public Long getNumberOfHEADRevision(ProjectUser projectUser) throws VCSException;

    /**
     *
     * @param project
     * @param projectUser
     * @param date
     * @return the last revision made until this date.
     * @throws VCSException
     */
    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Calendar calendar) throws VCSException;

    /**
     * Return revision number revisionNumber, if it exists.
     * @param project
     * @param projectUser
     * @param revisionNumber
     * @return
     * @throws VCSException
     */
    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Long revisionNumber) throws VCSException;
}
