/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.tools.maven;

/**
 *
 * @author Heliomar
 */
public class MavenException extends Exception{

    public MavenException() {
    }

    public MavenException(String msg) {
        super(msg);
    }

    public MavenException(Throwable t) {
        super(t);
    }
}
