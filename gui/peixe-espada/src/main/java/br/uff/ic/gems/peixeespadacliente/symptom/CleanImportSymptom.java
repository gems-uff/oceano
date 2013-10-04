package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.CleanImportResolution;
import br.uff.ic.gems.peixeespadacliente.tool.CleanImports;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.parser.ASTImpl;
import net.sf.refactorit.source.edit.CompoundASTImpl;

/**
 *
 * @author João Felipe
 */
public class CleanImportSymptom extends Symptom {

    private String compilationUnitPath;
    private String importText;

    public CleanImportSymptom(ASTImpl aSTImpl, CompilationUnit compilationUnit, CleanImports refactoringTool) {
        super(refactoringTool);

        CompoundASTImpl node = new CompoundASTImpl(aSTImpl.getParent());

        this.importText = node.getText();
        this.compilationUnitPath = compilationUnit.getDisplayPath();
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    @Override
    public List<CleanImportResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        List<CleanImportResolution> result = new ArrayList(Arrays.asList(new CleanImportResolution(this)));
        if (verify && !result.get(0).applyWorking(null)){
            return new ArrayList<CleanImportResolution>();
        }
        return result;
    }

    @Override
    public String toString() {
        return "CleanImport " + importText;
    }

    public String getCompilationUnitPath() {
        return compilationUnitPath;
    }

    public String getImportText() {
        return importText;
    }

    public CleanImports getEncapsulateRefactoringTool() {
        return (CleanImports) this.refactoringTool;
    }

    @Override
    public String getDescription() {
        return "    Removing unused import from " + this.compilationUnitPath + "\n"
                + "    -" + importText;
    }
}
