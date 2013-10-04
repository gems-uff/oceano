/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

/**
 *
 * @author wallace
 */
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.JavaProjectsHelper;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestRevisionUtil {

    private static JavaProjectsHelper testConstants;
    private static Revision revisionJava;

    @BeforeTest
    public static void setupTest() {
        System.out.println( TestRevisionUtil.class + " tests");

        testConstants = new JavaProjectsHelper();
        revisionJava = testConstants.getRevisionTestMavenProject();
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetSourceFilesFromChangedFiles() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getSourceFilesFromChangedFiles(revision);
    }

    @Test
    public void testGetSourceFilesFromChangedFilesJava() throws Exception {
        Revision revision = revisionJava;
        RevisionUtil.get().getSourceFilesFromChangedFiles(revision);
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetPackagesFromChangedFiles() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getPackagesFromChangedFiles(revision);
    }

    @Test
    public void testGetPackagesFromChangedFilesJava() throws Exception {
        Revision revision = revisionJava;
        RevisionUtil.get().getPackagesFromChangedFiles(revision);
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetSourceFiles() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getSourceFiles(revision);
    }

    @Test
    public void testGetSourceFilesJava() throws Exception {
        Revision revision = revisionJava;
        RevisionUtil.get().getSourceFiles(revision);
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetPackages() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getPackages(revision);
    }

    @Test
    public void testGetPackagesJava() throws Exception {
        Revision revision = revisionJava;
        RevisionUtil.get().getPackages(revision);
    }
}
