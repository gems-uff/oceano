package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushFieldSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinSourceTypeRef;

/**
 *
 * @author Heliomar, João Felipe
 */
public class PushDownFields extends PullPushFieldRefactoringTool {

    public PushDownFields(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    private boolean hasMemberInHierarchy(BinCITypeRef superClass, BinCITypeRef subClass, BinMember member) {
        if (subClass.getBinCIType().hasMemberWithSignature(member) != null) {
            return true;
        }

        Set middleClasses = subClass.getAllSupertypes(); //Encontra todos superiores da classe destino
        Set subClasses = new HashSet(superClass.getAllSubclasses()); //Encontra todos os inferiores entre o destino e a classe que possui o membro
        middleClasses.retainAll(subClasses); //Interseção = Todos entre a classe que possui e o destino


        return hasMemberInHierarchySet(middleClasses, member) || hasMemberInHierarchySet(superClass.getAllSupertypes(), member);
    }

    private boolean hasMemberInHierarchySet(Set classes, BinMember member) {

        for (Object sClass : classes) {
            if (sClass instanceof BinCITypeRef) {
                if (((BinCITypeRef) sClass).getBinCIType().hasMemberWithSignature(member) != null) {
                    return true;
                }
            } else if (sClass instanceof BinSourceTypeRef) {
                if (((BinSourceTypeRef) sClass).getBinCIType().hasMemberWithSignature(member) != null) {
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

            BinCITypeRef superClassCandidate = (BinCITypeRef) object;

            if (!classShouldBeVerified(superClassCandidate)) {
                continue;
            }

//            if (!bcit.isInterface()) {
            List<BinCITypeRef> listaSubClasses = superClassCandidate.getAllSubclasses();
            for (BinField binField : superClassCandidate.getBinCIType().getDeclaredFields()) {
                for (BinCITypeRef subClass : listaSubClasses) {
                    if (!classShouldBeVerified(subClass)) {
                        continue;
                    }
                    if (!hasMemberInHierarchy(superClassCandidate, subClass, binField)) {
                        Symptom symptom = new PullPushFieldSymptom(binField, subClass.getBinCIType(), this);
                        result.add(symptom);
                    }
                }
            }
        }
        return result;
    }
}
