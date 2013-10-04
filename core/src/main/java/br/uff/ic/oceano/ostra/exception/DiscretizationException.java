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
public class DiscretizationException extends ServiceException{

    public DiscretizationException() {
    }

    public DiscretizationException(String msg) {
        super(msg);
    }

    public DiscretizationException(Throwable t) {
        super(t);
    }
}
