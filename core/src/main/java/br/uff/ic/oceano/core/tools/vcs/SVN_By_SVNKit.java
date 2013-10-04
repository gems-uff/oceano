/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.vcs;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;

/**
 *
 * @author DanCastellani
 */
public class SVN_By_SVNKit implements VCS {

    private static final String name = "SVN";
    private static final String rationale = "Subversion is a Version Control System";
    private static Boolean FSRepositoryInitialized = false;
    private static Boolean DAVRepositoryInitialized = false;
    private static Boolean SVNRepositoryInitialized = false;

    private SVNClientManager getSVNClientManager(ProjectUser projectUser) {
        final DefaultSVNOptions myOptions = new DefaultSVNOptions();

        if (projectUser.isAnonymous()) {
            return SVNClientManager.newInstance(myOptions);
        } else {
            return SVNClientManager.newInstance(myOptions, projectUser.getLogin(), projectUser.getPassword());
        }
    }

    private ISVNAuthenticationManager getISVNAuthenticationManager(ProjectUser projectUser) {
        if (projectUser.isAnonymous()) {
            return SVNWCUtil.createDefaultAuthenticationManager();
        } else {
            return SVNWCUtil.createDefaultAuthenticationManager(projectUser.getLogin(), projectUser.getPassword());
        }
    }

    public Long getNumberOfHEADRevision(ProjectUser projectUser) throws VCSException {
        SVN_By_CommandLineInterface svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_CommandLineInterface.class);
        return svn.getNumberOfHEADRevision(projectUser);
    }

    public Revision doCheckout(Revision revision, ProjectUser projectUser, boolean createDirectory) throws VCSException {
        System.out.print("------------> preparing checkout... ");
        initRepositoryFactory(revision.getProject().getRepositoryUrl());

        SVNClientManager clientManager = getSVNClientManager(projectUser);

        try {
            SVNURL url;
            url = SVNURL.parseURIDecoded(revision.getProject().getRepositoryUrl());

            if (createDirectory) {
                if (revision.getNumber() == null) {
                    revision.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revision.getProject().getConfigurationItem().getName() + "-rHEAD");
                } else {
                    revision.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revision.getProject().getConfigurationItem().getName() + "-r" + revision.getNumber());
                }
            }
//            else {
//                    The directory is already bem setted
//            }
            File dir = new File(revision.getLocalPath());
            if (dir.exists()) {
                if (dir.list() != null) {
                    if (ApplicationConstants.DIRECTORY_MUST_BE_EMPITY_TO_CHECKOUT) {
                        return revision;
                    } else {
                        throw new VCSException("O diretório [" + revision.getLocalPath() + "] deve estar vazio");
                    }
                }
            } else {
                dir.mkdir();
            }

            clientManager.createRepository(url, true);
            SVNRevision svnRevision = SVNRevision.HEAD;
            if (revision.getNumber() != null) {
                svnRevision = SVNRevision.create(revision.getNumber());
            }

            System.out.print("    begin checkout... ");
            clientManager.getUpdateClient().doCheckout(url, dir, SVNRevision.UNDEFINED, svnRevision, true);
            System.out.println("    done!");

        } catch (SVNException ex) {
            Logger.getLogger(SVN_By_SVNKit.class.getName()).log(Level.SEVERE, null, ex);
            throw new VCSException(ex);
        }

        return revision;
    }

    public Revision doUpdate(Revision revision, ProjectUser projectUser, boolean updateFolderName) throws VCSException {
        System.out.print("------------> preparing update... ");
        initRepositoryFactory(revision.getProject().getRepositoryUrl());

        SVNClientManager clientManager = getSVNClientManager(projectUser);

        try {
            SVNURL url;
            url = SVNURL.parseURIDecoded(revision.getProject().getRepositoryUrl());

            File dir = new File(revision.getLocalPath());

            if (!dir.exists()) {
                throw new VCSException("O diretório [" + revision.getLocalPath() + "] deve ter uma revisão.");
            }

            clientManager.createRepository(url, true);
//            clientManager.getUpdateClient().doCheckout(url, dir, SVNRevision.UNDEFINED, SVNRevision.HEAD, true);
            System.out.print("    begin update... ");

            clientManager.getUpdateClient().doUpdate(dir, SVNRevision.create(revision.getNumber()), true);

            if (updateFolderName) {
                //change folder name to know the revision number
                revision.setLocalPath(revision.getLocalPath().substring(0, revision.getLocalPath().lastIndexOf("-r")) + "-r" + revision.getNumber());
                dir.renameTo(new File(revision.getLocalPath()));
            }

        } catch (SVNException ex) {
            Logger.getLogger(SVN_By_SVNKit.class.getName()).log(Level.SEVERE, null, ex);
            throw new VCSException(ex);
        }
        System.out.println("    done!");
        return revision;
    }

    public String getName() {
        return name;
    }

    public String getRationale() {
        return rationale;
    }

    public Set<Revision> getRevisions(SoftwareProject project, ProjectUser projectUser) throws VCSException {
        initRepositoryFactory(project.getRepositoryUrl());

        Revision revision;
        Set<Revision> revisions = new LinkedHashSet<Revision>();
        Set<VersionedItem> files;
//        DAVRepositoryFactory.setup();
        long startRevision = 0;
        long endRevision = -1;
        Calendar cal;
        SVNRepository repository = null;
        Set changedPathsSet;
        Iterator changedPaths;
        try {
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(project.getRepositoryUrl()));

            ISVNAuthenticationManager authManager = getISVNAuthenticationManager(projectUser);

            if (projectUser.isAnonymous()) {
                authManager = SVNWCUtil.createDefaultAuthenticationManager();
            } else {
                authManager = SVNWCUtil.createDefaultAuthenticationManager(projectUser.getLogin(), projectUser.getPassword());
            }
            repository.setAuthenticationManager(authManager);
            Collection logEntries = null;
            logEntries = repository.log(new String[]{""}, null, startRevision, endRevision, true, true);

            SVNLogEntry logEntry;
            Iterator entries = logEntries.iterator();
            while (entries.hasNext()) {
                logEntry = (SVNLogEntry) entries.next();

                revision = new Revision();
                revision.setProject(project);
                revision.setNumber(logEntry.getRevision());
                revision.setCommiter(logEntry.getAuthor());
                cal = Calendar.getInstance();
                cal.setTime(logEntry.getDate());
                revision.setCommitDate(cal);
                revision.setMessage(logEntry.getMessage());
                if (logEntry.getChangedPaths().size() > 0) {
                    changedPathsSet = logEntry.getChangedPaths().keySet();
                    changedPaths = changedPathsSet.iterator();
                    files = new LinkedHashSet<VersionedItem>();
                    while (changedPaths.hasNext()) {
                        SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());

                        //ignores entries from other paths than the project's path
                        if (!entryPath.getPath().startsWith(repository.getRepositoryPath(""))) {
                            continue;
                        }
                        int init = project.getRepositoryUrl().indexOf(repository.getRepositoryRoot(true).getPath());
                        init += repository.getRepositoryRoot(true).getPath().length();
                        final int basePathLenght = project.getRepositoryUrl().substring(init).length();

//                        System.out.println("basePath = " + project.getRepositoryUrl().substring(init));
//                        System.out.println("fullFilePath = " + entryPath.getPath());

                        String filePath = entryPath.getPath();
//                        System.out.println("filePath.length() - basePathLenght=" + (filePath.length() - basePathLenght));

                        if ((filePath.length() - basePathLenght) > 0) {
                            filePath = entryPath.getPath().substring(basePathLenght);

                            VersionedItem vi = new VersionedItem();
                            vi.setItem(new Item(filePath));
                            vi.setRevision(revision);
                            vi.setType(entryPath.getType());
                            files.add(vi);
                        }
                    }
                    //files change in loop and so its hashcode
                    revision.setChangedFiles(files);
//                    System.out.println("files = " + files);
                }

                revisions.add(revision);

            }


        } catch (SVNException ex) {
            throw new VCSException(ex);
        }

        return revisions;
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Calendar calendar) throws VCSException {
        initRepositoryFactory(project.getRepositoryUrl());
        try {
            Revision revision = new Revision();
            revision.setProject(project);
            long startRevision = 0;
            long endRevision = -1;
            Calendar cal;
            Set changedPathsSet;
            Iterator changedPaths;

            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(project.getRepositoryUrl()));

            ISVNAuthenticationManager authManager = getISVNAuthenticationManager(projectUser);

            repository.setAuthenticationManager(authManager);
            Collection logEntries = repository.log(new String[]{""}, null, startRevision, endRevision, true, true);

            SVNLogEntry logEntry, auxlogEntry;
            Iterator entries = logEntries.iterator();

            logEntry = (SVNLogEntry) entries.next();
            auxlogEntry = logEntry;
            while (entries.hasNext() && ((calendar.getTime().compareTo(logEntry.getDate())) >= 0)) {
                logEntry = (SVNLogEntry) entries.next();
                if ((calendar.getTime().compareTo(logEntry.getDate())) >= 0) {
                    auxlogEntry = logEntry;
                }
            }
            logEntry = auxlogEntry;
            revision.setNumber(logEntry.getRevision());
            revision.setCommiter(logEntry.getAuthor());
            cal = Calendar.getInstance();
            cal.setTime(logEntry.getDate());
            revision.setCommitDate(cal);
            if (logEntry.getChangedPaths().size() > 0) {
                changedPathsSet = logEntry.getChangedPaths().keySet();
                changedPaths = changedPathsSet.iterator();
                Set<VersionedItem> files = new LinkedHashSet<VersionedItem>();
                while (changedPaths.hasNext()) {
                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());

                    int init = project.getRepositoryUrl().indexOf(repository.getRepositoryRoot(true).getPath());
                    init += repository.getRepositoryRoot(true).getPath().length();
                    final int basePathLenght = project.getRepositoryUrl().substring(init).length();
                    String filePath = entryPath.getPath();

                    if ((filePath.length() - basePathLenght) > 0) {
                        filePath = entryPath.getPath().substring(basePathLenght);

                        VersionedItem vi = new VersionedItem();
                        vi.setItem(new Item(filePath));
                        vi.setRevision(revision);
                        vi.setType(entryPath.getType());
                        files.add(vi);
                    }
                }
                revision.setChangedFiles(files);
            }
            return revision;
        } catch (Exception e) {
            throw new VCSException(e);
        }
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Long revisionNumber) throws VCSException {
        initRepositoryFactory(project.getRepositoryUrl());
        try {
            Revision revision = new Revision();
            revision.setProject(project);

            ISVNAuthenticationManager authManager = getISVNAuthenticationManager(projectUser);

            SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(project.getRepositoryUrl()));
            repository.setAuthenticationManager(authManager);

            //Collection logEntries = repository.log(new String[]{""}, null, revisionNumber, revisionNumber, true, false);
            final List<SVNLogEntry> logEntries = new LinkedList<SVNLogEntry>();
            String[] paths = new String[]{""};
            String[] properties = null;//to get all properties
            ISVNLogEntryHandler handler = new ISVNLogEntryHandler() {
                public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
                    logEntries.add(logEntry);
                }
            };
            boolean getChangedPaths = true;
            boolean getMergedNodes = true;
            repository.log(paths,revisionNumber, revisionNumber,getChangedPaths,false,1, getMergedNodes, properties,handler);
            if (logEntries == null || logEntries.isEmpty()) {
                return null;
            } /*else if (logEntries.size() > 1) {
                throw new Exception("Too many log entries on revision " + revisionNumber);
            }*/

            SVNLogEntry logEntry = (SVNLogEntry) logEntries.iterator().next();

            revision.setNumber(logEntry.getRevision());
            revision.setCommiter(logEntry.getAuthor());
            Calendar cal = Calendar.getInstance();
            cal.setTime(logEntry.getDate());
            revision.setCommitDate(cal);

            Set changedPathsSet = logEntry.getChangedPaths().keySet();
            Iterator changedPaths = changedPathsSet.iterator();
            Set<VersionedItem> files = new LinkedHashSet<VersionedItem>();
            while (changedPaths.hasNext()) {
                SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());

                int init = project.getRepositoryUrl().indexOf(repository.getRepositoryRoot(true).getPath());
                init += repository.getRepositoryRoot(true).getPath().length();
                final int basePathLenght = project.getRepositoryUrl().substring(init).length();
                String filePath = entryPath.getPath();

                if ((filePath.length() - basePathLenght) > 0) {
                    filePath = entryPath.getPath().substring(basePathLenght);

                    VersionedItem vi = new VersionedItem();
                    vi.setItem(new Item(filePath));
                    vi.setRevision(revision);
                    vi.setType(entryPath.getType());
                    files.add(vi);
                }
            }
            revision.setChangedFiles(files);
            return revision;
        } catch (Exception e) {
            throw new VCSException(e);
        }
    }

    public synchronized void initRepositoryFactory(final String repositoryUrl) {
        if (repositoryUrl.matches("^file://.*$") && !FSRepositoryInitialized) {
            System.out.println("----------------> Initializing FSRepositoryFactory");
            FSRepositoryFactory.setup();
            FSRepositoryInitialized = true;

        } else if (repositoryUrl.matches("^https?://.*$") && !DAVRepositoryInitialized) {
            System.out.println("----------------> Initializing DAVRepositoryFactory");
            DAVRepositoryFactory.setup();
            DAVRepositoryInitialized = true;

        } else if (repositoryUrl.matches("^svn(\\+.+)?://.*$") && !SVNRepositoryInitialized) {
            System.out.println("----------------> Initializing SVNRepositoryFactory");
            SVNRepositoryFactoryImpl.setup();
            DAVRepositoryInitialized = true;
        }
    }

    public OutputStream doDiff(ProjectUser projectUser, String urlMainBranch, String urlSecondaryBranch, Long numberRevision) throws VCSException {
        System.out.print("------------> preparing doDiff... ");
        OutputStream resultDiff = null;
        initRepositoryFactory(projectUser.getProject().getRepositoryUrl());

        SVNClientManager clientManager = getSVNClientManager(projectUser);

        try {
            //calculo responsavel em fazer o diff
            SVNDiffClient diffClient = clientManager.getDiffClient();
            resultDiff = new ByteArrayOutputStream();

            SVNURL repo1 = SVNURL.parseURIEncoded(urlMainBranch);
            SVNURL repo2 = SVNURL.parseURIEncoded(urlSecondaryBranch);

            SVNRevision svnRevision = SVNRevision.create(numberRevision.longValue());

            System.out.print("    begin doDiff... ");

            diffClient.doDiff(repo1, svnRevision, repo2, svnRevision,
                    SVNDepth.INFINITY, true, resultDiff);

        } catch (SVNException ex) {
            Logger.getLogger(SVN_By_SVNKit.class.getName()).log(Level.SEVERE, null, ex);
            throw new VCSException(ex);
        }
        System.out.println("    done!");
        return resultDiff;
    }

    public List<String> getBranches(SoftwareProject project, ProjectUser projectUser) throws VCSException {
        System.out.print("------------> preparing getBranches... ");
        List<String> branchList = new ArrayList<String>();
        initRepositoryFactory(project.getRepositoryUrl());

        try {
            ISVNAuthenticationManager aManager = getISVNAuthenticationManager(projectUser);

            ConfigurationItem ci = project.getConfigurationItem();
            String urlBranch = PathUtil.getWellFormedURL(ci.getBaseUrl(), ci.getBranchPath());
            SVNURL url = SVNURL.parseURIEncoded(urlBranch);

            SVNRepository repos = SVNRepositoryFactory.create(url);
            repos.setAuthenticationManager(aManager);

            System.out.print("    begin getBranches... ");
            System.out.println("urlBranch: " + urlBranch);
            long headRevision = repos.getLatestRevision();
            Collection<SVNDirEntry> entriesList = repos.getDir("", headRevision, null, (Collection) null);

            branchList.add(ci.getTrunkPath());
            //branchList.add("trunk");
            for (SVNDirEntry entry : entriesList) {
                branchList.add(entry.getName());
                System.out.println("Entrada: " + entry.getName());
            }
        } catch (SVNException ex) {
            Logger.getLogger(SVN_By_SVNKit.class.getName()).log(Level.SEVERE, null, ex);
            throw new VCSException(ex);
        }

        System.out.println("    done!");
        return branchList;
    }

    public void doFullRevert(File[] paths) throws VCSException {
        SVNWCClient cliente;
        ISVNAuthenticationManager auth = null;
        cliente = new SVNWCClient(auth, null);
        try {
            cliente.doRevert(paths, SVNDepth.INFINITY, null);
        } catch (SVNException ex) {
            Logger.getLogger(SVN_By_SVNKit.class.getName()).log(Level.SEVERE, null, ex);
            throw new VCSException(ex);
        }
    }

    public void doCopyTo(String pathWorkspace, ProjectUser projectUser, String urlDst) throws VCSException {
        initRepositoryFactory(projectUser.getProject().getRepositoryUrl());

        SVNClientManager clientManager = getSVNClientManager(projectUser);

        SVNURL sVNurlDst = null;
        try {
            sVNurlDst = SVNURL.parseURIDecoded(urlDst);
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }

        SVNCopySource copySource = new SVNCopySource(SVNRevision.WORKING, SVNRevision.HEAD, new File(pathWorkspace));
        SVNCopySource[] copySources = new SVNCopySource[]{copySource};

        try {

            clientManager.getCopyClient().doCopy(copySources, sVNurlDst, false, true, true, "Message Commit CopyTo", null);
        } catch (SVNException ex) {
            throw new VCSException(ex);

        }
    }

    public Long doCommit(String pathWorkspace, ProjectUser projectUserChanged, String commitMessage) throws VCSException {
        initRepositoryFactory(projectUserChanged.getProject().getRepositoryUrl());

        SVNClientManager clientManager = getSVNClientManager(projectUserChanged);

        try {
            return clientManager.getCommitClient().doCommit(new File[]{new File(pathWorkspace)}, true, commitMessage, null, null, true, true, SVNDepth.INFINITY).getNewRevision();
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }
    }

    public Long doSwitchTo(String pathWorkspace, ProjectUser projectUserChanged, String urlTarget) throws VCSException {
        initRepositoryFactory(projectUserChanged.getProject().getRepositoryUrl());

        SVNClientManager clientManager = getSVNClientManager(projectUserChanged);

        try {
            return clientManager.getUpdateClient().doSwitch(new File(pathWorkspace), SVNURL.parseURIDecoded(urlTarget), SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false, true);
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }
    }

    public Long doCheckout(File createdFileToworkspace, ProjectUser projectUser) throws VCSException {
        initRepositoryFactory(projectUser.getProject().getRepositoryUrl());

        DefaultSVNOptions myOptions = new DefaultSVNOptions();
        myOptions.setInteractiveConflictResolution(false);
        ISVNAuthenticationManager authenticationManager = getISVNAuthenticationManager(projectUser);

        SVNClientManager clientManager = SVNClientManager.newInstance(myOptions, authenticationManager);

        try {
            return clientManager.getUpdateClient().doCheckout(SVNURL.parseURIDecoded(projectUser.getProject().getRepositoryUrl()), createdFileToworkspace, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, true);
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }
    }
}
