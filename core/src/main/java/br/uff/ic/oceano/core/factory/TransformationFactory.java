/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.factory;

import br.uff.ic.oceano.core.tools.Tool;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author DanCastellani
 */
public class TransformationFactory implements ToolFactory {

    private static final String name = "Transformation Factory";
    private static final String rationale = "A Factory that instantiates some Transformations to automaticaly make new configurations.";
    private static TransformationFactory transformationFactory;
    private static Set<Tool> transformationSet;

    /**
     * Here comes the Tools that this factory provides
     */
    static {
        transformationSet = new LinkedHashSet<Tool>();
    }

    private TransformationFactory() {
    }

    public static ToolFactory getInstance() {
        if (transformationFactory == null) {
            transformationFactory = new TransformationFactory();
        }

        return transformationFactory;
    }

    public String getName() {
        return name;
    }

    public String getRationale() {
        return rationale;
    }

    public Collection<Tool> getTools() {
        return transformationSet;
    }

    public Tool getTool(Class classe) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
