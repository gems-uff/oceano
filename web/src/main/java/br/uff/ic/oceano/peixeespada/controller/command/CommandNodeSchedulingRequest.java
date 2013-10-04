/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.peixeespada.service.AgentService;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONWriter;

/**
 *
 * @author Heliomar
 */
public class CommandNodeSchedulingRequest implements Command{

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private AgentService agentService = ObjectFactory.getObjectWithDataBaseDependencies(AgentService.class);;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String strOceanoUser = null;
        String strPassword = null;

        strOceanoUser = request.getParameter("username");
        strPassword = request.getParameter("password");

        OceanoUser oceanoUser =    oceanoUserService.autenticarUsuario(strOceanoUser, strPassword);
        List<Agent> listaAgentes = agentService.getActiveByOceanoUser(oceanoUser);

        JSONWriter jSONWriter = new  JSONWriter(response.getWriter());
        jSONWriter.object().key("orchestratorAgents").array();
        for (Agent agent : listaAgentes) {
            jSONWriter.object()
                    .key("idAgent")
                    .value(agent.getIdAgent())
                    .key("role")
                    .value(agent.getQualityAttribute().getName())
                    .key("idQualityAttribute")
                    .value(agent.getQualityAttribute().getId())
                    .key("project")
                    .value(agent.getProject().getConfigurationItem().getName())
                    .key("idProject")
                    .value(agent.getProject().getId())
                    .key("mavenProject")
                    .value(agent.getProject().isMavenProject())
                    .key("urlProject")
                    .value(agent.getProject().getRepositoryUrl())
                    .key("nameRepository")
                    .value(agent.getProject().getConfigurationItem().getRepository().getName())
                    .endObject();
        }
        jSONWriter.endArray().key("idOceanUser").value(oceanoUser.getId()).endObject();

        response.flushBuffer();

    }

}
