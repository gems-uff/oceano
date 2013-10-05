package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushMethodSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinSourceTypeRef;

/**
 *
 * @author Heliomar, João Felipe
 */
public class PullUpMethods extends PullPushMethodRefactoringTool {

    public PullUpMethods(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    private boolean hasMethodInHierarchy(BinCITypeRef binCITypeRef, BinMember member) {
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

                for (BinCITypeRef subClass : listaSubClasses) {
                    if (!classShouldBeVerified(subClass)) {
                        continue;
                    }
                    for (BinMethod method : subClass.getBinCIType().getDeclaredMethods()) {

                        if (!hasMethodInHierarchy(typeRef, method)) {
                            Symptom symptom = new PullPushMethodSymptom(method, typeRef.getBinCIType(), this);
                            result.add(symptom);
                        }
                    }
                }
            }

        }
        return result;
    }
}
