/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.discretizer;

import br.uff.ic.oceano.ostra.controle.Constantes;

/**
 *
 * @author daniel
 */
public class NumberOfFilesDiscretizer extends Discretizer {

    private static final String ONE_OR_LESS = "1-";
    private static final String TWO = "2";
    private static final String TREE_TO_FOUR = "3-4";
    private static final String FIVE_TO_EIGHT = "5-8";
    private static final String NINE_OR_MORE = "9+";

    NumberOfFilesDiscretizer(String attributeTarget) {
        super(attributeTarget);
    }

    @Override
    public String discretize(String s) {
        if (s == null) {
            throw new NumberFormatException();
        }
        try {
            int i = Integer.valueOf(s);

            if (i <= 1) {
                return ONE_OR_LESS;
            }
            if (i == 2) {
                return TWO;
            }
            if (i <= 4) {
                return TREE_TO_FOUR;
            }
            if (i <= 8) {
                return FIVE_TO_EIGHT;
            }
            return NINE_OR_MORE;

        } catch (NumberFormatException ex) {
            System.out.println("s = " + s);
            throw ex;
        }
    }

    @Override
    public String getTargetType() {
        return Constantes.NUMERIC;
    }

    public String getPrefix() {
        return "";
    }

    @Override
    public String getHeaderDeclaration(String substring) {
        return ONE_OR_LESS + ", " + TWO + ", " + TREE_TO_FOUR + ", " + FIVE_TO_EIGHT + ", " + NINE_OR_MORE;
    }
}
