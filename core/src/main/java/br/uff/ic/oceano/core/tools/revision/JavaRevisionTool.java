/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.revision;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.vcs.VCSUtil;
import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.ostra.controle.Constantes;
import static br.uff.ic.oceano.ostra.controle.Constantes.DOT_JAVA;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.io.File;
import java.util.*;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

/**
 *
 * @author DanCastellani TODO Define Interface and create RevisionUtil
 * implementation for each supported language
 */
public class JavaRevisionTool extends AbstractRevisionTool implements RevisionTool {

    public JavaRevisionTool() {
    }

    public void validate(Revision revision) throws Exception {
        if (revision == null) {
            throw new Exception("Revision is null");
        } else if (revision.getProject() == null) {
            throw new Exception("Revision project is null");
        }

        Language lang = revision.getProject().getLanguage();
        if (!lang.equals(Language.JAVA)) {
            throw new Exception("Unsupported language: " + lang);
        }

        if (!revision.getProject().isMavenProject()) {
            throw new Exception("Only Maven projects are supported");
        }
    }

    /**
     * Return absolute source files paths. Ignores files on VCS path or which
     * contains Maven test folder pattern.
     *
     * @param revision
     * @return files string paths
     * @throws Exception
     */
    public Set<String> getSourceFiles(Revision revision) throws Exception {
        validate(revision);

        Set<String> pathsForSourceFiles = new HashSet<String>();

        for (String classPath : getSourceClassPaths(revision)) {
            File fileBuffer = new File(classPath);
            for (String filePath : FileUtils.getAllFilesInFolderAndSubFolders(fileBuffer, Constantes.DOT_JAVA)) {
                fileBuffer = new File(filePath);
                if (!fileBuffer.exists()
                        || VCSUtil.isVCSpath(filePath)
                        || filePath.contains(MavenUtil.MAVEN2_BASE_TEST_FOLDER)) {
                    continue;
                }

                if (!fileBuffer.isAbsolute()) {
                    filePath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(filePath);
                }

                pathsForSourceFiles.add(filePath);
            }
        }
        return pathsForSourceFiles;
    }

    /**
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public Set<VersionedItem> getSourceFilesFromChangedFiles(Revision revision) throws Exception {
        validate(revision);

        Set<VersionedItem> dotJavaFiles = new HashSet<VersionedItem>();

        if (revision.getChangedFiles() == null) {
            return dotJavaFiles;
        }

        for (VersionedItem changedFile : revision.getChangedFiles()) {
            String absolutePath = PathUtil.getWellFormedPath(changedFile.getItem().getPath());

            if (!isJavaFilePath(absolutePath) //not java file
                    || absolutePath.contains(MavenUtil.MAVEN2_BASE_MAIN_SOURCE_FILES) //ignores test packages
                    ) {
                continue;
            }

            dotJavaFiles.add(changedFile);
        }
        return dotJavaFiles;
    }

    /**
     * //TODO return package with dot as separator
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public Set<String> getPackages(Revision revision) throws Exception {
        validate(revision);

        Set<String> packages = new HashSet<String>();

        for (String filePath : getSourceFiles(revision)) {
            if (isJavaFilePath(filePath)) {
                //get the package of this file
                packages.add(filePath.substring(0, filePath.lastIndexOf(SystemUtil.FILESEPARATOR)));
            }
        }
        return packages;
    }

    /**
     *
     * @param revision
     * @return
     * @throws Exception
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
            //we consider only src/main/java classes
            if (!filePath.contains(MavenUtil.MAVEN2_BASE_MAIN_SOURCE_FILES)) {
                continue;
            }

            if (isJavaFilePath(filePath)) {
                //get the package of this file
                VersionedItem changedFilePackage = new VersionedItem();
                changedFilePackage.setType(changedFile.getType());
                changedFilePackage.setRevision(revision);

                Item i = new Item();
                i.setPath(filePath.substring(0, filePath.lastIndexOf(SystemUtil.FILESEPARATOR)));
                changedFilePackage.setItem(i);

                packages.add(changedFilePackage);
            }
        }
        return packages;
    }

    /**
     * Return path of compilation. Example: ...\target. Only for MAVEN projects.
     * In case of subprojects, there will be several results.
     *
     * @param revision
     * @return
     */
    public Set<String> getCompilationFolders(Revision revision) throws Exception {
        if (!revision.getProject().isMavenProject()) {
            throw new Exception("Not a maven project");
        }

        validate(revision);

        final String fileSeparator = SystemUtil.FILESEPARATOR;
        String pathclasses = revision.getLocalPath();
        if (!pathclasses.endsWith(fileSeparator)) {
            pathclasses = pathclasses.concat(fileSeparator);
        }

        File file = new File(pathclasses);
        if (!file.isAbsolute()) {
            pathclasses = PathUtil.getAbsolutePathFromRelativetoCurrentPath(pathclasses);
            file = new File(pathclasses);
        }

        List<String> folderNames = FileUtils.getAllFoldersAndSubFoldersEndsWith(file, MavenUtil.MAVEN2_BASE_COMPILED_FILES);
        if (folderNames == null) {
            throw new Exception("No folders found");
        }
        return new HashSet<String>(folderNames);
    }

    /**
     * Return absolute source class paths. Ignores files on VCS path or which
     * contains Maven test folder pattern.
     *
     * @param revision
     * @return
     */
    @Override
    public Set<String> getSourceClassPaths(Revision revision) throws Exception {
        validate(revision);

        File revisionPath = new File(revision.getLocalPath());
        Set<String> sourcePaths = new HashSet<String>();
        for (String possibleSourcePath : FileUtils.getAllFoldersAndSubFoldersContains(revisionPath, MavenUtil.MAVEN2_BASE_MAIN_SOURCE_FILES)) {

            File file = new File(possibleSourcePath);

            if (!file.exists()
                    || VCSUtil.isVCSpath(possibleSourcePath)
                    || possibleSourcePath.contains(MavenUtil.MAVEN2_BASE_TEST_FOLDER)) {
                continue;
            }

            if (!file.isAbsolute()) {
                possibleSourcePath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(possibleSourcePath);
            }

            sourcePaths.add(possibleSourcePath);
        }
        return sourcePaths;
    }

    /**
     * Return absolute path of main source files of a project revision.
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public String getMainSourcePath(Revision revision) throws Exception {
        validate(revision);

        File revisionPath = new File(revision.getLocalPath());
        List<String> sourcePaths = FileUtils.getAllFoldersAndSubFoldersEndsWith(revisionPath, MavenUtil.MAVEN2_BASE_MAIN_SOURCE_FILES);

        if (sourcePaths == null || sourcePaths.isEmpty()) {
            return null;
        } else if (sourcePaths.size() > 1) {
            throw new Exception("Oversized result found");
        }

        String possibleSourcePath = sourcePaths.get(0);

        File file = new File(possibleSourcePath);

        if (!file.exists()) {
            throw new Exception("Directory '" + possibleSourcePath + "' does not exist");
        } else if (VCSUtil.isVCSpath(possibleSourcePath)) {
            throw new Exception("Directory '" + possibleSourcePath + "' is from VCS");
        } else if (possibleSourcePath.contains(MavenUtil.MAVEN2_BASE_TEST_FOLDER)) {
            throw new Exception("Directory '" + possibleSourcePath + "' is from test classes");
        }

        if (!file.isAbsolute()) {
            possibleSourcePath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(possibleSourcePath);
        }
        return possibleSourcePath;
    }
    
    public String getMainCompilationFolder(Revision revision) throws Exception {
        validate(revision);

        File revisionPath = new File(revision.getLocalPath());
        List<String> sourcePaths = FileUtils.getAllFoldersAndSubFoldersEndsWith(revisionPath, MavenUtil.MAVEN2_BASE_COMPILED_FILES);

        if (sourcePaths == null || sourcePaths.isEmpty()) {
            return null;
        } else if (sourcePaths.size() > 1) {
            throw new Exception("Oversized result found");
        }

        String possibleSourcePath = sourcePaths.get(0);

        File file = new File(possibleSourcePath);

        if (!file.exists()) {
            throw new Exception("Directory '" + possibleSourcePath + "' does not exist");
        } else if (VCSUtil.isVCSpath(possibleSourcePath)) {
            throw new Exception("Directory '" + possibleSourcePath + "' is from VCS");
        } else if (possibleSourcePath.contains(MavenUtil.MAVEN2_BASE_TEST_FOLDER)) {
            throw new Exception("Directory '" + possibleSourcePath + "' is from test classes");
        }

        if (!file.isAbsolute()) {
            possibleSourcePath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(possibleSourcePath);
        }
        return possibleSourcePath;
    }

    /**
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public List<String> getCompiledClassNames(Revision revision) throws Exception {
        validate(revision);

        final Set<String> fileNames = getPathsFromCompiledJavaClasses(revision);

        List<String> classNames = new LinkedList();
        for (String filePath : fileNames) {
            ClassParser cp = new ClassParser(filePath);
            JavaClass jc = cp.parse();
            classNames.add(jc.getClassName());
        }
        return classNames;
    }

    /**
     *
     * @param revision
     * @return
     * @throws Exception
     */
    public Set<String> getPathsFromCompiledJavaClasses(Revision revision) throws Exception {
        validate(revision);

        Set<String> pathsForCompiledClasses = new HashSet<String>();
        for (String aClassPath : getCompilationFolders(revision)) {
            final File revisionPath = new File(aClassPath);
            pathsForCompiledClasses.addAll(FileUtils.getAllFilesInFolderAndSubFolders(revisionPath, Constantes.DOT_CLASS));
        }
        return pathsForCompiledClasses;
    }

    private boolean isJavaFilePath(String path) {
        return path.endsWith(DOT_JAVA);
    }

    
}
