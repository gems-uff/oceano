/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ostra.exception;

/**
 *
 * @author Heliomar
 */
public class DataMiningException extends Exception{

    public DataMiningException() {
    }

    public DataMiningException(String msg) {
        super(msg);
    }

    public DataMiningException(Throwable t) {
        super(t);
    }
}
