/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.service.ClientService;
import java.util.Date;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class Services {

    private ClientService clientService = ObjectFactory.getObjectWithDataBaseDependencies(ClientService.class);

    @Test
    public void salvaEstado() throws ServiceException{
        Date dateInicial = new Date();
    
        Date dateFinal = new Date();
        String descricao = "teste";
        Long autobranch = 10l;


        clientService.saveEstado(dateInicial, dateFinal, descricao, autobranch);
        
    }

}
