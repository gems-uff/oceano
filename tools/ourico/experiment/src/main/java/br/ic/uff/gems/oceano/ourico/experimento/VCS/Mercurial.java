package br.ic.uff.gems.oceano.ourico.experimento.VCS;

import br.ic.uff.gems.oceano.ourico.experimento.VCS.type.DistributedVCS;
import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import sandbox.experimento.utils.FileBasedVCS;
import sandbox.experimento.utils.FileUtils;

/**
 * Encapsulates Git commands
 * 
 * @author kann
 */
public class Mercurial extends FileBasedVCS implements DistributedVCS {

    /**
     * Git program
     */
    private String HG = "hg";
    /**
     * Init repository
     */
    private static final String INIT = "init";
    /**
     * Import command
     */
    private static final String PUSH = "push";
    /**
     * pull command
     */
    private static String PULL = "pull";
    /**
     * Export command
     */
    private static final String EXPORT = "export";
    /**
     * Check-out command
     */
    private static final String CLONE = "clone";
    /**
     * Check-in command
     */
    private static final String COMMIT = "commit";
    /**
     * Log command
     */
    private static final String LOG = "log";
    /**
     * Show command
     */
    private static final String SHOW = "show";
    /**
     * Add command
     */
    private static final String ADD = "add";
    /**
     * Remove command
     */
    private static final String REMOVE = "rm";
    /**
     * Remove command
     */
    private static final String REMOTE = "remote";
    /**
     * origin branch
     */
    private static final String ORIGIN = "origin";
    /**
     * master branch
     */
    private static final String MASTER = "master";
    /**
     * && command
     */
    private static final String AND = "&&";
    /**
     * CD command
     */
    private static final String CD = "cd";
    /**
     * Message switch
     */
    private static final String MESSAGE = "-m";
    /**
     * Revision switch
     */
    private static final String REVISION = "--revision";
    /**
     * Non interactive switch
     */
    private static final String NON_INTERACTIVE = "--quiet";
    /**
     * Verbose switch
     */
    private static final String VERBOSE = "--verbose";
    /**
     * Force switch
     */
    private static final String FORCE = "--force";

    private static final String TIP = "tip";
    /**
     * Singleton instance
     */
    private static Mercurial instance = null;
    private int countPush = 10;
    private int countPushOrigin = 10;
    private boolean delDir = true;

    /**
     * Singleton constructor
     */
    private Mercurial() {
    }

    public void setPushOriginal(String push){
        if(push == null || push.trim().isEmpty()){
            countPushOrigin = 1;
        }else{
            countPushOrigin = Integer.parseInt(push.trim());
        }
        countPush = countPushOrigin;
    }

    /**
     * Provides the singleton instance
     */
    public synchronized static Mercurial getInstance() {
        if (instance == null) {
            instance = new Mercurial();
        }
        return instance;
    }

    /**
     * command: hg init <repository>
     */
    public void create(String repository) throws IOException {

        Process p = Runtime.getRuntime().exec(new String[]{"mkdir", repository});
        try {
            p.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(Mercurial.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] command = {HG, INIT};

        long before = System.currentTimeMillis();
        run(command, null, null, new File(repository));
        long after = System.currentTimeMillis();
    }

    /**
     * command: hg push <repository>
     */
    public void insert(String exportedWorkspace, String studyRepository) throws IOException {

        /*
            Copia o diretorio exportado para o repositorio de estudo
         
         */
//
        File wp = new File(exportedWorkspace);
        String[] command = null;
//
        File studyRep = new File(studyRepository);

        long before = System.currentTimeMillis();

//        FileUtils.copyDirectory(wp, studyRep);
        command = new String[]{HG, INIT, exportedWorkspace};
        run(command, null, null, null);
        //command = (new String[]{HG, REMOTE, ADD, ORIGIN, studyRepository});
        //run(command, null, null, wp);
        command = (new String[]{HG, ADD, "."});
        run(command, null, null, wp);
        command = (new String[]{HG, COMMIT, MESSAGE, "Initial Commit"});
        run(command, null, null, wp);
        command = new String[]{HG, PUSH, studyRepository};
        run(command, null, null, wp);

        long after = System.currentTimeMillis();
        delDir = true;
    }

    /**
     * command: git export --revision <configuration> --non-interactive <repository> <workspace>
     */
    public void export(String configuration, String repository, String workspace) throws IOException {
        String[] command = {HG, EXPORT, REVISION, configuration, NON_INTERACTIVE, repository, workspace};
        run(command, null, null, null);
    }

    /**
     * command: hg clone <repository> <workspace>
     */
    public void clone(String configuration, String repository, String workspace) throws IOException {
        String[] command = {HG, CLONE, repository, workspace};

        long before = System.currentTimeMillis();
        run(command, null, null, null);
        long after = System.currentTimeMillis();

    }

    /**
     * command: "hg commit -m <message>"
     */
    public void checkin(String workspace) throws IOException {
        String[] command = {HG, COMMIT, MESSAGE, "message"};

        File wp = new File(workspace);


        long before = System.currentTimeMillis();
        run(command, null, null, wp);
        long after = System.currentTimeMillis();


    }

    public void push(File workSpace, String destinationRepositoryURL) throws IOException {

        long before = System.currentTimeMillis();

        run(new String[]{HG, PUSH, FORCE, destinationRepositoryURL}, null, null, workSpace);
        long after = System.currentTimeMillis();
    }

    /**
     * command: "hg log -pr tip"
     */
    public void list(String repository) throws IOException {
        String[] command = {HG, LOG, "-pr", TIP};

        long before = System.currentTimeMillis();
        run(command, null, null, new File(repository));
        long after = System.currentTimeMillis();
    }

    /**
     * command: "git add <path>"
     */
    protected void add(String path) throws IOException {
        String[] command = {HG, ADD, path};
        run(command, null, null, null);
    }

    protected void add(String path, File pathWorkSpace) throws IOException {
        String[] command = {HG, ADD, path};
        run(command, null, null, pathWorkSpace);
    }

    protected void addAll(String path) throws IOException {
        String[] command = {HG, ADD, "."};
        run(command, null, null, new File(path));
    }

    /**
     * command: "git rm <path> --force --non-interactive"
     */
    protected void remove(String path) throws IOException {
//        String[] command = {GIT, FORCE, NON_INTERACTIVE, REMOVE, path};
        String[] command = {HG, REMOVE, path, "-r"};
        run(command, null, null, null);
    }

    

    protected void remove(File fileRemove, File workSpace) throws IOException {
        String[] command = null;

        String path = fileRemove.getParent();
//        if(fileRemove.isDirectory()){
//            command = new String[]{HG, REMOVE, fileRemove.getPath(), "-f"};
//        }else{
        command = new String[]{HG, REMOVE, fileRemove.getPath(), "-f"};
//        }
        run(command, null, null, workSpace);

        new File(path).mkdirs();
//        if(!fileRemove.isDirectory()){
//            String nameFile = fileRemove.getParent();
////            nameFile = nameFile.substring(0, nameFile.lastIndexOf("\\"));
//            File f = new File(nameFile);
//            if(!f.exists()){
//                f.mkdir();
//            }
//        }
    }

    public File createWorkSpace(String workspaceStudy) {
        File f = new File(workspaceStudy);
        FileUtils.recursiveDelete(f);
        try {
            Runtime.getRuntime().exec(new String[]{HG, INIT, workspaceStudy}).waitFor();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        return f;
    }

    @Override
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
            if(studyWorkspace.listFiles() != null){
//                System.err.println("REPOSITORY WITHOUT FILES "+studyWorkspace);
                oldWorkspace.addAll(Arrays.asList(studyWorkspace.listFiles()));
            }else{
//                oldWorkspace.add(studyWorkspace);
                return;
            }

            File mercurialControlDirectory = new File(studyWorkspace, ".hg");
            oldWorkspace.remove(mercurialControlDirectory);

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
                    remove(fileToRemove, studyWorkspace);
//                    if(fileToRemove.exists()) {
//                        if (!fileToRemove.delete()) {
//                            System.err.println("NÃO PODE REMOVER O ARQUIVO " + fileToRemove.getPath());
//                            run(new String[]{GIT, REMOVE, fileToRemove.getPath(), "-q", "-f", "-r"}, null, null, studyWorkspace);
//                        } else {
//                            System.out.println("DELECÃO COM SUCESSO depois de dar um git rm");
//                            run(new String[]{GIT, REMOVE, fileToRemove.getPath(), "-q", "-f", "-r"}, null, null, studyWorkspace);
//                        }
//                    } else {
//                        System.out.println("DELECÃO COM SUCESSO "+fileToRemove.getPath());
//                    }
                } catch (Exception e) {
//                    try {
//                        run(new String[]{"rm", "-rf", fileToRemove.getName()}, null, null, null);
////                        fileToRemove.mkdirs();
//                    } catch (IOException ex) {
//                        Logger.global.info("Could not remove RM -RF" + fileToRemove + ": " + e.getMessage());
//                    }
                    Logger.global.info("Could not remove file " + fileToRemove + ": " + e.getMessage());
                }
            }

//			// Add all "Added Files"
            for (File fileToAdd : addedFiles) {
                File newFile = new File(studyWorkspace, fileToAdd.getName());
                try {
                    if(fileToAdd.renameTo(newFile)){
//                    while (!fileToAdd.renameTo(newFile)) {
//                        String nameFile = newFile.getAbsolutePath();
//                        nameFile = nameFile.substring(0, nameFile.lastIndexOf("\\"));
//                        new File(nameFile).mkdir();
//                    }
//                    add(newFile.getPath(), studyWorkspace);
                            System.out.println("add " + newFile.getPath());
                }
                } catch (Exception e) {
                    e.printStackTrace();
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

    

    public void plan(String studyRepositoryURL, File workspace, int countResivion, String workspaceStudy) throws Exception {

        // List the repository
        list(studyRepositoryURL);

        File studyWorkspace = null;
        if (delDir) {
            delDir = false;
            // Create study workspace
            studyWorkspace = createWorkSpace(workspaceStudy);
            
        }else{
            studyWorkspace = new File(workspaceStudy);
        }
        
        // Clone the configuration
        File clonedWorkspace = new File(workspace.getPath()+"HgClone");
        FileUtils.recursiveDelete(clonedWorkspace);
        clone("HEAD", studyRepositoryURL, clonedWorkspace.getAbsolutePath());

        // Merge the configurations
        merge(workspace, studyWorkspace);
        
        addAll(studyWorkspace.getAbsolutePath());
//        System.out.println("CHECKINNNNNNN \n");
        checkin(studyWorkspace.getPath());

        if (--countPush == 0 || countResivion == 0) {
           push(studyWorkspace, studyRepositoryURL);
           countPush = countPushOrigin;
           FileUtils.recursiveDelete(clonedWorkspace);
        }
    }

    public void setConfigureMainCommands(String path) {
        HG = path+HG;
    }
}
