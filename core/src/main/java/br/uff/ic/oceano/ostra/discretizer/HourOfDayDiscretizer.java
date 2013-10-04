/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.discretizer;

import br.uff.ic.oceano.ostra.controle.Constantes;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author daniel
 */
public class HourOfDayDiscretizer extends Discretizer {

    private static final boolean english = false;

    HourOfDayDiscretizer(String attributeTarget) {
        super(attributeTarget);
    }

    @Override
    public String discretize(String s) {
        try {
            return s.substring(s.indexOf(" ") + 3, s.indexOf(":"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getTargetType() {
        return Constantes.NOMINAL;
    }

    public String getPrefix() {
        return "hour-";
    }

    @Override
    public String getHeaderDeclaration(String values) {
        return values;
    }
}
