package br.uff.ic.oceano.ostra.discretizer;

import br.uff.ic.oceano.core.exception.ServiceException;

/**
 *
 * @author daniel
 */
public class DiscretizerFactory {

    public static Discretizer getDiscretizer(String attributeTargetName, Class discretizerClass) throws ServiceException {
        if (attributeTargetName == null) {
            throw new ServiceException("Attribute name cannot be null.");
        } else if (attributeTargetName.isEmpty()){
            throw new ServiceException("Attribute name cannot be empty.");
        }
        
        if ( !discretizerClass.isAssignableFrom(Discretizer.class)){
            throw new ServiceException(discretizerClass.getCanonicalName() + " is not a "+ Discretizer.class.getCanonicalName() + " subclass!");
        }
        try {
            Discretizer newDiscretizer = (Discretizer)discretizerClass.newInstance();
            newDiscretizer.setAttributeTarget(attributeTargetName);
            return newDiscretizer;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }          
    }
}
