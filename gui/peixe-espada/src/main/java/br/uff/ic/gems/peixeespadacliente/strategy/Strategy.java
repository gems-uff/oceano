/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;

/**
 *
 * @author GEMS
 */
public interface Strategy {
    
    public void performRefactoring(LocalManagerAgent agentPeixeEspada) throws ServiceException;
    
}
