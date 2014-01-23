/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util;

import br.uff.ic.oceano.util.file.FileUtils;
import java.io.File;
import java.util.Collection;
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
}
