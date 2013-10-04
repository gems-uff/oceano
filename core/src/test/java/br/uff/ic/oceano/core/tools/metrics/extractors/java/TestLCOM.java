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
 * TODO check exception on finding Veiculo.class file
 */
public class TestLCOM extends BaseMetricTest {

    public TestLCOM() {
        super(MetricEnumeration.LCOM.getName(), Language.JAVA,LackOfCohesionOfMethodsExtractorJava.class);

    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();
        addTestScenario(new TestScenario(testConstantsJava.getRevisionTestMavenProject(), JavaProjectsHelper.CARRO_CLASS, 37.0));

    }
}
