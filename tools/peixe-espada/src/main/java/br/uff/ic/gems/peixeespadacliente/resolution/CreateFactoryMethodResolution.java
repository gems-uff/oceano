package br.uff.ic.gems.peixeespadacliente.resolution;

import br.uff.ic.gems.peixeespadacliente.symptom.CreateFactoryMethodSymptom;


/**
 *
 * @author João Felipe
 */
public class CreateFactoryMethodResolution extends Resolution implements Comparable {

    private String factoryName;
    
    public CreateFactoryMethodResolution(CreateFactoryMethodSymptom symptom, String className) {
        super(symptom);
        factoryName = "create" + className;
    }

    public CreateFactoryMethodSymptom getCreateFactoryMethodSymptom() {
        return (CreateFactoryMethodSymptom) getSymptom();
    }
    
    public String getMethodName() {
        return factoryName;
    }
    
    public boolean getOptimizeVisibility() {
        return true;
    }

}
