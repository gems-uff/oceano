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
public class CommandFailRefactoring implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strRefactoring = request.getParameter("refactoring");
        ContextoAmbiente.getInstance().alteraCiclosFailAgenteTrabalhador(Long.parseLong(request.getParameter("idWorkAgent")), Long.parseLong(request.getParameter("idOrchestratorAgent")), strRefactoring);
        response.getWriter().println("\n orquestrador agent updated ");
    }

}
