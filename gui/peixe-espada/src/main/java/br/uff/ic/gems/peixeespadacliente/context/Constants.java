package br.uff.ic.gems.peixeespadacliente.context;

import br.uff.ic.gems.peixeespadacliente.utils.ResourceUtil;

/**
 *
 * @author Heliomar
 */
public class Constants {

    public static final String URL_OCEANO = ResourceUtil.getStringBundleProperties("URL_OCEAN") + "/JSONServlet";
    public static String REPOSITORY_MAVEN_LOCAL_DEFAULT;
    public static String SETTINGS_MAVEN_DEFAULT;
    public static String WORKSPACE_TEMP_DIRECTORY = "peixeespada_workspaces";

    public static String ORIGINAL_DIRECTORY = "original";
    public static String MODIFIED_DIRECTORY = "modified";

    static {
        String separetor = System.getProperty("file.separator");
        SETTINGS_MAVEN_DEFAULT = System.getProperty("user.home").concat(separetor).concat(".m2").concat(separetor).concat("settings.xml");
        REPOSITORY_MAVEN_LOCAL_DEFAULT = System.getProperty("user.home").concat(separetor).concat(".m2").concat(separetor).concat("repository");
    }
}
