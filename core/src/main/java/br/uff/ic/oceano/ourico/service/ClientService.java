/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import java.util.Date;

/**
 *
 * @author marapao
 */
public class ClientService {

    public String posCheckoutVerification(String oceanoURL) throws ServiceException {
        JSONService jSONService = new JSONService();

        return jSONService.posCheckoutVerification(oceanoURL);
    }

    public String recoverProtectedPath(String autobranch, String oceanoURL) throws ServiceException {
        JSONService jSONService = new JSONService();

        return jSONService.recoverProtectedPath(autobranch, oceanoURL);
    }

    public String preparaAutobranch(String urlCheckedOut, String senhaSVN, String loginSVN, String oceanoURL) throws ServiceException {
        JSONService jSONService = new JSONService();

        return jSONService.preparaAutobranch(urlCheckedOut, senhaSVN, loginSVN, oceanoURL);
    }

    public String getCheckoutByAutobranch(Long autobranch, String loginOceano, String senhaOceano) throws ServiceException {
        JSONService jSONService = new JSONService();

        return jSONService.getCheckoutByAutobranch(autobranch, loginOceano, senhaOceano);
    }

    public String checkoutGetAll(String loginOceano, String senhaOceano) throws ServiceException {
        JSONService jSONService = new JSONService();

        return jSONService.checkoutGetAll(loginOceano, senhaOceano);

    }

    public String saveCheckout(String estado, String revisao, String idCheckOut, String autobranch, String pathRepositorio, String dirProtegido, String dirAutobranch, String senhaSVN, String loginSVN, String politica, String projeto,String responsavel, String workspace, String loginOceano, String senhaOceano) throws ServiceException{
        JSONService jSONService = new JSONService();

    return jSONService.saveCheckout(estado, revisao, idCheckOut, autobranch, pathRepositorio, dirProtegido, dirAutobranch, senhaSVN, loginSVN, politica, projeto, responsavel, workspace, loginOceano, senhaOceano);
    }

    public String saveEstado(Date dataInicial, Date dataFinal, String descricao, Long autobranch) throws ServiceException{
        JSONService jSONService = new JSONService();

        return jSONService.saveEstado(dataInicial, dataFinal, descricao, autobranch);
    }

    public String verification(String autobranch, String urlOceano) throws ServiceException {
        JSONService jSONService = new JSONService();

        return  jSONService.verification(autobranch, urlOceano);
    }


    
}
