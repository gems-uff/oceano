/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.metrics;

import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.extractors.BaseMetricTest;
import br.uff.ic.oceano.core.tools.metrics.extractors.TestScenario;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.DependomenterExtractor;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import org.testng.annotations.BeforeClass;
import static org.testng.Assert.*;

/**
 *
 * @author Daniel
 */
public class CumulativeComponentDependency_DependomenterExtractorCppNGTest extends BaseMetricTest{

    public CumulativeComponentDependency_DependomenterExtractorCppNGTest() {
        super(MetricEnum.CumulativeComponentDependency,Language.CPP,DependomenterExtractor.class);
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        try {
            super.beforeClass();

            //addTestScenario(new TestScenario(testConstantsCpp.getEasyCountRevision(),0));
            addTestScenario(new TestScenario(testConstantsCpp.getNeopzRevision(), 831.0));
            
            addTestScenario(new TestScenario(testConstantsCpp.checkoutNeoPzRevision(4500), 1716.0));
        } catch (Exception ex) {
            fail(ex.getMessage(),ex);
        }
    }
}
