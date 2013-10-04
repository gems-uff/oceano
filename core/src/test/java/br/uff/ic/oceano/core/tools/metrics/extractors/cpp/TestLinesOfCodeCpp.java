package br.uff.ic.oceano.core.tools.metrics.extractors.cpp;

import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.extractors.BaseMetricTest;
import br.uff.ic.oceano.core.tools.metrics.extractors.TestScenario;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Daniel
 */
public class TestLinesOfCodeCpp extends BaseMetricTest{

    public TestLinesOfCodeCpp() {
        super(MetricEnumeration.LOC.getName(),Language.CPP,LinesOfCodeExtractorCpp.class);
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();

        addTestScenario(new TestScenario(testConstantsCpp.getEasyCountRevision(), CppProjectsHelper.EASYCOUNT_C, 254.0));
        addTestScenario(new TestScenario(testConstantsCpp.getEasyCountRevision(), 254.0));
        addTestScenario(new TestScenario(testConstantsCpp.getNeopzRevision(), 88561.0));

    }
}
