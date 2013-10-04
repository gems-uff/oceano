package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.CreateFactoryMethodResolution;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.CreateFactoryMethodSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.factorymethod.FactoryMethod;
import net.sf.refactorit.test.refactorings.NullContext;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class CreateFactoryMethods extends AbstractRefactoringTool {

    private FactoryMethod factorer = null;
    private BinCIType currentClass = null;
    private BinConstructor currentConstructor = null;

    public CreateFactoryMethods(ProjectVCS projectVCS) {
        super(projectVCS);
    }
    
    public boolean reloadRefactoring() {
        try {
            factorer = new FactoryMethod(currentConstructor, new NullContext(getProject()));
            factorer.setHostingClass((BinClass) currentClass);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();
//        reloadEnv();

        CreateFactoryMethodSymptom createFactoryMethodSymptom = (CreateFactoryMethodSymptom) symptom;

        currentClass = getProject().getTypeRefForSourceName(createFactoryMethodSymptom.getParentQualifiedName()).getBinCIType();

        BinClass cls = (BinClass) currentClass;
        BinMember member = cls.getConstructor(createFactoryMethodSymptom.getParametersTypes());
        if (member == null) {
            member = getConstructorManually(currentClass, createFactoryMethodSymptom.getBinParameters());
        }
        currentConstructor = (BinConstructor) member;

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
    
            if (bcitr.isInnerType() && !bcitr.isStatic()) {
                continue;
            }
            
            if (bcitr.isAbstract()) {
                continue;
            }
            
            if (!classShouldBeVerified(binCITypeRef)) {
                continue;
            }
            
            BinClass cls = (BinClass) bcitr;
            BinConstructor[] constructors = cls.getConstructors();
            for (BinConstructor binConstructor : constructors) {
                Symptom symptom = new CreateFactoryMethodSymptom(binConstructor, this);
                result.add(symptom);
            }
    
        }
        return result;
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        CreateFactoryMethodResolution cfmResolution = (CreateFactoryMethodResolution) resolution;
        factorer.setMethodName(cfmResolution.getMethodName());
        factorer.setOptimizeVisibility(cfmResolution.getOptimizeVisibility());
        refactoring = factorer;
        
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            throw new RefactoringException(translate.notRefactored(status.getAllMessages()));
        }
        status.merge(checkUserInput());
        status.merge(factorer.apply());
        if (!status.isOk()) {
            throw new RefactoringException(translate.error(status.getAllMessages()));
        }
        System.out.println(status.getAllMessages());
        return !status.isErrorOrFatal();
    }

    
}
