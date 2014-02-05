/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.discretizer;

/**
 *
 * @author daniel
 */
public abstract class Discretizer {

    protected String attributeTarget;

    protected Discretizer(final String attributeTarget) {
        this.attributeTarget = attributeTarget;
    }

    public abstract String discretize(String s);

    public abstract String getTargetType();

    /**
     * @return the attributeTarget
     */
    public String getAttributeTarget() {
        return attributeTarget;
    }

    /**
     * @param attributeTarget the attributeTarget to set
     */
    public void setAttributeTarget(String attributeTarget) {
        this.attributeTarget = attributeTarget;
    }

    public String getPrefix() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " on " + this.attributeTarget;
    }

    public abstract String getHeaderDeclaration(String substring);
}
