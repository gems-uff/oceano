package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.InlineMethodSymptom;
import net.sf.refactorit.refactorings.inlinemethod.InlineMethod;

/**
 *
 * @author João Felipe
 */
public class InlineMethodResolution extends Resolution implements Comparable {

    public InlineMethodResolution(InlineMethodSymptom symptom) {
        super(symptom);
    }

    public InlineMethodSymptom getInlineMethodSymptom() {
        return (InlineMethodSymptom) getSymptom();
    }

    public int getMethodDeclarationAction() {
        return InlineMethod.DELETE_METHOD_DECLARATION;
    }
}
