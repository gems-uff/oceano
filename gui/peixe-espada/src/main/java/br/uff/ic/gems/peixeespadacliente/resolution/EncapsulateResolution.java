package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.EncapsulateSymptom;

/**
 *
 * @author João Felipe
 */
public class EncapsulateResolution extends Resolution implements Comparable {

    public EncapsulateResolution(EncapsulateSymptom symptom) {
        super(symptom);
    }

    public EncapsulateSymptom getEncapsulateSymptom() {
        return (EncapsulateSymptom) getSymptom();
    }

}
