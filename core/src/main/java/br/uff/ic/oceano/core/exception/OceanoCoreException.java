/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 */
public class OceanoCoreException extends Exception{

    public OceanoCoreException() {
    }

    public OceanoCoreException(String msg) {
        super(msg);
    }

    public OceanoCoreException(Throwable t) {
        super(t);
    }

    public OceanoCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
