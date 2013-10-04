/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.Adapter;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import java.util.LinkedList;
import java.util.List;
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
    public void testDumpEasycountResult() {
        println("dumping easyCount");
        Revision revision = testConstantsCpp.getEasyCountRevision();
        String path = dump(revision);
        FileUtils.deleteDirectory(path);
    }

    @Test
    public void testDumpNeopzResult() throws Exception {
        println("dumping neopz");
        Revision revision = testConstantsCpp.getNeopzRevision();
        //Revision revision = testConstantsCpp.checkoutNeoPzRevision(2);
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
