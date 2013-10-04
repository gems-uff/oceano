/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.vcs;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.util.CommandLineIinterfaceUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.Output;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author DanCastellani
 */
public class SVN_By_CommandLineInterface implements VCS {

    private static final String name = "SVN";
    private static final String rationale = "Subversion is a Version Control System. This is a Command Line Interface implementation.";
    private static final String COMMAND_CHECKOUT = "svn checkout ";
    private static final String COMMAND_UPDATE = "svn update ";
    private static final String PARAMETTER_REVISION = " -r";
    private static final String SPACE = " ";
    private static final String USERNAME = "--username";
    private static final String PASSWORD = "--password";
    private static final String INFO = "info";
    private static final String LIST = "list";
    private static final String MERGE = "merge";
    private static final String SVN = "svn";
    public static final String R = "-r";
    public static final String V = "-v";
    public static final String RECURSIVE = "-R";
    public static final String HISTORY = "history";
    public static final String HEAD = "HEAD";
    public static final String NON_INTERACTIVE = "--non-interactive";

    public Long getNumberOfHEADRevision(ProjectUser projectUser) throws VCSException {
        String[] comandLastRevision = null;

        if (projectUser.getLogin() == null || projectUser.getLogin().trim().isEmpty()) {
            comandLastRevision = new String[]{SVN, INFO, projectUser.getProject().getRepositoryUrl(), R, HEAD};
        } else {
            comandLastRevision = new String[]{SVN, INFO, projectUser.getProject().getRepositoryUrl(), R, HEAD, USERNAME, projectUser.getLogin(), PASSWORD, projectUser.getPassword(), NON_INTERACTIVE};
        }

        Process p;
        try {
            p = CommandLineIinterfaceUtils.executeComand(comandLastRevision);
        } catch (IOException ex) {
            throw new VCSException(ex);
        }
        readerError(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        /*
         COMMAND: svn info file:///d:/rep_pe/branches

         1- Path: branches
         2- URL: file:///D:/rep_pe/branches
         3- Repository Root: file:///D:/rep_pe
         4- Repository UUID: d1dc73b7-5fb2-0646-bfb7-f1b7128fe7dd
         5- Revision: 61
         6- Node Kind: directory
         7- Last Changed Author: dancastellani
         8 -Last Changed Rev: 41
         9- Last Changed Date: 2009-11-16 13:32:55 -0200 (seg, 16 nov 2009)
         */

        String line = null;
        try {
            line = reader.readLine();           //1
            line = reader.readLine();           //2
            line = reader.readLine();           //3
            line = reader.readLine();           //4
            line = reader.readLine();           //5
            line = line.substring(line.lastIndexOf(" ") + 1);
        } catch (IOException ex) {
            throw new VCSException(ex);
        }
        return Long.parseLong(line.trim());
    }

    public Revision doCheckout(Revision revision, ProjectUser projectUser, boolean createDirectory) throws VCSException {
        Output.println("------------> begin checkout... ");

        if (createDirectory) {
            if (revision.getNumber() != null) {
                revision.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revision.getProject().getConfigurationItem().getName() + "-r" + revision.getNumber());
            } else {
                revision.setLocalPath(ApplicationConstants.DIR_BASE_CHECKOUTS + revision.getProject().getConfigurationItem().getName());
            }
        }
        File checkoutDirectory = new File(revision.getLocalPath());
        if (checkoutDirectory.exists()) {
            if (!PathUtil.isEmpty(checkoutDirectory) && ApplicationConstants.DIRECTORY_MUST_BE_EMPITY_TO_CHECKOUT) {
                throw new VCSException("O diretório [" + checkoutDirectory + "] deve estar vazio");
            }
        } else {
            try {
                PathUtil.mkDirs(checkoutDirectory.getAbsolutePath());
            } catch (IOException ex) {
                throw new VCSException(ex);
            }
        }

        StringBuilder command = new StringBuilder(COMMAND_CHECKOUT);

        if (revision.getNumber() != null) {
            command.append(PARAMETTER_REVISION).append(revision.getNumber());
        }

        //set the urlRepository
        command.append(SPACE).append("\"").append(revision.getProject().getRepositoryUrl()).append("\"");

        //set the destination folder
        command.append(SPACE).append("\"").append(checkoutDirectory.getPath()).append("\"");

        if (!projectUser.isAnonymous()) {
            command.append(SPACE).append(USERNAME).append(SPACE).append(projectUser.getLogin());
            command.append(SPACE).append(PASSWORD).append(SPACE).append(projectUser.getPassword());
        }

        try {
            CommandLineIinterfaceUtils ecs = new CommandLineIinterfaceUtils(command.toString());
            ecs.executa();
//            Process p = Runtime.getRuntime().exec(command.toString());
        } catch (Throwable ex) {
            throw new VCSException(ex);
        }

        Output.println("------------> checkout done.");

        return revision;
    }

    public Revision doUpdate(Revision revision, ProjectUser projectUser, boolean updateFolderName) throws VCSException {
        Output.println("------------> begin update... ");

        File dir = new File(revision.getLocalPath());

        if (!dir.exists()) {
            throw new VCSException("O diretório [" + revision.getLocalPath() + "] deve ter uma revisão.");
        }

        StringBuilder command = new StringBuilder(COMMAND_UPDATE);

        if (revision.getNumber() != null) {
            command.append(PARAMETTER_REVISION).append(revision.getNumber());
            Output.println("------------> updating to revision " + revision.getNumber());
        }

        if (updateFolderName) {
            //change folder name to know the revision number
            revision.setLocalPath(revision.getLocalPath().substring(0, revision.getLocalPath().lastIndexOf("-r")) + "-r" + revision.getNumber());
            dir.renameTo(new File(revision.getLocalPath()));
        }

        //set the path to update
        command.append(SPACE).append(revision.getLocalPath());

        if (!projectUser.isAnonymous()) {
            command.append(SPACE).append(USERNAME).append(SPACE).append(projectUser.getLogin());
            command.append(SPACE).append(PASSWORD).append(SPACE).append(projectUser.getPassword());
        }

        try {
            CommandLineIinterfaceUtils ecs = new CommandLineIinterfaceUtils(command.toString());
            ecs.executa();
//            while (t.isAlive()) {
//                wait(100L);
//            }
        } catch (Throwable ex) {
            throw new VCSException(ex);
        }

        Output.println("------------> update done. ");
        return revision;
    }

    public String getName() {
        return name;
    }

    public String getRationale() {
        return rationale;
    }

    public List<String> getAllFiles(String url, ProjectUser projectUser) throws VCSException {
        Output.println("------------> begin getAllFiles... ");
        List<String> filesList = new ArrayList<String>();
        String[] comandLastRevision = null;

        if (projectUser.getLogin() == null || projectUser.getLogin().trim().isEmpty()) {
            comandLastRevision = new String[]{SVN, LIST, url, R, HEAD, RECURSIVE};
        } else {
            comandLastRevision = new String[]{SVN, LIST, url, R, HEAD, RECURSIVE, USERNAME, projectUser.getLogin(), PASSWORD, projectUser.getPassword()};
        }

        Process p;
        try {
            p = CommandLineIinterfaceUtils.executeComand(comandLastRevision);
        } catch (IOException ex) {
            throw new VCSException(ex);
        }
        readerError(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line = null;
        try {
            line = reader.readLine();
            Output.println("line=" + line);
            while (line != null) {
                filesList.add(line);
                line = reader.readLine();
                Output.println("line=" + line);
            }
        } catch (IOException ex) {
            throw new VCSException(ex);
        }

        Output.println("------------> getAllFiles done. ");
        return filesList;
    }

    private void readerError(Process p) throws VCSException {
        BufferedReader readerError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        if (readerError != null) {
            String erro;
            try {
                erro = readerError.readLine();
            } catch (IOException ex) {
                throw new VCSException(ex);
            }
            if (erro != null) {
                throw new VCSException(erro);
            }
        }
    }

    public Date getDateOfRevision(ProjectUser projectUser, Long numberRevision) throws VCSException {
        String[] comandLastRevision = null;

        if (projectUser.getLogin() == null || projectUser.getLogin().trim().isEmpty()) {
            comandLastRevision = new String[]{SVN, INFO, projectUser.getProject().getRepositoryUrl(), R, numberRevision.toString()};
        } else {
            comandLastRevision = new String[]{SVN, INFO, projectUser.getProject().getRepositoryUrl(), R, numberRevision.toString(), USERNAME, projectUser.getLogin(), PASSWORD, projectUser.getPassword(), NON_INTERACTIVE};
        }

        Process p;
        try {
            p = CommandLineIinterfaceUtils.executeComand(comandLastRevision);
        } catch (IOException ex) {
            throw new VCSException(ex);
        }
        readerError(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        /*
         COMMAND: svn info file:///d:/rep_pe/branches

         1- Path: branches
         2- URL: file:///D:/rep_pe/branches
         3- Repository Root: file:///D:/rep_pe
         4- Repository UUID: d1dc73b7-5fb2-0646-bfb7-f1b7128fe7dd
         5- Revision: 61
         6- Node Kind: directory
         7- Last Changed Author: dancastellani
         8 -Last Changed Rev: 41
         9- Last Changed Date: 2009-11-16 13:32:55 -0200 (seg, 16 nov 2009)
         */

        String line = null;
        try {
            line = reader.readLine();           //1
            line = reader.readLine();           //2
            line = reader.readLine();           //3
            line = reader.readLine();           //4
            line = reader.readLine();           //5
            line = reader.readLine();           //6
            line = reader.readLine();           //7
            line = reader.readLine();           //8
            line = reader.readLine();           //9
            Output.println("getDateOfRevision(line)=" + line);
            line = line.substring(24, 43);
        } catch (IOException ex) {
            throw new VCSException(ex);
        }
        return convertStringToDate(line, "yyyy-MM-dd hh:mm:ss");
    }

    public int doMerge(ProjectUser projectUser, String url, String ws, Long revFinal) throws VCSException {
        String[] comandLastRevision = null;
        Output.println("url=" + url);
        Output.println("ws=" + ws);

        if (projectUser.getLogin() == null || projectUser.getLogin().trim().isEmpty()) {
            comandLastRevision = new String[]{SVN, MERGE, R, "1:" + revFinal, url, ws, NON_INTERACTIVE};
        } else {
            comandLastRevision = new String[]{SVN, MERGE, R, "1:" + revFinal, url, ws, USERNAME, projectUser.getLogin(), PASSWORD, projectUser.getPassword(), NON_INTERACTIVE};
        }

        Process p;
        try {
            p = CommandLineIinterfaceUtils.executeComand(comandLastRevision);
        } catch (IOException ex) {
            throw new VCSException(ex);
        }
        //readerError(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        /*
         COMMAND: svn merge -r N:M SOURCE[@REV] [WCPATH]
         ...
         Summary of conflicts:
         Text conflicts: N
         Tree conflicts: N
         Skipped paths: N
         */

        String line = null;
        int qtdConflFisico = 0;
        try {
            line = reader.readLine();
            Output.println("line=" + line);

            while (line != null) {
                if (line.indexOf("Text conflicts:") != -1) {
                    qtdConflFisico = Integer.parseInt(line.substring(line.lastIndexOf(":") + 2));
                }

                line = reader.readLine();
                Output.println("line=" + line);
            }
        } catch (IOException ex) {
            throw new VCSException(ex);
        }

        return qtdConflFisico;
    }

    public static synchronized Date convertStringToDate(String texto, String formato) throws VCSException {
        try {
            DateFormat formatter = new SimpleDateFormat(formato);
            return (Date) formatter.parse(texto);
        } catch (ParseException ex) {
            throw new VCSException(ex);
        }
    }

    /**
     * <REFATORAR PARA LINHA DE COMANDO> @param project
     * @param projectUser
     * @return
     * @throws VCSException
     */
    public Set<Revision> getRevisions(SoftwareProject project, ProjectUser projectUser) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        return svn.getRevisions(project, projectUser);
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Calendar calendar) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        return svn.getRevision(project, projectUser, calendar);
    }

    public void doFullRevert(File[] paths) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        svn.doFullRevert(paths);
    }

    public void doCopyTo(String pathWorkspace, ProjectUser projectUser, String urlTarget) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        svn.doCopyTo(pathWorkspace, projectUser, urlTarget);
    }

    public Long doCommit(String pathWorkspace, ProjectUser projectUserChanged, String commitMessage) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        return svn.doCommit(pathWorkspace, projectUserChanged, commitMessage);
    }

    public Long doSwitchTo(String pathWorkspace, ProjectUser projectUserChanged, String urlTarget) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        return svn.doSwitchTo(pathWorkspace, projectUserChanged, urlTarget);
    }

    public Long doCheckout(File createdFileToworkspace, ProjectUser projectUser) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        return svn.doCheckout(createdFileToworkspace, projectUser);
    }

    public Revision getRevision(SoftwareProject project, ProjectUser projectUser, Long revisionNumber) throws VCSException {
        SVN_By_SVNKit svn = ObjectFactory.getObjectWithDataBaseDependencies(SVN_By_SVNKit.class);
        return svn.getRevision(project, projectUser, revisionNumber);
    }
}
