package br.uff.ic.gems.peixeespadacliente.service;

import br.uff.ic.gems.peixeespadacliente.context.Constants;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.gems.peixeespadacliente.thread.ThreadScheduling;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class ClientService {

    private JSONService jSONService = ObjectFactory.getObjectWithoutDataBaseDependencies(JSONService.class);

    public void validateSchedulingRequest(Date localDateInitial, Date localDateEnd) throws ServiceException {
        Translate translate = Translate.getTranslate();
        Calendar gcInitial = new GregorianCalendar();
        gcInitial.setTime(localDateInitial);

        gcInitial.add(Calendar.MINUTE, -2);
        if (gcInitial.compareTo(Calendar.getInstance()) != 1) {
            throw new ServiceException(translate.initialDate2min());
        }

        Calendar gcEnd = new GregorianCalendar();
        gcEnd.setTime(localDateEnd);

        gcInitial.add(Calendar.MINUTE, 2);
        if (gcInitial.compareTo(gcEnd) != -1) {
            throw new ServiceException(translate.endDateBigger());
        }
    }

    public LocalManagerAgent scheduling(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        agentPeixeEspada = jSONService.getWorkAgentScheduled(agentPeixeEspada);
        new ThreadScheduling(agentPeixeEspada).start();
        return agentPeixeEspada;
    }

    public List<Agent> getOrchestratorAgents(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        return jSONService.getListOrchestratorAgents(agentPeixeEspada);
    }

    public String agentAvaiable(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        String message = jSONService.agentAvaiable(agentPeixeEspada);
        agentPeixeEspada.appendMessage(message);
        return message;
    }

    public List<String> agentWorking(LocalManagerAgent agentPeixeEspada) throws ServiceException {

        String message = jSONService.agentWorking(agentPeixeEspada);
        return jSONService.getRefactorings(message);
    }

    public String agentUnavaiable(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        String message = jSONService.agentUnavaiable(agentPeixeEspada);
        agentPeixeEspada.appendMessage(message);
        return message;

    }

    public void createBranchToSucessRefactoringAndInformOrchestradorAgent(LocalManagerAgent agentPeixeEspada, String refactoring) throws ServiceException {
        Translate translate = Translate.getTranslate();
        
        agentPeixeEspada.appendMessage(translate.requestBranch());
        String branch = jSONService.getNextBranchtoSucessRefactoring(agentPeixeEspada, refactoring);
        agentPeixeEspada.appendMessage(translate.copyToBranch(branch));
        try {
            ProjectVCS vcs = agentPeixeEspada.getProjectVCS();
            vcs.doCopyTo(branch);
            vcs.doSwitchTo(branch);
            vcs.doCommit("positive changes in work agent project");
        } catch (VCSException ex) {
            throw new ServiceException(ex);
        }

        agentPeixeEspada.appendMessage(translate.newBranch(branch));
        agentPeixeEspada.getOrchestratorAgent().getProject().setRepositoryUrl(branch);

    }

    public void sendMessageToFailRefactoring(LocalManagerAgent agentPeixeEspada, Symptom symptom) throws ServiceException {
        Translate translate = Translate.getTranslate();

        agentPeixeEspada.appendMessage(translate.informingFailure());
        jSONService.sendMessageAboutRefactoring(agentPeixeEspada, symptom.getRefactoringTool().toString(), JSONService.CMD_FAIL_REFACTORING);
        agentPeixeEspada.appendMessage(translate.agentAware());
    }

    public Long doCheckout(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        File workspace = getWorkspaceToCheckout(agentPeixeEspada);

        File workspaceOriginal = new File(workspace, Constants.ORIGINAL_DIRECTORY);
        workspaceOriginal.mkdir();
        
        agentPeixeEspada.getProjectVCS().setLocalPath(workspaceOriginal.getAbsolutePath());
        agentPeixeEspada.setBaseWorkspace(workspace.getAbsolutePath());
        
        try {
            return agentPeixeEspada.getProjectVCS().doCheckout(workspaceOriginal);
        } catch (VCSException ex) {
            throw new ServiceException(ex);
        }
    }

    private File getWorkspaceToCheckout(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        File workspace = null;
        String baseWorkspaceName = agentPeixeEspada.getProjectUser().getProject().getConfigurationItem().getName().replace(" ", "_");
        int index = 0;

        do {
            workspace = new File(getParentFileToCheckout(), baseWorkspaceName + "_" + (index++));
        } while (workspace.exists());
        
        workspace.mkdir();
        if (!workspace.exists()) {
            throw new ServiceException(Translate.getTranslate().cannotCreateWorkspace(workspace.getAbsolutePath()));
        }
        return workspace;
    }

    private File getParentFileToCheckout() throws ServiceException {
        File temp;
        try {
            temp = File.createTempFile("temp", ".txt");
        } catch (IOException ex) {
            throw new ServiceException(ex);
        }
        File tempDirectory = temp.getParentFile();

        File tempPathParentToWorkspaces = new File(tempDirectory, Constants.WORKSPACE_TEMP_DIRECTORY);

        if (!tempPathParentToWorkspaces.exists()) {
            tempPathParentToWorkspaces.mkdir();

            if (!tempPathParentToWorkspaces.exists()) {
                throw new ServiceException(Translate.getTranslate().cannotCreatePath(tempPathParentToWorkspaces.getAbsolutePath()));
            }
        }

        temp.delete();
        return tempPathParentToWorkspaces;
    }

    public void sendMessageToSucessRefactoring(LocalManagerAgent agentPeixeEspada, Symptom symptom) throws ServiceException {
        Translate translate = Translate.getTranslate();
        agentPeixeEspada.appendMessage(translate.informingSuccess());
        jSONService.sendMessageAboutRefactoring(agentPeixeEspada, symptom.getRefactoringTool().toString(), JSONService.CMD_SUCCESS_REFACTORING);
        agentPeixeEspada.appendMessage(translate.agentAware());
    }

    public void sendMessageToNotImproveNorWorseRefactoring(LocalManagerAgent agentPeixeEspada, Symptom symptom) throws ServiceException {
        Translate translate = Translate.getTranslate();
        agentPeixeEspada.appendMessage(translate.informingNoChange());
        jSONService.sendMessageAboutRefactoring(agentPeixeEspada, symptom.getRefactoringTool().toString(), JSONService.CMD_NOT_IMPROVE_NOR_WORSEN_REFACTORING);
        agentPeixeEspada.appendMessage(translate.agentAware());
    }

    public QualityAttribute getQualityAttributeWithMetricsAndFactors(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        return jSONService.getQualityAttributeWithMetricsAndFactors(agentPeixeEspada);
    }
}
