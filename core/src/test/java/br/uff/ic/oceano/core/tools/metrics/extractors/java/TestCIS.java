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
 * @author dheraclio
 */
public class TestCIS extends BaseMetricTest {

    public TestCIS() {
        super(MetricEnumeration.CIS.getName(), Language.JAVA,ClassInterfaceSizeExtractorJava.class);

    }

    @BeforeClass
    @Override
    public void beforeClass() {
        super.beforeClass();

        addTestScenario(new TestScenario(testConstantsJava.getRevisionTestMavenProject(), JavaProjectsHelper.CARRO_CLASS, 6.0));
        addTestScenario(new TestScenario(testConstantsJava.getRevisionTestMavenProject(), 37.0));
        addTestScenario(new TestScenario(testConstantsJava.getRevisionAnimalSniffer(), 140.0));
        
        //takes to long to test
        //addTestScenario(new TestScenario(testConstantsJava.getRevisionMaven3(), 4232.0));

    }
}
