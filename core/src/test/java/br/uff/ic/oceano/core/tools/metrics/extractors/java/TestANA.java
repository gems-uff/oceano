/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.extractors.BaseMetricTest;
import br.uff.ic.oceano.core.tools.metrics.extractors.TestScenario;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author wallace
 * @author dheraclio
 */
public class TestANA extends BaseMetricTest {

    public TestANA() {
        super(MetricEnumeration.ANA.getName(), Language.JAVA,AverageNumberOfAncestorsExtractorJava.class);
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        addTestScenario(new TestScenario(testConstantsJava.getRevisionTestMavenProject(), 1.25));
        
        //IncompatibleClassChangeError error
        //google points to problems in compiled code vs libraries used
        //addTestScenario(new TestScenario(testConstantsJava.getRevisionAnimalSniffer(), 1.361));
    }
}
