package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.tool.PullPushRefactoringTool;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;

/**
 *
 * @author João Felipe
 */
public class PullPushMethodSymptom extends PullPushSymptom {
    
    private BinParameter[] binParameters;
    
    public PullPushMethodSymptom(BinMethod method, BinCIType targetClass, PullPushRefactoringTool refactoringTool) {
        super(method, targetClass, refactoringTool);
        binParameters = method.getParameters();
    }

    public BinParameter[] getBinParameters() {
        return binParameters;
    }
    
    
}
