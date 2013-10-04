/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.compiler;

import br.uff.ic.oceano.JavaProjectsHelper;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import org.testng.annotations.Test;

/**
 *
 */
public class CompilerServiceNGTest extends AbstractNGTest{  
   
    private JavaProjectsHelper javaHelper  = new JavaProjectsHelper();
            
    /**
     * 
     */
    @Test
    public void testCompile() throws Exception {
        println("compile");
        Revision revision = javaHelper.getRevisionMaven3();
        CompilerService.compile(revision);
    }

}