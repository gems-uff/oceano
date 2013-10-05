/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbox.CLI;

import java.util.ArrayList;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import static sandbox.controller.Parametros.*;

/**
 *
 * @author marapao
 */
public class OuricoChooserCLI {

    @Option(name = COMMAND, required = true, usage = COMMAND_USAGE)
    private String command;
    @Option(name = PASSWORD, required = true, usage = PASSWORD_USAGE)
    private String password;
    @Option(name = USERNAME, required = true, usage = USERNAME_USAGE)
    private String username;
    @Option(name = VERSION, aliases = {VERSION_ALIASES_1}, usage = VERSION_USAGE)
    private String version;
    @Option(name = URL, usage = URL_USAGE)
    private String url;
    @Option(name = WORKSPACE, usage = WORKSPACE)
    private String workspace;

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public void run(String[] args) {

        CmdLineParser cmdLineParser = new CmdLineParser(this);

        try {

            cmdLineParser.parseArgument(args);

            ArrayList<String> array = new ArrayList<String>();


            if (command.equals(COMMAND_CHECKOUT) || command.equals(COMMAND_CO)) {


                array.add(PASSWORD);
                array.add(password);

                array.add(USERNAME);
                array.add(username);


                if (workspace != null) {
                    array.add(WORKSPACE);
                    array.add(workspace);
                }
                
                if (version != null) {
                    array.add(VERSION);
                    array.add(version);
                }

                if (url != null) {
                    array.add(URL);
                    array.add(url);
                }

                String[] parametros = new String[array.size()];
                int i = 0;

                for (String string : array) {
                    parametros[i++] = string;
                }

                OuricoCheckoutCLI checkout = new OuricoCheckoutCLI();
                checkout.run(parametros);

            } else if(command.equals(COMMAND_UPDATE) || command.equals(COMMAND_UP)){

                array.add(PASSWORD);
                array.add(password);

                array.add(USERNAME);
                array.add(username);


                if (workspace != null) {
                    array.add(WORKSPACE);
                    array.add(workspace);
                }

            }
        } catch (CmdLineException ex) {
            System.err.println(ex.getMessage());
            cmdLineParser.printUsage(System.err);
        }
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the workspace
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }
}
