package br.uff.ic.oceano.core.tools.metrics.extractors.cpp.easycount;

/**
 *
 * @author Daniel
 */
public class EasyCountServiceException extends Exception{

    public EasyCountServiceException(String message) {
        super(message);
    }

    public EasyCountServiceException(Throwable cause) {
        super(cause);
    }

    public EasyCountServiceException(String string, Exception ex) {
        super(string, ex);
    }

}
