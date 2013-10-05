package br.ic.uff.gems.oceano.ourico.experimento.VCS;

import br.ic.uff.gems.oceano.ourico.experimento.VCS.type.CentralizedVCS;
import java.io.File;
import java.io.IOException;
import sandbox.experimento.utils.FileBasedVCS;
import sandbox.experimento.utils.FileUtils;

/**
 * Encapsulates Subversion commands
 * 
 * @author murta
 */
public class CVS extends FileBasedVCS implements CentralizedVCS {
	
	/**
	 * Subversion program
	 */
	private static final String CVS = "cvs";
	
	/**
	 * Create command
	 */
	private static final String INIT = "init";
	
	/**
	 * Import command
	 */
	private static final String IMPORT = "import";
	
	/**
	 * Check-out command
	 */
	private static final String CHECKOUT = "checkout";
	
	/**
	 * Check-in command
	 */
	private static final String COMMIT = "commit";
	
	/**
	 * Log command
	 */
	private static final String LOG = "rlog";
	
	/**
	 * Add command
	 */
	private static final String ADD = "add";
	
	/**
	 * Remove command
	 */
	private static final String REMOVE = "remove";
	
	/**
	 * Message switch
	 */
	private static final String CVSROOT = "-d";
	
	/**
	 * Directory switch
	 */
	private static final String DIRECTORY = "-d";
	
	/**
	 * Message switch
	 */
	private static final String MESSAGE = "-m";
	
	/**
	 * Recursive switch
	 */
	private static final String RECURSIVE = "-R";
	
	/**
	 * Force switch
	 */
	private static final String FORCE = "-f";
	
	/**
	 * command: cvs -d <repository> init
	 */
    public void create(String repository) throws IOException {
		String[] command = { CVS, CVSROOT, repository, INIT }; 

		long before = System.currentTimeMillis();
		run(command, null, null, null);
		long after = System.currentTimeMillis();
    }

	/**
	 * command: cvs -d <repository> import -m "" module vendor release
	 */
    public void insert(String workspace, String repository) throws IOException {
		String[] command = { CVS, CVSROOT, repository, IMPORT, MESSAGE, "\"\"", "module", "vendor", "release" };
		
		
		long before = System.currentTimeMillis();
		run(command, null, null, new File(workspace));
		long after = System.currentTimeMillis();
    }
	
	/**
	 * command: cvs -d <repository> checkout module
	 */
    public void checkout(String configuration, String repository, String workspace) throws IOException {
    	String[] command = { CVS, CVSROOT, repository, CHECKOUT, DIRECTORY, workspace, "module" }; 
		
		long before = System.currentTimeMillis();
		run(command, null, null, new File(workspace).getParentFile());
		long after = System.currentTimeMillis();
    }
    
    /**
     * command: cvs commit -m ""
     */
    public void checkin(String workspace) throws IOException {
		String[] command = { CVS, COMMIT, MESSAGE, "\"\"" }; 

		long before = System.currentTimeMillis();
		run(command, null, null, new File(workspace));
		long after = System.currentTimeMillis();
    }
    
	/**
	 * command: cvs -d <repository> rlog module
	 */
    public void list(String repository) throws IOException {		
		String[] command = { CVS, CVSROOT, repository, LOG, "module" };
		
		long before = System.currentTimeMillis();
		run(command, null, null, null);
		long after = System.currentTimeMillis();
	}
    
    /**
     * command: "svn add <path>"
     */
    protected void add(String path) throws IOException {
    	File file = new File(path);
    	
    	if (!"CVS".equals(file.getName())) {
	    	String[] command = { CVS, ADD, file.getName() }; 
			run(command, null, null, file.getParentFile());

	    	if (file.isDirectory()) {
	    		for (File subFile : file.listFiles()) {
	    			add(subFile.getPath());
	    		}
	    	}
    	}
    }
    
    /**
     * command: "cvs remove -R -f <path>"
     */
    protected void remove(String path) throws IOException {
    	File file = new File(path);
		String[] command = { CVS, REMOVE, RECURSIVE, FORCE, file.getName() }; 
		run(command, null, null, file.getParentFile());
    }

    public File createWorkSpace(String workSpaceStudy) {
        File f = new File(workSpaceStudy);
        FileUtils.recursiveDelete(f);
        return f;
    }

    public void plan(String studyRepositoryURL, File workspace, int finalConfiguration, String workspaceStudy) throws Exception {
         // List the repository
        list(studyRepositoryURL);
        // Create study workspace
        File studyWorkspace = createWorkSpace(workspaceStudy);
        // Check-out the configuration
        checkout("HEAD", studyRepositoryURL, studyWorkspace.getPath());
        // Merge the configurations
        merge(workspace, studyWorkspace);
        // Check-in XMI to Odyssey-VCS
        checkin(studyWorkspace.getPath());
    }

    public void setConfigureMainCommands(String vcs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
