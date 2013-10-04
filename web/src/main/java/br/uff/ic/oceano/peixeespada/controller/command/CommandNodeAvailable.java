/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONWriter;

/**
 *
 * @author Heliomar
 */
public class CommandNodeAvailable implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strStatusAgent = request.getParameter("statusAgent");
        ContextoAmbiente.getInstance().alteraStatusAgenteTrabalhador(Long.parseLong(request.getParameter("idWorkAgent")), Long.parseLong(request.getParameter("idOrchestratorAgent")), strStatusAgent);
        List<String> refactorings = ContextoAmbiente.getInstance().solicitaRefatoracoes(Long.parseLong(request.getParameter("idWorkAgent")), Long.parseLong(request.getParameter("idOrchestratorAgent")));

        JSONWriter jSONWriter = new JSONWriter(response.getWriter());
        jSONWriter.array();
        for (String refactoringName : refactorings) {
            jSONWriter.object().key("refactoringName").value(refactoringName).endObject();
        }
        jSONWriter.endArray();

        response.flushBuffer();
    }
}
