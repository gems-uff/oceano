package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.InlineMethodResolution;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.InlineMethodSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.SourceConstruct;
import net.sf.refactorit.classmodel.statements.BinCITypesDefStatement;
import net.sf.refactorit.classmodel.statements.BinReturnStatement;
import net.sf.refactorit.classmodel.statements.BinStatement;
import net.sf.refactorit.query.SinglePointVisitor;
import net.sf.refactorit.query.usage.Finder;
import net.sf.refactorit.query.usage.InvocationData;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.inlinemethod.InlineMethod;
import net.sf.refactorit.test.refactorings.NullContext;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class InlineMethods extends AbstractRefactoringTool {

    private InlineMethod inliner = null;
    private BinMethod currentMethod = null;

    public InlineMethods(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    public boolean reloadRefactoring() {
        try {
            Finder.clearInvocationMap();
            inliner = new InlineMethod(new NullContext(getProject()), currentMethod);
            
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();
//        reloadEnv();

        InlineMethodSymptom inlineMethodSymptom = (InlineMethodSymptom) symptom;

        BinCIType cls = getProject().getTypeRefForSourceName(inlineMethodSymptom.getParentQualifiedName()).getBinCIType();

        BinMember member = cls.getDeclaredMethod(inlineMethodSymptom.getMethodName(), inlineMethodSymptom.getBinParameters());
        if (member == null) {
            member = getMethodManually(inlineMethodSymptom.getMethodName(), cls, inlineMethodSymptom.getBinParameters());
        }
        currentMethod = (BinMethod) member;

        return reloadRefactoring();
    }

    private boolean usedOnlyInReturn(List invocations) {
        for (Iterator iter = invocations.iterator(); iter.hasNext();) {
            InvocationData invocation = (InvocationData) iter.next();
            SourceConstruct inConstruct = invocation.getInConstruct();
            if (!(inConstruct.getParent() instanceof BinReturnStatement)) {
                return false;
            }
        }
        return true;
    }

    private boolean usedInDirectReturn(List invocations) {
        for (Iterator iter = invocations.iterator(); iter.hasNext();) {
            InvocationData invocation = (InvocationData) iter.next();
            SourceConstruct inConstruct = invocation.getInConstruct();
            if ((inConstruct.getParent() instanceof BinReturnStatement)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkReturnPoints(BinMethod method) {
        if (method.getBody().getStatements().length > 0) {
            final List deepReturns = new ArrayList(3);
            final BinStatement last = method.getBody().getStatements()[method.getBody().getStatements().length - 1];
            SinglePointVisitor visitor = new SinglePointVisitor() {

                int inner = 0;

                @Override
                public void onEnter(Object o) {
                    if (o instanceof BinCITypesDefStatement) {
                        inner++;
                    } else if (o instanceof BinReturnStatement) {
                        if ((inner == 0) && (o != last)) {
                            deepReturns.add(o);
                        }
                    }
                }

                @Override
                public void onLeave(Object o) {
                    if (o instanceof BinCITypesDefStatement) {
                        inner--;
                    }
                }
            };
            visitor.visit(method);
            if (deepReturns.size() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLastReturnStatement(BinMethod method) {
        try {
            final BinStatement last = method.getBody().getStatements()[method.getBody().getStatements().length - 1];
            if (!(last instanceof BinReturnStatement)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Symptom> findAllSymptoms() throws RefactoringException {
        if (!loadEnvironment()) {
            return null;
        }

        List<Symptom> result = new ArrayList<Symptom>();

        for (Object object : getProject().getDefinedTypes()) {
            BinCITypeRef binCITypeRef = (BinCITypeRef) object;
            BinCIType bcitr = binCITypeRef.getBinCIType();

            if (bcitr.isInterface()) {
                continue;
            }

            if (!bcitr.isFromCompilationUnit()) {
                continue;
            }

            if (!classShouldBeVerified(binCITypeRef)) {
                continue;
            }


            BinMethod[] methods = bcitr.getDeclaredMethods();
            for (BinMethod binMethod : methods) {
                if ((!binMethod.findOverrides().isEmpty()) || (!bcitr.getSubMethods(binMethod).isEmpty())) {
                    continue;
                }

                List invocations = Finder.getInvocations(binMethod);
                if (!usedOnlyInReturn(invocations) && !checkReturnPoints(binMethod)) {
                    continue;
                }
                if (usedInDirectReturn(invocations) && !checkLastReturnStatement(binMethod)) {
                    continue;
                }
                Symptom symptom = new InlineMethodSymptom(binMethod, this);
                result.add(symptom);
            }

            
        }
        return result;
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        inliner.setMethodDeclarationAction(((InlineMethodResolution) resolution).getMethodDeclarationAction());
        
        refactoring = inliner;
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            throw new RefactoringException(translate.notRefactored(status.getAllMessages()));
        }
        status.merge(checkUserInput());
        status.merge(inliner.apply());
        if (!status.isOk()) {
            throw new RefactoringException(translate.error(status.getAllMessages()));
        }
        System.out.println(status.getAllMessages());
        return !status.isErrorOrFatal();
    }
}
