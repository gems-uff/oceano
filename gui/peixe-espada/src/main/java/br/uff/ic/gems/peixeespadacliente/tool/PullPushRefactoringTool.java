package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.conflicts.ConflictResolver;
import net.sf.refactorit.refactorings.pullpush.PullPush;
import translation.Translate;

/**
 *
 * @author Joao Felipe
 */
public abstract class PullPushRefactoringTool extends AbstractRefactoringTool {

    protected PullPush pullPush;
    protected BinCIType currentTarget;

    public PullPushRefactoringTool(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    abstract public boolean reloadRefactoring();

    public PullPush getPullPush() {
        return pullPush;
    }

    public ConflictResolver getResolver() {
        return pullPush.getResolver();
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        refactoring = pullPush;
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            throw new RefactoringException(translate.notRefactored(status.getAllMessages()));
        }
        status.merge(checkUserInput());
        status.merge(pullPush.apply());
        if (!status.isOk()) {
            throw new RefactoringException(translate.error(status.getAllMessages()));
        }
        System.out.println(status.getAllMessages());
        return !status.isErrorOrFatal();
    }

    
}
