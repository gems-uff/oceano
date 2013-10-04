package br.uff.ic.gems.peixeespadacliente.conflicts;

import net.sf.refactorit.refactorings.conflicts.ChangedFunctionalityConflict;
import net.sf.refactorit.refactorings.conflicts.Conflict;
import net.sf.refactorit.refactorings.conflicts.ConflictResolver;
import net.sf.refactorit.refactorings.conflicts.DeleteOtherImplementersConflict;
import net.sf.refactorit.refactorings.conflicts.resolution.ConflictResolution;
import java.lang.reflect.Field;

/**
 *
 * @author João Felipe
 */
public class ConflictWithResolution {

    private Conflict conflict;
    private ConflictResolution resolution;
    private ConflictResolver resolver;

    public ConflictWithResolution(Conflict conflict, ConflictResolution resolution, ConflictResolver resolver) {
        this.conflict = conflict;
        this.resolution = resolution;
        this.resolver = resolver;
    }

    public Conflict getConflict() {
        return conflict;
    }

    public ConflictResolution getResolution() {
        return resolution;
    }

    public boolean resolveConflict() {
        //Escolher modo certo de resolver
//        conflict.resolve();

        if ((conflict == null) || (conflict instanceof ChangedFunctionalityConflict)) {
            return false;
        }

        if (resolution != null) {
            conflict.setResolution(resolution);
            resolution.runResolution(resolver);
            if (conflict instanceof DeleteOtherImplementersConflict) {
                try {
                    Field field = DeleteOtherImplementersConflict.class.getDeclaredField("isResolved");
                    field.setAccessible(true); // aqui eu troco o acesso dele
                    field.set(conflict, true);
                } catch (Exception e) {
                    return false;
                }
            }
        }

        if (conflict.isResolved()) {
            return true;
        } else {
            return false;
        }
    }
}
