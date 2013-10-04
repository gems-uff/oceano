/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ostra.exception;

import br.uff.ic.oceano.core.exception.ServiceException;

/**
 *
 * @author DanCastellani
 */
public class CompilerException extends ServiceException{

    public CompilerException() {
    }

    public CompilerException(String msg) {
        super(msg);
    }

    public CompilerException(Throwable t) {
        super(t);
    }
}
