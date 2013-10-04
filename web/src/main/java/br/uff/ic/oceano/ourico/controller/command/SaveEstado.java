/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller.command;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.OceanoUserService;
import br.uff.ic.oceano.ourico.model.Estado;
import br.uff.ic.oceano.ourico.service.EstadoService;
import br.uff.ic.oceano.controller.servlet.command.Command;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;

/**
 *
 * @author marapao
 */
public class SaveEstado implements Command {

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
    private EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

//        String loginOceano = request.getParameter(LOGIN_OCEANO);
//        String senhaOceano = request.getParameter(SENHA_OCEANO);
//
//        //autenticando usuário oceano
//        oceanoUserService.autenticarUsuario(loginOceano, senhaOceano);

        String dataInicial = request.getParameter(DATA_INICIAL);
        String dataFinal = request.getParameter(DATA_FINAL);
        String descricao = request.getParameter(DESCRICAO);
        Long autobranch = Long.parseLong(request.getParameter(AUTOBRANCH));


        Date dateDataInicial = null;
        Date dateDataFinal = null;

        try{
        dateDataInicial = new Date(Long.parseLong(dataInicial));
        dateDataFinal = new Date(Long.parseLong(dataFinal));
        } catch(NumberFormatException e){
            System.out.println(e.getMessage());
        }

        //verifiaca se ja existe
        Estado estado = null;
        try{
        estado = estadoService.getByAutobranchDescricao(autobranch, descricao);
        }catch(ObjetoNaoEncontradoException e){

        }
        if(estado == null)
            estado = new Estado();

        estado.setInicio(dateDataInicial);
        estado.setFim(dateDataFinal);
        estado.setDescricao(descricao);
        estado.setAutobranch(autobranch);

        estadoService.save(estado);

        response.getWriter().print("sucesso!");


    }
}
