/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.JavaProjectsHelper;
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
public class TestTCC extends BaseMetricTest {

    public TestTCC() {
        super(MetricEnumeration.TCC.getName(), Language.JAVA,TotalCyclomaticComplexityExtractorJava.class);
    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        addTestScenario(new TestScenario(testConstantsJava.getRevisionTestMavenProject(), JavaProjectsHelper.CARRO_SOURCE, 18.0));
    }
}
