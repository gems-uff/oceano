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
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.discretizer.DayOfWeekDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import br.uff.ic.oceano.ostra.discretizer.DiscretizerFactory;
import br.uff.ic.oceano.ostra.discretizer.NegativePositiveDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NumberOfFilesDiscretizer;
import br.uff.ic.oceano.ostra.service.DeltaMetricsRevisionDataBaseService;
import br.uff.ic.oceano.ostra.service.OstraMetricService;
import br.uff.ic.oceano.ostra.tools.datamining.util.DatabaseToArffService;
import br.uff.ic.oceano.util.file.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
public class TestDeltaMetricsDataBase {

    private SoftwareProjectService projectService;
    private DeltaMetricsRevisionDataBaseService dataBaseService;
    private List<SoftwareProject> projects = new ArrayList<SoftwareProject>();
    private List<MetricManager> metricManagers = new ArrayList<MetricManager>();
    private OceanoUser userDanielOceano;
    private OceanoUserDao oceanoUserDao;
    private OstraMetricService ostraMetricService;
    private boolean calculateStandardDeviation = false;
    private boolean extractOnlyQmoodMetrics = true;

    private void initializeProjectList() throws ServiceException {
        /*this.projects.add(projectService.getProjectToDetailById(1L));*/ //oceano - fora de execução
//        this.projects.add(projectService.getProjectToDetailById(2L)); //iduff
        this.projects.add(projectService.getProjectToDetailById(3L)); //publico-core
        this.projects.add(projectService.getProjectToDetailById(4L)); //commons-utils
        this.projects.add(projectService.getProjectToDetailById(5L)); //monitoria

//        this.projects.add(projectService.getProjectToDetailById(7L)); //academico-pos

        this.projects.add(projectService.getProjectToDetailById(8L)); //oceano-core
        this.projects.add(projectService.getProjectToDetailById(9L)); //oceano-web
//        this.projects.add(projectService.getProjectToDetailById(12L)); //iduff-svn-local
        this.projects.add(projectService.getProjectToDetailById(14L)); //iduff 2 - para o artigo seke
//        this.projects.add(projectService.getProjectToDetailById(15L)); //hudson-maven-plugin-branch
//        this.projects.add(projectService.getProjectToDetailById(17L)); //Eclipse IAM
//        this.projects.add(projectService.getProjectToDetailById(18L)); //Animal Sniffer
    }

//    @Test
    public void extracMetrics() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            ostraMetricService.extractAndSaveMetricsFromAllFilesInProjectRevisions(project, userDanielOceano, metricManagers);
        }

    }

    @Test
    public void calculateRevisionMetricValues() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            ostraMetricService.calculateRevisionMetricValuesFromVersionedItemMetricValues(project, getMetrics(metricManagers));
        }
    }

//    @Test
    public void buildDeltaMetricsDatabase() throws Throwable {

        System.out.println("********************** testBuildDeltaMetricsDatabase - INICIO");

        List<Discretizer> discretizers = new ArrayList<Discretizer>();
        discretizers.add(DiscretizerFactory.getDiscretizer("rdate", DayOfWeekDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("#files", NumberOfFilesDiscretizer.class));
        initializePositiveNegativeDiscretizersDiscretizers(discretizers);
//        initializeDoubleDecimalDiscretizers(discretizers);

        DataBaseSnapshot baseSnapshot = dataBaseService.buildDeltaMetricsDataBase(projects, discretizers, calculateStandardDeviation, true, getMetrics(MetricManagerFactory.getQmoodQualityAttributes()), true);

        showAttributes(baseSnapshot);
        showInstances(baseSnapshot);

        System.out.println("_________________________________________________");
        String arffContent = DatabaseToArffService.dataBaseToARFF(baseSnapshot, discretizers);
        System.out.println("********************** testBuildDeltaMetricsDatabase - FIM");

        saveArffFile(baseSnapshot, arffContent);
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
        this.ostraMetricService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricService.class);

        this.dataBaseService = ObjectFactory.getObjectWithDataBaseDependencies(DeltaMetricsRevisionDataBaseService.class);
        this.projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        this.oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);

        initializeProjectList();
        initializeMetricsList();

        this.userDanielOceano = oceanoUserDao.getByLogin("xan");
//
//        System.out.println("OceanoUser: " + userDanielOceano.getName());
//
        associateProjectsToOceanoUserWhenInMemoryDatabase();

        MetricManagerFactory.getInstance();
    }

    private void initializePositiveNegativeDiscretizersDiscretizers(List<Discretizer> discretizers) throws ServiceException {
        for (Metric metric : ostraMetricService.getAll()) {
            discretizers.add(DiscretizerFactory.getDiscretizer("dAvg-" + metric.getName(), NegativePositiveDiscretizer.class));
        }
    }

//    private void initializeDoubleDecimalDiscretizers(List<Discretizer> discretizers) throws ServiceException {
//        for (Metric metric : ostraMetricService.getAll()) {
//            discretizers.add(DiscretizerFactory.getDiscretizer("dAvg-" + metric.getName(), DoubleDecimalCaseDiscretizer.class));
//        }
//    }
    private void saveArffFile(DataBaseSnapshot baseSnapshot, String arffContent) throws IOException {
        final int projectsSize = projects.size();
        final int attributesSize = baseSnapshot.getAttributes().size();
        final String projectName = (projectsSize == 1 ? projects.get(0).getConfigurationItem().getName() + "_" : "");

        final String fileName = "./Oceano_" + projectName + DateUtil.format(Calendar.getInstance()).replace("/", "-").replace(" ", "_").replace(":", "-") + "_p" + projectsSize + "_a" + attributesSize + ".arff";
        System.out.println("fileName = " + fileName);
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(fileName)));
        bw.append(arffContent);
        bw.close();
    }

    private void showInstances(DataBaseSnapshot baseSnapshot) {
        System.out.println("");
        System.out.println("Instancias");
        System.out.println("_________________________________________________");
        for (String instance : baseSnapshot.getInstances()) {
            System.out.println(instance);
        }
    }

    private void showAttributes(DataBaseSnapshot baseSnapshot) {
        System.out.println("");
        System.out.println("");
        System.out.println("Attributes");
        System.out.println("_________________________________________________");
        for (String attribute : baseSnapshot.getAttributes()) {
            System.out.println("attribute: " + attribute);
        }
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

    private void initializeMetricsList() {
        if (extractOnlyQmoodMetrics) {
            metricManagers = MetricManagerFactory.getQmoodMetrics();
        } else {
            metricManagers.add((MetricManager) MetricManagerFactory.getInstance().getTools());
        }
    }

    private List<Metric> getMetrics(List<MetricManager> metricManagers) {
        List<Metric> metrics = new ArrayList(metricManagers.size());
        for (MetricManager metricManager : metricManagers) {
            metrics.add(metricManager.getMetric());
        }
        return metrics;
    }
}
