/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.ourico.service.EstadoService;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;
/**
 *
 * @author marapao
 */
public class RecoverProtectedPath implements Command {

    private CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Date inicio = new Date();
        String autobranch = request.getParameter(AUTOBRANCH);
        CheckOut info = checkOutService.getbyAutobranch(Long.parseLong(autobranch));

        estadoService.saveEstado(inicio, new Date(), "Realizando update do workspace", null, autobranch);
        response.getWriter().print(info.getUrlCheckedOut());
    }

}
