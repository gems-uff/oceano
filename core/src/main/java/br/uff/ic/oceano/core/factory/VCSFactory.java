/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.factory;

import br.uff.ic.oceano.core.tools.Tool;
import br.uff.ic.oceano.core.tools.vcs.SVN_By_SVNKit;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author DanCastellani
 */
public class VCSFactory implements ToolFactory {

    private static final String name = "Version Control System Factory";
    private static final String rationale = "A Factory that instantiates the Version Control Systems";
    private static VCSFactory vcsFactory;
    private static Set<Tool> vcsSet;

    /**
     * Here comes the Tools that this factory provides
     */
    static {
        vcsSet = new LinkedHashSet<Tool>();
        vcsSet.add(new SVN_By_SVNKit());
    }

    private VCSFactory() {
    }

    public static ToolFactory getInstance() {
        if (vcsFactory == null) {
            vcsFactory = new VCSFactory();
        }

        return vcsFactory;
    }

    public String getName() {
        return name;
    }

    public String getRationale() {
        return rationale;
    }

    public Collection<Tool> getTools() {
        return vcsSet;
    }

    public Tool getTool(Class classe) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
