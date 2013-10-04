/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.util;

import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.JavaProjectsHelper;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 *
 */
public class MavenUtilNGTest extends AbstractNGTest{
    
    private static JavaProjectsHelper testConstants;
    private static Revision revisionFromMavenProjectMfa;

    @BeforeTest
    public static void setupTest() {
        testConstants = new JavaProjectsHelper();
        revisionFromMavenProjectMfa = new Revision();
        revisionFromMavenProjectMfa.setLocalPath("./src/test/resources/mavenprojectMFA");
    }

    @Test
    public void createSettingsXml() throws Throwable {
        File settingXml = new File(MavenUtil.SETTINGS_MAVEN_DEFAULT);
        if (settingXml.exists()) {
            return;
        }

        File createSettingsXml = MavenUtil.createSettingsXml();
        Assert.assertTrue(createSettingsXml.exists());

        //after create it, delete it to maintain the environment intact
        createSettingsXml.delete();
    }

    @Test
    public void testExecuteCleanCompile_TestMavenProject() {
        String path = PathUtil.getAbsolutePathFromRelativetoCurrentPath("./src/test/resources/TestMavenProject");
        testExecuteCleanCompile(path);
    }

    @Test
    public void testExecuteCleanCompile_AnimalSniffer() {
        String path = PathUtil.getAbsolutePathFromRelativetoCurrentPath("./src/test/resources/animal-sniffer");
        testExecuteCleanCompile(path);
    }

    @Test
    public void testExecuteCleanCompile_mavenprojectMFA() {
        String path = PathUtil.getAbsolutePathFromRelativetoCurrentPath("./src/test/resources/mavenprojectMFA");
        testExecuteCleanCompile(path);
    }

    private void testExecuteCleanCompile(String path) {
        //cleanup before
        File compiledPath = new File(path + "/target/");
        if (!compiledPath.exists()) {
            FileUtils.deleteDirectory(compiledPath);
        }

        //Compile
        try {
            List<Throwable> result = MavenUtil.execute(path, Arrays.asList("clean", "compile"), null);
            assertNull(result, String.valueOf(result));
        } catch (Exception ex) {
            ex.printStackTrace();
            assertNotNull(ex, ex.getMessage());
        }

        //cleanup after
        FileUtils.deleteDirectory(compiledPath);
    }

    @Test
    public void testSequentialExecuteCleanCompile_TestMavenProject() {
        String path = PathUtil.getAbsolutePathFromRelativetoCurrentPath("./src/test/resources/TestMavenProject");
        testSequentialExecuteCleanCompile(path);
    }

    @Test
    public void testSequentialExecuteCleanCompile_AnimalSniffer() {
        String path = PathUtil.getAbsolutePathFromRelativetoCurrentPath("./src/test/resources/animal-sniffer");
        testSequentialExecuteCleanCompile(path);
    }

    @Test
    public void testSequentialExecuteCleanCompile_mavenprojectMFA() {
        String path = PathUtil.getAbsolutePathFromRelativetoCurrentPath("./src/test/resources/mavenprojectMFA");
        testSequentialExecuteCleanCompile(path);
    }

    private void testSequentialExecuteCleanCompile(String path) {
        //cleanup before
        File compiledPath = new File(path + "/target/");
        if (!compiledPath.exists()) {
            FileUtils.deleteDirectory(compiledPath);
        }

        //Compile
        try {
            List<Throwable> result = MavenUtil.execute(path, Arrays.asList("clean", "compile"), null);
            assertNull(result, String.valueOf(result));

            result = MavenUtil.execute(path, Arrays.asList("clean", "compile"), null);
            assertNull(result, String.valueOf(result));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        //cleanup after
        FileUtils.deleteDirectory(compiledPath);
    }

    @Test
    public void getClassPathsUsingCommandLineFromAnimalSniffer() throws Throwable {
        Set<String> correctClassPaths = new HashSet<String>();
        // <editor-fold defaultstate="collapsed" desc="initialization of correct response">
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/asm/asm-all/3.3.1/asm-all-3.3.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/ow2/asm/asm-all/4.0/asm-all-4.0.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/classworlds/classworlds/1.1-alpha-2/classworlds-1.1-alpha-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/classworlds/classworlds/1.1/classworlds-1.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/com/jcraft/jsch/0.1.23/jsch-0.1.23.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/com/jcraft/jsch/0.1.27/jsch-0.1.27.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/commons-cli/commons-cli/1.0/commons-cli-1.0.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/commons-httpclient/commons-httpclient/2.0.2/commons-httpclient-2.0.2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/commons-logging/commons-logging/1.0.4/commons-logging-1.0.4.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/de/zeigermann/xml/xml-im-exporter/1.1/xml-im-exporter-1.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/jdom/jdom/1.0/jdom-1.0.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/jtidy/jtidy/4aug2000r7-dev/jtidy-4aug2000r7-dev.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/junit/junit/3.8.1/junit-3.8.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/junit/junit/3.8.2/junit-3.8.2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/ant/ant-launcher/1.7.1/ant-launcher-1.7.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/ant/ant/1.7.1/ant-1.7.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/doxia/doxia-sink-api/1.0-alpha-10/doxia-sink-api-1.0-alpha-10.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/doxia/doxia-sink-api/1.0-alpha-6/doxia-sink-api-1.0-alpha-6.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/enforcer/enforcer-api/1.0/enforcer-api-1.0.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-artifact-manager/2.0.1/maven-artifact-manager-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-artifact-manager/2.0.9/maven-artifact-manager-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-artifact/2.0.1/maven-artifact-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-artifact/2.0.9/maven-artifact-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-core/2.0.1/maven-core-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-core/2.0.9/maven-core-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-error-diagnostics/2.0.1/maven-error-diagnostics-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-error-diagnostics/2.0.9/maven-error-diagnostics-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-model/2.0.1/maven-model-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-model/2.0.9/maven-model-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-monitor/2.0.1/maven-monitor-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-monitor/2.0.9/maven-monitor-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-api/2.0.1/maven-plugin-api-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-api/2.0.9/maven-plugin-api-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-descriptor/2.0.1/maven-plugin-descriptor-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-descriptor/2.0.9/maven-plugin-descriptor-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-parameter-documenter/2.0.1/maven-plugin-parameter-documenter-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-parameter-documenter/2.0.9/maven-plugin-parameter-documenter-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-registry/2.0.1/maven-plugin-registry-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-plugin-registry/2.0.9/maven-plugin-registry-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-profile/2.0.1/maven-profile-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-profile/2.0.9/maven-profile-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-project/2.0.1/maven-project-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-project/2.0.9/maven-project-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-repository-metadata/2.0.1/maven-repository-metadata-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-repository-metadata/2.0.9/maven-repository-metadata-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-settings/2.0.1/maven-settings-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-settings/2.0.9/maven-settings-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/maven-toolchain/2.2.1/maven-toolchain-2.2.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/reporting/maven-reporting-api/2.0.1/maven-reporting-api-2.0.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/reporting/maven-reporting-api/2.0.9/maven-reporting-api-2.0.9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/shared/maven-common-artifact-filters/1.2/maven-common-artifact-filters-1.2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/shared/maven-plugin-testing-harness/1.1/maven-plugin-testing-harness-1.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-file/1.0-alpha-5/wagon-file-1.0-alpha-5.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-file/1.0-beta-2/wagon-file-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-http-lightweight/1.0-alpha-5/wagon-http-lightweight-1.0-alpha-5.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-http-lightweight/1.0-beta-2/wagon-http-lightweight-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-http-shared/1.0-beta-2/wagon-http-shared-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-provider-api/1.0-alpha-5/wagon-provider-api-1.0-alpha-5.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-provider-api/1.0-beta-2/wagon-provider-api-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-ssh-common/1.0-beta-2/wagon-ssh-common-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-ssh-external/1.0-beta-2/wagon-ssh-external-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-ssh/1.0-alpha-5/wagon-ssh-1.0-alpha-5.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-ssh/1.0-beta-2/wagon-ssh-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/apache/maven/wagon/wagon-webdav/1.0-beta-2/wagon-webdav-1.0-beta-2.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/mojo/animal-sniffer/1.8-SNAPSHOT/animal-sniffer-1.8-SNAPSHOT.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/mojo/java-boot-classpath-detector/1.8-SNAPSHOT/java-boot-classpath-detector-1.8-SNAPSHOT.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/plexus/plexus-archiver/1.0-alpha-7/plexus-archiver-1.0-alpha-7.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/plexus/plexus-container-default/1.0-alpha-9/plexus-container-default-1.0-alpha-9.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/plexus/plexus-interactivity-api/1.0-alpha-4/plexus-interactivity-api-1.0-alpha-4.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/plexus/plexus-utils/1.5.1/plexus-utils-1.5.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/org/codehaus/plexus/plexus-utils/1.5.6/plexus-utils-1.5.6.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/slide/slide-webdavlib/2.1/slide-webdavlib-2.1.jar"));
        correctClassPaths.add(PathUtil.getWellFormedPath("repository/xml-apis/xml-apis/1.0.b2/xml-apis-1.0.b2.jar"));
        // </editor-fold>

        String paths[] = MavenUtil.getClassPathsByCommandLine(testConstants.getRevisionAnimalSniffer());
        for (int i = 0; i < paths.length; i++) {
            String resultPath = paths[i];
            resultPath = resultPath.substring(resultPath.indexOf("repository"));
            if (!correctClassPaths.contains(resultPath)) {
                System.out.println("Not found: " + resultPath);
            }
            assertTrue(correctClassPaths.contains(resultPath),resultPath + " not found.");
        }
    }
    
    /**
     * 
     */
    @Test
    public void testGetAllFoldersAndSubFoldersContains() {
        final JavaProjectsHelper testConstantsJava = new JavaProjectsHelper();

        Revision rev = testConstantsJava.getRevisionTestMavenProject();
        File fileOrFolder = new File(rev.getLocalPath());
        String contains = MavenUtil.MAVEN2_BASE_COMPILED_FILES;

        List<String> result = FileUtils.getAllFoldersAndSubFoldersContains(fileOrFolder, contains);
        assertTrue(!result.isEmpty());
        for (String string : result) {
            assertTrue(string.contains(contains), "Invalid folder");
        }
    }
}