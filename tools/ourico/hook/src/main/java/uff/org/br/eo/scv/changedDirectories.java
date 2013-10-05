/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uff.org.br.eo.scv;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
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
