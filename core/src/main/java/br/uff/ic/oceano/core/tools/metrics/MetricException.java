/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.tools.metrics;

/**
 *
 * @author Heliomar
 */
public class MetricException extends Exception{

    public MetricException() {
    }

    public MetricException(String msg) {
        super(msg);
    }

    public MetricException(Throwable t) {
        super(t);
    }

    public MetricException(String msg,Throwable t) {
        super(msg,t);
    }
}
