package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoFinishWork extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        // GERA RELATORIO e ENVIA AO AGENTE ORQUESTRADOR

        agentPeixeEspada.appendMessage(translate.finishWorkSeparatorStart());
        if (agentPeixeEspada.hasBranch()) {
            agentPeixeEspada.appendMessage(translate.finalRelatory(
                agentPeixeEspada.getFinalRelatory().getName()
            ));
            agentPeixeEspada.appendMessage(translate.metricsRelatory(
                agentPeixeEspada.getMetricsRelatory().getName()
            ));
            agentPeixeEspada.appendMessage(translate.branchPath(
                agentPeixeEspada.getProjectVCS().getRepositoryUrl()
            ));
        } else {
            agentPeixeEspada.appendMessage(translate.noBranch(
                agentPeixeEspada.getProjectVCS().getRepositoryUrl()
            ));
        }
        clientService.agentUnavaiable(agentPeixeEspada);
        agentPeixeEspada.appendMessage(translate.finishWorkSeparatorEnd());
        agentPeixeEspada.getOutput().fieldStatus.setText(translate.inative());
//        agentPeixeEspada.getOutput().setClosable(true);
        return agentPeixeEspada;
    }
}
