package br.uff.ic.oceano.experiments.ostra;

import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.discretizer.DayOfWeekDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import br.uff.ic.oceano.ostra.discretizer.DiscretizerFactory;
import br.uff.ic.oceano.ostra.discretizer.HourOfDayDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NegativePositiveDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NumberOfFilesDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.RoundOfDayDiscretizer;
import br.uff.ic.oceano.ostra.service.DeltaMetricsRevisionDataBaseService;
import br.uff.ic.oceano.ostra.service.OstraMetricService;
import br.uff.ic.oceano.ostra.service.OstraQualityAtributesWithout_HardCoded_Service;
import br.uff.ic.oceano.util.CargaDefaultWeb;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * From Dancastellani TestOstra class.
 *
 * @author Dheraclio
 */
public class ExtractMetricsFromNeoPZ extends AbstractNGTest {

    private OstraQualityAtributesWithout_HardCoded_Service ostraQualityAtributesService;
    private DeltaMetricsRevisionDataBaseService deltaMetricsRevisionDataBaseService;
    private List<SoftwareProject> projects = new ArrayList<SoftwareProject>();
    private List<MetricManager> metricManagers = new ArrayList<MetricManager>();
    private OceanoUser userDanielOceano;
    private OstraMetricService ostraMetricService;

    @BeforeClass
    public void setUpClass() throws Exception {
        Constantes.SHOW_OUTPUT_CLI = false;
        Constantes.SHOW_OUTPUT_COMPILATION = false;
        ApplicationConstants.DIR_BASE_CHECKOUTS = SystemUtil.getTempDirectory() + "\\revisions\\";

        if (ApplicationConstants.CLEAN_CHECKOUT_DIRECTORY) {
            File file = new File(ApplicationConstants.DIR_BASE_CHECKOUTS);
            FileUtils.deleteDirectory(file);
            System.out.println(">>>> Cleaning checkout directory... " + !file.exists());
        }

        this.ostraMetricService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricService.class);
        this.ostraMetricService.setUsingUpdateRevision(true);
        this.ostraMetricService.setRevisionThreshold(0);

        this.ostraQualityAtributesService = ObjectFactory.getObjectWithDataBaseDependencies(OstraQualityAtributesWithout_HardCoded_Service.class);
        this.deltaMetricsRevisionDataBaseService = ObjectFactory.getObjectWithDataBaseDependencies(DeltaMetricsRevisionDataBaseService.class);

        if (!CargaDefaultWeb.isDefaultDataInserted()) {
            CargaDefaultWeb.insertDefaultData();
        }

        MetricManagerFactory.getInstance();

        OceanoUserDao oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);
        this.userDanielOceano = oceanoUserDao.getByLogin("dheraclio");

        initializeProjectList();
        initializeMetricsList();
    }

    @Test
    public void executaExperimentos() throws Throwable {

        final boolean EXTRAI_METRICAS = true;
        if (EXTRAI_METRICAS) {
            extractMetrics();
        }

        final boolean CALCULA_METRIC_VALUES = true;
        if (CALCULA_METRIC_VALUES) {
            calculateRevisionMetricValues();
        }

        final boolean CALCULA_ATRIBUTOS_DE_QUALIDADE = true;
        if (CALCULA_ATRIBUTOS_DE_QUALIDADE) {
            extractQualityAttributes();
        }

        //deltas            
        List<Discretizer> discretizers = getDiscretizers();
        List<Metric> metrics = getMetrics(metricManagers);
        deltaMetricsRevisionDataBaseService.buildDeltaMetricsDataBase(projects, discretizers, true, false, metrics, true);
    }

    private List<Discretizer> getDiscretizers() throws ServiceException {
        List<Discretizer> discretizers = new ArrayList<Discretizer>();
        discretizers.add(DiscretizerFactory.getDiscretizer("#files", NumberOfFilesDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rday", DayOfWeekDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rhour", HourOfDayDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rRound", RoundOfDayDiscretizer.class));
        for (Metric metric : getMetrics(metricManagers)) {
            discretizers.add(DiscretizerFactory.getDiscretizer(Constantes.PREFIX_DELTA_METRIC_AVARAGE + metric.getName(), NegativePositiveDiscretizer.class));

            discretizers.add(DiscretizerFactory.getDiscretizer(Constantes.PREFIX_DELTA_METRIC_STANDARD_DEVIATON + metric.getName(), NegativePositiveDiscretizer.class));
        }
        return discretizers;
    }

    private void initializeProjectList() throws ServiceException {
        try {
            SoftwareProject neopzLocal = CppProjectsHelper.getDBNeoPZProject();
            assertNotNull(neopzLocal);
            this.projects.add(neopzLocal);
        } catch (ObjetoNaoEncontradoException ex) {
            fail(ex.getMessage());
        }
    }

    private void extractMetrics() throws Throwable {
        for (SoftwareProject project : projects) {
            try {
                System.out.println("======================================================");
                System.out.println("EXTRACTING METRICS FROM PROJECT " + project);
                ostraMetricService.extractAndSaveMetricsFromAllFilesInProjectRevisions(project, userDanielOceano, metricManagers);
                System.out.println("======================================================\n");
            } catch (Throwable t) {
                println(t.getMessage());
            }
        }

    }

    private void calculateRevisionMetricValues() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            try {
                System.out.println("======================================================");
                System.out.println("CALCULATING REVISION METRIC VALUES FROM PROJECT " + project);
                ostraMetricService.calculateRevisionMetricValuesFromVersionedItemMetricValues(project, getMetrics(metricManagers));
                System.out.println("======================================================\n");

            } catch (Throwable t) {
                println(t.getMessage());
            }
        }
    }

    private void extractQualityAttributes() throws Throwable {
//        measure projects
        for (SoftwareProject project : projects) {
            try {
                System.out.println("======================================================");
                System.out.println("CALCULATING QUALITY ATTRIBUTES FOR PROJECT " + project);
                ostraQualityAtributesService.calculateQualityAttributes(project);
                System.out.println("======================================================\n");
            } catch (Throwable t) {
                println(t.getMessage());
            }
        }
    }

    private void initializeMetricsList() throws ServiceException {
        MetricManagerFactory fact = MetricManagerFactory.getInstance();
        Collection<MetricManager> metricManagrs = fact.getMetricManagers();
        for (MetricManager mng : metricManagrs) {
            if (!mng.isLanguageSupported(Language.CPP)) {
                continue;
            }
            metricManagers.add(mng);
        }
        println("Metrics to consider = " + metricManagers);
    }

    private List<Metric> getMetrics(List<MetricManager> metricManagers) {
        List<Metric> metrics = new ArrayList(metricManagers.size());
        for (MetricManager metricManager : metricManagers) {
            metrics.add(metricManager.getMetric());
        }
        return metrics;
    }
}
