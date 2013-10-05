/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uff.org.br.eo.hookpost;

import org.testng.annotations.Test;
import uff.org.br.eo.scv.SvnInformation;

/**
 *
 * @author marapao
 */
public class TestSVNInfo {

    @Test
    public void testeLog(){

        String rep = "/home/marapao/repositorio/svn/testando";
        String rev = "215";

        SvnInformation info = new SvnInformation();
        info.setRepository(rep);
        info.setVersion(rev);

        System.out.println("Log "+info.retornaMensagem());
    }
}
