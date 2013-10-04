/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.SystemUtil;

/**
 *
 * @author Daniel Heraclio
 */
public abstract class AbstractRevisionTool {

    private static final String SEPARATOR = ".";

    protected AbstractRevisionTool() {
    }

    protected String pathToPackageNotation(String path) {
        //remove file separators
        path = PathUtil.getWellFormedPath(path);
        path = path.replace(SystemUtil.FILESEPARATOR, SEPARATOR);
        
        //remove last file separator
        if(path.endsWith(SEPARATOR)){
            path = path.substring(0, path.length()-1);
        }
        //Packages are lowcase
        path = path.toLowerCase();
        
        return path;
    }
}
