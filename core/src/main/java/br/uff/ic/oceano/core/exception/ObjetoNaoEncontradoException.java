/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 * @author Heliomar
 */
public class ObjetoNaoEncontradoException extends Exception{

    public ObjetoNaoEncontradoException() {
    }

    public ObjetoNaoEncontradoException(String msg) {
        super(msg);
    }

    public ObjetoNaoEncontradoException(Throwable t) {
        super(t);
    }
}
