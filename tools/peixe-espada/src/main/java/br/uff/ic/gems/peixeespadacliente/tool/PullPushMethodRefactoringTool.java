package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushMethodSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.refactorings.pullpush.PullPush;
import net.sf.refactorit.test.refactorings.NullContext;

/**
 *
 * @author João Felipe
 */
public abstract class PullPushMethodRefactoringTool extends PullPushRefactoringTool {

    protected BinMethod currentMethod;

    public PullPushMethodRefactoringTool(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    @Override
    public boolean reloadRefactoring() {
        try {
            List<BinMethod> list = new ArrayList<BinMethod>(1);
            list.add(currentMethod);
            pullPush = new PullPush(new NullContext(getProject()), currentMethod.getParentType(), list);
            pullPush.getResolver().setTargetType(currentTarget);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();
//        reloadEnv();

        PullPushMethodSymptom pullPushMethodSymptom = (PullPushMethodSymptom) symptom;

        currentTarget = getProject().getTypeRefForSourceName(pullPushMethodSymptom.getTargetQualifiedName()).getBinCIType();
        BinCIType owner = getProject().getTypeRefForSourceName(pullPushMethodSymptom.getParentQualifiedName()).getBinCIType();

        BinMember member = owner.getDeclaredMethod(pullPushMethodSymptom.getMemberName(), pullPushMethodSymptom.getBinParameters());
        if (member == null) {
            member = getMethodManually(pullPushMethodSymptom.getMemberName(), owner, pullPushMethodSymptom.getBinParameters());
        }
        currentMethod = (BinMethod) member;

        return reloadRefactoring();
    }
}
