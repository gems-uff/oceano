package br.uff.ic.oceano.util;

/**
 * This Logger uses log4j.
 * 
 */
public class Logger {

    private static String getCallerClassName() {
        final StackTraceElement lastCalledMethod = Thread.currentThread().getStackTrace()[3];
        return lastCalledMethod.getClassName();
    }

    public static void debug(String message) {
        org.apache.log4j.Logger.getLogger(getCallerClassName()).debug(message);
    }

    public static void info(String message) {
        org.apache.log4j.Logger.getLogger(getCallerClassName()).info(message);
    }

    public static void warn(String message) {
        org.apache.log4j.Logger.getLogger(getCallerClassName()).warn(message);
    }

    public static void error(String message) {
        org.apache.log4j.Logger.getLogger(getCallerClassName()).error(message);
    }
}
