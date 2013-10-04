/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.ProtocolService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.text.SimpleDateFormat;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONWriter;

/**
 *
 * @author Heliomar
 */
public class CommandNodeScheduling implements Command{

    private ProjectUserService projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat  sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        String strDateInitial = null;
        String strDateEnd = null;
        Long idOrchestratorAgent = null;
        Long idProject = null;
        Long idOceanUser = null;
        String strStatusAgent = null;

        strDateInitial = request.getParameter("dateInitial");
        strDateEnd = request.getParameter("dateEnd");

        strStatusAgent = request.getParameter("statusAgent");
        idOrchestratorAgent = Long.parseLong(request.getParameter("idOrchestratorAgent"));

        idOceanUser = Long.parseLong(request.getParameter("idOceanUser"));
        idProject = Long.parseLong(request.getParameter("idProject"));

        SoftwareProject softwareProject = new SoftwareProject();
        softwareProject.setId(idProject);
        OceanoUser oceanoUser = new OceanoUser();
        oceanoUser.setId(idOceanUser);
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(softwareProject, oceanoUser);

        Agent agente = new Agent();
        agente.setCycles(0);
        agente.setSuccessCycles(0);

        agente.setName(request.getParameter("adressToString"));
        agente.setInitDate(sdf.parse(strDateInitial));
        agente.setEndDate(sdf.parse(strDateEnd));
        agente.setStatus(strStatusAgent);

        ContextoAmbiente.getInstance().registraAgenteTrabalhador(agente, idOrchestratorAgent);


        JSONWriter jSONWriter = new JSONWriter(response.getWriter());
        jSONWriter.object()
                .key("idWorkAgent")
                .value(agente.getIdAgent())
                .key("idProjectUser")
                .value(projectUser.getId())
                .key("passwordVCS")
                .value(projectUser.getPassword())
                .key("usernameVCS")
                .value(projectUser.getLogin()).endObject();


        response.flushBuffer();


    }

}
