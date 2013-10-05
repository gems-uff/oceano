package br.uff.ic.gems.peixeespadacliente.tool;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.resolution.ExtractInterfaceResolution;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.ExtractInterfaceSymptom;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinCITypeRef;
import net.sf.refactorit.classmodel.BinField;
import net.sf.refactorit.classmodel.BinMember;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.refactorings.RefactoringStatus;
import net.sf.refactorit.refactorings.extractsuper.ExtractSuper;
import net.sf.refactorit.refactorings.usesupertype.UseSuperTypeRefactoring;
import net.sf.refactorit.test.refactorings.NullContext;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class ExtractInterfaces extends AbstractRefactoringTool {

    private ExtractSuper extractor = null;
    private BinTypeRef currentClassRef = null;
    private BinCIType currentClass = null;

    public ExtractInterfaces(ProjectVCS projectVCS) {
        super(projectVCS);
    }
    
    public boolean reloadRefactoring() {
        try {
            extractor = new ExtractSuper(new NullContext(getProject()), currentClassRef);
            
            extractor.setNewPackageName(currentClass.getPackage().getQualifiedName());
            
            List<BinMember> extractableMembers = interfaceExtractableMembers(currentClass);
            List membersToExtract = new ArrayList();
            Set abstractMethods = new HashSet();
            for (BinMember binMember : extractableMembers) {
                membersToExtract.add(binMember);
                abstractMethods.add(binMember);
            }
            this.extractor.setMembersToExtract(membersToExtract);
            this.extractor.setExplicitlyAbstractMethods(abstractMethods);
            
            
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean prepareSymptom(Symptom symptom) throws RefactoringException {
        loadEnvironment();
//        reloadEnv();

        ExtractInterfaceSymptom extractInterfaceSymptom = (ExtractInterfaceSymptom) symptom;

        currentClassRef = getProject().getTypeRefForSourceName(extractInterfaceSymptom.getClassQualifiedName());
        currentClass = currentClassRef.getBinCIType();

        return reloadRefactoring();
    }

    private List<BinMember> interfaceExtractableMembers(BinCIType bcitr) {
        List<BinMember> result = new ArrayList<BinMember>();
        List members = new ArrayList();
        members.addAll(Arrays.asList(bcitr.getDeclaredFields()));
        members.addAll(Arrays.asList(bcitr.getDeclaredMethods()));
        // searching for at least one member satisfying our conditions
        for (int i = 0, max = members.size(); i < max; i++) {
            final BinMember member = (BinMember) members.get(i);
            // members must be public
            if (!member.isPublic()) {
                continue;
            }
            // fields should be public static final
            if (member instanceof BinField) {
                if (!member.isFinal() || !member.isStatic()) {
                    continue;
                }
            }
            //
            result.add(member);
        }

        return result;
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
            
            if (interfaceExtractableMembers(bcitr).isEmpty()) {
                continue;
            }
           
            Symptom symptom = new ExtractInterfaceSymptom(bcitr, this);
            result.add(symptom);
        }
        return result;
    }

    @Override
    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException {
        Translate translate = Translate.getTranslate();
        ExtractInterfaceResolution eiResolution = (ExtractInterfaceResolution) resolution;
        extractor.setNewTypeName(eiResolution.getNewTypeName());
        extractor.setExtractClass(eiResolution.getExtractClass()); 
        extractor.setForceExtractMethodsAbstract(eiResolution.getForceExtractMethodsAbstract());
        extractor.setExtractWithOldName(eiResolution.getExtractWithOldName());
        
        refactoring = extractor;
        RefactoringStatus status = checkPreconditions();
        if (status.isErrorOrFatal()) {
            throw new RefactoringException(translate.notRefactored(status.getAllMessages()));
        }
        status.merge(checkUserInput());
        status.merge(extractor.apply());
        if (!status.isOk()) {
            throw new RefactoringException(translate.error(status.getAllMessages()));
        }
        System.out.println(status.getAllMessages());
        return !status.isErrorOrFatal() && useSuperTypeWhereIsPossible(status); 
//        return !status.isErrorOrFatal(); 
        /* Talvez usar o método useSuperTypeWhereIsPossible dificulte a aplicação com sucesso */
    }
    
    public boolean useSuperTypeWhereIsPossible(RefactoringStatus status) {
        loadEnvironment();
//        context.rebuildAndUpdateEnvironment();
        
        String supertypeName = extractor.getSupertypeQualifiedName();
        String subtypeName = extractor.getSubtypeQualifiedName();

        BinTypeRef subtypeRef = getProject().findTypeRefForName(subtypeName);
        BinTypeRef supertypeRef = getProject().findTypeRefForName(supertypeName);

        if (subtypeRef == null) {
          //DialogManager.getInstance().showCustomError(context, "Type " + subtypeName + " not found after rebuild");
            return false;
        }

        if (supertypeRef == null) {
          //DialogManager.getInstance().showCustomError(context, "Type " + supertypeName + " not found after rebuild");
            return false;
        }

        final UseSuperTypeRefactoring ust = new UseSuperTypeRefactoring(
            supertypeRef.getBinCIType(),
            IDEController.getInstance().createProjectContext()){
          
            @Override
            public String getDescription() {
                return extractor.getDescription() + " and use it where possible";
            }
          
            @Override
            public String getName(){
                return extractor.getName();
            }
        };
        
        ust.getTransformationManager().getEditorManager().setPersistantLineManager(true);
        ust.setSupertype(supertypeRef);
        ust.setSubtypes(Collections.singletonList(subtypeRef));
        ust.setUseInstanceOf(false);

        status.addEntry(ust.checkPreconditions());
        status.addEntry(ust.checkUserInput());
        if (status != null && status.isOk()){
          // FIXME: temporary solution to RIM-765 just to make RIT stable
          /*LineManager lineManager = extractor.getTransformationManager()
              .getEditorManager().getLineManager();
          lineManager.remapSources(context);
          ust.getTransformationManager().getEditorManager().setLineManager(
              lineManager);*/
            status.addEntry(ust.apply());
        }
        return status != null && status.isOk();
    }

    
}
