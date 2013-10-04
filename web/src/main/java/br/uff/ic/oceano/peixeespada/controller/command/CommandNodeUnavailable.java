/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Heliomar
 */
public class CommandNodeUnavailable implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long idOrchestratorAgent = Long.parseLong(request.getParameter("idOrchestratorAgent"));
        Long idWorkAgent = Long.parseLong(request.getParameter("idWorkAgent"));
        
        ContextoAmbiente.getInstance().desregistraAgenteTrabalhador(idWorkAgent, idOrchestratorAgent);

        response.getWriter().println("Agent: "+request.getParameter("adressToString"));
        response.getWriter().println("\n was unregistred with sucessfull");
    }

}
