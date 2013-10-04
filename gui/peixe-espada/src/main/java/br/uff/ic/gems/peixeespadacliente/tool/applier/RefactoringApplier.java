package br.uff.ic.gems.peixeespadacliente.tool.applier;

import br.uff.ic.gems.peixeespadacliente.action.DoAskOrchestratorAgentAboutBranchAndCommit;
import br.uff.ic.gems.peixeespadacliente.action.DoSendMessageAboutFailRefactoring;
import br.uff.ic.gems.peixeespadacliente.action.DoSendMessageAboutNotImproveNorWorselRefactoring;
import br.uff.ic.gems.peixeespadacliente.action.demo.DoCommitDemoLocal;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.QualityAttribute;
import java.util.Collections;
import java.util.List;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class RefactoringApplier {

    public static boolean applyBestCalculatedResolution(LocalManagerAgent agentPeixeEspada, List<? extends Resolution> resolutions) throws RefactoringException, ServiceException {
        agentPeixeEspada.testMessage(1, Translate.getTranslate().foundResolutions(resolutions.size()));
        if (!resolutions.isEmpty()) {
            return applyBestWorkingResolution(agentPeixeEspada, resolutions);
        } else {
            return false;
            // Nenhuma resolução encontrada
        }
    }

    private static boolean applyBestWorkingResolution(LocalManagerAgent agentPeixeEspada, List<? extends Resolution> resolutions) throws RefactoringException, ServiceException {
        Translate translate = Translate.getTranslate();
        Boolean improved = null;
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringResolutionBuilder = new StringBuilder();

        Resolution idealResolution = Collections.max(resolutions);
        stringBuilder.append(translate.idealRefactoring());

        improved = verifyImproved(agentPeixeEspada, idealResolution.getResolutionQuality());

        agentPeixeEspada.setHasImproved(improved);
        if (improved == null) {
            new DoSendMessageAboutNotImproveNorWorselRefactoring(idealResolution.getSymptom()).execute(agentPeixeEspada);
            return false;
            //Para de aplicar, pois atributo de qualidade é igual
        } else if (!improved) {
            new DoSendMessageAboutFailRefactoring(idealResolution.getSymptom()).execute(agentPeixeEspada);
            return false;
            //Para de aplicar, pois atributo de qualidade é pior
        }
        RefactoringTool refactoringTool = idealResolution.getSymptom().getRefactoringTool();
        refactoringTool.revertMoDifications();

        stringResolutionBuilder.append(idealResolution.getSymptom().getDescription());
        stringBuilder.append(translate.changingTheCode());
        try {
            if (!idealResolution.applyWorking(stringResolutionBuilder)) {
                throw new Exception("");
            }
        } catch (Throwable exception) {
            refactoringTool.revertMoDifications();
            agentPeixeEspada.printTestError(1, exception);
            agentPeixeEspada.appendMessage(stringResolutionBuilder.toString());
            agentPeixeEspada.testMessage(1, stringBuilder.toString());
            agentPeixeEspada.appendMessage(translate.brokenResolution());

            //Nos testes que fiz, só caia aqui quando não era feito clean build, então deixei clean build por padrão
            //mas se cair aqui, é bom ter uma segunda opção de commitar a segunda melhor refatoração
            resolutions.remove(idealResolution);
            return RefactoringApplier.applyBestCalculatedResolution(agentPeixeEspada, resolutions);

        }

        stringBuilder.append(translate.commitingChanges());
        agentPeixeEspada.getFinalRelatory().openAppendMultiLineAndClose("\n" + stringResolutionBuilder.toString());
        agentPeixeEspada.appendMessage(stringResolutionBuilder.toString());
        agentPeixeEspada.testMessage(1, stringBuilder.toString());

        if (agentPeixeEspada.isTesting()) {
            new DoCommitDemoLocal().execute(agentPeixeEspada);
        } else {
            agentPeixeEspada.getMeterAgent().setCurrentQualityAttributeBySucess(agentPeixeEspada, idealResolution.getResolutionQuality());
            agentPeixeEspada.appendMetricRelatory();
            new DoAskOrchestratorAgentAboutBranchAndCommit(idealResolution.getSymptom()).execute(agentPeixeEspada);
        }
        return true;
    }

    private static Boolean verifyImproved(LocalManagerAgent agentPeixeEspada, QualityAttribute qualityAttribute) {
        Boolean improved = null;
        if (agentPeixeEspada.isTesting()) {
            improved = true;
        } else {
            agentPeixeEspada.getMeterAgent().printQualityAttribute(agentPeixeEspada, qualityAttribute);
            improved = agentPeixeEspada.getMeterAgent().hasImproved(agentPeixeEspada, qualityAttribute);
        }
        return improved;

    }
}
