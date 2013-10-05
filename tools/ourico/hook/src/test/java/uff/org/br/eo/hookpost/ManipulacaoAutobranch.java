/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uff.org.br.eo.hookpost;

import javax.swing.JOptionPane;
import org.testng.annotations.Test;
import uff.org.br.eo.autobranch.TrataAutobranch;

/**
 *
 * @author marapao
 */
public class ManipulacaoAutobranch {

    @Test
    public void testeAutobranch(){
        String diretorioAlterado = "/autobranch/2/src/bla/blz/bla...";
        String pathAutobranch = "/autobranch";

       String resultado = "Autobranch = " + TrataAutobranch.autobranch(diretorioAlterado, pathAutobranch);
        JOptionPane.showMessageDialog(null, resultado);
    }

}
