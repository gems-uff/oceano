/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.verificacao.svnLook;

import java.util.ArrayList;
import java.util.List;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.admin.ISVNChangedDirectoriesHandler;

/**
 *
 * @author marapao
 */
public class changedDirectories implements ISVNChangedDirectoriesHandler {

    private List<String> directories;

    public changedDirectories() {
        directories = new ArrayList<String>();
    }

    public List<String> getDirectories() {
        return directories;
    }

    public void setDirectories(List<String> directories) {
        this.directories = directories;
    }


    @Override
    public void handleDir(String path) throws SVNException {
        directories.add(path);
    }

    

}
