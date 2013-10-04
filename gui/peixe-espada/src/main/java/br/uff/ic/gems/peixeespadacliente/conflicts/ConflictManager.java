package br.uff.ic.gems.peixeespadacliente.conflicts;

import br.uff.ic.gems.peixeespadacliente.resolution.PullPushResolution;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.conflicts.ChangedFunctionalityConflict;
import net.sf.refactorit.refactorings.conflicts.Conflict;
import net.sf.refactorit.refactorings.conflicts.ConflictData;
import net.sf.refactorit.refactorings.conflicts.ConflictModel;
import net.sf.refactorit.refactorings.conflicts.ConflictResolver;
import net.sf.refactorit.refactorings.conflicts.DeleteOtherImplementersConflict;
import net.sf.refactorit.refactorings.conflicts.MRUpDownMemberConflict;
import net.sf.refactorit.refactorings.conflicts.MRUpMemberConflict;
import net.sf.refactorit.refactorings.conflicts.UnresolvableConflict;
import net.sf.refactorit.refactorings.conflicts.resolution.ConflictResolution;
import net.sf.refactorit.refactorings.conflicts.resolution.DeleteOtherImplementersResolution;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class ConflictManager {

    private ConflictResolver resolver;
    private List<RefactoringStatus.Entry> unresolvedConflictList;
    private List<RefactoringStatus.Entry> resolvedConflictList;
    private Map<Conflict, List> mapSugestion = new HashMap<Conflict, List>();
    private List<Conflict> conflictList = new ArrayList<Conflict>();
    private int[] resArray;

    public ConflictManager(ConflictResolver resolver) throws RefactoringException {
        this.resolver = resolver;
        this.findUnresolvedAndResolvedConflicts();
        this.findAllConflictsAndResolutions();
        this.makeConflictList();
    }

    public void setResolver(ConflictResolver resolver) {
        this.resolver = resolver;
    }

    public List<RefactoringStatus.Entry> getUnresolvedConflictList() {
        return unresolvedConflictList;
    }

    public final void findUnresolvedAndResolvedConflicts() throws RefactoringException {
        unresolvedConflictList = new ArrayList();
        resolvedConflictList = new ArrayList();
        ConflictModel conflictModel = new ConflictModel(resolver);
        List binMembersToMove = resolver.getBinMembersToMove();

        for (int i = 0, max = binMembersToMove.size(); i < max; i++) {
            ConflictData data = resolver.getConflictData(binMembersToMove.get(i));
            if (data.unresolvedConflictsExist()) {
                RefactoringStatus status = conflictModel.getUnresolvedConflictsStatus(data);
                if (status != null) {
                    List entries = status.getEntries();
                    unresolvedConflictList.add((RefactoringStatus.Entry) entries.get(0));
                }

            }
            if (data.resolvedConflictsExist()) {
                RefactoringStatus status = conflictModel.getResolvedConflictsStatus(data);
                if (status != null) {
                    List entries = status.getEntries();
                    resolvedConflictList.add((RefactoringStatus.Entry) entries.get(0));
                }
            }
        }
    }

    public final void findResolvedConflicts() throws RefactoringException {
        resolvedConflictList = new ArrayList();

        ConflictModel conflictModel = new ConflictModel(resolver);
        List binMembersToMove = resolver.getBinMembersToMove();

        for (int i = 0, max = binMembersToMove.size(); i < max; i++) {
            ConflictData data = resolver.getConflictData(binMembersToMove.get(i));
            if (data.resolvedConflictsExist()) {
                RefactoringStatus status = conflictModel.getResolvedConflictsStatus(
                        data);

                if (status == null) {
                    continue;
                }

                List entries = status.getEntries();
                resolvedConflictList.add((RefactoringStatus.Entry) entries.get(0));
            }
        }
    }

    public boolean isUnresovableConflictsExists() {
        for (RefactoringStatus.Entry unresolvedConflict : unresolvedConflictList) {
            Object entryBin = unresolvedConflict.getBin();
            if (!resolver.getConflictData(entryBin).unresolvableConflictsExist()) {
                Conflict conflict = findLastConflict(unresolvedConflict);
                List resolutionList = getResolutionsList(conflict);
                if (resolutionList.isEmpty()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public List<ConflictWithResolution> prepareNextResolutionAnagram() {
        if (incrementResArrayIndex(0)) {
            return readResArray();
        } else {
            return null;
        }
    }

    public void reloadResolutions(PullPushResolution sequence, StringBuilder builder) throws RefactoringException {
        int count = 0;
        while (sequence.getResolutions().size() > count) {
            findUnresolvedAndResolvedConflicts();
            findAllConflictsAndResolutions();
            List<Conflict> conflicts = new ArrayList<Conflict>();
            conflicts.clear();
            conflicts.addAll(mapSugestion.keySet());
            for (Conflict conflict : conflicts) {
                count++;
                ConflictResolution resolution;
                try {
                    PullPushConflictResolution pullPushResolution = sequence.getResolutions().get(conflict.getDescription());
                    resolution = getResolution(mapSugestion.get(conflict), pullPushResolution);

                } catch (Exception e) {
                    PullPushConflictResolution pullPushResolution = sequence.getResolutions().get(conflict.getDescription());
                    resolution = getResolution(mapSugestion.get(conflict), pullPushResolution);
                    throw new RefactoringException(e);
                }
                ConflictWithResolution conflictWithResolution = new ConflictWithResolution(conflict, resolution, resolver);
                conflictWithResolution.resolveConflict();
                resolutionMessage(resolution, builder, conflict);
            }

            resolver.resolveConflicts();
            findUnresolvedAndResolvedConflicts();
        }
        if (sequence.getResolutions().isEmpty()) {
            resolver.resolveConflicts();
            findUnresolvedAndResolvedConflicts();

        }
    }

    public void resolutionMessage(ConflictResolution resolution, StringBuilder builder, Conflict conflict) {
        Translate translate = Translate.getTranslate();
        if (conflict != null) {
            builder.append(translate.conflictSelected());
            translate.conflictDescription(builder, conflict.getDescription());
        }
        translate.resolutionDescription(builder, resolution.getDescription());
        List downMembers = resolution.getDownMembers();
        if (!downMembers.isEmpty()) {
            for (Object object : downMembers) {
                try {
                    Class classe = Class.forName(object.getClass().getName());
                    Method metodo = classe.getMethod("getQualifiedName", new Class[0]);
                    String mensagem = (String) metodo.invoke(object, new Object[0]);
                    translate.resolutionItem(builder, mensagem);
                } catch (Exception ex) {
                    System.out.println("Aqui");
                }

            }
        }

    }

    private List getResolutionsList(Conflict conflict) {
        List retorno = new ArrayList();
        if (conflict == null) {
        } else if (conflict instanceof UnresolvableConflict) {
        } else if (conflict instanceof ChangedFunctionalityConflict) {
            // conflict.setResolution(null);
//            retorno.add(conflict.getResolution());
        } else if (conflict instanceof MRUpMemberConflict) {
            retorno = ((MRUpMemberConflict) conflict).getPossibleResolutions();
        } else if (conflict instanceof DeleteOtherImplementersConflict) {
            retorno.add(new DeleteOtherImplementersResolution(((DeleteOtherImplementersConflict) conflict).getDownMembers()));
            //  conflict.setResolution(null);
        } else if (conflict instanceof MRUpDownMemberConflict) {
            retorno = ((MRUpDownMemberConflict) conflict).getPossibleResolutions();
        } else {
            //CreateOnlyDeclarationConflict, AddImplementationConflict,
            //OtherImplementersExistConflict, MakeStaticConflict,
            //SubstituteAbstractMethodConflict, MoveDependentConflict,
            //WeakAccessConflict, MockConflict*
            retorno.add(conflict.getResolution());
        }
        return retorno;
    }

    private Conflict findLastConflict(RefactoringStatus.Entry unresolved) {
        Conflict conflict = null;
        if (unresolved.getSubEntries() == null) {
            conflict = unresolved.getConflict();
        } else {
            for (Object subEntry : unresolved.getSubEntries()) {
                Conflict temp = findLastConflict((RefactoringStatus.Entry) subEntry);
                if (temp != null) {
                    conflict = temp;
                } else {
                    conflict = ((RefactoringStatus.Entry) subEntry).getConflict();
                }

                if (conflict != null) {
                    break;
                }
            }
        }
        return conflict;

    }

    private boolean findAllConflictsAndResolutions() {
        mapSugestion.clear();
        for (RefactoringStatus.Entry unresolvedConflict : unresolvedConflictList) {
            Object entryBin = unresolvedConflict.getBin();
            if (!resolver.getConflictData(entryBin).unresolvableConflictsExist()) {
                Conflict conflict = findLastConflict(unresolvedConflict);
                List resolutionList = getResolutionsList(conflict);
                if (resolutionList.isEmpty()) {
                    return false;
                } else {
                    mapSugestion.put(conflict, resolutionList);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private void makeConflictList() {
        conflictList.clear();
        conflictList.addAll(mapSugestion.keySet());
        resArray = new int[conflictList.size()];
        if (!conflictList.isEmpty()) {
            resArray[0] = -1;
        }
    }

    private List<ConflictWithResolution> readResArray() {
        List<ConflictWithResolution> result = new ArrayList<ConflictWithResolution>();
        for (int i = 0; i < resArray.length; i++) {
            Conflict conflict = conflictList.get(i);
            ConflictResolution resolution = (ConflictResolution) mapSugestion.get(conflict).get(resArray[i]);
            result.add(new ConflictWithResolution(conflict, resolution, resolver));
        }
        return result;
    }

    private boolean incrementResArrayIndex(int index) {
        try {
            resArray[index] = resArray[index] + 1;
            if (resArray[index] == mapSugestion.get(conflictList.get(index)).size()) {
                resArray[index] = 0;
                return incrementResArrayIndex(index + 1);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private ConflictResolution getResolution(List resolutions, PullPushConflictResolution conflictResolution) {
        for (Object r : resolutions) {
            if (conflictResolution.equals((ConflictResolution) r)) {
                return (ConflictResolution) r;
            }
        }
        return null;
    }
//  	MockConflict -> Não achei
//	UpMemberConflict:
//            CreateOnlyDeclarationConflict -> getResolution() -
//            ChangedFunctionalityConflict: getResolution() -
//                MethodInheritanceConflict: getResolution() =
//                    OverridesMethodsConflict -> getResolution() =
//                    OverridenMethodConflict -> getResolution() =
//            MRUpMemberConflict: -
//                DeclarationOfDefinitionConflict -> getPossibleResolutions =
//            UpDownMemberConflict:
//                AddImplementationConflict -> getResolution() -
//                DeleteOtherImplementersConflict -> new DeleteOtherImplementersResolution -
//                OtherImplementersExistConflict -> getResolution() -
//                MRUpDownMemberConflict: => getPossibleResolutions -
//                    MRUsedByConflict -> getPossibleResolutions * =
//                    MRUsesConflict -> getPossibleResolutions =
//                MakeStaticConflit -> getResolution() -
//                SubstituteAbstractMethodConflict -> getResolution() -
//                MoveDependentConflict -> getResolution() -
//                UnresolvableConflict -> null -
//                    ImportNotPossibleConflict =
//                    InstanceNotAccessibleConflict =
//                WeakAccessConflict -> getResolution() -
}
