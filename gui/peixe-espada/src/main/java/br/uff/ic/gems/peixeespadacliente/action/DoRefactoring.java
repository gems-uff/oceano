package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.strategy.CurrentStrategy;
import br.uff.ic.gems.peixeespadacliente.strategy.Strategy;
import br.uff.ic.oceano.core.exception.ServiceException;
import java.lang.reflect.Method;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoRefactoring extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        Strategy strategy = new CurrentStrategy();
        
        agentPeixeEspada.appendMessage(translate.startingPlan());
        agentPeixeEspada.appendMessage(translate.checkingImplemented());

        strategy.performRefactoring(agentPeixeEspada);

        return agentPeixeEspada;
    }
}
