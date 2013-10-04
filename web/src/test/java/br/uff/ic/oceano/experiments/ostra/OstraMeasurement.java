package br.uff.ic.oceano.experiments.ostra;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.RevisionService;
import br.uff.ic.oceano.core.tools.Tool;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.discretizer.DayOfWeekDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import br.uff.ic.oceano.ostra.discretizer.DiscretizerFactory;
import br.uff.ic.oceano.ostra.discretizer.NegativePositiveDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NumberOfFilesDiscretizer;
import br.uff.ic.oceano.ostra.service.OstraMetricService;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.discretizer.HourOfDayDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.RoundOfDayDiscretizer;
import br.uff.ic.oceano.ostra.service.OstraQualityAtributesWithout_HardCoded_Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Dancastellani
 */
public class OstraMeasurement {

    private RevisionService revisionService = ObjectFactory.getObjectWithDataBaseDependencies(RevisionService.class);

    private OstraQualityAtributesWithout_HardCoded_Service ostraQualityAtributesService;
    private SoftwareProjectService projectService;
    private List<SoftwareProject> projects = new ArrayList<SoftwareProject>();
    private List<MetricManager> metricManagers = new ArrayList<MetricManager>();
    private OceanoUser userDanielOceano;
    private OceanoUserDao oceanoUserDao;
    private OstraMetricService ostraMetricService;
    //--------------- CONTROLE EXPERIMENTOS
    private static final boolean extractOnlyQmoodMetrics = false;
    //
    public static final boolean EXTRAI_METRICAS = false;
    public static final boolean CALCULA_METRIC_VALUES = false;
    public static final boolean CALCULA_ATRIBUTOS_DE_QUALIDADE = true;
    public static final boolean CALCULA_DELTA = true;
    public static final boolean PROJETO_APENAS = true;
    public static final boolean SALVA_ARQUIVO_ARFF = false;
    //---------------
    /**
     * Em milisegundos
     */
    public static final long WAIT_UNTIL_NEXT_MEASUREMENT = 60 * 1000;
    public static final boolean CONTINUAR_SE_DER_ERRO_COM_ALGUM_PROJETO = false;
    //

    static {
        Constantes.SHOW_OUTPUT_CLI = false;
        Constantes.SHOW_OUTPUT_COMPILATION = true;
        ApplicationConstants.DIR_BASE_CHECKOUTS = "F:/" + ApplicationConstants.DIR_BASE_CHECKOUTS;
    }

    @BeforeMethod
    public void setUp() throws ObjetoNaoEncontradoException, ServiceException {
        this.ostraMetricService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricService.class);
        this.ostraQualityAtributesService = ObjectFactory.getObjectWithDataBaseDependencies(OstraQualityAtributesWithout_HardCoded_Service.class);
        
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

//    @Test
    public void verifyMeasurementOfReusabilityWithMavenJavadocPluginRevision388171() throws Throwable {
        final String expectedMetricName = QMOOD.QA_REUSABILITY;
        MetricManager qaMetricManager = (MetricManager) MetricManagerFactory.getInstance().getMetricByName(expectedMetricName);
        Revision revision = revisionService.getById(9585l);
        double doubleValue = qaMetricManager.extractMetric(revision).getDoubleValue();
        Assert.assertEquals(doubleValue, 16.639835858585858d);
    }

    @Test
    public void executaExperimentos() throws Throwable {

        do {
            if (EXTRAI_METRICAS) {
                extracMetrics();
            }
            if (CALCULA_METRIC_VALUES) {
                calculateRevisionMetricValues();
            }
            if (CALCULA_ATRIBUTOS_DE_QUALIDADE) {
                extractQualityAttributes();
            }
            if (CALCULA_DELTA || SALVA_ARQUIVO_ARFF) {
                buildDeltaMetricsDatabase();
            }
            Thread.sleep(WAIT_UNTIL_NEXT_MEASUREMENT);
        } while (false);
    }

    private void initializeProjectList() throws ServiceException {
// <editor-fold defaultstate="collapsed" desc="projetos analisados nos experimentos">
// --------------- DISSERTACAO EXPERIMENTO
//        this.projects.add(projectService.getProjectToDetailById(211L)); //iduff 2 - REMEDICAO - dissertacao
        this.projects.add(projectService.getProjectToDetailById(212L)); //Publico Core - REMEDICAO - dissertacao
        this.projects.add(projectService.getProjectToDetailById(213L)); //Maven GWT Plugin - REMEDICAO - dissertacao
        this.projects.add(projectService.getProjectToDetailById(214L)); //Maven Javadoc Plugin - REMEDICAO - dissertacao ?????????????????

// --------------- STI
//        this.projects.add(projectService.getProjectToDetailById(31L)); //commons-mustang
//        this.projects.add(projectService.getProjectToDetailById(32L)); //commons-mustang-hibernate
//        this.projects.add(projectService.getProjectToDetailById(33L)); //commons-mustang-jpa
//        this.projects.add(projectService.getProjectToDetailById(4L)); //commons-utils
//        this.projects.add(projectService.getProjectToDetailById(3L)); //publico-core
//        this.projects.add(projectService.getProjectToDetailById(5L)); //monitoria-core
//        this.projects.add(projectService.getProjectToDetailById(30L)); //Monitoria-web
//        this.projects.add(projectService.getProjectToDetailById(7L)); //academico-pos
//        this.projects.add(projectService.getProjectToDetailById(14L)); //iduff 2 - para o artigo seke
// --------------- Mestrado
//        this.projects.add(projectService.getProjectToDetailById(8L)); //oceano-core
//        this.projects.add(projectService.getProjectToDetailById(9L)); //oceano-web
// --------------- Codehaus Mojo
//        this.projects.add(projectService.getProjectToDetailById(15L)); //hudson-maven-plugin-branch
//        this.projects.add(projectService.getProjectToDetailById(17L)); //Eclipse IAM
//        this.projects.add(projectService.getProjectToDetailById(18L)); //Animal Sniffer
//        this.projects.add(projectService.getProjectToDetailById(24L)); //Antlr3 Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(25L)); //Antlr Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(26L)); //App Assembler
//        this.projects.add(projectService.getProjectToDetailById(27L)); //Apt Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(28L)); //AspectJ Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(29L)); //Axis Tools
//        this.projects.add(projectService.getProjectToDetailById(34L)); //Batik maven plugin
//        this.projects.add(projectService.getProjectToDetailById(35L)); //Build Helper Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(36L)); //Build Number Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(37L)); //Cassandra Maven plugin
//        this.projects.add(projectService.getProjectToDetailById(38L)); //Castor Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(39L)); //Ci Aggregator
//        this.projects.add(projectService.getProjectToDetailById(40L)); //Clirr Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(41L)); //Cobertura Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(42L)); //Commons Attributes Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(43L)); //Dashboard Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(44L)); //DBUnit Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(45L)); //DBUpgrade Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(46L)); //Dita Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(47L)); //Dock Book Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(48L)); //Emma Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(49L)); //Exec Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(50L)); //Extra Enforcer maven plugin
//        this.projects.add(projectService.getProjectToDetailById(51L)); //Find Bugs Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(52L)); //Fitnesse Maven plugin
//        this.projects.add(projectService.getProjectToDetailById(53L)); //GWT Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(54L)); //Hibernate 2 Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(55L)); //Hibernate 3 Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(56L)); //Ianal Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(57L)); //Idea UI Designer Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(58L)); //Idlj Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(59L)); //Jalopy Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(60L)); //Jasper Reports Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(61L)); //Javacc Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(62L)); //Javancss Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(63L)); //Jaxb2 Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(64L)); //JBoss Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(65L)); //JBoss Packaging Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(66L)); //JDepend Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(67L)); //JDiff maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(68L)); //JPox Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(69L)); //JRuby Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(70L)); //JRuby Std Lib
//        this.projects.add(projectService.getProjectToDetailById(71L)); //JSLint Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(72L)); //JSPC
//        this.projects.add(projectService.getProjectToDetailById(73L)); //KeyTool maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(74L)); //L10n Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(75L)); //Latex Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(76L)); //License Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(77L)); //Maven Extensions
//        this.projects.add(projectService.getProjectToDetailById(78L)); //Maven Native
//        this.projects.add(projectService.getProjectToDetailById(79L)); //Mojo Archetypes
//        this.projects.add(projectService.getProjectToDetailById(80L)); //Mojo Parent
//        this.projects.add(projectService.getProjectToDetailById(81L)); //Native 2 ASCII Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(82L)); //Nbm Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(83L)); //Open JPA Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(84L)); //OSX App Bunbler Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(85L)); //Ounce Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(86L)); //Pde Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(87L)); //Plugin Support
//        this.projects.add(projectService.getProjectToDetailById(88L)); //Properties Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(89L)); //Retrotranslator Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(90L)); //RMic Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(91L)); //RPM Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(92L)); //Sablecc Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(93L)); //SCM Changelog Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(94L)); //Selenium Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(95L)); //Ship Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(96L)); //Shitty Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(97L)); //SMC Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(98L)); //Solaris Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(99L)); //Sonar Maven plugin
//        this.projects.add(projectService.getProjectToDetailById(100L)); //Spring WS Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(101L)); //SQL Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(102L)); //SQLJ Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(103L)); //Sysdeo Tomcat Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(104L)); //Taglist Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(105L)); //Truezip Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(106L)); //Unix
//        this.projects.add(projectService.getProjectToDetailById(107L)); //Versions Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(108L)); //Virtualization Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(109L)); //Wagon Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(110L)); //Was6 Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(111L)); //Weblogic Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(112L)); //Webstart
//        this.projects.add(projectService.getProjectToDetailById(113L)); //Webtest Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(114L)); //Xdoclet Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(115L)); //Xml Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(116L)); //Xmlbeans Maven Plugin
        //-----------  ASF Maven Plugin
//        this.projects.add(projectService.getProjectToDetailById(117L)); //Checkstyle MP
//        this.projects.add(projectService.getProjectToDetailById(118L)); //AntRun MP
//        this.projects.add(projectService.getProjectToDetailById(119L)); //Ant MP
//        this.projects.add(projectService.getProjectToDetailById(120L)); //Acr MP
//        this.projects.add(projectService.getProjectToDetailById(121L)); //Changes MP
//        this.projects.add(projectService.getProjectToDetailById(122L)); //Changelog MP
//        this.projects.add(projectService.getProjectToDetailById(123L)); //Assembly MP
//        this.projects.add(projectService.getProjectToDetailById(124L)); //Dependency MP
//        this.projects.add(projectService.getProjectToDetailById(125L)); //Compiler MP
//        this.projects.add(projectService.getProjectToDetailById(126L)); //Clean MP
//        this.projects.add(projectService.getProjectToDetailById(127L)); //Eclipse MP
//        this.projects.add(projectService.getProjectToDetailById(128L)); //Ear MP
//        this.projects.add(projectService.getProjectToDetailById(129L)); //Docck MP
//        this.projects.add(projectService.getProjectToDetailById(130L)); //Doap MP
//        this.projects.add(projectService.getProjectToDetailById(131L)); //Deploy MP
//        this.projects.add(projectService.getProjectToDetailById(132L)); //Ejb MP
//        this.projects.add(projectService.getProjectToDetailById(133L)); //Jarsigner MP
//        this.projects.add(projectService.getProjectToDetailById(134L)); //Jar MP
//        this.projects.add(projectService.getProjectToDetailById(135L)); //Invoker MP
//        this.projects.add(projectService.getProjectToDetailById(136L)); //Install MP
//        this.projects.add(projectService.getProjectToDetailById(137L)); //Idea MP
//        this.projects.add(projectService.getProjectToDetailById(138L)); //Help MP
//        this.projects.add(projectService.getProjectToDetailById(139L)); //GPG MP
//        this.projects.add(projectService.getProjectToDetailById(140L)); //Javadoc MP
//        this.projects.add(projectService.getProjectToDetailById(141L)); //Linkcheck MP
//        this.projects.add(projectService.getProjectToDetailById(142L)); //One MP
//        this.projects.add(projectService.getProjectToDetailById(143L)); //Patch MP
//        this.projects.add(projectService.getProjectToDetailById(144L)); //PDF MP
//        this.projects.add(projectService.getProjectToDetailById(145L)); //Maven Plugins
//        this.projects.add(projectService.getProjectToDetailById(146L)); //PMD MP
//        this.projects.add(projectService.getProjectToDetailById(147L)); //Project Info Reports MP
//        this.projects.add(projectService.getProjectToDetailById(148L)); //Reactor MP
//        this.projects.add(projectService.getProjectToDetailById(149L)); //Remote Resources MP
//        this.projects.add(projectService.getProjectToDetailById(150L)); //Repository MP
//        this.projects.add(projectService.getProjectToDetailById(151L)); //Resources MP
//        this.projects.add(projectService.getProjectToDetailById(152L)); //Shade MP
//        this.projects.add(projectService.getProjectToDetailById(153L)); //Site MP
//        this.projects.add(projectService.getProjectToDetailById(154L)); //Source MP
//        this.projects.add(projectService.getProjectToDetailById(155L)); //Stage MP
//        this.projects.add(projectService.getProjectToDetailById(156L)); //Toolchains
//        this.projects.add(projectService.getProjectToDetailById(157L)); //Verifier MP
//        this.projects.add(projectService.getProjectToDetailById(158L)); //War MP
//        this.projects.add(projectService.getProjectToDetailById(159L)); //Jakarta String Taglib
//        this.projects.add(projectService.getProjectToDetailById(160L)); //Jakarta Cactus
//        this.projects.add(projectService.getProjectToDetailById(164L)); //Jakarta Regexp Taglib
//        this.projects.add(projectService.getProjectToDetailById(197L)); //Abdera
//        this.projects.add(projectService.getProjectToDetailById(201L)); //Avro
//        this.projects.add(projectService.getProjectToDetailById(203L)); //Axis 2 Java Rampart
//        this.projects.add(projectService.getProjectToDetailById(202L)); //Axis 2 Java Core
//        this.projects.add(projectService.getProjectToDetailById(204L)); //Axis 2 Java Sandesha
//        this.projects.add(projectService.getProjectToDetailById(207L)); //Chemistry Opencmis
        //-----------  Primeira medição
//        this.projects.add(projectService.getProjectToDetailById(209L)); //Cocoon 3
//        this.projects.add(projectService.getProjectToDetailById(206L)); //Cayenne Main
//        this.projects.add(projectService.getProjectToDetailById(205L)); //Camel
//        this.projects.add(projectService.getProjectToDetailById(208L)); //Cocoon +
        // Out 4 now
//        this.projects.add(projectService.getProjectToDetailById(210L)); //Commons BeanUtils -- broken, it stops...
//        this.projects.add(projectService.getProjectToDetailById(200L)); //Aries -- broken, it stops...
//        this.projects.add(projectService.getProjectToDetailById(199L)); //Archiva -- broken, it stops...
//        this.projects.add(projectService.getProjectToDetailById(198L)); //Active MQ   - Deve continuar//
//        </editor-fold>
    }

//    @Test
    public void extracMetrics() throws Throwable {
        for (SoftwareProject project : projects) {
            try {
                System.out.println("======================================================");
                System.out.println("EXTRACTING METRICS FROM PROJECT " + project);
                ostraMetricService.extractAndSaveMetricsFromAllFilesInProjectRevisions(project, userDanielOceano, metricManagers);
                System.out.println("======================================================\n");
            } catch (Throwable t) {
                if (!CONTINUAR_SE_DER_ERRO_COM_ALGUM_PROJETO) {
                    throw t;
                }
            }
        }

    }

//    @Test
    public void calculateRevisionMetricValues() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            try {
                System.out.println("======================================================");
                System.out.println("CALCULATING REVISION METRIC VALUES FROM PROJECT " + project);
                ostraMetricService.calculateRevisionMetricValuesFromVersionedItemMetricValues(project, getMetrics(metricManagers));
                System.out.println("======================================================\n");

            } catch (Throwable t) {
                if (!CONTINUAR_SE_DER_ERRO_COM_ALGUM_PROJETO) {
                    throw t;
                }
            }
        }
    }

//    @Test
    public void extractQualityAttributes() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            try {
                System.out.println("======================================================");
                System.out.println("CALCULATING QUALITY ATTRIBUTES FOR PROJECT " + project);
                ostraQualityAtributesService.calculateQualityAttributes(project);
                System.out.println("======================================================\n");
            } catch (Throwable t) {
                if (!CONTINUAR_SE_DER_ERRO_COM_ALGUM_PROJETO) {
                    throw t;
                }
            }
        }
    }

//    @Test
    public void buildDeltaMetricsDatabase() throws Throwable {
        System.out.println("======================================================");
        System.out.println("CALCULATING DELTA METRICS FROM PROJECTS " + projects);
        System.out.println("********************** testBuildDeltaMetricsDatabase - INICIO");

        List<Discretizer> discretizers = new ArrayList<Discretizer>();
        discretizers.add(DiscretizerFactory.getDiscretizer("#files", NumberOfFilesDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rday", DayOfWeekDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rhour", HourOfDayDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rRound", RoundOfDayDiscretizer.class));
        initializePositiveNegativeDiscretizersDiscretizers(discretizers);
//        initializeDoubleDecimalDiscretizers(discretizers);

        if (PROJETO_APENAS) {
            for (SoftwareProject project : projects) {
//                ostraMetricService.calculateAndSaveDeltaMetricsForMetricValues(project, getMetrics(metricManagers));
            }
        } else {
//            DataBaseSnapshot baseSnapshot = dataBaseService.buildDeltaMetricsDataBase(projects, discretizers, calculateStandardDeviation, usesOnlyCompilingRevisions, getMetrics(metricManagers), CALCULA_DELTA);
//
//            if (SALVA_ARQUIVO_ARFF) {
//                showAttributes(baseSnapshot);
//                showInstances(baseSnapshot);
//
//                System.out.println("_________________________________________________");
//                String arffContent = DatabaseToArffService.dataBaseToARFF(baseSnapshot, discretizers);
//                System.out.println("********************** testBuildDeltaMetricsDatabase - FIM");
//
//                saveArffFile(baseSnapshot, arffContent);
//            }
        }
        System.out.println("======================================================\n");
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("----------------------------------------");
        if (ApplicationConstants.CLEAN_CHECKOUT_DIRECTORY) {
            File file = new File(ApplicationConstants.DIR_BASE_CHECKOUTS);
            FileUtils.deleteDirectory(file);
            System.out.println(">>>> Cleaning checkout directory... " + !file.exists());
        }
        System.out.println("----------------------------------------");
        System.out.println("");
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
            for (Tool tool : MetricManagerFactory.getInstance().getTools()) {
                MetricManager mm = (MetricManager) tool;
                if (mm.getMetric().getName().equals("Number of Interfaces")) {
                    continue;
                }

                if (!mm.getMetric().isDerived()) {
                    metricManagers.add(mm);
                }
            }
        }
        System.out.println("Metrics to consider = " + metricManagers);
    }

    private List<Metric> getMetrics(List<MetricManager> metricManagers) {
        List<Metric> metrics = new ArrayList(metricManagers.size());
        for (MetricManager metricManager : metricManagers) {
            metrics.add(metricManager.getMetric());
        }
        return metrics;
    }
}
