/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.checkout.ciclo;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.service.ClientService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.SVNException;

/**
 *
 * @author marapao
 */
public class CheckoutCiclo {

    public CheckOut remoto(String urlCheckedOut, String senhaSVN, String loginSVN, String workspaceLocal, String oceanoURL) throws ServiceException, SVNException {

        String urlGerada;

        Subversion svn = new Subversion(urlCheckedOut, loginSVN, senhaSVN);
        CheckOut co = new CheckOut();




        ClientService clientService = new ClientService();
        urlGerada = clientService.preparaAutobranch(urlCheckedOut, senhaSVN, loginSVN, oceanoURL);
        String posCheckoutVerification = clientService.posCheckoutVerification(oceanoURL);

        String[] partes = urlGerada.split("/");
        String autobranchGerado = null;
        for (String string : partes) {
            autobranchGerado = string;
        }

        //checkout do autobranch para o workspace
        try {
            Long revision = svn.checkout(urlGerada, workspaceLocal + "/" + autobranchGerado);
            co.setAutobranch(Long.parseLong(autobranchGerado));
            co.setRevisao(revision);
            return co;
        } catch (SVNException ex) {
            Logger.getLogger(CheckoutCiclo.class.getName()).log(Level.SEVERE, null, ex);
            throw  ex;
        }


    }
}

