/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 * @author Heliomar
 */
public class MetodoInterceptadoException extends RuntimeException{

    public MetodoInterceptadoException() {
        super("O método deveria ter sido interceptado, verifique o interesse ortogonal (provavelmente o método não foi anotado corretamente)");
    }

    public MetodoInterceptadoException(String msg) {
        super(msg);
    }

    public MetodoInterceptadoException(Throwable t) {
        super(t);
    }
}
