/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.CppProjectsHelper;
import java.util.Iterator;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel
 */
public class TestCPPRevisionTool extends AbstractNGTest{

    private CppProjectsHelper testConstants;
    private Revision easyCount;
    private Revision neoPz;

    public TestCPPRevisionTool(){
        testConstants = new CppProjectsHelper();
        easyCount = testConstants.getEasyCountRevision();
        neoPz = testConstants.getNeopzRevision();
    }

    /**
     * Test of validate method, of class CPPRevisionTool.
     */
    @Test(expectedExceptions=Exception.class)
    public void testValidate() throws Exception {
        Revision revision = null;
        CPPRevisionTool instance = new CPPRevisionTool();
        instance.validate(revision);
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetSourceFilesFromChangedFiles() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getSourceFilesFromChangedFiles(revision);
    }

    @Test
    public void testGetSourceFilesFromChangedFilesJava() throws Exception {
        Revision revision = easyCount;
        RevisionUtil.get().getSourceFilesFromChangedFiles(revision);
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetPackagesFromChangedFilesNull() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getPackagesFromChangedFiles(revision);
    }

    @Test
    public void testGetPackagesFromChangedFilesNotNull() throws Exception {
        Revision revision = easyCount;
        RevisionUtil.get().getPackagesFromChangedFiles(revision);
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetSourceFilesNull() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getSourceFiles(revision);
    }

    @Test
    public void testGetSourceFilesNotNull() throws Exception {
        Revision revision = easyCount;
        Set<String> files = RevisionUtil.get().getSourceFiles(revision);
        assertEquals(files.size(),1);

        revision = neoPz;
        files = RevisionUtil.get().getSourceFiles(revision);
        assertEquals(files.size(),473);

        Iterator<String> it = files.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if(path.endsWith(".c") ||path.endsWith(".cpp") ||path.endsWith(".cc") ||path.endsWith(".h")){
                it.remove();
            }
        }
        toOutput("Files left",files);
        assertTrue(files.isEmpty());
    }

    @Test(expectedExceptions=Exception.class)
    public void testGetPackagesNull() throws Exception {
        Revision revision = null;
        RevisionUtil.get().getPackages(revision);
    }

    @Test
    public void testGetPackagesNotNull() throws Exception {
        Revision revision = easyCount;
        RevisionUtil.get().getPackages(revision);
    }


}
