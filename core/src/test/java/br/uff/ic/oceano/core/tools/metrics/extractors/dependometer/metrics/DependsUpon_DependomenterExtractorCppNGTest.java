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
public class DependsUpon_DependomenterExtractorCppNGTest extends BaseMetricTest{

    public DependsUpon_DependomenterExtractorCppNGTest() {
        super(MetricEnumeration.DCC,Language.CPP,DependomenterExtractor.class);
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        
        addTestScenario(new TestScenario(testConstantsCpp.getNeopzRevision(), 835.0));
    }
}
