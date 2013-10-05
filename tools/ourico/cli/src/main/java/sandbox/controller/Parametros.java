/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sandbox.controller;

import br.uff.ic.oceano.ourico.rcs.context.Constants;

/**
 *
 * @author marapao
 */
public class Parametros {

    public static final String VERSION = "-version";
    public static final String VERSION_ALIASES_1 = "-v";
    public static final String VERSION_USAGE = "Choose a version to check it out";

    public static final String PASSWORD = "-password";
    public static final String PASSWORD_USAGE = "Passwork of revision control system";
    
    public static final String USERNAME = "-username";
    public static final String USERNAME_USAGE = "Username of revision control system";
    
    public static final String URL = "-url";
    public static final String URL_USAGE = "Url of the repository";

    
    public static final String COMMAND = "-command";
    public static final String COMMAND_USAGE = "Choose the action: checkout, checkin and so on.";
    
    public static final String WORKSPACE = "-workspace";
    public static final String WORKSPACE_USAGE = "Path of workspace";



    
    public static final String COMMAND_CHECKOUT = "checkout";
    public static final String COMMAND_CO= "co";
    public static final String CHECKOUT = "-checkout";
    public static final String CHECKOUT_ALIAS_1 = "-co";

    public static final String COMMAND_UPDATE = "update";
    public static final String COMMAND_UP = "up";
    public static final String UPDATE = "-update";
    public static final String UPDATE_ALIAS_1 = "-up";

    public static final String URL_OCEANO = Constants.URL_OCEANO;



}
