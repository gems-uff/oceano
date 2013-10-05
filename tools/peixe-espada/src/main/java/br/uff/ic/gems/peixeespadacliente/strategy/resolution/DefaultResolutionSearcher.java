/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy.resolution;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.service.PeixeEspadaService;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.List;

/**
 *
 * @author GEMS
 */
public class DefaultResolutionSearcher implements ResolutionSearcher {

    PeixeEspadaService pes;

    public DefaultResolutionSearcher() {
        pes = new PeixeEspadaService(null);
    }

    @Override
    public List<Resolution> getResolutions(List<Symptom> symptoms) throws RefactoringException{
            return pes.getResolutions(symptoms);

    }
}

