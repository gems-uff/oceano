/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.ourico.checkout.verificacao.VerificacaoCheckout;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author marapao
 */
public class DoPosCheckoutVerification implements Command {

   

    
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
   
        VerificacaoCheckout verificacaoCheckout = new VerificacaoCheckout();

        verificacaoCheckout.start();



        response.getWriter().print("Finalizado");
    }
}
