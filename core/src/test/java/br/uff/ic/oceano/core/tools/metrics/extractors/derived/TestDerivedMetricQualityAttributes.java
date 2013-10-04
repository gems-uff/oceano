/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.derived;

import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.expression.MetricExpression;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import br.uff.ic.oceano.JavaProjectsHelper;
import br.uff.ic.oceano.core.tools.metrics.service.DerivedMetricService;
import br.uff.ic.oceano.core.util.DefaultDatabaseLoader;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author DanCastellani
 */
public class TestDerivedMetricQualityAttributes {

    private JavaProjectsHelper testConstants;
    private DerivedMetricService derivedMetricService;

    @BeforeClass
    public void beforeClass() {
        System.out.println("DerivedMetricQualityAttributes tests");
        //JPAUtil.startUp();
        testConstants = new JavaProjectsHelper();
        derivedMetricService = new DerivedMetricService();
        
        //estrang bug found once
        assertTrue(DefaultDatabaseLoader.isDefaultDataInserted(),"Dabase not loaded");
    }

    @AfterClass
    public void afterClass() {
        //JPAUtil.shutdown();
    }

    @Test
    public void verifyExpressionOfQualityAttributeEffectiveness() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression(QMOOD.QUALITY_ATTRIBUTE_EFFECTIVENESS);
        assertNotNull(derivedMetric);
    }

    @Test
    public void verifyExpressionOfQualityAttributeExtendability() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression(QMOOD.QUALITY_ATTRIBUTE_EXTENDABILITY);
        assertNotNull(derivedMetric);
    }

    @Test
    public void verifyExpressionOfQualityAttributeFlexibility() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression(QMOOD.QUALITY_ATTRIBUTE_FLEXIBILITY);
        assertNotNull(derivedMetric);
    }

    @Test
    public void verifyExpressionOfQualityAttributeFunctionality() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression(QMOOD.QUALITY_ATTRIBUTE_FUNCTIONALITY);
        assertNotNull(derivedMetric);
    }

    @Test
    public void verifyExpressionOfQualityAttributeReusability() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression(QMOOD.QUALITY_ATTRIBUTE_REUSABILITY);
        assertNotNull(derivedMetric);
    }

    @Test
    public void verifyExpressionOfQualityAttributeUnderstandability() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression(QMOOD.QUALITY_ATTRIBUTE_UNDERSTANDABILITY);
        assertNotNull(derivedMetric);
    }

    @Test
    public void verifyMetricFactoryCreationOfEffectiveness(){
        verifyMetricFactoryCreation(QMOOD.QA_EFFECTIVENESS);
    }

    @Test
    public void verifyMetricFactoryCreationOfExtendability(){
        verifyMetricFactoryCreation(QMOOD.QA_EXTENDABILITY);
    }

    @Test
    public void verifyMetricFactoryCreationOfFlexibility() {
        verifyMetricFactoryCreation(QMOOD.QA_FLEXIBILITY);
    }

    @Test
    public void verifyMetricFactoryCreationOfFunctionality() {
        verifyMetricFactoryCreation(QMOOD.QA_FUNCTIONALITY);
    }

    @Test
    public void verifyMetricFactoryCreationOfReusability() {
        verifyMetricFactoryCreation(QMOOD.QA_REUSABILITY);
    }

    @Test
    public void verifyMetricFactoryCreationOfUnderstandability() {
        verifyMetricFactoryCreation(QMOOD.QA_UNDERSTANDABILITY);
    }

    private void verifyMetricFactoryCreation(String expectedMetricName) {
        try {
            MetricManagerFactory fact = MetricManagerFactory.getInstance();
            assertNotNull(fact);
            MetricManager qaMetricManager = fact.getMetricByName(expectedMetricName);
            assertNotNull(qaMetricManager);
            assertNotNull(qaMetricManager.getMetric());
            assertEquals(expectedMetricName, qaMetricManager.getMetric().getName());
        } catch (Throwable tr) {
            tr.printStackTrace();
            fail(tr.getMessage());
        }
    }

    /**
     *
     * @throws Throwable
     */
    @Test
    public void verifyQualityAttributeReusability() throws Throwable {
        final String expectedMetricName = QMOOD.QA_REUSABILITY;
        MetricManager qaMetricManager = MetricManagerFactory.getInstance().getMetricByName(expectedMetricName);
        assertNotNull(qaMetricManager);
        MetricValue metricValue = qaMetricManager.extractMetric(testConstants.getRevisionTestMavenProject());
        System.out.println("doubleValue = " + metricValue.getDoubleValue());
    }
}
