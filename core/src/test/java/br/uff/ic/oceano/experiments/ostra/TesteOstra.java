package br.uff.ic.oceano.experiments.ostra;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import br.uff.ic.oceano.ostra.service.DataMiningPatternService;
import br.uff.ic.oceano.ostra.service.DataMiningResultService;
import junit.framework.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel
 */
public class TesteOstra {

    static final StringBuilder originalResultString = new StringBuilder();
    static final StringBuilder desiredResultString = new StringBuilder();

    public TesteOstra() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

        originalResultString.append("=== Run information ===\n");
        originalResultString.append("\n");
        originalResultString.append("Scheme:       weka.associations.Apriori -N 10 -T 0 -C 0.9 -D 0.05 -U 1.0 -M 0.1 -S -1.0 -c -1\n");
        originalResultString.append("Relation:     Oceano 17/08/10 - 01:35:12\n");
        originalResultString.append("Instances:    25\n");
        originalResultString.append("Attributes:   10\n");
        originalResultString.append("              project-revision\n");
        originalResultString.append("              rdate\n");
        originalResultString.append("              rcommiter\n");
        originalResultString.append("              #files\n");
        originalResultString.append("              dAvg-Number Of Attributes\n");
        originalResultString.append("              dAvg-Method Lines Of Code\n");
        originalResultString.append("              dAvg-Number Of Static Methods\n");
        originalResultString.append("              dAvg-Cyclomatic Complexity\n");
        originalResultString.append("              dAvg-Number Of Static Attributes\n");
        originalResultString.append("              dAvg-Weighted Methods Per Class\n");
        originalResultString.append("=== Associator model (full training set) ===\n");
        originalResultString.append("\n");
        originalResultString.append("\n");
        originalResultString.append("Apriori\n");
        originalResultString.append("=======\n");
        originalResultString.append("\n");
        originalResultString.append("Minimum support: 0.55 (14 instances)\n");
        originalResultString.append("Minimum metric <confidence>: 0.9\n");
        originalResultString.append("Number of cycles performed: 9\n");
        originalResultString.append("\n");
        originalResultString.append("Generated sets of large itemsets:\n");
        originalResultString.append("\n");
        originalResultString.append("Size of set of large itemsets L(1): 6\n");
        originalResultString.append("\n");
        originalResultString.append("Size of set of large itemsets L(2): 6\n");
        originalResultString.append("\n");
        originalResultString.append("Size of set of large itemsets L(3): 2\n");
        originalResultString.append("\n");
        originalResultString.append("Best rules found:\n");
        originalResultString.append("\n");
        originalResultString.append(" 1. dAvg-Weighted Methods Per Class=+ 18 ==> dAvg-Method Lines Of Code=+ 18    conf:(1)\n");
        originalResultString.append(" 2. dAvg-Method Lines Of Code=+ 18 ==> dAvg-Weighted Methods Per Class=+ 18    conf:(1)\n");
        originalResultString.append(" 3. dAvg-Cyclomatic Complexity=+ 16 ==> dAvg-Method Lines Of Code=+ 16    conf:(1)\n");
        originalResultString.append(" 4. dAvg-Cyclomatic Complexity=+ 16 ==> dAvg-Weighted Methods Per Class=+ 16    conf:(1)\n");
        originalResultString.append(" 5. dAvg-Cyclomatic Complexity=+ dAvg-Weighted Methods Per Class=+ 16 ==> dAvg-Method Lines Of Code=+ 16    conf:(1)\n");
        originalResultString.append(" 6. dAvg-Method Lines Of Code=+ dAvg-Cyclomatic Complexity=+ 16 ==> dAvg-Weighted Methods Per Class=+ 16    conf:(1)\n");
        originalResultString.append(" 7. dAvg-Cyclomatic Complexity=+ 16 ==> dAvg-Method Lines Of Code=+ dAvg-Weighted Methods Per Class=+ 16    conf:(1)\n");
        originalResultString.append(" 8. #files=1- dAvg-Weighted Methods Per Class=+ 14 ==> dAvg-Method Lines Of Code=+ 14    conf:(1)\n");
        originalResultString.append(" 9. #files=1- dAvg-Method Lines Of Code=+ 14 ==> dAvg-Weighted Methods Per Class=+ 14    conf:(1)\n");
        originalResultString.append("10. dAvg-Number Of Static Methods=- 17 ==> #files=1- 16    conf:(0.94)\n");

        desiredResultString.append("=== Run information ===\n");
        desiredResultString.append("\n");
        desiredResultString.append("Scheme:       weka.associations.Apriori -N 10 -T 0 -C 0.9 -D 0.05 -U 1.0 -M 0.1 -S -1.0 -c -1\n");
        desiredResultString.append("Relation:     Oceano 17/08/10 - 01:35:12\n");
        desiredResultString.append("Instances:    25\n");
        desiredResultString.append("Attributes:   10\n");
        desiredResultString.append("              project-revision\n");
        desiredResultString.append("              rdate\n");
        desiredResultString.append("              rcommiter\n");
        desiredResultString.append("              #files\n");
        desiredResultString.append("              dAvg-Number Of Attributes\n");
        desiredResultString.append("              dAvg-Method Lines Of Code\n");
        desiredResultString.append("              dAvg-Number Of Static Methods\n");
        desiredResultString.append("              dAvg-Cyclomatic Complexity\n");
        desiredResultString.append("              dAvg-Number Of Static Attributes\n");
        desiredResultString.append("              dAvg-Weighted Methods Per Class\n");
        desiredResultString.append("=== Associator model (full training set) ===\n");
        desiredResultString.append("\n");
        desiredResultString.append("\n");
        desiredResultString.append("Apriori\n");
        desiredResultString.append("=======\n");
        desiredResultString.append("\n");
        desiredResultString.append("Minimum support: 0.55 (14 instances)\n");
        desiredResultString.append("Minimum metric <confidence>: 0.9\n");
        desiredResultString.append("Number of cycles performed: 9\n");
        desiredResultString.append("\n");
        desiredResultString.append("Generated sets of large itemsets:\n");
        desiredResultString.append("\n");
        desiredResultString.append("Size of set of large itemsets L(1): 6\n");
        desiredResultString.append("\n");
        desiredResultString.append("Size of set of large itemsets L(2): 6\n");
        desiredResultString.append("\n");
        desiredResultString.append("Size of set of large itemsets L(3): 2");
    }
//    @Test
    public void getDataMiningPatternFromString() {
        DataMiningPattern dmp = DataMiningPatternService.getDataminingPaternFromOutputStringLineRule("   7. dAvg-Cyclomatic Complexity=+ 16 ==> dAvg-Method Lines Of Code=+ 16    conf:(1)", null);
        Assert.assertEquals(01d, dmp.getConfidence());
        Assert.assertEquals(16d, dmp.getSupport());
        Assert.assertEquals("dAvg-Cyclomatic Complexity=+ 16 ==> dAvg-Method Lines Of Code=+ 16", dmp.getPattern());
    }

//    @Test
    public void cleanMiningResultsResultString() {
        DataMiningResult dmr = new DataMiningResult();
        dmr.setResultData(originalResultString.toString());
        DataMiningResultService.cleanDataMiningResult(dmr);
        Assert.assertEquals(dmr.getResultData(), desiredResultString.toString());
    }

    @Test
    public void getAllMetrics() {
        MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
        metricService.getAll();
    }

//    @Test
    public void loadMetrics() {
        MetricManagerFactory.getInstance();
    }
}
