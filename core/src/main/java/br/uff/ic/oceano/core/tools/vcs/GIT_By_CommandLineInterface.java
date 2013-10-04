/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.vcs;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.RevisionGit;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.vcs.PathModel.PathChangeModel;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 *
 * @author heron
 */
public class GIT_By_CommandLineInterface implements VCS {


    public Revision doCheckout(Revision revision, ProjectUser projectUser, boolean createDirectory) throws VCSException
    {
        try
        {
            RevisionGit revisionGit = (RevisionGit)revision;
            System.out.println("------------> begin checkout (Revision "+ revisionGit.getSHA1() + "... ");
            GitCommands git = new GitCommands(revisionGit.getProject().getRepositoryUrl());

            if (createDirectory) {
                if (revisionGit.getNumber() != null) {
                    revisionGit.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revisionGit.getProject().getConfigurationItem().getName() + "-r" + revisionGit.getNumber());

                } else {
                    revisionGit.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revisionGit.getProject().getConfigurationItem().getName());
                }
            }

            File checkoutDirectory = new File(revisionGit.getLocalPath());
            if (checkoutDirectory.exists()) {
                if (checkoutDirectory.list() != null) {
                    if ((ApplicationConstants.DIRECTORY_MUST_BE_EMPITY_TO_CHECKOUT) && (checkoutDirectory.list().length > 0)) {
                        throw new VCSException("O diretório [" + revisionGit.getLocalPath() + "] deve estar vazio");
                    }
                }
            } else {
                checkoutDirectory.mkdir();
            }

            git.checkoutRevision(revisionGit.getSHA1());

            System.out.println("------------> checkout done.");

            return revisionGit;
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(JGitInternalException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(InvalidRefNameException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(RefAlreadyExistsException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch(RefNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public Long doCheckout(File createdFileToworkspace, ProjectUser projectUser) throws VCSException
    {
        return null;
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Calendar calendar) throws VCSException
    {
        return null;
    }

    public Set<Revision> getRevisions(SoftwareProject project, ProjectUser projectUser) throws VCSException
    {
        //project.getRepositoryUrl() é, por enquanto, o path do repositório git local (já clonado).
        //id da revisão é o hashCode do id da revisão do Git
        //argumento 'type' de VersionedItem.setType é a primeira posição da String MODIFIED de Chage
        //id da Revision == Number da Revision

        Set<Revision> revisions = new LinkedHashSet<Revision>();
        Set<VersionedItem> files;

        GitCommands git;
        try
        {
            git = new GitCommands(project.getRepositoryUrl());

            Iterator<RevCommit> log = git.log();

            Calendar cal;

            while (log.hasNext())
            {
                RevCommit rc = log.next();
                RevisionGit revision = new RevisionGit();
                revision.setProject(project);
                revision.setSHA1(rc.getId().getName());
                revision.setNumber(rc.getCommitterIdent().getWhen().getTime());
                revision.setCommiter(rc.getCommitterIdent().getName());
                cal = Calendar.getInstance();
                cal.setTime(rc.getCommitterIdent().getWhen());
                revision.setCommitDate(cal);
                revision.setMessage(rc.getFullMessage());

                List<PathChangeModel> filesInCommit = git.getFilesInCommit(rc);

                if (filesInCommit.size() > 0)
                {
                    Iterator<PathChangeModel> changedPaths = filesInCommit.iterator();
                    files = new LinkedHashSet<VersionedItem>();
                    revision.setChangedFiles(files);
                    while(changedPaths.hasNext())
                    {
                        PathChangeModel pcm = changedPaths.next();

                        String filePath = pcm.path;

                        VersionedItem vi = new VersionedItem();
                        vi.setItem(new Item(filePath));
                        vi.setRevision(revision);
                        vi.setType(pcm.changeType.toString().charAt(0));
                        files.add(vi);
                    }
                }

                revisions.add(revision);
            }

            return revisions;
        }
        catch (NoHeadException ex) {
            System.out.println(ex.getMessage());
        } catch (MissingObjectException ex) {
            System.out.println(ex.getMessage());
        } catch (IncorrectObjectTypeException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    public Long getNumberOfHEADRevision(ProjectUser projectUser) throws VCSException
    {
        return null;
    }

    public Revision doUpdate(Revision configuration, ProjectUser projectUser, boolean updateFilderName) throws VCSException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doCopyTo(String pathWorkspace, ProjectUser projectUser, String urlTarget) throws VCSException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Long doCommit(String pathWorkspace, ProjectUser projectUserChanged, String commitMessage) throws VCSException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Long doSwitchTo(String pathWorkspace, ProjectUser projectUserChanged, String urlTarget) throws VCSException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void doFullRevert(File[] paths) throws VCSException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Long revisionNumber) throws VCSException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRationale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
