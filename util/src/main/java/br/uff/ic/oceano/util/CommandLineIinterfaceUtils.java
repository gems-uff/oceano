/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author daniel
 */
public class CommandLineIinterfaceUtils {

    public static Process executeComand(String[] comand) throws IOException {
        return Runtime.getRuntime().exec(comand);
    }
    private String comando;

    public CommandLineIinterfaceUtils(String comando) {
        this.comando = comando;
    }

    public String executa() throws Exception {
        try {

            Output.println("   ________________________________________________________________________________________________________________");
            Output.println("  / " + comando + "                                                                                                \\");
            Output.println(" ____________________________________________________________________________________________________________________");
            Output.println("/                                                                                                                    \\");

            String[] environmentVariables = SystemUtil.getEnvironmentVariables();
            Process p = Runtime.getRuntime().exec(comando, environmentVariables);
//            Output.println(">>>>>>>>>> Aguardando processo...");
//            p.waitFor();
//            Output.println(">>>>>>>>>> Processo terminado...");

            BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));            
            StringBuilder sb = new StringBuilder();
            String line = buf.readLine();
            while (line != null) {
                Output.println("|   " + line);
                sb.append(line).append("\n");
                line = buf.readLine();
            }
            Output.println("\\____________________________________________________________________________________________________________________/");
            Output.println("  \\ " + comando + "                                                                                                 /");
            Output.println("   ________________________________________________________________________________________________________________");

            return sb.toString();
        } catch (IOException ex) {
            throw new Exception(ex);
        }
    }
}
