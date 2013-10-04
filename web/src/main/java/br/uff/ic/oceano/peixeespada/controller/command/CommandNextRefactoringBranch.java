/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONWriter;

/**
 *
 * @author Heliomar
 */
public class CommandNextRefactoringBranch implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String branch = ContextoAmbiente.getInstance().nextBranch(Long.parseLong(request.getParameter("idWorkAgent")), Long.parseLong(request.getParameter("idOrchestratorAgent")));

        JSONWriter jSONWriter = new JSONWriter(response.getWriter());
        jSONWriter.object().key("branch").value(branch).endObject();
        response.flushBuffer();
    }

}
