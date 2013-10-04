package br.uff.ic.oceano.experiments.ostra;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Repository;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.RepositoryService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ostra.service.traceability.TraceabilityService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Dancastellani
 */
public class TestTraceability {

    private SoftwareProject project;
    private ProjectUser pu;
    private Repository SVN;
    private static TraceabilityService traceabilityService;
    private static SoftwareProjectService softwareProjectService;
    private static ProjectUserService projectUserService;
    private static OceanoUserService oceanoUserService;
    private static RepositoryService repositoryService;

    public TestTraceability() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        traceabilityService = ObjectFactory.getObjectWithDataBaseDependencies(TraceabilityService.class);
        projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
        softwareProjectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
        repositoryService = ObjectFactory.getObjectWithDataBaseDependencies(RepositoryService.class);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws ObjetoNaoEncontradoException, ServiceException {
        project = softwareProjectService.getById(16L);
        pu = projectUserService.getByProjectAndOceanoUser(project, oceanoUserService.getByLogin("xan"));

//        ///////////////Configurar manualmente
//        SVN = repositoryService.getByName("SVN");
//        ConfigurationItem ci = new ConfigurationItem();
//        ci.setRepository(SVN);
//
//        project = new SoftwareProject();
//        project.setConfigurationItem(ci);
//        pu = new ProjectUser();
//        pu.setProject(project);
//
//        ci.setName("");
//        project.setRepositoryUrl("");
//        pu.setLogin("");
//        pu.setPassword("");
    }

    @Test
    public void buildTraceabilityMatrix() throws ServiceException, IOException {
        System.out.println("-------------------------------------------------- begin building matrix");
        String[][] buildTraceabilityMatrix = traceabilityService.buildTraceabilityMatrix(project, pu);

        final String SEPARATOR = ";";
        System.out.println("--------------------------------------------------");
        final StringBuilder matrix = new StringBuilder();
        for (String[] linha : buildTraceabilityMatrix) {
            String lineToPrint = "";
            for (String celula : linha) {
                if (celula != null) {
                    lineToPrint += celula;
                }
                lineToPrint += SEPARATOR;
            }
            System.out.println(lineToPrint.substring(0, lineToPrint.length() - 1));
            matrix.append(lineToPrint.substring(0, lineToPrint.length() - 1) + "\n");
        }
        System.out.println("--------------------------------------------------");

        final String fileName = "Traceability_" + project.getConfigurationItem().getName() + ".csv";
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
        bw.append(matrix.toString());
        bw.close();
        System.out.println("Arquivo salvo: " + fileName);
    }
}
