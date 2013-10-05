/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.service.PeixeEspadaService;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.List;

/**
 *
 * @author GEMS
 */
public class DefaultSymptomSearcher implements SymptomSearcher {

    PeixeEspadaService pes;

    public DefaultSymptomSearcher() {
        pes = new PeixeEspadaService(null);
    }

    @Override
    public List<Symptom> getSymptoms(List<String> refactorings) throws RefactoringException {
        return pes.getSymptoms(refactorings);
    }
}
