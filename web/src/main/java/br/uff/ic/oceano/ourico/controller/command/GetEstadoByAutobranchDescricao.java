/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
import br.uff.ic.oceano.ourico.model.Estado;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.controller.servlet.command.Command;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;

/**
 *
 * @author marapao
 */
public class GetEstadoByAutobranchDescricao implements Command {

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

//        String loginOceano = request.getParameter(LOGIN_OCEANO);
//        String senhaOceano = request.getParameter(SENHA_OCEANO);
//
//        String autobranch = request.getParameter(AUTOBRANCH);
//        String descricao = request.getParameter(DESCRICAO);

        String loginOceano = "gleiph";
        String senhaOceano = "gleiph";

        String autobranch = "1";
        String descricao = ConstantesOurico.ANALISE_SINTATICA_1_OK;

        //autenticar usuário
        OceanoUser oceanoUser = oceanoUserService.autenticarUsuario(loginOceano, senhaOceano);

        Estado estado = estadoService.getByAutobranchDescricao(Long.parseLong(autobranch), descricao);

        StringBuffer message = new StringBuffer();

        message.append(ID_ESTADO);
            message.append("=");
            message.append(estado.getIdEstado());
            message.append("&");

            message.append(AUTOBRANCH);
            message.append("=");
            message.append(estado.getAutobranch());
            message.append("&");

            message.append(DATA_INICIAL);
            message.append("=");
            message.append(estado.getInicio());
            message.append("&");

            message.append(DATA_FINAL);
            message.append("=");
            message.append(estado.getFim());
            message.append("&");

            message.append(DESCRICAO);
            message.append("=");
            message.append(estado.getDescricao());
            message.append("\n");

            response.getWriter().print(message);

    }
}
