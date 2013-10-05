package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.symptom.PullPushMethodSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.HashSet;
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
public class PushDownMethods extends PullPushMethodRefactoringTool {

    public PushDownMethods(ProjectVCS projectVCS) {
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

    private boolean isInterfaceOrAbstract(BinCITypeRef subClass) {
        return (subClass.getBinType().isAbstract() || subClass.getBinType().isAbstract());
    }

    @Override
    public List<Symptom> findAllSymptoms() throws RefactoringException {
        if (!loadEnvironment()) {
            return null;
        }

        List<Symptom> result = new ArrayList<Symptom>();

        for (Object object : getProject().getDefinedTypes()) {

            BinCITypeRef superClassCandidate = (BinCITypeRef) object;
            BinCIType bcit = superClassCandidate.getBinCIType();

            if (!classShouldBeVerified(superClassCandidate)) {
                continue;
            }

            if (!bcit.isInterface()) {
                List<BinCITypeRef> listaSubClasses = superClassCandidate.getAllSubclasses();

                for (BinMethod binMethod : superClassCandidate.getBinCIType().getDeclaredMethods()) {
                    for (BinCITypeRef subClass : listaSubClasses) {
                        if (!classShouldBeVerified(subClass)) {
                            continue;
                        }
                        if ((binMethod.isAbstract() && isInterfaceOrAbstract(subClass)) || //Move metodo abstrato para classe abstrata ou interface
                                ((!binMethod.isAbstract()) && (!isInterfaceOrAbstract(subClass))) || //Move metodo não abstrato para classe não abstrata nem interface
                                ((!binMethod.isAbstract()) && (bcit.isAbstract()) && (subClass.getBinCIType().isAbstract())) || //Move metodo não abstrato de classe abstrata para classe abstrata
                                (false) //                           ((!binMethod.isOverriddenOrOverrides()))
                                ) {
                            if (!hasMemberInHierarchy(superClassCandidate, subClass, binMethod)) {
                                Symptom symptom = new PullPushMethodSymptom(binMethod, subClass.getBinCIType(), this);
                                result.add(symptom);
                            }
                        }
                    }
                }

            }
        }
        return result;
    }
}
