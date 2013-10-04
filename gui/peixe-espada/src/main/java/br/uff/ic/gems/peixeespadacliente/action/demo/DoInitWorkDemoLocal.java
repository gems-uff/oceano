package br.uff.ic.gems.peixeespadacliente.action.demo;

import br.uff.ic.gems.peixeespadacliente.action.*;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import br.uff.ic.oceano.core.exception.ServiceException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoInitWorkDemoLocal extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        
        agentPeixeEspada.appendMessage(translate.startingTestPlan());

        List<String> refactorings = new ArrayList<String>(Arrays.asList(
                RefactoringTool.PULL_UP_METHODS,
//                RefactoringTool.PULL_UP_FIELDS,
//                RefactoringTool.PUSH_DOWN_METHODS,
//                RefactoringTool.PUSH_DOWN_FIELDS,
//                RefactoringTool.ENCAPSULATE_FIELDS,
//                RefactoringTool.CLEAN_IMPORTS,
//                RefactoringTool.ADD_DELEGATE_METHODS,
//                RefactoringTool.EXTRACT_INTERFACES,
//                RefactoringTool.USE_SUPER_TYPES,
//                RefactoringTool.CREATE_FACTORY_METHODS,
//                RefactoringTool.INLINE_METHODS,
                "<<FIM>>" // Linha usada para permitir deixar "," sobrando na lista. Ela é removida, não adicione nada após 
          ));
        refactorings.remove(refactorings.size()-1);
        agentPeixeEspada.appendMessage(translate.suggestedRefactorings(refactorings+""));
        agentPeixeEspada.setSequenceRefactorings(refactorings);
        
        agentPeixeEspada.setStatus(LocalManagerAgent.STATUS_WORKING);
        agentPeixeEspada.getOutput().fieldStatus.setText(LocalManagerAgent.STATUS_WORKING);
        return agentPeixeEspada;
    }
}
