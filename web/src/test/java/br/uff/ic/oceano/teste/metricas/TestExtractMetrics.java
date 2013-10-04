package br.uff.ic.oceano.teste.metricas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.model.*;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_CommandLineInterface;
import br.uff.ic.oceano.core.tools.vcs.VCS;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.exception.CompilerException;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * 
 */
public class TestExtractMetrics {

    private static VCS svn;

    public TestExtractMetrics() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        svn = new SVN_By_CommandLineInterface();
        Constantes.SHOW_OUTPUT_CLI = true;
        Constantes.SHOW_OUTPUT_COMPILATION = true;
    }

    /**
     * //TODO Load metric from database
     * @throws VCSException
     * @throws MetricException
     * @throws CompilerException
     * @throws ServiceException
     */
    @Test
    public void testExtractSingleMetric() throws VCSException, MetricException, CompilerException, ServiceException {
        System.out.println("========================================================================== COMEÇOU O TESTE");

        //MetricManager metricManager = ObjectFactory.getObjectWithDataBaseDependencies(CyclomaticComplexity.class);
        MetricManager metricManager = MetricManagerFactory.getInstance().getMetricByName(MetricEnumeration.ACC.getName());

        metricManager.getMetric().setExtractsFromFont(false);
        metricManager.getMetric().setExtratcsFrom(Metric.EXTRACTS_FROM_FILE);
        //

        List<String> projectURls = new ArrayList<String>();
//        projectURls.add("https://svn.codehaus.org/mojo/trunk/mojo/gwt-maven-plugin/");
//        projectURls.add("https://svn.codehaus.org/mojo/trunk/mojo/nbm-maven-plugin/");
//        projectURls.add("https://svn.codehaus.org/mojo/trunk/mojo/versions-maven-plugin/");
//        projectURls.add("https://svn.codehaus.org/mojo/trunk/mojo/maven-native/");
//        projectURls.add("https://svn.codehaus.org/mojo/trunk/mojo/javacc-maven-plugin/");

//        projectURls.add("https://gems.ic.uff.br/svn/oceano/oceano-core/trunk");
//        projectURls.add("https://gems.ic.uff.br/svn/oceano/oceano-web/trunk");

//        projectURls.add("https://svn.codehaus.org/mojo/trunk/mojo/gwt-maven-plugin/");
//        projectURls.add("https://svn.apache.org/repos/asf/maven/plugins/trunk/maven-changes-plugin/");
        projectURls.add("https://svn.apache.org/repos/asf/maven/plugins/trunk/maven-javadoc-plugin/");
//        projectURls.add("https://svn.apache.org/repos/asf/maven/plugins/trunk/maven-pmd-plugin/");
//        projectURls.add("https://svn.apache.org/repos/asf/maven/plugins/trunk/maven-project-info-reports-plugin/");

        for (String url : projectURls) {

            String projectUrl = url;
            Long revisionNumber = 388169L;

//            ProjectUser pu = new ProjectUser("login", "senha");
            ProjectUser pu = new ProjectUser(true);


            long time = System.currentTimeMillis();
            Revision r = checkoutRevision(projectUrl, revisionNumber, pu);
            r.getProject().setMavenProject(true);

            time = System.currentTimeMillis() - time;
            System.out.println("time to checkout: " + time);

            MetricValue metricValue = metricManager.extractMetric(r);

            System.out.println("ProjectUrl " + projectUrl);
            System.out.println("Metric " + metricManager.getName() + " = " + metricValue.getDoubleValue());
            System.out.println("========================================================================== ACABOU O TESTE");
        }
    }

    private Revision checkoutRevision(String projectUrl, Long revisionNumber, ProjectUser pu) throws VCSException {
        ConfigurationItem ci = new ConfigurationItem();
        ci.setName("TestExtrackMetric-project" + System.currentTimeMillis());

        SoftwareProject sp = new SoftwareProject();
        sp.setRepositoryUrl(projectUrl);
        sp.setConfigurationItem(ci);
        sp.setMavenProject(true);

        Revision r = new Revision();
        r.setProject(sp);
        r.setNumber(revisionNumber);

        svn.doCheckout(r, pu, true);
        return r;
    }
}
