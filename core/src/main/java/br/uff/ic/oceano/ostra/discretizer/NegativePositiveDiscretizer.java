/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.discretizer;

import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.ostra.controle.Constantes;

/**
 *
 * @author daniel
 */
public class NegativePositiveDiscretizer extends Discretizer {

    private static final String ZERO = "0";
    private static final String POSITIVE = "+";
    private static final String NEGATIVE = "-";

    NegativePositiveDiscretizer(String attributeTarget) {
        super(attributeTarget);
    }

    @Override
    public String discretize(String s) {
        if (s == null) {
            throw new NumberFormatException();
        }
        try {
            Double d = Double.valueOf(s);

            if (NumberUtil.isEquivalent(d, 0)) {
                return ZERO;
            } else if (d < 0D) {
                return NEGATIVE;
            } else {
                return POSITIVE;
            }
        } catch  (NumberFormatException ex){
            System.out.println("s = " + s);
            throw ex;
        }
    }

    @Override
    public String getTargetType() {
        return Constantes.REAL;
    }

    public String getPrefix() {
        return "dNZP-";
    }

    @Override
    public String getHeaderDeclaration(String substring) {
        return NEGATIVE + ", " + ZERO + ", " + POSITIVE;
    }
}
