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
import br.uff.ic.oceano.ostra.exception.DataMiningException;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import br.uff.ic.oceano.ostra.tools.datamining.MyApriori;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import junit.framework.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import weka.core.Instances;

public class TestMyApriori {

    private static String pathOfTestArff = "./src/test/resources/DataMining/Oceano_Commons Utils_23-11-10_-_12-37-44_p1_a24.arff";
    private static Instances instances;
    private MyApriori myApriori;
    private DataMiningControl dataMiningControl;

    @BeforeTest
    public static void setupTest() throws IOException {
        instances = new Instances(new FileReader(pathOfTestArff));
    }

    @BeforeMethod
    public void setupMethod() throws IOException {
        myApriori = new MyApriori();
        dataMiningControl = new DataMiningControl();
    }

    @Test
    public void mineUsingConffidenceWithMyApriori() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.CONFIDENCE);

        myApriori = new MyApriori(dataMiningControl);
        myApriori.buildAssociations(instances);

        Assert.assertEquals(5000, myApriori.getAssociationRules().size());

    }

    @Test
    public void mineUsingConvictionWithMyApriori() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.CONVICTION);

        myApriori = new MyApriori(dataMiningControl);
        myApriori.buildAssociations(instances);

        Assert.assertEquals(5000, myApriori.getAssociationRules().size());
    }

    @Test
    public void mineUsingLeverageWithMyApriori() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.LEVERAGE);
        dataMiningControl.setMinMetric(0.1D);
        dataMiningControl.setMaxRules(20);

        myApriori = new MyApriori(dataMiningControl);
        myApriori.buildAssociations(instances);

        Assert.assertEquals(20, myApriori.getAssociationRules().size());
    }

    @Test
    public void mineUsingLiftWithMyApriori() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.LIFT);

        myApriori = new MyApriori(dataMiningControl);
        myApriori.buildAssociations(instances);

        Assert.assertEquals(5000, myApriori.getAssociationRules().size());
    }

    @Test
    public void mineUsingDefaultConfigurationWithMyApriori() throws Exception {
        myApriori.buildAssociations(instances);

        Assert.assertEquals(10, myApriori.getAssociationRules().size());
    }

    @Test(expectedExceptions = DataMiningException.class)
    public void getRulesFromMyAprioriButThrowsDMExceptionCauseRulesNotMined() throws DataMiningException {
        try {
            myApriori.getAssociationRules();
        } catch (DataMiningException ex) {
            Assert.assertEquals(MyApriori.ERROR_NO_RULES_FOUND, ex.getMessage());
            throw ex;
        }
    }

    //@Test
    public void getRulesFromMyAprioriUsingDefaultDataMiningControl() throws Exception {
        myApriori.buildAssociations(instances);
        List<DataMiningPattern> associationRules = myApriori.getAssociationRules();

        Assert.assertEquals(10, associationRules.size());

        for (DataMiningPattern dataMiningPattern : associationRules) {
            Assert.assertNotNull(dataMiningPattern.getPrecedent());
            Assert.assertNotNull(dataMiningPattern.getPrecedentSize());
            Assert.assertNotNull(dataMiningPattern.getConsequent());
            Assert.assertNotNull(dataMiningPattern.getConsequentSize());

            Assert.assertNotNull(dataMiningPattern.getSupport());
            Assert.assertNotNull(dataMiningPattern.getConfidence());

            Assert.assertNull(dataMiningPattern.getConviction());
            Assert.assertNull(dataMiningPattern.getLeverage());
            Assert.assertNull(dataMiningPattern.getLift());
        }
    }

    //@Test
    public void getRulesFromMyAprioriUsingLeverage() throws Exception {
        dataMiningControl.setMetricType(DataMiningControl.LEVERAGE);
        dataMiningControl.setMinMetric(0.1D);
        dataMiningControl.setMaxRules(20);

        myApriori = new MyApriori(dataMiningControl);
        myApriori.buildAssociations(instances);

        List<DataMiningPattern> associationRules = myApriori.getAssociationRules();

        Assert.assertEquals(20, associationRules.size());

        for (DataMiningPattern dataMiningPattern : associationRules) {
//            System.out.println("dataMiningPattern = " + dataMiningPattern);

            Assert.assertNotNull(dataMiningPattern.getPrecedent());
            Assert.assertNotNull(dataMiningPattern.getPrecedentSize());
            Assert.assertNotNull(dataMiningPattern.getConsequent());
            Assert.assertNotNull(dataMiningPattern.getConsequentSize());

            Assert.assertNotNull(dataMiningPattern.getSupport());
            Assert.assertNotNull(dataMiningPattern.getConfidence());
            Assert.assertNotNull(dataMiningPattern.getConviction());
            Assert.assertNotNull(dataMiningPattern.getLeverage());
            Assert.assertNotNull(dataMiningPattern.getLift());
        }
    }
}
