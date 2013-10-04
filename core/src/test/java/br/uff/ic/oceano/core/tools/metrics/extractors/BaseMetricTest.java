/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.JavaProjectsHelper;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricExtractorService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.util.DefaultDatabaseLoader;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.file.PathUtil;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;

/**
 * Defines basic tests to run over metrics. Capable of testing Java and Cpp
 * metrics tests. How to use Create a unit test class that extends this class.
 * Create constructor, setting metric name and supported languages Before any
 * test, add test scenarios and extra metric extractors
 *
 * @author dheraclio
 */
public abstract class BaseMetricTest extends AbstractNGTest {

    private String metricName;
    private Language supportedLanguage;
    private Class extractorClass;
    private List<TestScenario> testScenarios = new LinkedList<TestScenario>();
    private static final MetricService metricService = new MetricService();
    private static final MetricExtractorService ms = new MetricExtractorService();
    protected static final JavaProjectsHelper testConstantsJava = new JavaProjectsHelper();
    protected static final CppProjectsHelper testConstantsCpp = new CppProjectsHelper();

    protected BaseMetricTest(MetricEnum metricEnum, Language language, Class clazz) {
        this(metricEnum.name(), language, clazz);
    }

    protected BaseMetricTest(MetricEnumeration metricEnum, Language language, Class clazz) {
        this(metricEnum.getName(), language, clazz);
    }

    protected BaseMetricTest(String metricName, Language language, Class clazz) {
        this.metricName = metricName;
        this.supportedLanguage = language;
        this.extractorClass = clazz;
        metricService.setup();
        ms.setup();
    }

    @Override
    protected String getTestName() {
        return this.getClass().getCanonicalName() + "(" + metricName + ")";
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        JPAUtil.startUp();

        synchronized (this) {
            try {
                if (!DefaultDatabaseLoader.isDefaultDataInserted()) {
                    DefaultDatabaseLoader.insertDefaultData();
                }
            } catch (Exception ex) {
                fail(ex.getMessage(), ex);
            }
        }
    }

    @AfterClass
    public void afterClass() {
    }

    protected void addTestScenario(TestScenario test) {
        this.testScenarios.add(test);
    }

    protected IMetricExtractor getMetricExtractor() {
        try {
            Metric metric = getMetricManager().getMetric();
            AbstractMetricExtractor metricExtractorCreated = (AbstractMetricExtractor) extractorClass.newInstance();
            metricExtractorCreated.setMetric(metric);
            metricExtractorCreated.setLanguage(this.supportedLanguage);
            return metricExtractorCreated;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected MetricManager getMetricManager() {
        assertNotNull(metricName, "Metric name not set");
        return MetricManagerFactory.getInstance().getMetricByName(metricName);
    }

    protected MetricExtractor getMetricExtractor(Language language) {
        MetricManager metricManager = getMetricManager();
        assertNotNull(metricManager, "MetricManager not found for " + language + " to " + metricName);

        Metric metric = metricManager.getMetric();
        assertNotNull(metric, "Metric " + metricName + "not found");

        List<MetricExtractor> metricExtractors = ms.getMetricExtractorsByMetric(metric);
        for (MetricExtractor me : metricExtractors) {
            if (me.getLanguage().equals(language.name())) {
                return me;
            }
        }
        return null;
    }

    private Metric getMetric() throws ServiceException {
        return metricService.getMetric(metricName);
    }

    @Test
    public void metricTest() {
        try {
            String message = getTestName() + ": metric not found :" + metricName;
            assertNotNull(getMetric(), message);
        } catch (ServiceException ex) {

            fail(metricName + ": " + ex.getMessage(), ex);
        }
    }

    @Test
    public void metricManagerTest() {
        String message = getTestName() + ": MetricManager not found for " + metricName;
        assertNotNull(getMetricManager(), message);
    }

    /**
     * Test if metric extractor is available for metric listed languages
     *
     * @throws Throwable
     */
    @Test
    public void metricExtractorLanguagesSupportedTest() {
        String message = getTestName() + supportedLanguage.name() + ": extractor not found";
        assertNotNull(getMetricExtractor(supportedLanguage), message);
    }

    @Test
    public void extractTests() {
        try {
            String testClass = getTestName() + ": scenarios";
            println(getTestName() + ": scenarios");
            for (TestScenario testScenario : this.testScenarios) {

                println(testScenario.getName() + " scenario for " + metricName);

                Double fMM = extractFromMetricManager(testScenario);
                println("Extract with MetricManager: " + fMM);

                Double fMS = extractFromMeasurementService(testScenario);
                println("Extract with MeasurementService: " + fMS);

                Double fME = extractFromMetricExtractor(testScenario);
                println("Extract with MetricExtractor: " + fME);

                //has to return the same value allways
                assertTrue(NumberUtil.isEquivalent(fMM, fMS), testClass);
                assertTrue(NumberUtil.isEquivalent(fMM, fME), testClass);
                assertTrue(NumberUtil.isEquivalent(fME, fMS), testClass);

            }
        } catch (Exception ex) {
            fail(ex.getMessage(), ex);
        }
    }

    /**
     *
     * @param testScenario
     * @return
     * @throws Exception
     */
    private Double extractFromMetricManager(TestScenario testScenario) throws Exception {
        String testClass = getTestName() + ": ";
        MetricManager metricManager = getMetricManager();
        if (metricManager == null) {
            assertTrue(DefaultDatabaseLoader.isDefaultDataInserted(), "default db not loaded");
        }
        assertNotNull(metricManager, testClass + "MetricManager not found");

        Revision revision = testScenario.getRevision();
        assertNotNull(revision, testClass + "Null revision");

        MetricValue returnValue;
        if (testScenario.getPath() == null) {
            returnValue = metricManager.extractMetric(revision);
        } else {
            String path = testScenario.getPath();
            path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(revision.getLocalPath() + path);
            assertNotNull(testClass + "Path not set", path);

            returnValue = metricManager.extractMetric(revision, path);
        }

        if (returnValue == null) {
            assertNull(testScenario.getResult(), testClass + " MetricManager result failed for revision: " + revision.getLocalPath());
            return null;
        }

        assertEquals(returnValue.getDoubleValue(), testScenario.getResult(), testClass + " MetricManager result failed for revision: " + revision.getLocalPath());

        return returnValue.getDoubleValue();
    }

    private Double extractFromMeasurementService(TestScenario testScenario) throws Exception {
        String testClass = getClass().getCanonicalName() + "(" + metricName + "): ";

        MetricManager mm = getMetricManager();
        assertNotNull(mm, testClass + "MetricManager not found");

        Revision revision = testScenario.getRevision();
        assertNotNull(revision, testClass + "Revision not found");

        MetricValue returnValue;
        if (testScenario.getPath() == null) {
            returnValue = MeasurementService.extractMetric(mm, revision);
        } else {
            String path = PathUtil.getWellFormedPath(revision.getLocalPath() + testScenario.getPath());
            returnValue = MeasurementService.extractMetric(mm, revision, path);
        }

        if (returnValue == null) {
            assertNull(testScenario.getResult(), testClass + " MeasurementService result failed for revision: " + revision.getLocalPath());
            return null;
        }

        assertEquals(returnValue.getDoubleValue(), testScenario.getResult(), testClass + "MeasurementService result failed for revision: " + revision.getLocalPath());

        return returnValue.getDoubleValue();
    }

    private Double extractFromMetricExtractor(TestScenario testScenario) throws Exception {
        String testClass = getClass().getCanonicalName() + "(" + metricName + "): ";

        IMetricExtractor ame = getMetricExtractor();
        assertNotNull(ame, testClass + "Null metricExtractor");

        //Set metric manager for direct metrics
        Assert.assertTrue(ame instanceof AbstractMetricExtractor, "Not instance of AbstractMetricExtractor");
        ((AbstractMetricExtractor) ame).setMetricManager(getMetricManager());

        Revision revision = testScenario.getRevision();
        assertNotNull(ame, testClass + "Null revision");

        MetricValue returnValue;
        if (testScenario.getPath() == null) {
            returnValue = ame.extractMetric(revision);
        } else {
            String path = testScenario.getPath();
            path = revision.getLocalPath() + path;
            assertNotNull(testClass + "Path not set", path);
            returnValue = ame.extractMetric(revision, path);
        }
        if (returnValue == null) {
            assertNull(testScenario.getResult(), testClass + " MetricExtractor result failed for revision: " + revision.getLocalPath());
            return null;
        }

        assertEquals(returnValue.getDoubleValue(), testScenario.getResult(), testClass + "MetricExtractor result failed for revision: " + revision.getLocalPath());

        return returnValue.getDoubleValue();
    }
}
