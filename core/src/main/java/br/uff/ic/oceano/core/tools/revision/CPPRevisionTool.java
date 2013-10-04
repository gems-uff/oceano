/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.vcs.VCSUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.util.SystemUtil;
import java.io.File;
import java.util.*;

/**
 *
 * @author DanCastellani TODO Define Interface and create RevisionUtil
 * implementation for each supported language
 */
public class CPPRevisionTool extends AbstractRevisionTool implements RevisionTool {

    private final static String[] EXTENSIONS = {".c", ".cc", ".h", ".cpp"};

    public CPPRevisionTool() {
    }

    public void validate(Revision revision) throws Exception {
        if (revision == null) {
            throw new Exception("Revision is null");
        } else if (revision.getProject() == null) {
            throw new Exception("Revision project is null");
        }

        Language lang = revision.getProject().getLanguage();

        if (!lang.equals(Language.CPP)) {
            throw new Exception("Unsupported language: " + lang);
        }
    }

    /**
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public Set<String> getSourceFiles(Revision revision) throws Exception {
        validate(revision);

        return getSourceFiles(revision.getLocalPath());
    }

    @Override
    public Set<VersionedItem> getSourceFilesFromChangedFiles(Revision revision) throws Exception {
        validate(revision);

        Set<VersionedItem> files = new HashSet<VersionedItem>();

        //no changed files
        if (revision.getChangedFiles() == null) {
            return files;
        }

        for (VersionedItem changedFile : revision.getChangedFiles()) {
            String absolutePath = PathUtil.getWellFormedPath(changedFile.getItem().getPath());
            if (VCSUtil.isVCSpath(absolutePath)) {
                continue;
            }
            if (isCppFilePath(absolutePath)) {
                files.add(changedFile);
            }
        }
        return files;
    }

    /**
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public Set<String> getSourceClassPaths(Revision revision) throws Exception {
        validate(revision);

        Set<String> paths = new HashSet<String>();
        List<String> directories = FileUtils.getAllFoldersAndSubFoldersEndsWith(new File(revision.getLocalPath()), null);
        for (String path : directories) {
            //ignore vcs dirs and files
            if (VCSUtil.isVCSpath(path)) {
                continue;
            }

            File file = new File(path);
            if (!file.exists()) {
                continue;
            } else if (!file.isAbsolute()) {
                path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);
            }

            paths.add(path);
            file = null;
        }
        return paths;
    }
    
    /**
     * 
     * @param revision
     * @return
     * @throws Exception 
     */
    public Set<String> getPackages(Revision revision) throws Exception {
        
        validate(revision);
        String revisionPath = revision.getLocalPath();
        File revisionFile = new File(revisionPath);
        if (!revisionFile.isAbsolute()) {
            revisionPath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(revisionPath);
        }
        Set<String> packages = new HashSet<String>();
        List<String> directories = FileUtils.getAllFoldersAndSubFoldersEndsWith(revisionFile, null);
        for (String path : directories) {
            //ignore vcs dirs and files
            if (VCSUtil.isVCSpath(path)) {
                continue;
            }

            File file = new File(path);
            if (!file.exists()) {
                continue;
            }

            assert file.isDirectory();

            if (!file.isAbsolute()) {
                path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);
            }
            
            //Packages are relative
            path = path.replace(revisionPath, "");
            
            //fix notation
            path = pathToPackageNotation(path);
            if(!path.isEmpty()){
                packages.add(path);
            }
            
            //release file
            file = null;
        }
        return packages;
    }

    /**
     *
     * //TODO extract real namespace with C++ parser, for now use path as
     * namespace
     *
     * @param revision
     * @return
     */
    public Set<VersionedItem> getPackagesFromChangedFiles(Revision revision) throws Exception {
        validate(revision);

        Set<VersionedItem> packages = new HashSet<VersionedItem>();

        //its still empty
        if (revision.getChangedFiles() == null) {
            return packages;
        }

        for (VersionedItem changedFile : revision.getChangedFiles()) {

            //to garantee that every \ or / is changed for the OS-SLASH
            final String filePath = PathUtil.getWellFormedPath(changedFile.getItem().getPath());
            if (VCSUtil.isVCSpath(filePath)) {
                continue;
            }
            if (!isCppFilePath(filePath)) {
                continue;
            }
            //get the package of this file
            VersionedItem changedFilePackage = new VersionedItem();
            changedFilePackage.setType(changedFile.getType());
            changedFilePackage.setRevision(revision);

            Item i = new Item();
            i.setPath(filePath.substring(0, filePath.lastIndexOf(SystemUtil.FILESEPARATOR)));
            changedFilePackage.setItem(i);

            packages.add(changedFilePackage);
        }
        return packages;
    }

    /**
     * Return absolute paths of CPP files on root folder and subfolders.
     *
     * @author dheraclio
     * @param folder root directory.
     * @return
     */
    public Set<String> getSourceFiles(String folder) throws Exception {
        final List<String> extensions = new LinkedList<String>(Arrays.asList(EXTENSIONS));

        folder = PathUtil.getWellFormedPath(folder);

        if (PathUtil.isRelativePath(folder)) {
            folder = PathUtil.getAbsolutePathFromRelativetoCurrentPath(folder);
        }

        File file = new File(folder);
        Set<String> pathsForSourceFiles = new HashSet<String>();
        for (String filePath : FileUtils.getAllFilesInFolderAndSubFolders(file, extensions)) {
            pathsForSourceFiles.add(filePath);
        }

        return pathsForSourceFiles;
    }

    public Set<String> getCompilationFolders(Revision revision) throws Exception {
        throw new Exception("Not supported for Cpp projects");
    }

    /**
     *
     * @param path
     * @return
     */
    public boolean isCppFilePath(String path) {
        for (String extension : EXTENSIONS) {
            if (path.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
