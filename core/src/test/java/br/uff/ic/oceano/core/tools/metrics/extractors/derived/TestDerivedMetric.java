/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.derived;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.tools.metrics.expression.MetricExpression;
import br.uff.ic.oceano.core.tools.metrics.service.DerivedMetricService;
import br.uff.ic.oceano.JavaProjectsHelper;
import junit.framework.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * refactored by dheraclio
 * @author wallace
 */
public class TestDerivedMetric {

    private DerivedMetricService derivedMetricService;
    private JavaProjectsHelper testConstants;
    private String pathOfTestClass;

    @BeforeClass
    public void setupTest() {
        System.out.println("DerivedMetric tests");
        //JPAUtil.startUp();
        derivedMetricService = new DerivedMetricService();
        testConstants = new JavaProjectsHelper();
        pathOfTestClass = testConstants.getRevisionTestMavenProject().getLocalPath() + "/target/classes/br/uff/ic/oceano/test/testmavenproject/Carro.class";
    }

    @AfterClass
    public void afterClass(){
        //JPAUtil.shutdown();
    }

    @Test
    public void verifyMetricsFactoryInstantiation() throws Throwable {
        MetricManagerFactory instance = MetricManagerFactory.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void verifyExpressionEstructure() throws Throwable {
        MetricExpression derivedMetric = null;
//        String expression = "-(sqrt(LCOM/NOM)^2)";
//        String expression = "(LCOM+5)^3*NOM";
//        String expression = "((68*3)+LCOM/2)-(NOM)";
        String expression = "52-sqrt((14)*97)";
        try {
            derivedMetric = derivedMetricService.buildExpression(expression);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("O resultado e :" + derivedMetric.getDoubleValue(testConstants.getRevisionTestMavenProject(), pathOfTestClass));
        Assert.assertNotNull(derivedMetric);
    }

    @Test
    public void verifySumOperatorOnePlusTwo() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("1+2");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(3d, doubleValue);
    }

    @Test
    public void verifySumOperatorOnePlusTwoPlusThree() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("1+2+3");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(6d, doubleValue);
    }

    @Test
    public void verifySumOperatorOnePlusZero() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("1+0");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(1d, doubleValue);
    }

    @Test
    public void verifyMinusOperatorWithMinusOne() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("-1");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(-1d, doubleValue);
    }

    @Test
    public void verifyMinusOperatorWithOneMinusOne() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("1-1");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(0d, doubleValue);
    }

    @Test
    public void verifyMinusOperatorWithTenMinusFive() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("10-5");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(5d, doubleValue);
    }

    @Test
    public void verifyMinusOperatorWithTenMinusTwenty() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("10-20");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(-10d, doubleValue);
    }

    @Test
    public void verifyMultiplicationOperatorWithTwoTimesThree() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("2*3");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(6d, doubleValue);
    }

    @Test
    public void verifyMultiplicationOperatorWithMinusTwoTimesThree() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("-2*3");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(-6d, doubleValue);
    }

    @Test
    public void verifyDivisionOperatorWithTwoDividedByOne() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("2/1");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(2d, doubleValue);
    }

    @Test
    public void verifyDivisionOperatorWithTwoDividedByTwo() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("2/2");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(1d, doubleValue);
    }

    @Test
    public void verifyDivisionOperatorWithSixDividedByTwo() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("6/2");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(3d, doubleValue);
    }

    @Test
    public void verifyDivisionOperatorWithFiveDividedByTwo() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("5/2");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(2.5, doubleValue);
    }

    @Test
    public void verifyDivisionOperatorWithMinusFiveDividedByTwo() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("-5/2");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(-2.5, doubleValue);
    }

    @Test
    public void verifyPowOperatorWithTwoPowOne() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("2^1");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(2d, doubleValue);
    }

    @Test
    public void verifyPowOperatorWithTwoPowThree() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("2^3");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(8d, doubleValue);
    }

    @Test
    public void verifyPowOperatorWithTwoPowTwo() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("2^2");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(4d, doubleValue);
    }

    @Test
    public void verifyPowOperatorWithFourPowMinusOne() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("4^-1");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(0.25, doubleValue);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void verifySquareRootOperatorWithFourWhithoutParenthesisThatFails() throws Throwable {
        derivedMetricService.buildExpression("sqrt4");
    }

    @Test
    public void verifySquareRootOperatorWithFour() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("sqrt(4)");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(2d, doubleValue);
    }

    @Test
    public void verifySquareRootOperatorWithNine() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("sqrt(9)");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(3d, doubleValue);
    }

    @Test
    public void verifySquareRootOperatorWithSixteen() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("sqrt(16)");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(4d, doubleValue);
    }

    @Test
    public void verifyComplexEquation1() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("1 + 2 * 3 + 4");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(11d, doubleValue);
    }

    @Test
    public void verifyComplexEquation2() throws Throwable {
        MetricExpression derivedMetric = derivedMetricService.buildExpression("-0.25*1 +0.25*1.55934343434 +0.5*26 +0.5*7");
        double doubleValue = derivedMetric.getDoubleValue(null);
        Assert.assertEquals(16.639835858585002d, doubleValue);
    }

}
