/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util.file;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 */
public class ArchiveNGTest extends AbstractNGTest{
       
    
    /**
     * 
     */
    @Test
    public void testOpenAppendAndClose() {
        println("openAppendAndClose");
        String fileName = "test.txt";
        Archive instance = new Archive(fileName);
        
        instance.openAppendAndClose("testing");                
        assertTrue(instance.existsFile(),"File created");
        
        instance.deleteFile();
        assertTrue(!instance.existsFile(),"File deleted");
    }

}