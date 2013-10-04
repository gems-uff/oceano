/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.OceanoUserService;
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
public class SaveCheckOut implements Command {

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Long autobranch = Long.parseLong(request.getParameter(AUTOBRANCH));
        String dirAutobranch = request.getParameter(DIRETORIO_AUTOBRANCH);
        String dirProtegido = request.getParameter(DIRETORIO_PROTEGIDO);
        String estado = request.getParameter(ESTADO);

        Long idCheckout;
        try {
            idCheckout = Long.parseLong(request.getParameter(ID_CHECKOUT));
        } catch (NumberFormatException e) {
            idCheckout = null;
        }

        String politica = request.getParameter(POLITICA);
        String projeto = request.getParameter(PROJETO);
        Long revisao;

        try {
            revisao = Long.parseLong(request.getParameter(REVISAO));
        } catch (NumberFormatException e) {
            revisao = null;
        }

        String pathRepositorio = request.getParameter(PATH_REPOSITORIO);
        String usuario = request.getParameter(RESPONSAVEL);
        String workspace = request.getParameter(WORKSPACE);
        String loginOceano = request.getParameter(LOGIN_OCEANO);
        String senhaOceano = request.getParameter(SENHA_OCEANO);


        //autenticar usuário
        OceanoUser oceanoUser = oceanoUserService.autenticarUsuario(loginOceano, senhaOceano);


        CheckOut checkOut = new CheckOut();

        checkOut.setAutobranch(autobranch);
//        checkOut.setDiretorioAutobranch(dirAutobranch);
//        checkOut.setDiretorioProtegido(dirProtegido);
//        checkOut.setEstado(estado);
        checkOut.setIdCheckout(idCheckout);
        checkOut.setPolitica(politica);
//        checkOut.setProjeto(projeto);
        checkOut.setRevisao(revisao);
//        checkOut.setUrlRepositorio(pathRepositorio);
//        checkOut.setUsuario(usuario);
        checkOut.setWorkspace(workspace);


        CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);

        checkOutService.save(checkOut);

        response.getWriter().print("finalizado");

    }
}
