package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.gems.peixeespadacliente.symptom.UseSuperTypeSymptom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.usesupertype.UsageInfoCollector;
import net.sf.refactorit.refactorings.usesupertype.UsageInfoCollector.ConvertResult;
import net.sf.refactorit.refactorings.usesupertype.UseSuperTypeRefactoring;
import net.sf.refactorit.refactorings.usesupertype.UseSuperTypeUtil;
import net.sf.refactorit.test.refactorings.NullContext;
import net.sf.refactorit.utils.ProgressShower;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class UseSuperTypes extends AbstractRefactoringTool {

    private UseSuperTypeRefactoring useSuperType = null;
    private BinTypeRef currentClassRef = null;
    private BinCIType currentClass = null;

    public UseSuperTypes(ProjectVCS projectVCS) {
        super(projectVCS);
    }

    public boolean reloadRefactoring() {
        try {
            useSuperType = new UseSuperTypeRefactoring(currentClass, new NullContext(getProject()));
            useSuperType.setSupertype(currentClassRef);
            useSuperType.setSubtypes(getAllSubtypes(currentClassRef));

            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();
//        reloadEnv();

        UseSuperTypeSymptom useSuperTypeSymptom = (UseSuperTypeSymptom) symptom;

        currentClassRef = getProject().getTypeRefForSourceName(useSuperTypeSymptom.getClassQualifiedName());
        currentClass = currentClassRef.getBinCIType();

        return reloadRefactoring();
    }

    private List getAllSubtypes(BinTypeRef ref) {
        List subtypes = new ArrayList(UseSuperTypeUtil.getAllSubtypes(ref));
        for (Iterator iter = subtypes.iterator(); iter.hasNext();) {
            // don't show anonymous types
            if (((BinTypeRef) iter.next()).getBinCIType().isAnonymous()) {
                iter.remove();
            }
        }
        Collections.sort(subtypes, BinTypeRef.QualifiedNameSorter.getInstance());

        return subtypes;
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

            List subtypes = getAllSubtypes(binCITypeRef);

            if (subtypes.isEmpty()) {
                continue;
            }

            List searchTarget = new ArrayList();
            for (int i = 0; i < subtypes.size(); ++i) {
                BinTypeRef type = (BinTypeRef) subtypes.get(i);
                searchTarget.add(type.getBinCIType());
            }
            UsageInfoCollector usgCollector = new UsageInfoCollector(searchTarget);
            usgCollector.collectUsages(new ProgressShower(0, 100));

            ConvertResult convertResult = usgCollector.computeConvertResult(bcitr);
            Set usagesToChange = convertResult.getUsagesToChange();

            if (usagesToChange.isEmpty()) {
                continue;
            }


            Symptom symptom = new UseSuperTypeSymptom(bcitr, this);
            result.add(symptom);
        }
        return result;
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        refactoring = useSuperType;
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            throw new RefactoringException(translate.notRefactored(status.getAllMessages()));
        }
        status.merge(checkUserInput());
        status.merge(useSuperType.apply());
        if (!status.isOk()) {
            throw new RefactoringException(translate.error(status.getAllMessages()));
        }
        System.out.println(status.getAllMessages());
        return !status.isErrorOrFatal();
    }
}
