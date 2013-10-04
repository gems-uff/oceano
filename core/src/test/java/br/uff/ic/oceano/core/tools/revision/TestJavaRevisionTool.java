/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.JavaProjectsHelper;
import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.util.file.PathUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author Daniel
 */
public class TestJavaRevisionTool {

    private static JavaProjectsHelper testConstants;
    //
    private final static String windowsPackage = "\\oceano\\windows\\";
    private final static String windowsDotJavaPath = "src\\main\\java" + windowsPackage;
    private final static String linuxPackage = "/oceano/linux/";
    private final static String linuxDotJavaPath = "src/main/java" + linuxPackage;
    //Where tests are running
    private final String testRunPath = "./target/test-classes/";

    @BeforeTest
    public static void setupTest() {
        System.out.println(TestJavaRevisionTool.class + " tests");

        testConstants = new JavaProjectsHelper();

        final Set<VersionedItem> fakeChangedFiles = new HashSet<VersionedItem>();
        testConstants.getRevisionTestMavenProject().setChangedFiles(fakeChangedFiles);

        VersionedItem fakeVersionedItem = new VersionedItem();
        fakeVersionedItem.setItem(new Item(windowsDotJavaPath + "fake1.java"));
        fakeVersionedItem.setRevision(testConstants.getRevisionTestMavenProject());
        fakeChangedFiles.add(fakeVersionedItem);

        fakeVersionedItem = new VersionedItem();
        fakeVersionedItem.setItem(new Item(linuxDotJavaPath + "fake2.java"));
        fakeVersionedItem.setRevision(testConstants.getRevisionTestMavenProject());
        fakeChangedFiles.add(fakeVersionedItem);

        fakeVersionedItem = new VersionedItem();
        fakeVersionedItem.setItem(new Item(linuxDotJavaPath + "fake3.java"));
        fakeVersionedItem.setRevision(testConstants.getRevisionTestMavenProject());
        fakeChangedFiles.add(fakeVersionedItem);
    }

    /**
     * Test of validate method, of class JavaRevisionTool.
     */
    @Test(expectedExceptions = Exception.class)
    public void testValidate() throws Exception {
        Revision revision = null;
        JavaRevisionTool instance = new JavaRevisionTool();
        instance.validate(revision);
    }

    /**
     * Test of getSourceFiles method, of class JavaRevisionTool.
     */
    public void testGetSourceFiles() throws Exception {
        Revision revision = testConstants.getRevisionTestMavenProject();
        JavaRevisionTool instance = new JavaRevisionTool();
        Collection result = instance.getSourceFiles(revision);
        assertEquals(3, result.size());
    }

    /**
     * Test of getSourceFilesFromChangedFiles method, of class JavaRevisionTool.
     */
    public void testGetSourceFilesFromChangedFiles() throws Exception {
        Revision revision = testConstants.getRevisionTestMavenProject();
        JavaRevisionTool instance = new JavaRevisionTool();
        Collection result = instance.getSourceFiles(revision);
        assertEquals(3, result.size());
    }

    @Test
    public void testGetPackages() throws Exception {
        Revision revision = testConstants.getRevisionTestMavenProject();
        JavaRevisionTool instance = new JavaRevisionTool();

        Set<VersionedItem> packages = instance.getPackagesFromChangedFiles(revision);
        assertEquals(2, packages.size());
    }

    @Test
    public void testGetSourceFoldersTestMavenProject() throws Throwable {
        Revision revision = testConstants.getRevisionTestMavenProject();
        
        JavaRevisionTool instance = new JavaRevisionTool();

        Collection<String> sourceClassPaths = instance.getSourceClassPaths(revision);
        assertEquals(10, sourceClassPaths.size());        
    }

    @Test
    public void testGetCompilationFoldersTestMavenProject() throws Throwable {
        Revision revision = testConstants.getRevisionTestMavenProject();
        CompilerService.compile(revision);
        JavaRevisionTool instance = new JavaRevisionTool();

        Collection<String> sourceClassPaths = instance.getCompilationFolders(revision);
        assertEquals(sourceClassPaths.size(), 1);

        String sourceFolder = sourceClassPaths.iterator().next();
        String testFolder = PathUtil.getAbsolutePathFromRelativetoCurrentPath(testRunPath + "TestMavenProject\\target\\classes");
        assertEquals(testFolder, sourceFolder);
    }

    @Test
    public void testGetCompiledClassPaths_RevisionMavenprojectMFA() throws Throwable {
        Revision revision = testConstants.getRevisionMavenprojectMFA();
        CompilerService.compile(revision);
        JavaRevisionTool instance = new JavaRevisionTool();

        Collection<String> result = instance.getCompilationFolders(revision);

        String contains = MavenUtil.MAVEN2_BASE_COMPILED_FILES;
        assertTrue(!result.isEmpty());
        for (String string : result) {
            assertTrue(string.contains(contains), "Invalid folder");
        }

    }

    @Test
    public void testGetCompiledClassNamesTestMavenProject() throws Throwable {
        Revision revision = testConstants.getRevisionTestMavenProject();
        JavaRevisionTool instance = new JavaRevisionTool();

        List<String> compiledClassNames = instance.getCompiledClassNames(revision);
        assertEquals(compiledClassNames.size(),12);

        List<String> correctCompiledClassPaths = new ArrayList<String>(12);
        // <editor-fold defaultstate="collapsed" desc="initiation of correctCompiledClassPaths">
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.App");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.Carro");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.Moto");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.Veiculo");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage.NewChartColor");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage.NewInterface");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage1.NewInterface2");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage1.NewInterface3");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage2.Alcool");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage2.Combustivel");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage2.Gasolina");
        correctCompiledClassPaths.add("br.uff.ic.oceano.test.testmavenproject.newpackage2.NewClass");// </editor-fold>

        Collections.sort(compiledClassNames);

        for (int i = 0; i < compiledClassNames.size(); i++) {
            assertEquals(correctCompiledClassPaths.get(i), compiledClassNames.get(i));
        }
    }

    @Test
    public void testGetSourceClassNamesTestMavenProject() throws Throwable {
        Revision revision = testConstants.getRevisionTestMavenProject();
        JavaRevisionTool instance = new JavaRevisionTool();

        Collection<String> tempList = instance.getSourceFiles(revision);
        assertEquals(tempList.size(),12);
        List<String> sourceJavaFiles = new LinkedList<String>(tempList);
        Collections.sort(sourceJavaFiles);

        List<String> correctSourceJavaFiles = new ArrayList<String>(12);
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\App.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\Carro.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\Moto.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\Veiculo.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage1\\NewInterface2.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage1\\NewInterface3.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage2\\Alcool.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage2\\Combustivel.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage2\\Gasolina.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage2\\NewClass.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage\\NewChartColor.java");
        correctSourceJavaFiles.add("TestMavenProject\\src\\main\\java\\br\\uff\\ic\\oceano\\test\\testmavenproject\\newpackage\\NewInterface.java");


        for (int i = 0; i < sourceJavaFiles.size(); i++) {
            String result = PathUtil.getAbsolutePathFromRelativetoCurrentPath(testRunPath + correctSourceJavaFiles.get(i));
            assertEquals(sourceJavaFiles.get(i), result);
        }
    }

    @Test
    public void testGetSourceFoldersAnimalSniffer() throws Throwable {
        Revision revision = testConstants.getRevisionAnimalSniffer();
        JavaRevisionTool instance = new JavaRevisionTool();

        List<String> sourceClasses = new ArrayList(instance.getSourceClassPaths(revision));
        assertEquals(sourceClasses.size(), 67);

        List<String> correctSourceClassPaths = new ArrayList<String>(67);
        // <editor-fold defaultstate="collapsed" desc="initialize correct list">
//        correctSourceClassPaths.add("animal-sniffer-annotations/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-ant-tasks/src/it/merge-test/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-ant-tasks/src/it/negative-test/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-ant-tasks/src/it/smoke-test/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-ant-tasks/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-enforcer-rule/src/it/setup-002/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-enforcer-rule/src/it/smoke-test/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-enforcer-rule/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/jdk14-with-sig14/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/jdk15-with-sig14/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/jdk15-with-sig15/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/manimalsniffer-6/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/manimalsniffer-9/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/merge-test/api/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/merge-test/other/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/smoke-test/api-1/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/smoke-test/api-2/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/smoke-test/client/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/it/with-deps/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer-maven-plugin/src/main/java");
//        correctSourceClassPaths.add("animal-sniffer/src/main/java");
//        correctSourceClassPaths.add("java-boot-classpath-detector/src/main/java");
//        PathUtil.fixPathFileSeparator(correctSourceClassPaths);
        // </editor-fold>

//        Collections.sort(sourceClasses);
//        Collections.sort(correctSourceClassPaths);
//        for (int i = 0; i < sourceClasses.size(); i++) {
//            assertTrue(sourceClasses.get(i).contains(correctSourceClassPaths.get(i)));
//        }
    }

    @Test
    public void testGetCompiledFoldersTestMavenProjectAnimalSniffer() throws Throwable {
        Revision revision = testConstants.getRevisionAnimalSniffer();
        JavaRevisionTool instance = new JavaRevisionTool();

        List<String> compiledClassPaths = new ArrayList(instance.getCompilationFolders(revision));
        assertEquals(compiledClassPaths.size(), 6);

        List<String> correctCompiledPaths = new ArrayList<String>(22);
        // <editor-fold defaultstate="collapsed" desc="initialize correct list">
        correctCompiledPaths.add("animal-sniffer/animal-sniffer/target/classes");
        correctCompiledPaths.add("animal-sniffer/animal-sniffer-annotations/target/classes");
        correctCompiledPaths.add("animal-sniffer/animal-sniffer-ant-tasks/target/classes");
        correctCompiledPaths.add("animal-sniffer/animal-sniffer-enforcer-rule/target/classes");
        correctCompiledPaths.add("animal-sniffer/animal-sniffer-maven-plugin/target/classes");
        correctCompiledPaths.add("animal-sniffer/java-boot-classpath-detector/target/classes");
        PathUtil.fixPathFileSeparator(correctCompiledPaths);
        // </editor-fold>

        Collections.sort(compiledClassPaths);
        Collections.sort(correctCompiledPaths);
        for (int i = 0; i < compiledClassPaths.size(); i++) {
            assertTrue(compiledClassPaths.get(i).contains(correctCompiledPaths.get(i)), "Invalid folder");
        }
    }

    @Test
    public void testGetCompiledClassNamesAnimalSniffer() throws Throwable {
        Revision revision = testConstants.getRevisionAnimalSniffer();
        JavaRevisionTool instance = new JavaRevisionTool();

        List<String> compiledClassNames = instance.getCompiledClassNames(revision);
        assertEquals(compiledClassNames.size(), 36);

        List<String> correctCompiledClassPaths = new ArrayList<String>(36);
        // <editor-fold defaultstate="collapsed" desc="initiation of correctCompiledClassPaths">
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ClassFileVisitor");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ClassListBuilder");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ClassListBuilder$1");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.Clazz");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.Main");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.RegexUtils");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureBuilder");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureBuilder$1");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureBuilder$SignatureVisitor");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker$CheckingVisitor");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker$CheckingVisitor$1");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker$ExactMatchRule");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker$MatchRule");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker$PrefixMatchRule");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureChecker$RegexMatchRule");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.SignatureMerger");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ant.AntLogger");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ant.BuildSignaturesTask");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ant.CheckSignatureTask");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ant.Ignore");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.ant.Signature");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.enforcer.CheckSignatureRule");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.enforcer.MavenLogger");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.enforcer.Signature");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.jbcpd.ShowClassPath");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.logging.Logger");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.logging.PrintWriterLogger");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.BuildSignaturesMojo");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.CheckSignatureMojo");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.HelpMojo");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.JdkToolchain");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.JdkToolchainConverter");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.MavenLogger");
        correctCompiledClassPaths.add("org.codehaus.mojo.animal_sniffer.maven.Signature");
        // </editor-fold>

        Collections.sort(compiledClassNames);
        Collections.sort(correctCompiledClassPaths);
        for (int i = 0; i < compiledClassNames.size(); i++) {
            assertTrue(compiledClassNames.get(i).equals(correctCompiledClassPaths.get(i)), "Invalid class name");
        }
    }

    @Test
    public void testGetSourceClassNamesAnimalSniffer() throws Throwable {
        Revision revision = testConstants.getRevisionAnimalSniffer();
        JavaRevisionTool instance = new JavaRevisionTool();

        Collection<String> tempList = instance.getSourceFiles(revision);
        assertEquals(tempList.size(), 43);

        List<String> correctSourceJavaFiles = new ArrayList<String>(12);
        // <editor-fold defaultstate="collapsed" desc="initiation of correctSourceJavaFiles">
        correctSourceJavaFiles.add("animal-sniffer-annotations\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\IgnoreJRERequirement.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\it\\merge-test\\src\\main\\java\\JDK15.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\it\\negative-test\\src\\main\\java\\JDK15.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\it\\smoke-test\\src\\main\\java\\JDK15.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ant\\AntLogger.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ant\\BuildSignaturesTask.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ant\\CheckSignatureTask.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ant\\Ignore.java");
        correctSourceJavaFiles.add("animal-sniffer-ant-tasks\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ant\\Signature.java");
        correctSourceJavaFiles.add("animal-sniffer-enforcer-rule\\src\\it\\setup-002\\src\\main\\java\\localdomain\\localhost\\Api.java");
        correctSourceJavaFiles.add("animal-sniffer-enforcer-rule\\src\\it\\smoke-test\\src\\main\\java\\JDK15.java");
        correctSourceJavaFiles.add("animal-sniffer-enforcer-rule\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\enforcer\\CheckSignatureRule.java");
        correctSourceJavaFiles.add("animal-sniffer-enforcer-rule\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\enforcer\\MavenLogger.java");
        correctSourceJavaFiles.add("animal-sniffer-enforcer-rule\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\enforcer\\Signature.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\jdk14-with-sig14\\src\\main\\java\\localhost\\Main.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\jdk15-with-sig14\\src\\main\\java\\localhost\\Main.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\jdk15-with-sig15\\src\\main\\java\\localhost\\Main.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\manimalsniffer-6\\src\\main\\java\\localhost\\Main.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\manimalsniffer-9\\src\\main\\java\\localhost\\Main.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\merge-test\\api\\src\\main\\java\\localdomain\\localhost\\Api.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\merge-test\\other\\src\\main\\java\\localdomain\\localhost\\OtherApi.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\smoke-test\\api-1\\src\\main\\java\\localdomain\\localhost\\Api.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\smoke-test\\api-2\\src\\main\\java\\localdomain\\localhost\\Api.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\smoke-test\\client\\src\\main\\java\\JDK15.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\with-deps\\src\\main\\java\\test\\SSOException.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\it\\with-deps\\src\\main\\java\\test\\SSOUtils.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\maven\\BuildSignaturesMojo.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\maven\\CheckSignatureMojo.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\maven\\JdkToolchain.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\maven\\JdkToolchainConverter.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\maven\\MavenLogger.java");
        correctSourceJavaFiles.add("animal-sniffer-maven-plugin\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\maven\\Signature.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ClassFileVisitor.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\ClassListBuilder.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\Clazz.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\Main.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\RegexUtils.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\SignatureBuilder.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\SignatureChecker.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\SignatureMerger.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\logging\\Logger.java");
        correctSourceJavaFiles.add("animal-sniffer\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\logging\\PrintWriterLogger.java");
        correctSourceJavaFiles.add("java-boot-classpath-detector\\src\\main\\java\\org\\codehaus\\mojo\\animal_sniffer\\jbcpd\\ShowClassPath.java");
        PathUtil.fixPathFileSeparator(correctSourceJavaFiles);
        // </editor-fold>

        List<String> sourceJavaFiles = new LinkedList<String>(tempList);
        Collections.sort(sourceJavaFiles);
        Collections.sort(correctSourceJavaFiles);
        for (int i = 0; i < sourceJavaFiles.size(); i++) {
            assertTrue(sourceJavaFiles.get(i).contains(correctSourceJavaFiles.get(i)), "Invalid source file");
        }
    }

}
