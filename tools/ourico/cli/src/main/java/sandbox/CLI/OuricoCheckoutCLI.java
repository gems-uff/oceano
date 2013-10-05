/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sandbox.CLI;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.ourico.checkout.ciclo.CheckoutCiclo;
import br.uff.ic.oceano.ourico.model.CheckOut;
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
public class OuricoCheckoutCLI {

    @Option(name=URL, usage=URL_USAGE, required=true)
    private String url;

    @Option(name = VERSION, usage = VERSION_USAGE, aliases = {VERSION_ALIASES_1})
    private String version;

    @Option(name=PASSWORD, usage=PASSWORD_USAGE, required=true)
    String password;

    @Option(name=USERNAME, usage=USERNAME_USAGE, required=true)
    String username;

    @Option(name=WORKSPACE, usage=WORKSPACE_USAGE, required=true)
    String workspace;

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

    public void run(String[] args){

        CmdLineParser cmdLineParser = new CmdLineParser(this);

        try {

            cmdLineParser.parseArgument(args);
//            System.out.println("version = "+versio\n+" url = "+url);

            CheckoutCiclo checkoutCiclo = new CheckoutCiclo();
            try {
                //                System.out.println("url = " + url);
                //                System.out.println("password = " + password);
                //                System.out.println("username = " + username);
                //                System.out.println("workspace = " + workspace);
                CheckOut remoto = checkoutCiclo.remoto(url, password, username, workspace, URL_OCEANO);

                System.out.println("The check-out from url "+url+" was successfully peformed at revision "+remoto.getRevisao()+".");
            } catch (ServiceException ex) {
                System.out.println("Problema com o servidor");
                Logger.getLogger(OuricoCheckoutCLI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SVNException ex) {
                Logger.getLogger(OuricoCheckoutCLI.class.getName()).log(Level.SEVERE, null, ex);
            }


            
        } catch (CmdLineException e) {

            System.err.println(e.getMessage());
           System.err.println("\nTester [options...] arguments...");
           cmdLineParser.printUsage(System.err);
        }

    }
}
