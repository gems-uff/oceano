/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Heliomar
 */
public class ProtocolService {
    public static final String CMD_EXCEPTION = "cmd=exception";

    public String sendMessageToOcean(Object obj, String urlServlet) throws MalformedURLException, IOException {
        URL url = new URL(urlServlet);
        URLConnection urlc = url.openConnection();
        urlc.setDoOutput(true);

        OutputStreamWriter wr = new OutputStreamWriter(urlc.getOutputStream());
        wr.write(obj.toString() + "&adressToString=" + InetAddress.getLocalHost().toString());
        wr.flush();

        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder("");
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        wr.close();
        rd.close();

        return result.toString();
    }

    public String getMessageServer(Object obj, String urlServlet) throws ServiceException {
        String retorno = null;
        try {
            retorno = sendMessageToOcean(obj, urlServlet);
        } catch (MalformedURLException ex) {
            throw new ServiceException(ex);
        } catch (IOException ex) {
            throw new ServiceException(ex);
        }
        if (retorno.startsWith(CMD_EXCEPTION)) {
            throw new ServiceException(retorno.substring(CMD_EXCEPTION.length()));
        }
        return retorno;
    }
}
