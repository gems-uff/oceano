/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.util.Set;

/**
 * Defines common interface for different languages.
 *
 * @author Daniel
 */
public interface RevisionTool {

    public void validate(Revision revision) throws Exception;

    public Set<String> getSourceFiles(Revision revision) throws Exception;

    public Set<VersionedItem> getSourceFilesFromChangedFiles(Revision revision) throws Exception;

    /**
     * Return pakage with dot as separator.
     * Use getSourceClassPaths to get package directories
     * @param revision
     * @return
     * @throws Exception 
     */
    public Set<String> getPackages(Revision revision) throws Exception;

    /**
     * Return pakage with dot as separator
     * @param revision
     * @return
     * @throws Exception 
     */
    public Set<VersionedItem> getPackagesFromChangedFiles(Revision revision) throws Exception;

    public Set<String> getSourceClassPaths(Revision revision) throws Exception;

    public Set<String> getCompilationFolders(Revision revision) throws Exception;
}
