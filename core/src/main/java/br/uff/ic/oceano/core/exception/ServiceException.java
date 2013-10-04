/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 * @author Heliomar
 */
public class ServiceException extends Exception{

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable t) {
        super(t);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
