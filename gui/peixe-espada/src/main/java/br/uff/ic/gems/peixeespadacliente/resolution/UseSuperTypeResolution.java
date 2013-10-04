package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.UseSuperTypeSymptom;

/**
 *
 * @author João Felipe
 */
public class UseSuperTypeResolution extends Resolution implements Comparable {

    public UseSuperTypeResolution(UseSuperTypeSymptom symptom) {
        super(symptom);
    }

    public UseSuperTypeSymptom getUseSuperTypeSymptom() {
        return (UseSuperTypeSymptom) getSymptom();
    }

}
