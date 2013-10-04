/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.rcs.context;


/**
 *
 * @author Heliomar
 */
public class Constants {

//    public static final String URL_OCEANO = "https://gems.ic.uff.br/oceano/JSONServlet";
    public static final String URL_OCEANO = "http://10.0.0.104:8080/oceano/JSONServlet";
//    public static final String URL_OCEANO = "http://10.0.0.102:8092/oceano/JSONServlet";
//    public static final String URL_OCEANO = "http://192.168.0.104:8092/oceano/JSONServlet";
    public static String REPOSITORY_MAVEN_LOCAL_DEFAULT;
    public static String SETTINGS_MAVEN_DEFAULT;

    static {
        String separetor = System.getProperty("file.separator");
        SETTINGS_MAVEN_DEFAULT = System.getProperty("user.home").concat(separetor).concat(".m2").concat(separetor).concat("settings.xml");
        REPOSITORY_MAVEN_LOCAL_DEFAULT = System.getProperty("user.home").concat(separetor).concat(".m2").concat(separetor).concat("repository");
    }

}
