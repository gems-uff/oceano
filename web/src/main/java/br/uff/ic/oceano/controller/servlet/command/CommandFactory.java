/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.controller.servlet.command;

import br.uff.ic.oceano.ourico.controller.command.*;
import br.uff.ic.oceano.peixeespada.controller.command.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Heliomar
 */
public class CommandFactory {

    private static final Map<String, Command> mapCommand = new HashMap();

    static {
        //Admin
        mapCommand.put(Command.ADMIN_CARGA_DEFAULT, new CommandAdminChargingBDDefault());
        //PeixeEspada
        mapCommand.put(Command.NODE_AVAILABLE, new CommandNodeAvailable());
        mapCommand.put(Command.NODE_UNAVAILABLE, new CommandNodeUnavailable());
        mapCommand.put(Command.NODE_SCHEDULING, new CommandNodeScheduling());
        mapCommand.put(Command.NODE_SCHEDULING_REQUEST, new CommandNodeSchedulingRequest());
        mapCommand.put(Command.SUCESS_REFACTORING, new CommandSuccessRefactoring());
        mapCommand.put(Command.FAIL_REFACTORING, new CommandFailRefactoring());
        mapCommand.put(Command.NOT_IMPROVE_NOR_WORSEN_REFACTORING, new CommandNotImproveNorWorsenlRefactoring());
        mapCommand.put(Command.NEXT_BRANCH_TO_SUCESS_REFACTORING, new CommandNextBranchToSucessRefactoring());
        mapCommand.put(Command.NEXT_REFACTORING_BRANCH, new CommandNextRefactoringBranch());
        mapCommand.put(Command.GET_QUALITY_ATTRIBUTE_WITH_METRICS, new CommandGetFullQualitiAtributte());
        mapCommand.put(Command.PREPARE_BD_TO_EXPERIMENTS_PEIXE_ESPADA, new CommandCleanDataToExecuteExperiments());
        //ouriço
        mapCommand.put(Command.PREPARE_AUTOBRANCH, new AutobranchPrepare());
        mapCommand.put(Command.GET_CHECKOUT_BY_AUTOBRANCH, new GetCheckOutByAutobranch());
        mapCommand.put(Command.GET_ALL_CHECKOUTS, new GetAllCheckOuts());
        mapCommand.put(Command.SAVE_CHECKOUT, new SaveCheckOut());
        mapCommand.put(Command.SAVE_ESTADO, new SaveEstado());
        mapCommand.put(Command.GET_ESTADOS_BY_AUTOBRANCH, new GetEstadosByAutobranch());
        mapCommand.put(Command.GET_ESTADO_BY_AUTOBRANCH_DESCRICAO, new GetEstadoByAutobranchDescricao());
        mapCommand.put(Command.CMD_VERIFICATION, new DoVerification());
        mapCommand.put(Command.CMD_RECOVER_PROTECTED_PATH, new RecoverProtectedPath());
        mapCommand.put(Command.CMD_POS_CHECKOUT_VERIFICATION, new DoPosCheckoutVerification());



    }

    public static Command getCommand(String cmd) {
        return mapCommand.get(cmd);
    }
}
