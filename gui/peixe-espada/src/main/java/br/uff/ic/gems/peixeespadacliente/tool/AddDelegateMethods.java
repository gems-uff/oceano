package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.AddDelegateMethodSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.delegate.AddDelegatesModel;
import net.sf.refactorit.refactorings.delegate.AddDelegatesRefactoring;
import net.sf.refactorit.test.refactorings.NullContext;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class AddDelegateMethods extends AbstractRefactoringTool {

    /* O refactorit apenas cria o metodo delegado, não refatora os lugares que deveriam usá-lo, nem procura esses lugares =/ */
    
    private AddDelegatesRefactoring addDelegatesRefactoring = null;
    private BinCIType currentClass = null;
    private BinField currentField = null;
    private BinMethod currentMethod = null;

    public AddDelegateMethods(ProjectVCS projectVCS) {
        super(projectVCS);
    }
    
    public boolean reloadRefactoring() {
        try {
            addDelegatesRefactoring = new AddDelegatesRefactoring(new NullContext(getProject()), currentClass);
            addDelegatesRefactoring.setModel(new AddDelegatesModel((BinClass) currentClass, new ArrayList()) { 
                @Override
                  public Map getSelectedMap() {
                    Map result = new HashMap();
                    result.put(currentField, Arrays.asList(currentMethod));    
                    return result;
                  }
            });
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();
//        reloadEnv();

        AddDelegateMethodSymptom addDelegateMethodSymptom = (AddDelegateMethodSymptom) symptom;

        currentClass = getProject().getTypeRefForSourceName(addDelegateMethodSymptom.getParentQualifiedName()).getBinCIType();

        BinMember member = currentClass.getDeclaredField(addDelegateMethodSymptom.getFieldQualifiedName());
        if (member == null) {
            member = getFieldManually(addDelegateMethodSymptom.getFieldName(), currentClass);
        }
        currentField = (BinField) member;

        member = currentField.getTypeRef().getBinCIType().getDeclaredMethod(addDelegateMethodSymptom.getMethodName(), addDelegateMethodSymptom.getBinParameters());
        if (member == null) {
            member = getMethodManually(addDelegateMethodSymptom.getMethodName(), currentField.getTypeRef().getBinCIType(), addDelegateMethodSymptom.getBinParameters());
        }
        currentMethod = (BinMethod) member;
        
        return reloadRefactoring();
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
            
            if (!classShouldBeVerified(binCITypeRef)) {
                continue;
            }
            
            BinClass cls = (BinClass) bcitr;
            BinField[] fields = AddDelegatesRefactoring.getDelegateFields(cls);
            
            for (BinField binField : fields) {
                BinTypeRef fieldClass = binField.getTypeRef();
                if (fieldClass.isPrimitiveType() || fieldClass.isArray() || fieldClass.isString() || fieldClass.getBinCIType().getQualifiedName().startsWith("java.")) {
                    continue;
                }
                List delegatesList = AddDelegatesRefactoring.createDelegateMethodsList(binField);

                Collections.sort(delegatesList, new Comparator() {
                    @Override
                    public int compare(Object obj1, Object obj2) {
                        return ((BinMethod) obj1).getName()
                            .compareTo(((BinMethod) obj2).getName());
                    }
                });

                if (delegatesList.isEmpty()) {
                    continue;
                }
                
                for (Object method : delegatesList) {
                    BinMethod binMethod = (BinMethod) method;
                    if (binMethod.getName().equals("toString") || binMethod.getName().equals("hashCode") || binMethod.getName().equals("equals")) {
                        continue;
                    }
                    Symptom symptom = new AddDelegateMethodSymptom(binField, binMethod, this);
                    result.add(symptom);
                }
            }
        }
        return result;
    }
    
    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        refactoring = addDelegatesRefactoring;
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            throw new RefactoringException(translate.notRefactored(status.getAllMessages()));
        }
        status.merge(checkUserInput());
        status.merge(addDelegatesRefactoring.apply());
        if (!status.isOk()) {
            throw new RefactoringException(translate.error(status.getAllMessages()));
        }
        System.out.println(status.getAllMessages());
        return !status.isErrorOrFatal();
    }

    
}
