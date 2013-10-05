package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.List;

/**
 *
 * @author João Felipe
 */
public abstract class Symptom {
    protected RefactoringTool refactoringTool;
    
    protected Symptom(RefactoringTool refactoringTool) {
        this.refactoringTool = refactoringTool;
    }
    
    public abstract RefactoringTool getRefactoringTool();
    public abstract List<? extends Resolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException;
    public abstract String getDescription();
    
}
