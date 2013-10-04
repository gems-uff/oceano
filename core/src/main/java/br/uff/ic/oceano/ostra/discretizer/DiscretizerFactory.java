/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.discretizer;

import br.uff.ic.oceano.core.exception.ServiceException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author daniel
 */
public class DiscretizerFactory {

    private static final Set<Class> knownDiscretizerClasses = getKnownDiscretizerClasses();

    private static Set<Class> getKnownDiscretizerClasses() {
        Set<Class> knownDiscretizerClassesToReturn = new HashSet<Class>();
        knownDiscretizerClassesToReturn.add(NegativePositiveDiscretizer.class);

        return knownDiscretizerClassesToReturn;
    }

    public static Discretizer getDiscretizer(String attributeTargetName, Class discretizerClass) throws ServiceException {
        if (attributeTargetName == null) {
            throw new ServiceException("Attribute name cannot be null.");
        }
        if (discretizerClass.equals(NegativePositiveDiscretizer.class)) {
            return new NegativePositiveDiscretizer(attributeTargetName);
        }
        if (discretizerClass.equals(DayOfWeekDiscretizer.class)) {
            return new DayOfWeekDiscretizer(attributeTargetName);
        }
        if (discretizerClass.equals(HourOfDayDiscretizer.class)) {
            return new HourOfDayDiscretizer(attributeTargetName);
        }
        if (discretizerClass.equals(RoundOfDayDiscretizer.class)) {
            return new RoundOfDayDiscretizer(attributeTargetName);
        }
        if (discretizerClass.equals(NumberOfFilesDiscretizer.class)) {
            return new NumberOfFilesDiscretizer(attributeTargetName);
        }
        if (discretizerClass.equals(DoubleDecimalCaseDiscretizer.class)) {
            return new DoubleDecimalCaseDiscretizer(attributeTargetName);
        }
        throw new ServiceException("Unknown discretizer class: " + discretizerClass.toString());
        //        if (!knownDiscretizerClasses.contains(discretizerClass)) {
//            throw new ServiceException("Unknown discretizer class.");
//        }
//        try {
//            Constructor c = discretizerClass.getConstructor(new Class[]{String.class});
//
//            return (Discretizer) c.newInstance(attributeTargetName);
//        } catch (Exception ex) {
//            throw new ServiceException(ex);
//        }
    }
}
