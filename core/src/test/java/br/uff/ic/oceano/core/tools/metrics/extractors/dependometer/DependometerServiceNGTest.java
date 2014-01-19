/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.Adapter;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel
 */
public class DependometerServiceNGTest extends AbstractNGTest {

    protected static final CppProjectsHelper testConstantsCpp = new CppProjectsHelper();

    /**
     * 
     */
    @Test
    public void testData() throws ObjetoNaoEncontradoException {
        println("test Data");
        SoftwareProject neopz = testConstantsCpp.getDBNeoPZProject();
        double count = 0;
        int countUnchagedFiles = 0;
        int minCHanged = Integer.MAX_VALUE;
        int maxCHanged = 0;
        Long maxRevisionNumber = null;
        final Set<Revision> revisions = neopz.getRevisions();
        for (Revision revision : revisions) {
            final Set changedFiles = revision.getChangedFiles();
            if(changedFiles != null){
                final int size = changedFiles.size();
                count += size;
                minCHanged = minCHanged > size? size:minCHanged;
                if(maxCHanged < size){
                    maxCHanged = size;
                    maxRevisionNumber = revision.getNumber();
                }
                
                
                countUnchagedFiles = size == 0? countUnchagedFiles+1:countUnchagedFiles;
            } else {
                println("Revision "+ revision+" has no changed files.");
                countUnchagedFiles += 1;
            }
        }
        println("Average files per commit: " + count/revisions.size());
        println("Unchanged files per commit: " + countUnchagedFiles);
        println("Minimum changed files per commit: " + minCHanged);
        println("Maximum changed files per commit: " + maxCHanged+" in revision number: " + maxRevisionNumber);
    }
    
    /**
     * 
     */
    @Test
    public void testDumpEasycountResult() {
        println("dumping easyCount");
        Revision revision = testConstantsCpp.getEasyCountRevision();
        String path = dump(revision);
        FileUtils.deleteDirectory(path);
    }

    @Test
    public void testDumpNeopzResult() throws Exception {
        println("dumping neopz");
        //Revision revision = testConstantsCpp.getNeopzRevision();
        Revision revision = testConstantsCpp.checkoutNeoPzRevision(2);
        String path = dump(revision);
        FileUtils.deleteDirectory(path);
    }

    @Test
    public void testDumpNeopzRevsionResult() throws Exception {
        println("dumping several neopz revisions");
        List<String> paths = new LinkedList<String>();
        Revision revision = testConstantsCpp.checkoutNeoPzRevision(3181);
        String path = dump(revision);
        paths.add(path);
        
        revision = testConstantsCpp.checkoutNeoPzRevision(4500);
        path = dump(revision);
        paths.add(path);
        
        for (String string : paths) {
            FileUtils.deleteDirectory(string);
        }        
    }

    private String dump(Revision revision) {
        String path = ".\\target\\" +revision.getProject() + revision.getNumber()+ "_dependometer_result.xml";
        path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);
        try {
            Adapter service = new Adapter(revision);
            service.writeMetricsToXML(path);            
        } catch (Exception ex) {            
            fail(ex.getMessage(),ex);
        }   
        return path;
    }

    

    
}
