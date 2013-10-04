package br.uff.ic.gems.peixeespadacliente.tool;

import java.util.List;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import static br.uff.ic.oceano.peixeespada.model.Refactoring.*;
import java.util.HashMap;

/**
 *
 * @author Heliomar, João Felipe
 */
public interface RefactoringTool {

    // colocar native classe Refactoring a medida que implementando
    public static final String ENCAPSULATE_FIELDS = REFACTORING_ENCAPSULE_FIELDS;
    public static final String PUSH_DOWN_METHODS = REFACTORING_PUSH_DOWN_METHODS;
    public static final String PULL_UP_METHODS = REFACTORING_PULL_UP_METHODS;
    public static final String PULL_UP_FIELDS = REFACTORING_PULL_UP_FIELDS;
    public static final String PUSH_DOWN_FIELDS = REFACTORING_PUSH_DOWN_FIELDS;
    public static final String CLEAN_IMPORTS = REFACTORING_CLEAN_IMPORTS;
    public static final String ADD_DELEGATE_METHODS = REFACTORING_ADD_DELEGATE_METHODS;
    public static final String EXTRACT_INTERFACES = REFACTORING_EXTRACT_INTERFACES;
    public static final String USE_SUPER_TYPES = REFACTORING_USE_SUPER_TYPES;
    public static final String CREATE_FACTORY_METHODS = REFACTORING_CREATE_FACTORY_METHODS;
    public static final String INLINE_METHODS = REFACTORING_INLINE_METHODS;
    public static final HashMap<String, Class> classMap = new HashMap<String, Class>() {
        {
            put(ENCAPSULATE_FIELDS, EncapsulateFields.class);
            put(PUSH_DOWN_METHODS, PushDownMethods.class);
            put(PULL_UP_METHODS, PullUpMethods.class);
            put(PULL_UP_FIELDS, PullUpFields.class);
            put(PUSH_DOWN_FIELDS, PushDownFields.class);
            put(CLEAN_IMPORTS, CleanImports.class);
            put(ADD_DELEGATE_METHODS, AddDelegateMethods.class);
            put(EXTRACT_INTERFACES, ExtractInterfaces.class);
            put(USE_SUPER_TYPES, UseSuperTypes.class);
            put(CREATE_FACTORY_METHODS, CreateFactoryMethods.class);
            put(INLINE_METHODS, InlineMethods.class);
        }
    };

    public boolean applyCheckingPreAndPosCondictions(Resolution resolution) throws RefactoringException;

    public void revertMoDifications() throws RefactoringException;

    public boolean isWorking() throws RefactoringException;

    public boolean prepareSymptom(Symptom symptom) throws RefactoringException;

    public List<Symptom> findAllSymptoms() throws RefactoringException;

    public String toString();
}
