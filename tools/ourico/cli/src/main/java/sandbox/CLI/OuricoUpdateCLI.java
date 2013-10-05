/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sandbox.CLI;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.update.OuricoUpdate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.tmatesoft.svn.core.SVNException;
import static sandbox.controller.Parametros.*;

/**
 *
 * @author marapao
 */
public class OuricoUpdateCLI {

    @Option(name=UPDATE, aliases={UPDATE_ALIAS_1}, required=true)
    private String password;

    @Option(name = USERNAME, required = true, usage = USERNAME_USAGE)
    private String username;

    @Option(name=WORKSPACE, required=true, usage=WORKSPACE_USAGE)
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

    public void run(String[] args) {

        try {

            CmdLineParser cmdLineParser = new CmdLineParser(this);
            cmdLineParser.parseArgument(args);

            OuricoUpdate ouricoUpdate = new OuricoUpdate();

            ouricoUpdate.update(workspace, username, username, URL_OCEANO);

            System.out.println("The update in "+workspace+" was performed.");

        } catch (ServiceException ex) {
            Logger.getLogger(OuricoUpdateCLI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SVNException ex) {
            Logger.getLogger(OuricoUpdateCLI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CmdLineException ex) {
            Logger.getLogger(OuricoUpdateCLI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


}
