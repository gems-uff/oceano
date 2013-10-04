/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.rcs.Subversion;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNRevision;

/**
 *
 * @author marapao
 */
public class OuricoSVNService implements PersistenceService{

    private CheckOutService checkOutService;

    public OuricoSVNService() {
    }


    
    public void setup(){
        checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    }
    
    public CheckOut preparaAutobranch(String pathVerificado, String pathAutobranch, String loginSVN, String senhaSVN) throws SVNException {

        Subversion svn = new Subversion(pathVerificado, loginSVN, senhaSVN);
        CheckOut co = new CheckOut();
        Long autobranch;
        long revisaoGerada = 0;


        try {
            //Requisição de um autobranch que ainda não foi utilizado
            autobranch = checkOutService.getMaxAutobranch() + 1;

            co.setAutobranch(autobranch);
        }catch(NullPointerException ex){
            ex.printStackTrace();
            autobranch = 1l;
            co.setAutobranch(autobranch);
        }
        catch (ObjetoNaoEncontradoException ex) {
            autobranch = 1l;
            co.setAutobranch(autobranch);
        }


        //Completando o pathAutobranch
        if(!pathAutobranch.endsWith("/"))
            pathAutobranch = pathAutobranch.concat("/"+autobranch);
        else
            pathAutobranch = pathAutobranch.concat(autobranch.toString());


        //cópia do pathVerificado para o pathAutobranch
        try {
           revisaoGerada = svn.copia(pathVerificado, pathAutobranch, SVNRevision.HEAD);

        } catch (SVNException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println("Verifique os caminhos do repositório.");
        }


        co.setRevisao(revisaoGerada);

        return co;

    }
}
