package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.CleanImportSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.parser.ASTImpl;
import net.sf.refactorit.query.usage.InvocationData;
import net.sf.refactorit.refactorings.ImportUtils;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.source.edit.CompoundASTImpl;
import net.sf.refactorit.source.edit.StringEraser;
import net.sf.refactorit.transformations.TransformationManager;
import translation.Translate;

/**
 *
 * @author Heliomar, João Felipe
 */
public class CleanImports extends AbstractRefactoringTool {

    private InvocationData currentInvocationData = null;

    public CleanImports(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();

        CleanImportSymptom cleanImportSymptom = (CleanImportSymptom) symptom;
        CompilationUnit compilationUnit = getProject().getCompilationUnitForName(cleanImportSymptom.getCompilationUnitPath());
        ASTImpl[] unusedImports = ImportUtils.listUnusedImports(compilationUnit);
        for (ASTImpl aSTImpl : unusedImports) {
            CompoundASTImpl node = new CompoundASTImpl(aSTImpl.getParent());
            if (node.getText().equals(cleanImportSymptom.getImportText())) {
                currentInvocationData = new InvocationData(null, compilationUnit, aSTImpl);
            }
        }
        return true;
    }

    @Override
    public List<Symptom> findAllSymptoms() throws RefactoringException {
        if (!loadEnvironment()) {
            return null;
        }

        List<Symptom> result = new ArrayList<Symptom>();

        List<CompilationUnit> compilationUnits = getProject().getCompilationUnits();

        for (int i = 0, max = compilationUnits.size(); i < max; i++) {
            CompilationUnit compilationUnit = compilationUnits.get(i);
            ASTImpl[] list = ImportUtils.listUnusedImports(compilationUnit);
            for (ASTImpl aSTImpl : list) {
                Symptom symptom = new CleanImportSymptom(aSTImpl, compilationUnit, this);
                result.add(symptom);
            }
        }

        return result;
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        StringBuilder stringBuilder = new StringBuilder();
        TransformationManager manager = new TransformationManager(null);

        CompilationUnit sf = currentInvocationData.getCompilationUnit();
        ASTImpl importNode = currentInvocationData.getWhereAst();

        CompoundASTImpl node = new CompoundASTImpl(importNode.getParent());
        final StringEraser eraser = new StringEraser(sf, node, true);
        stringBuilder.append("\n        -").append(node.getText());

        manager.add(eraser);


        RefactoringStatus status = manager.performTransformations();
        if (status.isErrorOrFatal() || !status.isOk()) {
            stringBuilder.append(Translate.getTranslate().notRefactored(status.getAllMessages()));
            return false;
        }
        return true;
    }
}
