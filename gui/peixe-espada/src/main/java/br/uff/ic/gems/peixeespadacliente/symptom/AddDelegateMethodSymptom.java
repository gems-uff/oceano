package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import java.util.List;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.AddDelegateMethodResolution;
import br.uff.ic.gems.peixeespadacliente.tool.AddDelegateMethods;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.ArrayList;
import java.util.Arrays;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;

/**
 *
 * @author João Felipe
 */
public class AddDelegateMethodSymptom extends Symptom {

    private String methodQualifiedName;
    private String parentQualifiedName;
    private String methodName;
    private String fieldName;
    private BinParameter[] binParameters;

    public AddDelegateMethodSymptom(BinField field, BinMethod method, AddDelegateMethods refactoringTool) {
        super(refactoringTool);
        methodQualifiedName = method.getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(methodQualifiedName) == null) {
            methodQualifiedName = method.getParentType().getQualifiedName() + "." + method.getName();
        }
        binParameters = method.getParameters();
        
        parentQualifiedName = field.getParentType().getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(parentQualifiedName) == null) {
            parentQualifiedName = field.getParentType().getParentType().getQualifiedName() + "." + field.getParentType().getName();
        }

        methodName = method.getName();
        fieldName = field.getName();
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    public AddDelegateMethods getAddDelegateMethodsRefactoringTool() {
        return (AddDelegateMethods) this.refactoringTool;
    }

    @Override
    public List<AddDelegateMethodResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<AddDelegateMethodResolution> result = new ArrayList(Arrays.asList(new AddDelegateMethodResolution(this)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<AddDelegateMethodResolution>();
        }
        return result;
    }

    @Override
    public String toString() {
        return "Delegate Method: " + getFieldQualifiedName() + "." + methodName;
    }

    public String getMethodQualifiedName() {
        return methodQualifiedName;
    }

    public String getFieldQualifiedName() {
        return parentQualifiedName + "." + fieldName;
    }

    public String getParentQualifiedName() {
        return parentQualifiedName;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getFieldName() {
        return fieldName;
    }
    
    public BinParameter[] getBinParameters() {
        return binParameters;
    }

    @Override
    public String getDescription() {
        return "    " + this.toString();
    }
}
