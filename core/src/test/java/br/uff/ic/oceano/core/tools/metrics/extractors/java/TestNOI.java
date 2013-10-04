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
 *
 * @author wallace
 * @author dheraclio
 */
public class TestNOI extends BaseMetricTest {

    public TestNOI() {
        super(MetricEnumeration.NOI.getName(), Language.JAVA,NumberOfInterfacesExtractorJava.class);

    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        addTestScenario(new TestScenario(testConstantsJava.getRevisionTestMavenProject(), JavaProjectsHelper.NEWPACKAGE1, 2.0));
    }
}
