package br.uff.ic.gems.peixeespadacliente.action.demo;

import br.uff.ic.gems.peixeespadacliente.action.*;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoCommitDemoLocal extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        try {
            agentPeixeEspada.appendMessage(translate.commitingSuccessful());
            agentPeixeEspada.getProjectVCS().doCommit("positive changes in work agent project");
        } catch (VCSException ex) {
            throw new ServiceException(ex);
        }
        return agentPeixeEspada;
    }
}
