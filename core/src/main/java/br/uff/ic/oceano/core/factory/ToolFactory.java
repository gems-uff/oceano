/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.factory;

import br.uff.ic.oceano.core.tools.Tool;
import java.util.Collection;

/**
 *
 * @author DanCastellani
 */
public interface ToolFactory {
    public String getName();
    public String getRationale();
    public Collection<Tool> getTools();
    public Tool getTool(Class classe);
}
