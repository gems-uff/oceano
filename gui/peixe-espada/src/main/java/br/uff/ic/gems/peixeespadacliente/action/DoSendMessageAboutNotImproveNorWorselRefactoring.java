package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import translation.Translate;

/**
 *
 * @author Heliomar, João Felipe
 */
public class DoSendMessageAboutNotImproveNorWorselRefactoring extends AbstractAction {

    private Symptom symptom;

    public DoSendMessageAboutNotImproveNorWorselRefactoring(Symptom symptom) {
        this.symptom = symptom;
    }

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        agentPeixeEspada.appendMessage(translate.notImprovedWorsened());
        agentPeixeEspada.appendMessage(translate.revertingChanges());
        try {
            agentPeixeEspada.getProjectVCS().doReset();
        } catch (VCSException ex) {
            throw new ServiceException(ex);
        }
        clientService.sendMessageToNotImproveNorWorseRefactoring(agentPeixeEspada, symptom);

        return agentPeixeEspada;
    }
}
