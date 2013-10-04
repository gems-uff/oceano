package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushFieldSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinSourceTypeRef;

/**
 *
 * @author João Felipe
 */
public class PullUpFields extends PullPushFieldRefactoringTool {

    public PullUpFields(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    private boolean hasFieldInHierarchy(BinCITypeRef binCITypeRef, BinMember member) {
        if (binCITypeRef.getBinCIType().hasMemberWithSignature(member) != null) {
            return true;
        }

        Set superClasses = binCITypeRef.getAllSupertypes();
        for (Object superClass : superClasses) {
            if (superClass instanceof BinCITypeRef) {
                if (((BinCITypeRef) superClass).getBinCIType().hasMemberWithSignature(member) != null) {
                    return true;
                }

            } else if (superClass instanceof BinSourceTypeRef) {
                if (((BinSourceTypeRef) superClass).getBinCIType().hasMemberWithSignature(member) != null) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<Symptom> findAllSymptoms() throws RefactoringException {
        if (!loadEnvironment()) {
            return null;
        }

        List<Symptom> result = new ArrayList<Symptom>();


        for (Object object : getProject().getDefinedTypes()) {

            BinCITypeRef typeRef = (BinCITypeRef) object;
            BinCIType bcit = typeRef.getBinCIType();

            if (!classShouldBeVerified(typeRef)) {
                continue;
            }

            if (!bcit.isInterface()) {
                List<BinCITypeRef> listaSubClasses = typeRef.getAllSubclasses();

                List<BinField> fields = new ArrayList<BinField>();

                for (BinCITypeRef subClass : listaSubClasses) {
                    if (!classShouldBeVerified(subClass)) {
                        continue;
                    }
                    for (BinField field : subClass.getBinCIType().getDeclaredFields()) {

                        if (!hasFieldInHierarchy(typeRef, field)) {
                            Symptom symptom = new PullPushFieldSymptom(field, typeRef.getBinCIType(), this);
                            result.add(symptom);
                        }
                    }

                }
            }
        }
        return result;
    }
}
