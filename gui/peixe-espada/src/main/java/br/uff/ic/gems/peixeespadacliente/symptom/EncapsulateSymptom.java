package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.EncapsulateResolution;
import br.uff.ic.gems.peixeespadacliente.tool.EncapsulateFields;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.refactorit.classmodel.BinField;

/**
 *
 * @author João Felipe
 */
public class EncapsulateSymptom extends Symptom{
    
    private String parentQualifiedName;
    private String memberName;

    public EncapsulateSymptom(BinField field, EncapsulateFields refactoringTool) {
        super(refactoringTool);
    
        parentQualifiedName = field.getParentType().getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(parentQualifiedName) == null) {
            parentQualifiedName = field.getParentType().getParentType().getQualifiedName() + "." + field.getParentType().getName();
        }

        memberName = field.getName();
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    @Override
    public List<EncapsulateResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<EncapsulateResolution> result = new ArrayList(Arrays.asList(new EncapsulateResolution(this)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<EncapsulateResolution>();
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "Encapsulate " + getMemberName();
    }

    public String getParentQualifiedName() {
        return parentQualifiedName;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberQualifiedName() {
        return parentQualifiedName + "." + memberName;
    }
    
    public EncapsulateFields getEncapsulateRefactoringTool() {
        return (EncapsulateFields) this.refactoringTool;
    }
    
    @Override
    public String getDescription() {
        return "    Encapsulating Field: " + this.getMemberQualifiedName() + "\n";
    }
    
}
