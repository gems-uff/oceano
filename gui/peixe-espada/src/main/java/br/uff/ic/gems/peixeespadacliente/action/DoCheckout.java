package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoCheckout extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        //executa
        agentPeixeEspada.appendMessage(translate.doingCheckout(
            agentPeixeEspada.getOrchestratorAgent().getProject().toString()
        ));

        /**
         * atalho para nao fazer checout
         * <WARN>nao commit o trecho abaixo descomentado</WARN>
         */
//        agentPeixeEspada.getProjectVCS().setLocalPath("C:\\Users\\Joao\\AppData\\Local\\Temp\\peixeespada_workspaces\\BCEL_5");
        agentPeixeEspada.appendMessage(translate.checkoutVersion(
            clientService.doCheckout(agentPeixeEspada)
        ));
        agentPeixeEspada.appendMessage(translate.workspace(
            agentPeixeEspada.getProjectVCS().getLocalPath()
        ));

        return agentPeixeEspada;
    }
}
