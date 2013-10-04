package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import java.util.List;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.CreateFactoryMethodResolution;
import br.uff.ic.gems.peixeespadacliente.tool.CreateFactoryMethods;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.ArrayList;
import java.util.Arrays;
import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.classmodel.BinTypeRef;

/**
 *
 * @author João Felipe
 */
public class CreateFactoryMethodSymptom extends Symptom {

    private String constructorQualifiedName;
    private String parentQualifiedName;
    private BinParameter[] binParameters;
    private String className;
    
    public CreateFactoryMethodSymptom(BinConstructor constructor, CreateFactoryMethods refactoringTool) {
        super(refactoringTool);

        constructorQualifiedName = constructor.getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(constructorQualifiedName) == null) {
            constructorQualifiedName = constructor.getParentType().getQualifiedName() + "." + constructor.getName();
        }
        binParameters = constructor.getParameters();
        
        parentQualifiedName = constructor.getParentType().getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(parentQualifiedName) == null) {
            parentQualifiedName = constructor.getParentType().getParentType().getQualifiedName() + "." + constructor.getParentType().getName();
        }
        
        className = constructor.getParentType().getName();
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    public CreateFactoryMethods getCreateFactoryMethodsRefactoringTool() {
        return (CreateFactoryMethods) this.refactoringTool;
    }

    @Override
    public List<CreateFactoryMethodResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<CreateFactoryMethodResolution> result = new ArrayList(Arrays.asList(new CreateFactoryMethodResolution(this, className)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<CreateFactoryMethodResolution>();
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "Create Factory Method for " + parentQualifiedName + "(";
        for (BinParameter binParameter : binParameters) {
            result += binParameter.getText()+",";
        }
        if (binParameters.length == 0) {
            result += "."; //será apagado com o substring;
        }
        return result.substring(0, result.length()-1) + ")";
    }

    public String getConstructorQualifiedName() {
        return constructorQualifiedName;
    }

    public String getParentQualifiedName() {
        return parentQualifiedName;
    }

    public BinParameter[] getBinParameters() {
        return binParameters;
    }
    
    public BinTypeRef[] getParametersTypes() {
        BinTypeRef[] result = new BinTypeRef[binParameters.length];
        for (int i = 0; i < binParameters.length; i++) {
            BinParameter binParameter = binParameters[i];
            result[i] = binParameter.getTypeRef();
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "    " + this.toString();
    }

}
