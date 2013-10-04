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
public class DayOfWeekDiscretizer extends Discretizer {

    private static final boolean english = false;

    DayOfWeekDiscretizer(String attributeTarget) {
        super(attributeTarget);
    }

    @Override
    public String discretize(String s) {
        String dateWithoutTime = s.substring(0,s.indexOf(" "));

        int day = Integer.valueOf(dateWithoutTime.split("/")[0]);
        int month = Integer.valueOf(dateWithoutTime.split("/")[1]);
        int year = Integer.valueOf(dateWithoutTime.split("/")[2]);

        Calendar c = new GregorianCalendar(year, month, day);

        if (english) {
            switch (c.get(Calendar.DAY_OF_WEEK)){
                case Calendar.SUNDAY: return "Sunday";
                case Calendar.MONDAY: return "Monday";
                case Calendar.TUESDAY: return "Tuesday";
                case Calendar.WEDNESDAY: return "Wednesday";
                case Calendar.THURSDAY: return "Thursday";
                case Calendar.FRIDAY: return "Friday";
                case Calendar.SATURDAY: return "Saturday";
            }
        } else{
            switch (c.get(Calendar.DAY_OF_WEEK)){
                case Calendar.SUNDAY: return "Domingo";
                case Calendar.MONDAY: return "Segunda";
                case Calendar.TUESDAY: return "Terça";
                case Calendar.WEDNESDAY: return "Quarta";
                case Calendar.THURSDAY: return "Quinta";
                case Calendar.FRIDAY: return "Sexta";
                case Calendar.SATURDAY: return "Sábado";
            }
        }

        throw new RuntimeException();
//        throw new DiscretizationException();
//        return null;
    }

    @Override
    public String getTargetType() {
        return Constantes.NOMINAL;
    }

    public String getPrefix() {
        return "day-";
    }

    @Override
    public String getHeaderDeclaration(String values) {
        return values;
    }
}
