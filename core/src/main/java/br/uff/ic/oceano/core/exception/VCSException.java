/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 * @author Heliomar
 */
public class VCSException extends Exception{

    public VCSException() {
    }

    public VCSException(String msg) {
        super(msg);
    }

    public VCSException(Throwable t) {
        super(t);
    }
}
