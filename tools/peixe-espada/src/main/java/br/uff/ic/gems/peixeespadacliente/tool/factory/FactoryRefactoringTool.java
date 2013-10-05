package br.uff.ic.gems.peixeespadacliente.tool.factory;

import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import java.lang.reflect.Constructor;

/**
 *
 * @author Heliomar
 */
public class FactoryRefactoringTool {

    private FactoryRefactoringTool(){
    }

//    private static final Map<String, Class<? extends RefactoringTool>> mapa = new HashMap<String, Class<? extends RefactoringTool>>();

//    static {
//        mapa.put(RefactoringTool.ENCAPSULE_FIELDS, EncapsuleFields.class);
//        mapa.put(RefactoringTool.CLEAN_IMPORTS, CleanImports.class);
//        mapa.put(RefactoringTool.PULL_UP_METHODS, PullUpMethods.class);
//        mapa.put(RefactoringTool.PULL_UP_FIELDS, PullUpFields.class);
//        mapa.put(RefactoringTool.PUSH_DOWN_METHODS, PushDownMethods.class);

//    }

    public static  synchronized  <T> T getRefactoringTool(Class<? extends RefactoringTool> refactorinfToolClass, ProjectVCS projectVCS){
        Constructor constructor = null;
        T rt = null;
        try {
            constructor = refactorinfToolClass.getDeclaredConstructor(ProjectVCS.class);
            rt = (T) constructor.newInstance(projectVCS);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return rt;
    }

}
