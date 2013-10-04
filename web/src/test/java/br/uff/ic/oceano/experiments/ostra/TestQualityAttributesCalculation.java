package br.uff.ic.oceano.experiments.ostra;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.contexto.ConstantesAplicacao;
import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.ostra.service.OstraQualityAtributesService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
//import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;

/**
 *
 * @author Dancastellani
 */
public class TestQualityAttributesCalculation {

    private SoftwareProjectService projectService;
    private List<SoftwareProject> projects = new ArrayList<SoftwareProject>();
    private OceanoUser userDanielOceano;
    private OceanoUserDao oceanoUserDao;
    private OstraQualityAtributesService ostraQualityAtributesService;

    public TestQualityAttributesCalculation() {
    }

    private void initializeProjectList() throws ServiceException {
//        /*this.projects.add(projectService.getProjectToDetailById(1L));*/ //oceano - fora de execução
//        this.projects.add(projectService.getProjectToDetailById(2L)); //iduff
//        this.projects.add(projectService.getProjectToDetailById(3L)); //publico-core
//        this.projects.add(projectService.getProjectToDetailById(4L)); //commons-utils
//        this.projects.add(projectService.getProjectToDetailById(5L)); //monitoria

//        this.projects.add(projectService.getProjectToDetailById(7L)); //academico-pos

//        this.projects.add(projectService.getProjectToDetailById(8L)); //oceano-core
//        this.projects.add(projectService.getProjectToDetailById(9L)); //oceano-web
//        this.projects.add(projectService.getProjectToDetailById(12L)); //iduff-svn-local
//        this.projects.add(projectService.getProjectToDetailById(14L)); //iduff 2 - para o artigo seke
//        this.projects.add(projectService.getProjectToDetailById(15L)); //hudson-maven-plugin-branch
//        this.projects.add(projectService.getProjectToDetailById(17L)); //Eclipse IAM
//        this.projects.add(projectService.getProjectToDetailById(18L)); //Animal Sniffer
    }

    @Test
    public void extractQualityAttributes() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            ostraQualityAtributesService.calculateQualityAttributes(project);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("----------------------------------------");
        if (ApplicationConstants.CLEAN_CHECKOUT_DIRECTORY) {
            File file = new File(ConstantesAplicacao.DIR_BASE_CHECKOUTS);
            FileUtils.deleteDirectory(file);
            System.out.println(">>>> Cleaning checkout directory... " + !file.exists());
        }
        System.out.println("----------------------------------------");
        System.out.println("");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() throws ObjetoNaoEncontradoException, ServiceException {
        this.ostraQualityAtributesService = ObjectFactory.getObjectWithDataBaseDependencies(OstraQualityAtributesService.class);

        this.projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        this.oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);

        initializeProjectList();

        this.userDanielOceano = oceanoUserDao.getByLogin("xan");
//
//        System.out.println("OceanoUser: " + userDanielOceano.getName());
//
        associateProjectsToOceanoUserWhenInMemoryDatabase();

        MetricManagerFactory.getInstance();
    }

    private void associateProjectsToOceanoUserWhenInMemoryDatabase() throws ServiceException {
        if (!JPAUtil.isRunningOnMemoryDB()) {
            return;
        }
        for (SoftwareProject project : projects) {

            //inserindo um novo usuario de projeto
            ProjectUser pu = new ProjectUser();
            pu.setOceanoUser(userDanielOceano);
            pu.setLogin("login");
            pu.setPassword("pass");
            pu.setProject(project);
            ((ProjectUserService) ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class)).save(pu);
        }
    }
}
