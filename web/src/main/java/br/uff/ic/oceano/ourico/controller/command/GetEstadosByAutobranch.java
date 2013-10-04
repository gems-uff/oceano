/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import java.util.List;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.ourico.model.Estado;
import br.uff.ic.oceano.ourico.service.EstadoService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.uff.ic.oceano.controller.servlet.command.Command;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;

/**
 *
 * @author marapao
 */
public class GetEstadosByAutobranch implements Command {

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

//        String loginOceano = request.getParameter(LOGIN_OCEANO);
//        String senhaOceano = request.getParameter(SENHA_OCEANO);
//        String autobranchStr = request.getParameter(AUTOBRANCH);
//
//        Long autobranchLong = Long.parseLong(autobranchStr);
        String loginOceano = "gleiph";
        String senhaOceano = "gleiph";

        Long autobranchLong = 1l;

        //autenticando usuário oceano
        oceanoUserService.autenticarUsuario(loginOceano, senhaOceano);


        List<Estado> estados = estadoService.getByAutobranch(autobranchLong);

        StringBuffer message = new StringBuffer();

        for (Estado estado : estados) {

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

        }

        response.getWriter().print(message);

    }
}
