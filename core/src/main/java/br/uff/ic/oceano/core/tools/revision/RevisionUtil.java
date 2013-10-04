/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.util.*;

/**
 *
 * @author DanCastellani
 */
public class RevisionUtil implements RevisionTool {

    private static RevisionUtil self = new RevisionUtil();
    private RevisionTool javaTool = null;
    private RevisionTool cppTool = null;

    private RevisionUtil() {
        javaTool = new JavaRevisionTool();
        cppTool = new CPPRevisionTool();
    }

    public static RevisionUtil get() {
        return self;
    }

    public Set<VersionedItem> getSourceFilesFromChangedFiles(Revision revision) throws Exception {
        return getRevisionTool(revision).getSourceFilesFromChangedFiles(revision);
    }

    public Set<VersionedItem> getPackagesFromChangedFiles(Revision revision) throws Exception {
        return getRevisionTool(revision).getPackagesFromChangedFiles(revision);
    }

    public Set<String> getSourceFiles(Revision revision) throws Exception {
        return getRevisionTool(revision).getSourceFiles(revision);
    }

    public Set<String> getPackages(Revision revision) throws Exception {
        return getRevisionTool(revision).getPackages(revision);
    }

    public Set<String> getCompilationFolders(Revision revision) throws Exception {
        return getRevisionTool(revision).getCompilationFolders(revision);
    }

    public void validate(Revision revision) throws Exception {
        getRevisionTool(revision).validate(revision);
    }

    public Set<String> getSourceClassPaths(Revision revision) throws Exception {
        return getRevisionTool(revision).getSourceClassPaths(revision);
    }

    private RevisionTool getRevisionTool(Revision revision) throws Exception {

        if (revision == null) {
            throw new Exception("Revision is null");
        } else if (revision.getProject() == null) {
            throw new Exception("Revision project is null");
        }

        Language lang = revision.getProject().getLanguage();

        if (lang.equals(Language.CPP)) {
            return cppTool;
        } else if (lang.equals(Language.JAVA)) {
            return javaTool;
        } else {
            throw new Exception("Unsupported language: " + lang);
        }
    }
}
