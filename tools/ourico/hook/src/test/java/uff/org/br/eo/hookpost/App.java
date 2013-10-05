/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uff.org.br.eo.hookpost;

import junit.framework.TestCase;
import uff.org.br.eo.scv.SvnInformation;

/**
 *
 * @author marapao
 */
public class App extends TestCase {
    
    public App(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInfoSVN(){
        String rep = "/home/marapao/repositorio/svn/testando";
        String rev = "128";

        SvnInformation svnInfo = new SvnInformation(rep, rev);

        for(String dir : svnInfo.returnChangedDirectories())
            System.out.println(dir);
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

}
