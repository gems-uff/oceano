package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import java.util.List;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.ExtractInterfaceResolution;
import br.uff.ic.gems.peixeespadacliente.tool.ExtractInterfaces;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.ArrayList;
import java.util.Arrays;
import net.sf.refactorit.classmodel.BinCIType;
/**
 *
 * @author João Felipe
 */
public class ExtractInterfaceSymptom extends Symptom {

    private String classQualifiedName;
    private String className;

    public ExtractInterfaceSymptom(BinCIType cls, ExtractInterfaces refactoringTool) {
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

    public ExtractInterfaces getExtractInterfacesRefactoringTool() {
        return (ExtractInterfaces) this.refactoringTool;
    }

    @Override
    public List<ExtractInterfaceResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<ExtractInterfaceResolution> result = new ArrayList(Arrays.asList(new ExtractInterfaceResolution(this, className)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<ExtractInterfaceResolution>();
        }
        return result;
    }

    @Override
    public String toString() {
        return "Extract " + getInterfaceName() + " from " + getClassQualifiedName();
    }

    public String getClassQualifiedName() {
        return classQualifiedName;
    }

    public String getInterfaceName() {
        return className+"Interface";
    }

    public String getClassName() {
        return className;
    }

    @Override
    public String getDescription() {
        return "    " + this.toString();
    }
}
