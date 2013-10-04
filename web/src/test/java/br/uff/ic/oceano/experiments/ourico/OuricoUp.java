/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.rcs.context.Constants;
import br.uff.ic.oceano.ourico.update.OuricoUpdate;
import org.testng.annotations.Test;
import org.tmatesoft.svn.core.SVNException;


/**
 *
 * @author marapao
 */
public class OuricoUp {


    @Test
    public void uptade() throws ServiceException, SVNException{
        OuricoUpdate ouricoUpdate = new OuricoUpdate();

        ouricoUpdate.update("/home/marapao/Desktop/sandbox/1", "gleiph", "ghiotto",Constants.URL_OCEANO);
    }

}
