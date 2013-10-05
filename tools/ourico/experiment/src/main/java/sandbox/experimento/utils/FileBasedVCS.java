package sandbox.experimento.utils;

import br.ic.uff.gems.oceano.ourico.experimento.VCS.type.VCS;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

public abstract class FileBasedVCS implements VCS {

    /**
     * Generic merge for File based VCS
     */
    public void merge(File workspace, File studyWorkspace) {
        if (workspace.isDirectory()) {
            // "Note: this comparator imposes orderings that are inconsistent with equals."
            Comparator<File> comparator = new Comparator<File>() {

                public int compare(File f1, File f2) {
                    int result = (f1.isDirectory() ? 0 : 1) - (f2.isDirectory() ? 0 : 1);
                    if (result == 0) {
                        result = f1.getName().compareTo(f2.getName());
                    }
                    return result;
                }
            };

            // New Workspace
            SortedSet<File> newWorkspace = new TreeSet<File>(comparator);
            newWorkspace.addAll(Arrays.asList(workspace.listFiles()));

            // Old Workspace
            SortedSet<File> oldWorkspace = new TreeSet<File>(comparator);
            oldWorkspace.addAll(Arrays.asList(studyWorkspace.listFiles()));

            // Remove control directories from old workspace (.svn and CVS)
            File svnControlDirectory = new File(studyWorkspace, ".svn");
            File cvsControlDirectory = new File(studyWorkspace, "CVS");
//			File gitControlDirectory = new File(studyWorkspace, ".git");
            oldWorkspace.remove(svnControlDirectory);
            oldWorkspace.remove(cvsControlDirectory);
//			oldWorkspace.remove(gitControlDirectory);

            // Removed Files
            SortedSet<File> removedFiles = new TreeSet<File>(comparator);
            removedFiles.addAll(oldWorkspace);
            removedFiles.removeAll(newWorkspace);

            // Added Files
            SortedSet<File> addedFiles = new TreeSet<File>(comparator);
            addedFiles.addAll(newWorkspace);
            addedFiles.removeAll(oldWorkspace);

            // Keeped Source Files
            SortedSet<File> keepedSourceFiles = new TreeSet<File>(comparator);
            keepedSourceFiles.addAll(newWorkspace);
            keepedSourceFiles.retainAll(oldWorkspace);

            // Keeped Target Files
            SortedSet<File> keepedTargetFiles = new TreeSet<File>(comparator);
            keepedTargetFiles.addAll(oldWorkspace);
            keepedTargetFiles.retainAll(newWorkspace);

            // Remove all "Removed Files"
            for (File fileToRemove : removedFiles) {
                try {
                    remove(fileToRemove.getPath());
                } catch (Exception e) {
                    Logger.global.info("Could not remove file " + fileToRemove + ": " + e.getMessage());
                }
            }

            // Add all "Added Files"
            for (File fileToAdd : addedFiles) {
                File newFile = new File(studyWorkspace, fileToAdd.getName());
                try {
//                    if (fileToAdd.renameTo(newFile)) {
//                        add(newFile.getPath());
////                        System.out.println("add " + newFile.getPath());
//                    } else {
//                        throw new RuntimeException("Could not rename file " + fileToAdd + " to " + newFile);
//                    }
                    while (!fileToAdd.renameTo(newFile)) {
                        String nameFile = newFile.getAbsolutePath();
                        nameFile = nameFile.substring(0, nameFile.lastIndexOf("\\"));
                        new File(nameFile).mkdir();
                    }
                    add(newFile.getPath());
                    System.out.println("add " + newFile.getPath());

                } catch (Exception e) {
                    Logger.global.info("Could not add file " + newFile + ": " + e.getMessage());
                }
            }

            // Run recursivelly for "Keeped Files"
            for (File sourceFile : keepedSourceFiles) {
                File targetFile = keepedTargetFiles.tailSet(sourceFile).first();
                merge(sourceFile, targetFile);
            }
        } else {
            studyWorkspace.delete();
            workspace.renameTo(studyWorkspace);
        }
    }

    /**
     * Add a file to version control.
     */
    protected abstract void add(String path) throws IOException;

    /**
     * Remove a file from version control.
     */
    protected abstract void remove(String path) throws IOException;

    /**
     * Run a specific command with a input redirected from a file
     */
    protected void run(String[] command, final File inputFile, File outputFile, File workDirectory) throws IOException {
        FileOutputStream fileOut = null;
        BufferedInputStream inputStream = null;
        BufferedReader reader = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(workDirectory);
            processBuilder.redirectErrorStream(outputFile == null);

            final Process process = processBuilder.start();

            // Input Stream
            if (inputFile != null) {
                Thread thread = new Thread(new Runnable() {

                    public void run() {
                        BufferedOutputStream outputStream = null;
                        FileInputStream fileIn = null;
                        try {
                            outputStream = new BufferedOutputStream(process.getOutputStream());
                            fileIn = new FileInputStream(inputFile);
                            byte[] buffer = new byte[8 * 1024];
                            int length = fileIn.read(buffer);
                            while (length != -1) {
                                outputStream.write(buffer, 0, length);
                                length = fileIn.read(buffer);
                            }
                        } catch (Exception e) {
                            Logger.global.warning("Could not redirect input from " + inputFile);
                            throw new RuntimeException(e);
                        } finally {
                            try {
                                fileIn.close();
                            } catch (Exception e) {
                            }
                            try {
                                outputStream.close();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
                thread.start();
            }

            // Output Stream
            if (outputFile != null) {
                fileOut = new FileOutputStream(outputFile);
            }
            inputStream = new BufferedInputStream(process.getInputStream());
            byte[] buffer = new byte[8 * 1024];
            int length = inputStream.read(buffer);

            while (length != -1) {
                if (fileOut != null) {
                    fileOut.write(buffer, 0, length);
                } else {
                    System.out.print(new String(buffer, 0, length));

                }
                length = inputStream.read(buffer);
            }

            // Error Stream
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String message = reader.readLine();
            while (message != null) {
                System.out.print(message);

                message = reader.readLine();
            }
        } catch (IOException e) {
            Logger.global.warning("Could not run command " + Arrays.asList(command));
            throw e;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
            }
            try {
                fileOut.close();
            } catch (Exception e) {
            }
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
    }
    /**
     * @return the outputWindow
     */
}
