/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.controller.servlet.command.Command;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;

/**
 *
 * @author marapao
 */
public class GetCheckOutByAutobranch implements Command {

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String autobranch = request.getParameter(AUTOBRANCH);
        String loginOceano = request.getParameter(LOGIN_OCEANO);
        String senhaOceano = request.getParameter(SENHA_OCEANO);

        //autenticar usuário
        OceanoUser oceanoUser = oceanoUserService.autenticarUsuario(loginOceano, senhaOceano);


        CheckOut checkOut;
        CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);

        checkOut = checkOutService.getbyAutobranch(Long.parseLong(autobranch));

        StringBuffer saida = new StringBuffer();

        saida.append(AUTOBRANCH);
        saida.append("=");
        saida.append(checkOut.getAutobranch());
        saida.append("&");

//        saida.append(DIRETORIO_AUTOBRANCH);
//        saida.append("=");
//        saida.append(checkOut.getDiretorioAutobranch());
//        saida.append("&");
//
//        saida.append(DIRETORIO_PROTEGIDO);
//        saida.append("=");
//        saida.append(checkOut.getDiretorioProtegido());
//        saida.append("&");
//
//        saida.append(ESTADO);
//        saida.append("=");
//        saida.append(checkOut.getEstado());
//        saida.append("&");

        saida.append(ID_CHECKOUT);
        saida.append("=");
        saida.append(checkOut.getIdCheckout());
        saida.append("&");

        saida.append(POLITICA);
        saida.append("=");
        saida.append(checkOut.getPolitica());
        saida.append("&");

//        saida.append(PROJETO);
//        saida.append("=");
//        saida.append(checkOut.getProjeto());
//        saida.append("&");
//
//        saida.append(REVISAO);
//        saida.append("=");
//        saida.append(checkOut.getRevisao());
//        saida.append("&");
//
//        saida.append(PATH_REPOSITORIO);
//        saida.append("=");
//        saida.append(checkOut.getUrlRepositorio());
//        saida.append("&");
//
//        saida.append(USUARIO);
//        saida.append("=");
//        saida.append(checkOut.getUsuario());
//        saida.append("&");

        saida.append(WORKSPACE);
        saida.append("=");
        saida.append(checkOut.getWorkspace());

        response.getWriter().print(saida);

    }
}
