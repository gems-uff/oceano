package br.uff.ic.gems.peixeespadacliente.model;

import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.oceano.core.exception.VCSException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author João Felipe
 */
public class ProjectVCSTest extends ProjectVCS {

    private File vcsPath = null;
    private File workspace = null;
    private File current = null;

    public ProjectVCSTest(File vcsPath) {
        super();
        this.vcsPath = vcsPath;
        ProjectVCSTest.deleteDir(vcsPath);
        vcsPath.mkdirs();
    }

    @Override
    public void doReset() throws VCSException {
        //current -> workspace
        if (current == null || workspace == null) {
            throw new VCSException();
        }
        deleteDir(workspace);
        copyDirectory(current, workspace);
    }

    @Override
    public Long doCommit(String message) throws VCSException {
        if (current == null || workspace == null) {
            throw new VCSException();
        }
        deleteDir(current);
        copyDirectory(workspace, current);

        return 0L;
    }

    @Override
    public Long doCheckout(File workspace) throws VCSException {
        this.workspace = workspace;
        if (current == null) {
            current = (new File(vcsPath, "master")).getAbsoluteFile();
            copyDirectory(new File(repositoryUrl), current);
        }

        copyDirectory(current, workspace);

        if (workspace == null) {
            throw new VCSException();
        }
        return 0L;
    }

    @Override
    public void doCopyTo(String branch) throws VCSException {
        String branchName = branch.substring(branch.substring(0, branch.length() - 1).lastIndexOf("/") + 1, branch.length() - 1);
        //worskpace -> branch
        if (workspace == null) {
            throw new VCSException();
        }
        File b = new File(vcsPath, branchName).getAbsoluteFile();
        deleteDir(b);
        copyDirectory(workspace, b);

    }

    @Override
    public Long doSwitchTo(String branch) throws VCSException {
        String branchName = branch.substring(branch.substring(0, branch.length() - 1).lastIndexOf("/") + 1, branch.length() - 1);

        // current = branch
        // current -> workspace
        if (workspace == null) {
            throw new VCSException();
        }
        current = new File(vcsPath, branchName).getAbsoluteFile();
        deleteDir(workspace);
        copyDirectory(current, workspace);

        return 0L;
    }

    public static boolean deleteDir(File dir) {

        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDir(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (dir.delete());
    }

    public static void copyDirectory(File sourceLocation, File targetLocation) throws VCSException {
        try {
            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists()) {
                    targetLocation.mkdirs();
                }
                String[] children = sourceLocation.list();
                for (int i = 0; i < children.length; i++) {
                    copyDirectory(new File(sourceLocation, children[i]),
                            new File(targetLocation, children[i]));
                }
            } else {

                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (IOException e) {
            throw new VCSException(e);
        }
    }
}
