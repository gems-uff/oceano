package br.uff.ic.gems.peixeespadacliente.conflicts;

import br.uff.ic.gems.peixeespadacliente.resolution.PullPushResolution;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.exception.UnresovableConflictsException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushSymptom;
import br.uff.ic.gems.peixeespadacliente.tool.PullPushRefactoringTool;
import br.uff.ic.oceano.core.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.refactorings.conflicts.Conflict;
import net.sf.refactorit.refactorings.conflicts.resolution.ConflictResolution;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class ConflictResolver {

    private LocalManagerAgent agentPeixeEspada;
    private PullPushRefactoringTool pullPushRefactoringTool;
    private List<PullPushResolution> resolutions;

    private ConflictManager conflictManager;
    private List<ConflictWithResolution> anagrams;
    private StringBuilder stringBuilder;
    private boolean verify;

    public ConflictResolver(LocalManagerAgent agentPeixeEspada, PullPushRefactoringTool pullPushRefactoringTool, List<PullPushResolution> resolutions, boolean verify) throws RefactoringException {
        this.agentPeixeEspada = agentPeixeEspada;
        this.pullPushRefactoringTool = pullPushRefactoringTool;
        this.resolutions = resolutions;
        this.verify = verify;

        this.conflictManager = new ConflictManager(pullPushRefactoringTool.getResolver());
        this.anagrams = new ArrayList<ConflictWithResolution>();
        this.stringBuilder = new StringBuilder();
    }
    
    public static List<PullPushResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, PullPushSymptom symptom, boolean verify) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        List<PullPushResolution> result = new ArrayList<PullPushResolution>();
        PullPushRefactoringTool pullPushRefactoringTool = symptom.getPullPushRefactoringTool();
        try {
            pullPushRefactoringTool.prepareSymptom(symptom);
            agentPeixeEspada.testMessage(1, translate.moveMember(symptom.getMemberQualifiedName()));
            agentPeixeEspada.testMessage(1, translate.moveTo(symptom.getTargetQualifiedName()));
            ConflictResolver retorno = (new ConflictResolver(agentPeixeEspada, pullPushRefactoringTool, result, verify));
            retorno.resolveConflicts(new PullPushResolution(symptom));
        } catch (ServiceException ex) {
            agentPeixeEspada.testMessage(1, ex.getMessage());
        } catch (RefactoringException ex) {
            agentPeixeEspada.testMessage(1, ex.getMessage());
        } catch (UnresovableConflictsException ex) {
            agentPeixeEspada.testMessage(1, ex.getMessage());
        }
        pullPushRefactoringTool.revertMoDifications();
        agentPeixeEspada.appendMessage("");
        return result;
    }

    public void resolveConflicts(PullPushResolution currentResolution) throws RefactoringException, ServiceException, UnresovableConflictsException {
        boolean findedConflicts = false;
        while (updateConflicts()) {
            findedConflicts = true;
            showConflictCount();
            resolveAll();
            apply(currentResolution);
            showMessages();
        }
        if (!findedConflicts){
            anagrams = new ArrayList<ConflictWithResolution>();
            if (conflictManager.getUnresolvedConflictList().isEmpty()){
                apply(currentResolution);
            }else{
                throw new UnresovableConflictsException(
                        Translate.getTranslate().unresolvableConflicts());
            }
            showMessages();
        }

    }
    
    private static ConflictResolver resolve(LocalManagerAgent agentPeixeEspada, PullPushRefactoringTool pullPushRefactoringTool, List<PullPushResolution> resolutions, PullPushResolution currentResolution, boolean verify) throws RefactoringException, ServiceException, UnresovableConflictsException{
        ConflictResolver retorno = (new ConflictResolver(agentPeixeEspada, pullPushRefactoringTool, resolutions, verify));
        retorno.resolveConflicts(currentResolution);
        return retorno;
    }
    
    private boolean updateConflicts() throws RefactoringException, UnresovableConflictsException{
        if (anagrams == null){
            conflictManager.findResolvedConflicts();
            pullPushRefactoringTool.getResolver().resolveConflicts();
            conflictManager.findUnresolvedAndResolvedConflicts();
            
            if (conflictManager.isUnresovableConflictsExists()) {
                throw new UnresovableConflictsException(
                        Translate.getTranslate().unresolvableConflicts());
            }
            
            anagrams = new ArrayList<ConflictWithResolution>();
        }
        if (anagrams.isEmpty()){
            anagrams = conflictManager.prepareNextResolutionAnagram();

        }
        return (anagrams != null);
    }

    private void showConflictCount(){
        Translate.getTranslate().foundConflicts(
                stringBuilder, 
                conflictManager.getUnresolvedConflictList().size()
        );
    }

    private void resolveAll() throws UnresovableConflictsException{
        boolean resolvidos = true;

        for (ConflictWithResolution conflictWithResolution : anagrams) {
            ConflictResolution resolution = conflictWithResolution.getResolution();
            Conflict conflict = conflictWithResolution.getConflict();
            resolvidos = resolvidos && conflictWithResolution.resolveConflict();

            conflictManager.resolutionMessage(resolution, stringBuilder, conflict);
        }
        
        if (!resolvidos)
            throw new UnresovableConflictsException(Translate.getTranslate().resolutionFail());
    }

    private boolean findedNewConflicts() throws RefactoringException{
        Translate translate = Translate.getTranslate();
        pullPushRefactoringTool.getResolver().resolveConflicts();
        conflictManager.findUnresolvedAndResolvedConflicts();
        
        if (conflictManager.getUnresolvedConflictList().size() > 0){
            stringBuilder.append(translate.newConflicts());
            return true;
        }else{
            stringBuilder.append(translate.conflictsResolved());
            return false;
        }
    }

    private boolean appliedAndIsWorking(PullPushResolution newResolution) throws RefactoringException {
        return (pullPushRefactoringTool.applyCheckingPreAndPosCondictions(newResolution) && (pullPushRefactoringTool.isWorking()));
    }

    private void revertAndReloadEnviroment(PullPushResolution currentResolution) throws RefactoringException{
            pullPushRefactoringTool.revertMoDifications();
            pullPushRefactoringTool.reloadRefactoring();
            conflictManager.setResolver(pullPushRefactoringTool.getResolver());
            currentResolution.reloadResolutions(conflictManager, null);
            anagrams.clear();
    }

    private void showMessages(){
        agentPeixeEspada.testMessage(2, stringBuilder.toString());
        stringBuilder = new StringBuilder();
    }

    private void apply(PullPushResolution currentResolution) throws RefactoringException{
        Translate translate = Translate.getTranslate();
        try{
            PullPushResolution newResolution = new PullPushResolution(currentResolution, anagrams);

            if (findedNewConflicts()){
                showMessages();
                resolve(agentPeixeEspada, pullPushRefactoringTool, resolutions, newResolution, verify);
            } else if (!verify) {
                resolutions.add(newResolution);
//                stringBuilder.append("\n    As refatorações não foram aplicadas \n");
            } else if (appliedAndIsWorking(newResolution)) {
                stringBuilder.append(translate.appliedRefactoring());

                newResolution.calculateQA(agentPeixeEspada);

                resolutions.add(newResolution);
            } else {
                translate.failedToApply(stringBuilder, translate.failedToApplyMessage());
            }
        } catch (RefactoringException refactoringException) {
            translate.failedToApply(stringBuilder, ": (RefactoringException) ").append(refactoringException.getMessage());
        } catch (ServiceException serviceException) {
            translate.failedToApply(stringBuilder, ": (ServiceException) ").append(serviceException.getMessage());
        } catch (UnresovableConflictsException conflictsException){
            stringBuilder.append("\n").append(conflictsException.getMessage());
        }

        revertAndReloadEnviroment(currentResolution);
    }

    
}
