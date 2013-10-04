package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.CleanImportSymptom;

/**
 *
 * @author João Felipe
 */
public class CleanImportResolution extends Resolution implements Comparable {

    public CleanImportResolution(CleanImportSymptom symptom) {
        super(symptom);
    }

    public CleanImportSymptom getCleanImportSymptom() {
        return (CleanImportSymptom) getSymptom();
    }

}
