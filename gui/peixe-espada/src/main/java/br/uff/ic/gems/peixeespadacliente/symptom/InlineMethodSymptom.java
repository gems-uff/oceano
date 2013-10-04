package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import java.util.List;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.InlineMethodResolution;
import br.uff.ic.gems.peixeespadacliente.tool.InlineMethods;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.ArrayList;
import java.util.Arrays;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;

/**
 *
 * @author João Felipe
 */
public class InlineMethodSymptom extends Symptom {

    private String methodQualifiedName;
    private String parentQualifiedName;
    private String methodName;
    private BinParameter[] binParameters;

    public InlineMethodSymptom(BinMethod method, InlineMethods refactoringTool) {
        super(refactoringTool);
        methodQualifiedName = method.getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(methodQualifiedName) == null) {
            methodQualifiedName = method.getParentType().getQualifiedName() + "." + method.getName();
        }
        binParameters = method.getParameters();
        
        parentQualifiedName = method.getParentType().getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(parentQualifiedName) == null) {
            parentQualifiedName = method.getParentType().getParentType().getQualifiedName() + "." + method.getParentType().getName();
        }

        methodName = method.getName();
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    public InlineMethods getInlineMethodsRefactoringTool() {
        return (InlineMethods) this.refactoringTool;
    }

    @Override
    public List<InlineMethodResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<InlineMethodResolution> result = new ArrayList(Arrays.asList(new InlineMethodResolution(this)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<InlineMethodResolution>();
        }
        return result;
    }

    @Override
    public String toString() {
        return "Inline Method: " + getMethodQualifiedName();
    }

    public String getMethodQualifiedName() {
        return methodQualifiedName;
    }
    public String getParentQualifiedName() {
        return parentQualifiedName;
    }

    public String getMethodName() {
        return methodName;
    }

    public BinParameter[] getBinParameters() {
        return binParameters;
    }

    @Override
    public String getDescription() {
        return "    " + this.toString();
    }
}
