/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy.symptom;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.List;

/**
 *
 * @author Gleiph
 */
public interface SymptomSearcher {

    public List<Symptom> getSymptoms(List<String> refactorings) throws RefactoringException;
    
}
