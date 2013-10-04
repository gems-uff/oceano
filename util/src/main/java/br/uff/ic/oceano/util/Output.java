/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util;

/**
 *
 */
public class Output {

    private static boolean bShowOutput = true;
    private static final String BREAK_LINE = "\n";
    private static StringBuffer logOutput;

    public static void append(String logMessage) {
        if (logOutput == null) {
            logOutput = new StringBuffer();
        }
        logOutput.append(logMessage);
    }

    public static String getLog() {
        return logOutput.toString();
    }

    public static void clearLog() {
        logOutput = new StringBuffer();
    }

    public static void print(String str) {
        if (isbShowOutput()) {
            System.out.print(str);
        }
        append(str);
    }

    public static void println(String str) {
        if (isbShowOutput()) {
            System.out.println(str);
        }
        append(str);
        append(BREAK_LINE);
    }

    /**
     * @return the bShowOutput
     */
    public static boolean isbShowOutput() {
        return bShowOutput;
    }

    /**
     * @param abShowOutput the bShowOutput to set
     */
    public static void setbShowOutput(boolean abShowOutput) {
        bShowOutput = abShowOutput;
    }
}
