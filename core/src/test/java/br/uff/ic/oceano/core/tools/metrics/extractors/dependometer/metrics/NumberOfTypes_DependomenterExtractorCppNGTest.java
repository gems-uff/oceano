/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.metrics;

import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.extractors.BaseMetricTest;
import br.uff.ic.oceano.core.tools.metrics.extractors.TestScenario;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependomenterExtractor;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Daniel
 */
public class NumberOfTypes_DependomenterExtractorCppNGTest extends BaseMetricTest{

    /**
     * Map to DSC metric
     */
    public NumberOfTypes_DependomenterExtractorCppNGTest() {
        super(MetricEnumeration.DSC.getName(),Language.CPP,DependomenterExtractor.class);
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();

        //addTestScenario(new TestScenario(testConstantsCpp.getEasyCountRevision(),0));
        addTestScenario(new TestScenario(testConstantsCpp.getNeopzRevision(), 559.0));
    }
}
