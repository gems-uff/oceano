/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.controller.servlet.command;

import br.uff.ic.oceano.core.util.DefaultDatabaseLoader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Heliomar
 */
public class CommandAdminChargingBDDefault implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DefaultDatabaseLoader.insertDefaultDataProduction();
        response.getWriter().println("DataBase was populed with sucessfull");
    }

}
