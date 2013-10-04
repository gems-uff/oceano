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
public class DoubleDecimalCaseDiscretizer extends Discretizer {

    DoubleDecimalCaseDiscretizer(String attributeTarget) {
        super(attributeTarget);
    }

    @Override
    public String discretize(String s) {
        return NumberUtil.format(Double.valueOf(s), 3);
    }

    @Override
    public String getTargetType() {
        return Constantes.REAL;

    }

    public String getPrefix() {
        return "dDDC-";

    }

    @Override
    public String getHeaderDeclaration(String substring) {
        return "real";
    }
}
