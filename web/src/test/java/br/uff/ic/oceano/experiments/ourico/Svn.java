/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.ourico.rcs.Subversion;
import org.testng.annotations.Test;
import org.tmatesoft.svn.core.SVNException;

/**
 *
 * @author marapao
 */
public class Svn {

    @Test
    public void teste() throws SVNException{
        Subversion svn = new Subversion();
        System.out.println(svn.getURL("/home/marapao/Desktop/sandbox/4"));

    }

}
