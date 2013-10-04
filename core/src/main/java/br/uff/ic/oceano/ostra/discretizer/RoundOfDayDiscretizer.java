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
public class RoundOfDayDiscretizer extends Discretizer {

    private static final String MANHA = "Manha";
    private static final String TARDE = "Tarde";
    private static final String NOITE = "Noite";
    private static final String MADRUGADA = "Madrugada";
    private HourOfDayDiscretizer hourOfDayDiscretizer;

    RoundOfDayDiscretizer(String attributeTarget) {
        super(attributeTarget);
        hourOfDayDiscretizer = new HourOfDayDiscretizer(attributeTarget);
    }

    @Override
    public String discretize(String s) {
        int hora = Integer.parseInt(hourOfDayDiscretizer.discretize(s));

        if ((hora >= 0 && hora <= 5) || hora == 24) {
            return MADRUGADA;

        } else if ((hora >= 6 && hora <= 11)) {
            return MANHA;

        } else if ((hora >= 12 && hora <= 17)) {
            return TARDE;

        } else if ((hora >= 18 && hora <= 23)) {
            return NOITE;

        }
        return "?";
    }

    @Override
    public String getTargetType() {
        return Constantes.NOMINAL;
    }

    public String getPrefix() {
        return "round-";
    }

    @Override
    public String getHeaderDeclaration(String values) {
        return values;
    }
}
