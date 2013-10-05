package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushFieldSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.refactorings.pullpush.PullPush;
import net.sf.refactorit.test.refactorings.NullContext;

/**
 *
 * @author João Felipe
 */
public abstract class PullPushFieldRefactoringTool extends PullPushRefactoringTool {

    protected BinField currentField;

    public PullPushFieldRefactoringTool(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    @Override
    public boolean reloadRefactoring() {
        try {
            List<BinField> list = new ArrayList<BinField>(1);
            list.add(currentField);
            pullPush = new PullPush(new NullContext(getProject()), currentField.getParentType(), list);
            pullPush.getResolver().setTargetType(currentTarget);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();

        PullPushFieldSymptom pullPushFieldSymptom = (PullPushFieldSymptom) symptom;

        currentTarget = getProject().getTypeRefForSourceName(pullPushFieldSymptom.getTargetQualifiedName()).getBinCIType();
        BinCIType owner = getProject().getTypeRefForSourceName(pullPushFieldSymptom.getParentQualifiedName()).getBinCIType();

        BinMember member = owner.getDeclaredField(pullPushFieldSymptom.getMemberQualifiedName());
        if (member == null) {
            member = getFieldManually(pullPushFieldSymptom.getMemberName(), owner);
        }
        currentField = (BinField) member;

        return reloadRefactoring();
    }
}
