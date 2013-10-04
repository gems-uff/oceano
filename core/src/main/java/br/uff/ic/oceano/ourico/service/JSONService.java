/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.service.ProtocolService;
import br.uff.ic.oceano.ourico.rcs.context.Constants;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import java.util.Date;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;

/**
 *
 * @author marapao
 */
public class JSONService {

    public static String CMD_AUTOBRANCH_PREPARE = "cmd=Prepare_Autobranch&";
    public static String CMD_GET_CHECKOUT_BY_AUTOBRANCH = "cmd=" + CHECKOUT_GET_BY_AUTOBRANCH + "&";
    public static String CMD_CHECKOUT_GET_ALL = "cmd=" + CHECKOUT_GET_ALL + "&";
    public static String CMD_CHECKOUT_SAVE = "cmd=" + CHECKOUT_SAVE + "&";
    public static String CMD_ESTADO_SAVE = "cmd=" + ESTADO_SAVE + "&";
    public static String CMD_VERIFICATION = "cmd=" + VERIFICATION + "&";
    public static String CMD_RECOVER_PROTECTED_PATH = "cmd=" + RECOVER_PROTECTED_PATH + "&";
    public static String CMD_POS_CHECKOUT_VERIFICATION = "cmd=" + POS_CHECKOUT_VERIFICATION + "&";
    public ProtocolService protocolService = ObjectFactory.getObjectWithDataBaseDependencies(ProtocolService.class);

    public String posCheckoutVerification(String oceanoURL) throws ServiceException {

        StringBuffer message = new StringBuffer();

        message.append(CMD_POS_CHECKOUT_VERIFICATION);

        return  protocolService.getMessageServer(message, oceanoURL);
    }

    public String recoverProtectedPath(String autobranch, String oceanoURL) throws ServiceException {

        StringBuffer message = new StringBuffer();


        message.append(CMD_RECOVER_PROTECTED_PATH);

        message.append(AUTOBRANCH);
        message.append("=");
        message.append(autobranch);

        return protocolService.getMessageServer(message, oceanoURL);


    }

    public String preparaAutobranch(String urlCheckedOut, String senhaSVN, String loginSVN, String oceanoURL) throws ServiceException {

        StringBuffer message = new StringBuffer();


        message.append(CMD_AUTOBRANCH_PREPARE);

        message.append(URL_CHECKED_OUT);
        message.append("=");
        message.append(urlCheckedOut);
        message.append("&");


        message.append(SENHA_SVN);
        message.append("=");
        message.append(senhaSVN);
        message.append("&");

        message.append(LOGIN_SVN);
        message.append("=");
        message.append(loginSVN);


        return protocolService.getMessageServer(message, oceanoURL);

    }

    public String saveEstado(Date dataInicial, Date dataFinal, String descricao, Long autobranch) throws ServiceException {

        StringBuffer message = new StringBuffer();
        long dataInicialLong;
        long dataFinalLong;

        message.append(CMD_ESTADO_SAVE);

        dataInicialLong = dataInicial.getTime();
        message.append(DATA_INICIAL);
        message.append("=");
        message.append(dataInicialLong);
        message.append("&");


        dataFinalLong = dataFinal.getTime();
        message.append(DATA_FINAL);
        message.append("=");
        message.append(dataFinalLong);
        message.append("&");

        message.append(DESCRICAO);
        message.append("=");
        message.append(descricao);
        message.append("&");

        message.append(AUTOBRANCH);
        message.append("=");
        message.append(autobranch);

        return protocolService.getMessageServer(message, Constants.URL_OCEANO);

    }

    public String getCheckoutByAutobranch(Long autobranch, String loginOceano, String senhaOceano) throws ServiceException {

        StringBuffer message = new StringBuffer();

        message.append(CMD_GET_CHECKOUT_BY_AUTOBRANCH);

        message.append(LOGIN_OCEANO);
        message.append("=");
        message.append(loginOceano);
        message.append("&");

        message.append(SENHA_OCEANO);
        message.append("=");
        message.append(senhaOceano);
        message.append("&");

        message.append(AUTOBRANCH);
        message.append("=");
        message.append(autobranch);

        return protocolService.getMessageServer(message, Constants.URL_OCEANO);


    }

    public String checkoutGetAll(String loginOceano, String senhaOceano) throws ServiceException {

        StringBuffer message = new StringBuffer();

        message.append(CMD_CHECKOUT_GET_ALL);

        message.append(LOGIN_OCEANO);
        message.append("=");
        message.append(loginOceano);
        message.append("&");

        message.append(SENHA_OCEANO);
        message.append("=");
        message.append(senhaOceano);

        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public String saveCheckout(String estado, String revisao, String idCheckout, String autobranch, String pathRepositorio, String dirProtegido, String dirAutobranch, String senhaSVN, String loginSVN, String politica, String projeto, String responsavel, String workspace, String loginOceano, String senhaOceano) throws ServiceException {

        StringBuffer message = new StringBuffer();

        message.append(CMD_CHECKOUT_SAVE);


        message.append(ESTADO);
        message.append("=");
        message.append(estado);
        message.append("&");

        message.append(REVISAO);
        message.append("=");
        message.append(revisao);
        message.append("&");

        message.append(ID_CHECKOUT);
        message.append("=");
        message.append(idCheckout);
        message.append("&");

        message.append(AUTOBRANCH);
        message.append("=");
        message.append(autobranch);
        message.append("&");

        message.append(PATH_REPOSITORIO);
        message.append("=");
        message.append(pathRepositorio);
        message.append("&");

        message.append(DIRETORIO_PROTEGIDO);
        message.append("=");
        message.append(dirProtegido);
        message.append("&");

        message.append(DIRETORIO_AUTOBRANCH);
        message.append("=");
        message.append(dirAutobranch);
        message.append("&");

        message.append(SENHA_SVN);
        message.append("=");
        message.append(senhaSVN);
        message.append("&");

        message.append(LOGIN_SVN);
        message.append("=");
        message.append(loginSVN);
        message.append("&");

        message.append(POLITICA);
        message.append("=");
        message.append(politica);
        message.append("&");

        message.append(PROJETO);
        message.append("=");
        message.append(projeto);
        message.append("&");

        message.append(RESPONSAVEL);
        message.append("=");
        message.append(responsavel);
        message.append("&");

        message.append(LOGIN_OCEANO);
        message.append("=");
        message.append(loginOceano);
        message.append("&");

        message.append(SENHA_OCEANO);
        message.append("=");
        message.append(senhaOceano);
        message.append("&");

        message.append(WORKSPACE);
        message.append("=");
        message.append(workspace);

        System.out.println("message = " + message);

        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public String verification(String autobranch, String urlOceano) throws ServiceException {

        StringBuffer message = new StringBuffer();

        message.append(CMD_VERIFICATION);

        message.append(AUTOBRANCH);
        message.append("=");
        message.append(autobranch);


        return protocolService.getMessageServer(message, Constants.URL_OCEANO);


    }
}
