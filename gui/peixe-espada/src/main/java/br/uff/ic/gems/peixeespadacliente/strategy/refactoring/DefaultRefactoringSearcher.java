/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy.refactoring;

import br.uff.ic.gems.peixeespadacliente.service.PeixeEspadaService;
import java.util.List;

/**
 *
 * @author GEMS
 */
public class DefaultRefactoringSearcher implements RefactoringSearcher {

    PeixeEspadaService pes;

    public DefaultRefactoringSearcher() {
        pes = new PeixeEspadaService(null);
    }

    @Override
    public List<String> getRefatorings() {
        return pes.getRefactorings();
    }
}
