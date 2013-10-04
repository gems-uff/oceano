package br.uff.ic.gems.peixeespadacliente.action.demo;

import br.uff.ic.gems.peixeespadacliente.action.*;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.util.file.PathUtil;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoCheckoutDemoLocal extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        //executa
        agentPeixeEspada.appendMessage(translate.doingCheckout(
            agentPeixeEspada.getProjectVCS().getName()
        ));

        agentPeixeEspada.appendMessage(translate.checkoutVersion(
            clientService.doCheckout(agentPeixeEspada)
        ));
        agentPeixeEspada.appendMessage(translate.workspace(
            agentPeixeEspada.getProjectVCS().getLocalPath()
        ));


        String branch = PathUtil.getWellFormedURL(agentPeixeEspada.getProjectUser().getProject().getConfigurationItem().getBaseUrl(), "branches", "peixeespadateste", "" + System.currentTimeMillis());
//        String branch = repositoryUrl.substring(repositoryUrl.lastIndexOf("/"))+"branch/peixeespadalocaltest/"+System.currentTimeMillis();

        Long commit = null;
        try {
            ProjectVCS vcs = agentPeixeEspada.getProjectVCS();
            vcs.doCopyTo(branch);
            vcs.doSwitchTo(branch);
            commit = vcs.doCommit("created to run tests");
        } catch (VCSException vCSException) {
            throw new ServiceException(vCSException);
        }

        agentPeixeEspada.getProjectUser().getProject().setRepositoryUrl(branch);
        agentPeixeEspada.getProjectVCS().setRepositoryUrl(branch);
        agentPeixeEspada.appendMessage(translate.branchCreated(branch, commit));


        return agentPeixeEspada;

    }
}
