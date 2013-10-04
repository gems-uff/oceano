package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;

/**
 *
 * @author Heliomar, João Felipe
 */
public class DoAskOrchestratorAgentAboutBranchAndCommit extends AbstractAction {

    private Symptom symptom;

    public DoAskOrchestratorAgentAboutBranchAndCommit(Symptom symptom) {
        this.symptom = symptom;
    }

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        if (!agentPeixeEspada.hasBranch()) {
            // cria um branch, comita nele e avisa o agente Orquestrador
            clientService.createBranchToSucessRefactoringAndInformOrchestradorAgent(agentPeixeEspada, symptom.getRefactoringTool().toString());
            agentPeixeEspada.setHasBranch(true);
        } else {
            try {
                // commita no local onde está (possivelmente em um branch)
                agentPeixeEspada.getProjectVCS().doCommit("positive changes in work agent project, refactoring: " + symptom.getRefactoringTool().getClass().getSimpleName());
            } catch (VCSException ex) {
                throw new ServiceException(ex);
            }
            // avisa o caso de sucesso ou não alteracao do atributo de qualidade ao agente orquestrador
            if (agentPeixeEspada.hasImproved() == null) {
                clientService.sendMessageToNotImproveNorWorseRefactoring(agentPeixeEspada, symptom);
            } else if (agentPeixeEspada.hasImproved()) {
                clientService.sendMessageToSucessRefactoring(agentPeixeEspada, symptom);
            }
        }
        return agentPeixeEspada;

    }
}
