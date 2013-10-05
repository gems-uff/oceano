package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.ExtractInterfaceSymptom;

/**
 *
 * @author João Felipe
 */
public class ExtractInterfaceResolution extends Resolution implements Comparable {

    private String interfaceName;
    
    public ExtractInterfaceResolution(ExtractInterfaceSymptom symptom, String className) {
        super(symptom);
        this.interfaceName = className+"Interface";
    }

    public ExtractInterfaceSymptom getExtractInterfaceSymptom() {
        return (ExtractInterfaceSymptom) getSymptom();
    }
    
    public String getNewTypeName() {
        return interfaceName;
    }
    
    public boolean getExtractClass() {
        return false;
    }
    
    public boolean getForceExtractMethodsAbstract() {
        return false;
    }

    public boolean getExtractWithOldName() {
        return false;
    }
    
}
