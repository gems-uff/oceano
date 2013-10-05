package br.uff.ic.gems.peixeespadacliente.conflicts;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.sf.refactorit.refactorings.conflicts.resolution.ConflictResolution;

/**
 *
 * @author João Felipe
 */
public class PullPushConflictResolution {

    private String description;
    private List<String> downList;

    public PullPushConflictResolution(ConflictResolution resolution) {
        this.description = resolution.getDescription();
        this.downList = new ArrayList<String>();

        List downMembers = resolution.getDownMembers();
        if (!downMembers.isEmpty()) {
            for (Object object : downMembers) {
                try {
                    Class classe = Class.forName(object.getClass().getName());
                    Method metodo = classe.getMethod("getQualifiedName", new Class[0]);
                    String mensagem = (String) metodo.invoke(object, new Object[0]);
                    downList.add(mensagem);
                } catch (Exception ex) {
                }
            }
        }


    }

    public boolean equals(ConflictResolution resolution) {
        return (this.description.equals(resolution.getDescription()));
    }
}
