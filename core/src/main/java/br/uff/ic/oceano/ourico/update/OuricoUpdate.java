/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.update;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.service.ClientService;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 *
 * @author marapao
 */
public class OuricoUpdate {

    public void update(String pathWorkspace, String loginSVN, String senhaSVN, String oceanoURL) throws ServiceException, SVNException {

        Subversion subversion = new Subversion();
        String url = subversion.getURL(pathWorkspace);
        String[] partes = url.split("/");

        String autobranchStr;

        autobranchStr = partes[partes.length - 1];

        ClientService clientService = new ClientService();
        String protectedPath = clientService.recoverProtectedPath(autobranchStr, oceanoURL);
        System.out.println("protectedPath = " + protectedPath);


        Subversion svn = new Subversion(protectedPath, loginSVN, senhaSVN);

        svn.mergePath(protectedPath, pathWorkspace);

        svn.update(pathWorkspace, SVNRevision.HEAD);//verificar depois
        
    }
}
