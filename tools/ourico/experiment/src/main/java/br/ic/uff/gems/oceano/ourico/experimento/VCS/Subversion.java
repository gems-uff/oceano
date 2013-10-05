package br.ic.uff.gems.oceano.ourico.experimento.VCS;

import br.ic.uff.gems.oceano.ourico.experimento.VCS.type.CentralizedVCS;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.checkout.ciclo.CheckoutCiclo;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.ourico.service.VerificacaoPosCheckoutService;
import java.io.File;
import java.io.IOException;
import sandbox.experimento.utils.FileBasedVCS;
import sandbox.experimento.utils.FileUtils;

/**
 * Encapsulates Subversion commands
 * 
 * @author murta
 */
public class Subversion extends FileBasedVCS implements CentralizedVCS {

    /**
     * Subversion program
     */
    private String SVN = "svn";
//    private static final String SVN = "svn";
    /**
     * Subversion admin program
     */
    private String SVN_ADMIN = "svnadmin";
    /**
     * Create command
     */
    private static final String CREATE = "create";
    /**
     * Import command
     */
    private static final String IMPORT = "import";
    /**
     * Export command
     */
    private static final String EXPORT = "export";
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
    private static final String LOG = "log";
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
    private static final String MESSAGE = "--message";
    /**
     * Revision switch
     */
    private static final String REVISION = "-r";
    /**
     * Non interactive switch
     */
    private static final String NON_INTERACTIVE = "--non-interactive";
    /**
     * Verbose switch
     */
    private static final String VERBOSE = "--verbose";
    /**
     * Force switch
     */
    private static final String FORCE = "--force";
    /**
     * Singleton instance
     */
    private static Subversion instance = null;
    /**
     * Singleton instance to Font
     */
    private static Subversion instanceFont;

    /**
     * Singleton constructor
     */
    private Subversion() {

        checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
        verificacaoPosCheckoutService = ObjectFactory.getObjectWithDataBaseDependencies(VerificacaoPosCheckoutService.class);

    }
    CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    VerificacaoPosCheckoutService verificacaoPosCheckoutService = ObjectFactory.getObjectWithDataBaseDependencies(VerificacaoPosCheckoutService.class);

    /**
     * Provides the singleton instance
     */
    public synchronized static Subversion getInstance() {
        if (instance == null) {
            instance = new Subversion();
        }
        return instance;
    }

    public synchronized static Subversion getInstanceToFont(String path) {
        if (instanceFont == null) {
            instanceFont = new Subversion();
            instanceFont.setConfigureMainCommands(path);
        }
        return instanceFont;
    }

    /**
     * command: svnadmin create <repository>
     */
    public void create(String repository) throws IOException {
        String[] command = {SVN_ADMIN, CREATE, repository};

        long before = System.currentTimeMillis();
        run(command, null, null, null);
        long after = System.currentTimeMillis();
    }

    /**
     * command: svn import --non-interactive <workspace> <repository>
     */
    public void insert(String workspace, String repository) throws IOException {
        String[] command = {SVN, IMPORT, MESSAGE, "\"\"", NON_INTERACTIVE, workspace, repository};


        long before = System.currentTimeMillis();
        run(command, null, null, null);
        long after = System.currentTimeMillis();
    }

    /**
     * command: svn export --revision <configuration> --non-interactive <repository> <workspace>
     */
    public void export(String configuration, String repository, String workspace) throws IOException {
        String[] command = {SVN, EXPORT, REVISION, configuration, repository, workspace};
//        String[] command = {SVN, EXPORT, REVISION, configuration, NON_INTERACTIVE, repository, workspace};



        run(command, null, null, null);
    }

    public void export(String configuration, String repository, String subRepository, String workspaceCO, String workspaceEX) throws IOException {

        checkout(configuration, repository, workspaceCO);

        String[] command = {SVN, EXPORT, workspaceCO + subRepository, workspaceEX};



        run(command, null, null, null);
    }

    /**
     * command: svn checkout --revision <configuration> --non-interactive <repository> <workspace>
     */
    public void checkout(String configuration, String repository, String workspace) throws IOException {
        String[] command = {SVN, CHECKOUT, REVISION, configuration, repository, workspace};
//        String[] command = {SVN, CHECKOUT, REVISION, configuration, NON_INTERACTIVE, repository, workspace};

        long before = System.currentTimeMillis();
        run(command, null, null, null);
        long after = System.currentTimeMillis();

    }

    /**
     * command: "svn commit <workspace> --message <message> --non-interactive"
     */
    public void checkin(String workspace) throws IOException {
        String[] command = {SVN, COMMIT, workspace, MESSAGE, "\"\"", NON_INTERACTIVE};

        long before = System.currentTimeMillis();
        run(command, null, null, null);
        long after = System.currentTimeMillis();

    }

    /**
     * command: "svn log --non-interactive <url>"
     */
    public void list(String repository) throws IOException {
        String[] command = {SVN, LOG, NON_INTERACTIVE, VERBOSE, repository};

        long before = System.currentTimeMillis();
        run(command, null, null, null);
        long after = System.currentTimeMillis();
    }

    /**
     * command: "svn add <path>"
     */
    protected void add(String path) throws IOException {
        String[] command = {SVN, ADD, path};
        run(command, null, null, null);
    }

    /**
     * command: "svn rm <path> --force --non-interactive"
     */
    protected void remove(String path) throws IOException {
        String[] command = {SVN, REMOVE, path, FORCE, NON_INTERACTIVE};
        run(command, null, null, null);
    }

    public File createWorkSpace(String workSpaceStudy) {
        File f = new File(workSpaceStudy);
        FileUtils.recursiveDelete(f);
        return f;
    }

    public void plan(String studyRepositoryURL, File exportedWorkspace, int finalConfiguration, String workspaceStudy) throws Exception {

//        String url_oceano = "http://localhost:8082/oceano/JSONServlet";
        String url_oceano = "http://localhost:8092/oceano/JSONServlet";
//        String url_oceano = "http://10.0.0.102:8092/oceano/JSONServlet";
//        String url_oceano = "https://gems.ic.uff.br/oceano/JSONServlet";


        // List the repository
        list(studyRepositoryURL);
        // Create study workspace

        File studyWorkspace = createWorkSpace(workspaceStudy);
        // Check-out the configuration
        CheckoutCiclo checkoutCiclo = new CheckoutCiclo();

        CheckOut remoto = checkoutCiclo.remoto(studyRepositoryURL, "marapa", "marapao", studyWorkspace.getPath(), url_oceano);
        System.out.println("svn co " + studyRepositoryURL + "  " + studyWorkspace.getPath());



        if (!workspaceStudy.endsWith("/")) {
            workspaceStudy += "/";
        }

        workspaceStudy += remoto.getAutobranch();
        studyWorkspace = new File(workspaceStudy);

        merge(exportedWorkspace, studyWorkspace);

        checkin(workspaceStudy);

        if (remoto.getAutobranch() % 20 == 0) {

            System.out.println("----------------Esperando 2 minutos---------------------------------");
            System.gc();
            Thread.sleep(60000);



            File f = new File("/home/marapao/experiementos/scripts/reiniciaglassfish");
            Runtime r = Runtime.getRuntime();
            Process exec = null;

            System.out.println("---------------------Reiniciando GlassFish---------------------------------------");
            if (f.canExecute()) {
                exec = r.exec(f.getAbsolutePath());
            }

            exec.waitFor();

            System.out.println("----------------Esperando 1 minutos---------------------------------");
            System.gc();
            Thread.sleep(60000);
            //reiniciar o glassfish
            ///home/marapao/glassfish/bin/asadmin stop-domain
            ///home/marapao/glassfish/bin/asadmin start-domain


        }

    }

    public void setConfigureMainCommands(String path) {
        SVN = path + SVN;
        SVN_ADMIN = path + SVN_ADMIN;
    }
}
