/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 * @author Heliomar
 */
public class InfraestruturaException extends RuntimeException{

    public InfraestruturaException() {
    }

    public InfraestruturaException(String msg) {
        super(msg);
    }

    public InfraestruturaException(Throwable t) {
        super(t);
    }
}
