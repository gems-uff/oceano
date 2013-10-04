package br.uff.ic.oceano.experiments.ostra;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.contexto.ConstantesAplicacao;
import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.Output;
import java.io.File;
import java.util.Set;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Dancastellani
 */
public class TestVerifyRepository {

    private static SoftwareProjectService projectService;
    private static VCSService vcsService;
    private static OceanoUserService oceanoUserService;
    private static ProjectUserService projectUserService;

    public TestVerifyRepository() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        vcsService = ObjectFactory.getObjectWithoutDataBaseDependencies(VCSService.class);
        oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
        projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);

        cleanCheckOutFolder();
    }

//    @AfterClass
    public static void tearDownClass() throws Exception {
        cleanCheckOutFolder();
    }

    private static void cleanCheckOutFolder() {
        System.out.println("----------------------------------------");
        if (ApplicationConstants.CLEAN_CHECKOUT_DIRECTORY) {
            File file = new File(ConstantesAplicacao.DIR_BASE_CHECKOUTS);
            FileUtils.deleteDirectory(file);
            System.out.println(">>>> Cleaning checkout directory... " + !file.exists());
        }
        System.out.println("----------------------------------------");
        System.out.println("");
    }

    @BeforeMethod
    public void setUp() throws ObjetoNaoEncontradoException, ServiceException {
    }

    @Test
    public void verifyProjectsRepository() throws Exception {
        final Long idProject = 2L;
        final String loginOceano = "xan";

        final SoftwareProject projectToVeifyRepository = projectService.getById(idProject);
//        projectToVeifyRepository.setRepositoryUrl("file:///e:/svn-iduff");
        
        final OceanoUser user = oceanoUserService.getByLogin(loginOceano);
        final ProjectUser pu = projectUserService.getByProjectAndOceanoUser(projectToVeifyRepository, user);


        final Set<Revision> revisions = vcsService.getRevisions(projectToVeifyRepository, pu);

        boolean first = true;
        System.out.println("Verify " + projectToVeifyRepository);
        String localPath = null;

        for (Revision revision : revisions) {
            Output.println("Checking: " + revision);

            if (first) {
                Output.println("    Chekout...");
                vcsService.doCheckout(revision, pu, true);
                localPath = revision.getLocalPath();
                first = false;
            } else {
                Output.println("    Update...");
                revision.setLocalPath(localPath);
                vcsService.doUpdate(revision, pu, false);
            }

            if (revision.getChangedFiles() == null) {
                continue;
            }

            Output.println("    Items to check: " + revision.getChangedFiles().size());
            for (VersionedItem versionedItem : revision.getChangedFiles()) {
                final Item item = versionedItem.getItem();
                final File itemFile = new File(revision.getLocalPath().concat(item.getPath()));

                Output.print("      Item: " + item.getPath());

                if (versionedItem.getType() == VersionedItem.TYPE_DELETED) {
                    Output.println("  deleted ");
                    continue;
                }

                if (!itemFile.exists()) {
                    Output.println("    Não encontrado!");
                    throw new RuntimeException(revision.toString() + ": Item <" + item.getPath() + ">not found.");
                } else {
                    Output.println("    ok");

                }
            }
            Output.println("");
        }
        Output.println("Project repository healthy.");
    }
}
