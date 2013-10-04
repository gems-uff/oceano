package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;

/**
 *
 * @author Heliomar
 */
public interface Action {

    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException;
}
