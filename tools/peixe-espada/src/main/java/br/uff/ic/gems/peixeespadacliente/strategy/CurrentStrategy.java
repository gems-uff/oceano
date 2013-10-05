/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.gems.peixeespadacliente.tool.PullUpMethods;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import br.uff.ic.gems.peixeespadacliente.tool.applier.RefactoringApplier;
import br.uff.ic.gems.peixeespadacliente.tool.factory.FactoryRefactoringTool;
import br.uff.ic.oceano.core.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import translation.Translate;

/**
 *
 * @author GEMS
 */
public class CurrentStrategy implements Strategy {

    @Override
    public void performRefactoring(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        try {
            for (String strRefactoring : agentPeixeEspada.getSequenceRefactorings()) {
                if (!agentPeixeEspada.isRunning()) {
                    return;
                }
                agentPeixeEspada.createNewMetricsRelatory(strRefactoring);
                agentPeixeEspada.appendMessage(translate.applying(strRefactoring));

                RefactoringTool refactoringTool = (RefactoringTool) FactoryRefactoringTool.getRefactoringTool(RefactoringTool.classMap.get(strRefactoring), agentPeixeEspada.getProjectVCS());

                applyRefactoring(agentPeixeEspada, refactoringTool);

                timeExhaustedMessage(agentPeixeEspada, strRefactoring);
            }
        } catch (Throwable throwable) {
            throw new ServiceException(throwable);
        } 
    }
    
    private void timeExhaustedMessage(LocalManagerAgent agentPeixeEspada, String refactoring) {
        Translate translate = Translate.getTranslate();
        if (agentPeixeEspada.isRunning()) {
            agentPeixeEspada.appendMessage(translate.refactoringCompleted(refactoring));
        } else {
            agentPeixeEspada.appendMessage(translate.refactoringNotCompleted(refactoring));
        }
    }
    
    private void applyRefactoring(LocalManagerAgent agentPeixeEspada, RefactoringTool refactoringTool) throws RefactoringException, ServiceException {
        Translate translate = Translate.getTranslate();
        int count = 1;
        try {
            long tempo = System.currentTimeMillis();
            List<Symptom> symptoms = refactoringTool.findAllSymptoms();
            if ((symptoms != null) && (!symptoms.isEmpty())) {
                agentPeixeEspada.appendMessage(translate.foundSymptoms(symptoms.size()));
                agentPeixeEspada.appendMessage(translate.milliseconds(System.currentTimeMillis() - tempo));
            }
            for (int i = 0; i < symptoms.size() && agentPeixeEspada.isRunning(); i++) {
                Symptom symptom = symptoms.get(i);

                agentPeixeEspada.appendMessage(translate.symptom(count++));
                try {
                    List<? extends Resolution> lista = symptom.generateResolutions(agentPeixeEspada, false);
                    List<Resolution> workingResolutions = new ArrayList<Resolution>();
                    for (Resolution resolution : lista) {
                        refactoringTool.revertMoDifications();
                        if (resolution.applyCalculateQA(agentPeixeEspada, null)) {
                            workingResolutions.add(resolution);
                        } else {
                            agentPeixeEspada.testMessage(1, translate.removed(resolution.toString()));
                        }
                    }
                    refactoringTool.revertMoDifications();
                    RefactoringApplier.applyBestCalculatedResolution(agentPeixeEspada, workingResolutions);

                } catch (Throwable refactoringException) {
                    agentPeixeEspada.printTestError(0, refactoringException);
                    refactoringException.printStackTrace();
                }
                refactoringTool.revertMoDifications();
                System.gc();
            }
        } catch (Exception ex) {
            throw new RefactoringException(ex);
        }
        System.gc();
    }
    
}
