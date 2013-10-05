package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.tool.PullPushRefactoringTool;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinField;

/**
 *
 * @author João Felipe
 */
public class PullPushFieldSymptom extends PullPushSymptom {
    
    public PullPushFieldSymptom(BinField field, BinCIType targetClass, PullPushRefactoringTool refactoringTool) {
        super(field, targetClass, refactoringTool);
    }

}
