/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util.file;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.util.SystemUtil;
import java.io.File;
import java.io.IOException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel
 */
public class PathUtilNGTest extends AbstractNGTest {

    /**
     * 
     */
    @Test
    public void testGetCurrentAbsolutePath() {
        println("getCurrentAbsolutePath");

        String pathSystem = System.getProperty("user.dir") + SystemUtil.FILESEPARATOR;
        String result = PathUtil.getCurrentAbsolutePath();
        assertTrue(result.compareTo(pathSystem) == 0, "should be equal");
    }

    /**
     *
     */
    @Test
    public void testIsEmptyOnUserDir() {
        println("IsEmptyOnUserDir");
        String pathSystem = System.getProperty("user.dir") + SystemUtil.FILESEPARATOR;
        assertFalse(PathUtil.isEmpty(pathSystem));
    }

    @Test
    public void testIsEmptyOnEmptyDirectory() {
        try {
            println("IsEmptyOnEmptyDirectory");
            String path = SystemUtil.getUniqueTempDirectory();
            PathUtil.mkDirs(path);
            assertTrue(PathUtil.isEmpty(path));
            FileUtils.deleteDirectory(new File(path));
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testGetAbsolutePathFromRelativetoCurrentPath() {
        println("GetAbsolutePathFromRelativetoCurrentPath");
        String path = ".\\target\\test-classes\\";
        String fixedPath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);
        assertTrue(PathUtil.exists(fixedPath));
    }
    
    @Test
    public void testGetWellFormedPath() throws IOException {
        println("GetWellFormedPath");
        
        String path = "test654687.txt"; //non existing file
        String fixedPath = PathUtil.getWellFormedPath(path);
        assertTrue(path.equals(fixedPath), "Paths must be equal");
        
        File file = new File(path);               
        assertTrue(!file.exists(), "File already exists");        
        
        //file must exist to test File.isDirectory() work        
        
        file.createNewFile();
        assertTrue(file.isFile(), "Has to be true to isFile()");        
        assertTrue(!file.isDirectory(), "Has to be false to isDirectory()");
        assertTrue(file.delete(), "File not erased");        
        assertTrue(!file.exists(), "File still exists");        
        
        fixedPath = PathUtil.getWellFormedPath(path);
        assertTrue(path.equals(fixedPath), "must be equal");
        file = new File(fixedPath);
        file.createNewFile();
        assertTrue(file.isFile(), "Has to be true to isFile()");        
        assertTrue(!file.isDirectory(), "Has to be false to isDirectory()");
        assertTrue(file.delete(), "File not erased");
        assertTrue(!file.exists(), "File still exists");        
    }
}
