package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.EncapsulateSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinModifier;
import net.sf.refactorit.refactorings.PropertyNameUtil;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.encapsulatefield.EncapsulateField;
import net.sf.refactorit.test.refactorings.NullContext;
import translation.Translate;

/**
 *
 * @author Heliomar, João Felipe
 */
public class EncapsulateFields extends AbstractRefactoringTool {

    private BinField currentField;

    public EncapsulateFields(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();

        EncapsulateSymptom encapsulateSymptom = (EncapsulateSymptom) symptom;

        BinCIType owner = getProject().getTypeRefForSourceName(encapsulateSymptom.getParentQualifiedName()).getBinCIType();
        currentField = owner.getDeclaredField(encapsulateSymptom.getMemberName());

        return true;
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

            if (!classShouldBeVerified(binCITypeRef)) {
                continue;
            }

            int fim = bcitr.getDeclaredFields() != null ? bcitr.getDeclaredFields().length : 0;
            for (int i = 0; i < fim; i++) {
                BinField field = binCITypeRef.getBinCIType().getDeclaredFields()[i];
                if (field.getModifiers() <= BinModifier.PUBLIC) {
                    Symptom symptom = new EncapsulateSymptom(field, this);
                    result.add(symptom);
                }
            }
        }

        return result;
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        EncapsulateField encapsulator = new EncapsulateField(new NullContext(currentField.getProject()), currentField);
        refactoring = encapsulator;
//        SetupUtils.setup(project);
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            System.err.println(translate.notRefactored(status.getAllMessages()));
            return false;
        }
        String setterName = PropertyNameUtil.getDefaultSetterName(currentField);
        String getterName = PropertyNameUtil.getDefaultGetterName(currentField)[0];

        encapsulator.setGetterName(getterName);
        encapsulator.setSetterName(setterName);

        encapsulator.setUsages(encapsulator.getAllUsages());
        encapsulator.setFieldVisibility(BinModifier.PRIVATE);
        encapsulator.setEncapsulateRead(true);
        encapsulator.setEncapsulateWrite(true);

        status.merge(checkUserInput());
        status.merge(encapsulator.apply());
        if (!status.isOk()) {
            System.err.println(translate.error(status.getAllMessages()));
            return false;
        }
        System.out.println(status.getAllMessages());
        return true;
    }
}
