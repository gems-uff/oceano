package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.AddDelegateMethodSymptom;

/**
 *
 * @author João Felipe
 */
public class AddDelegateMethodResolution extends Resolution implements Comparable {

    public AddDelegateMethodResolution(AddDelegateMethodSymptom symptom) {
        super(symptom);
    }

    public AddDelegateMethodSymptom getAddDelegateMethodsSymptom() {
        return (AddDelegateMethodSymptom) getSymptom();
    }

}
