package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.UseSuperTypeResolution;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import br.uff.ic.gems.peixeespadacliente.tool.UseSuperTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import net.sf.refactorit.classmodel.BinCIType;
/**
 *
 * @author João Felipe
 */
public class UseSuperTypeSymptom extends Symptom {

    private String classQualifiedName;
    private String className;

    public UseSuperTypeSymptom(BinCIType cls, UseSuperTypes refactoringTool) {
        super(refactoringTool);
        classQualifiedName = cls.getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(classQualifiedName) == null) {
            classQualifiedName = cls.getParentType().getQualifiedName() + "." + cls.getName();
        }
        className = cls.getName();
        
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    public UseSuperTypes getUseSuperTypesRefactoringTool() {
        return (UseSuperTypes) this.refactoringTool;
    }

    @Override
    public List<UseSuperTypeResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<UseSuperTypeResolution> result = new ArrayList(Arrays.asList(new UseSuperTypeResolution(this)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<UseSuperTypeResolution>();
        }
        return result;
    }

    @Override
    public String toString() {
        return "Use supertype " + classQualifiedName + " where is possible";
    }

    public String getClassQualifiedName() {
        return classQualifiedName;
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String getDescription() {
        return "    " + this.toString();
    }
}
