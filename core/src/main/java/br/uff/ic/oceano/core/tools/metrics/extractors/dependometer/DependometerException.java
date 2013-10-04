package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

/**
 *
 * @author Daniel
 */
public class DependometerException extends Exception{

    public DependometerException(String message) {
        super(message);
    }

    public DependometerException(String message, Exception ex) {
        super(message, ex);
    }

    public DependometerException(Throwable cause) {
        super(cause);
    }
}
