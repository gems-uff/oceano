/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.exception;

/**
 *
 * @author Heliomar
 */
public class ExecutionPlanException extends ServiceException{

    public ExecutionPlanException() {
    }

    public ExecutionPlanException(String msg) {
        super(msg);
    }

    public ExecutionPlanException(Throwable t) {
        super(t);
    }
}
