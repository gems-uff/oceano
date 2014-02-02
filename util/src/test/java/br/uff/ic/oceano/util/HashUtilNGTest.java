package br.uff.ic.oceano.util;

import br.uff.ic.oceano.util.file.FileUtils;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel
 */
public class HashUtilNGTest {

    public HashUtilNGTest() {
        System.out.println("Testing " + HashUtil.class.getCanonicalName());
    }

    /**
     * Teste de método sha1, da classe HashUtil.
     */
    @Test(expectedExceptions = Exception.class)
    public void testSha1_null() throws Exception {
        Object object = null;
        String expResult = "";
        String result = HashUtil.sha1(object);
        assertEquals(result, expResult);
    }

    /**
     * Teste de método getFileMD5, da classe HashUtil.
     */
    @Test(expectedExceptions = Exception.class)
    public void testGetFileMD5_null() throws Exception {
        String filename = null;
        String expResult = "";
        String result = HashUtil.getFileMD5(filename);
        assertEquals(result, expResult);
    }

    @Test
    public void testGetFileMD5_emptyFile() throws Exception {
        String tempDir = SystemUtil.getTempDirectory() + SystemUtil.FILESEPARATOR;
        tempDir += "hashtest-" + DateUtil.currentDate().getTime() + ".txt";
        File file = new File(tempDir);
        assertTrue(file.createNewFile());

        String result = HashUtil.getFileMD5(tempDir);
        assertNotNull(result);

        String checkResult = HashUtil.getFileMD5(tempDir);
        assertEquals(result, checkResult);
    }

    @Test
    public void testGetFileMD5_emptyFiles() throws Exception {
        String tempDir = SystemUtil.getTempDirectory() + SystemUtil.FILESEPARATOR;
        tempDir += "hashtest-" + DateUtil.currentDate().getTime() + ".txt";
        File file = new File(tempDir);
        assertTrue(file.createNewFile());

        String result = HashUtil.getFileMD5(tempDir);
        assertNotNull(result);

        Thread.sleep(500); //to change date

        tempDir = SystemUtil.getTempDirectory() + SystemUtil.FILESEPARATOR;
        tempDir += "hashtest2-" + DateUtil.currentDate().getTime() + ".txt";
        file = new File(tempDir);
        assertTrue(file.createNewFile());

        String result2 = HashUtil.getFileMD5(tempDir);
        assertNotNull(result2);

        assertEquals(result, result2);
    }

    /**
     * Teste de método getMD5, da classe HashUtil.
     */
    @Test(expectedExceptions = Exception.class)
    public void testGetMD5_null() throws Exception {
        Collection<String> paths = null;
        String expResult = "";
        String result = HashUtil.getMD5(paths);
        assertEquals(result, expResult);
    }

    @Test
    public void testTempDirectoryMD5() {
        try {
            String tempPath = SystemUtil.getTempDirectory();
            assertNotNull(tempPath,"Temp directory not found!");
            
            File tempDir = new File(tempPath);
            final Set<String> allFiles = FileUtils.getAllFilesInFolderAndSubFolders(tempDir);
            
            //limit to 100 files
            List<String> files = new LinkedList<String>();
            for (final String buffer : allFiles) {
                files.add(buffer);
                
                if(files.size()>=100){
                    break;
                }
            }
            
            //Check MD5 is not random
            String result = HashUtil.getMD5(files);
            String resultCheck = HashUtil.getMD5(files);
            assertEquals(result, resultCheck, "MD5 is creating random results!");
            
            //Check MD% is not random, even for unordered values
            final List<String> ordered = new LinkedList<String>(files);
            final List<String> shuffled = new LinkedList<String>(files);
            Collections.shuffle(ordered);
            result = HashUtil.getMD5(ordered);
            resultCheck = HashUtil.getMD5(shuffled);
            assertNotEquals(result, resultCheck, "MD5 is altering input collection!");
            
        } catch (Exception ex) {
            fail(ex.getMessage(),ex);
        }

    }
}
