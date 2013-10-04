/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.tools.datamining.test;

/**
 *
 * @author DanCastellani
 */
import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import java.io.IOException;
import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import weka.core.SelectedTag;

public class TestDataMiningControl {

    private DataMiningControl dataMiningControl;

    @BeforeTest
    public static void setupTest() throws IOException {
    }

    @BeforeMethod
    public void setupMethod() throws IOException {
        dataMiningControl = new DataMiningControl();
    }

    @Test
    public void configUsingConffidence() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.CONFIDENCE);

        SelectedTag selectedTag = dataMiningControl.getSelectedTag();
        final int tagId = selectedTag.getSelectedTag().getID();

        Assert.assertEquals(DataMiningControl.CONFIDENCE, tagId);
    }

    @Test
    public void configUsingConviction() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.CONVICTION);

        SelectedTag selectedTag = dataMiningControl.getSelectedTag();
        final int tagId = selectedTag.getSelectedTag().getID();

        Assert.assertEquals(DataMiningControl.CONVICTION, tagId);
    }

    @Test
    public void configUsingLeverage() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.LEVERAGE);

        SelectedTag selectedTag = dataMiningControl.getSelectedTag();
        final int tagId = selectedTag.getSelectedTag().getID();

        Assert.assertEquals(DataMiningControl.LEVERAGE, tagId);
    }

    @Test
    public void configUsingLift() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.LIFT);

        SelectedTag selectedTag = dataMiningControl.getSelectedTag();
        final int tagId = selectedTag.getSelectedTag().getID();

        Assert.assertEquals(DataMiningControl.LIFT, tagId);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void configUsingWrongNumberNegative() {
        try {
            dataMiningControl.setMetricType(-1);
        } catch (RuntimeException ex) {
            Assert.assertEquals("TagId not known. Metric's TagId type must be one of: 0, 1, 2, 3", ex.getMessage());
            throw ex;
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void configUsingWrongNumberMoreThanThree() {
        try {
            dataMiningControl.setMetricType(4);
        } catch (RuntimeException ex) {
            Assert.assertEquals("TagId not known. Metric's TagId type must be one of: 0, 1, 2, 3", ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void getReadableMetricTypeConfidence() {
        dataMiningControl.setMetricType(DataMiningControl.CONFIDENCE);
        String readableMetricType = dataMiningControl.getReadableMetricType();
        Assert.assertEquals("Confidence", readableMetricType);
    }
    
    @Test
    public void getReadableMetricTypeLift() {
        dataMiningControl.setMetricType(DataMiningControl.LIFT);
        String readableMetricType = dataMiningControl.getReadableMetricType();
        Assert.assertEquals("Lift", readableMetricType);
    }

    @Test
    public void getReadableMetricTypeLeverage() {
        dataMiningControl.setMetricType(DataMiningControl.LEVERAGE);
        String readableMetricType = dataMiningControl.getReadableMetricType();
        Assert.assertEquals("Leverage", readableMetricType);
    }

    @Test
    public void getReadableMetricTypeConviction() {
        dataMiningControl.setMetricType(DataMiningControl.CONVICTION);
        String readableMetricType = dataMiningControl.getReadableMetricType();
        Assert.assertEquals("Conviction", readableMetricType);
    }
}
