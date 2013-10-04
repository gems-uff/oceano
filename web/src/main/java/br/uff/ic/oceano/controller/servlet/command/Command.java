/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.controller.servlet.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static br.uff.ic.oceano.ourico.controle.VariaveisOurico.*;
/**
 *
 * @author Heliomar
 */
public interface Command {

    // commands Oceano-Web
    public static final String ADMIN_CARGA_DEFAULT = "cmdAdminCargaDefault";
    // commands PeixeEspadaCliente
    public static final String NODE_SCHEDULING = "Scheduling";
    public static final String NODE_SCHEDULING_REQUEST = "Scheduling_Request";
    public static final String NODE_AVAILABLE = "Avaiable";
    public static final String NODE_UNAVAILABLE = "Unavaiable";
    public static final String NODE_USE_REFACTORING = "Use_refactoring";
    public static final String NODE_USE_METRICS = "Use_metrics";
    public static final String NODE_KNOLEGED = "Use_metrics";
    public static final String PREPARE_AUTOBRANCH = "Prepare_Autobranch";
    public static final String SUCESS_REFACTORING = "Sucess_Refactoring";
    public static final String FAIL_REFACTORING = "Fail_Refactoring";
    public static final String NEXT_BRANCH_TO_SUCESS_REFACTORING = "Branch_Sucess_Refactoring";
    public static final String NEXT_REFACTORING_BRANCH = "Refactoring_Branch";
    public static final String GET_QUALITY_ATTRIBUTE_WITH_METRICS = "Get_QualityAttribute_With_Metrics";
    public static final String NOT_IMPROVE_NOR_WORSEN_REFACTORING = "Not_Improve_nor_Worsen Refactoring";
    public static final String PREPARE_BD_TO_EXPERIMENTS_PEIXE_ESPADA = "pepe";
    // commands ouriço
    public static final String GET_CHECKOUT_BY_AUTOBRANCH = CHECKOUT_GET_BY_AUTOBRANCH;
    public static final String GET_ALL_CHECKOUTS = CHECKOUT_GET_ALL;
    public static final String SAVE_CHECKOUT = CHECKOUT_SAVE;
    public static final String SAVE_ESTADO = ESTADO_SAVE;
    public static final String GET_ESTADOS_BY_AUTOBRANCH = ESTADOS_GET_BY_AUTOBRANCH;
    public static final String GET_ESTADO_BY_AUTOBRANCH_DESCRICAO = ESTADO_GET_BY_AUTOBRANCH_DESCRICAO;
    public static final String CMD_VERIFICATION = VERIFICATION;
    public static final String CMD_RECOVER_PROTECTED_PATH = RECOVER_PROTECTED_PATH;
    public static final String CMD_POS_CHECKOUT_VERIFICATION = POS_CHECKOUT_VERIFICATION;




    public  abstract void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
