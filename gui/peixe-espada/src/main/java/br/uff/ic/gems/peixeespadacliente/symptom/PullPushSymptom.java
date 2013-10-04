package br.uff.ic.gems.peixeespadacliente.symptom;

import br.uff.ic.gems.peixeespadacliente.conflicts.ConflictResolver;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.resolution.PullPushResolution;
import br.uff.ic.gems.peixeespadacliente.tool.PullPushRefactoringTool;
import java.util.List;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinMember;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;

/**
 *
 * @author João Felipe
 */
public abstract class PullPushSymptom extends Symptom {

    private String targetQualifiedName;
    private String parentQualifiedName;
    private String memberName;
    private String targetName;

    public PullPushSymptom(BinMember member, BinCIType targetClass, PullPushRefactoringTool refactoringTool) {
        super(refactoringTool);
        targetQualifiedName = targetClass.getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(targetQualifiedName) == null) {
            targetQualifiedName = targetClass.getParentType().getQualifiedName() + "." + targetClass.getName();
        }

        parentQualifiedName = member.getParentType().getQualifiedName();
        if (refactoringTool.getProject().getTypeRefForSourceName(parentQualifiedName) == null) {
            parentQualifiedName = member.getParentType().getParentType().getQualifiedName() + "." + member.getParentType().getName();
        }

        targetName = targetClass.getName();
        memberName = member.getName();
    }

    @Override
    public RefactoringTool getRefactoringTool() {
        return this.refactoringTool;
    }

    public PullPushRefactoringTool getPullPushRefactoringTool() {
        return (PullPushRefactoringTool) this.refactoringTool;
    }

    @Override
    public List<PullPushResolution> generateResolutions(LocalManagerAgent agentPeixeEspada, boolean verify) throws RefactoringException {
        return ConflictResolver.generateResolutions(agentPeixeEspada, this, verify);
    }

    @Override
    public String toString() {
        return getMemberName() + " to " + getTargetName();
    }

    public String getTargetQualifiedName() {
        return targetQualifiedName;
    }

    public String getMemberQualifiedName() {
        return parentQualifiedName + "." + memberName;
    }

    public String getParentQualifiedName() {
        return parentQualifiedName;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getTargetName() {
        return targetName;
    }

    @Override
    public String getDescription() {
        return "    Member: " + this.getMemberQualifiedName() + "\n"
             + "    To: " + this.targetQualifiedName + "\n";
    }
}
