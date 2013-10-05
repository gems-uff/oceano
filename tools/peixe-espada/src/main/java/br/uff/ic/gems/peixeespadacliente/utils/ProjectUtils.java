package br.uff.ic.gems.peixeespadacliente.utils;

import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.commonIDE.IDEController;
import net.sf.refactorit.utils.ClasspathUtil;
import net.sf.refactorit.vfs.local.LocalClassPath;
import net.sf.refactorit.vfs.local.LocalSourcePath;

/**
 *
 * @author Heliomar
 */
public class ProjectUtils {

    private ProjectUtils() {
    }

    public static Project getProjectRefactoring(ProjectVCS project) throws Exception {
        File pomMaven = new File(project.getLocalPath(), "pom.xml");
//        Project projectRefactorIt = IDEController.getInstance().getActiveProject();
//        if(projectRefactorIt != null && projectRefactorIt.getPaths().getSourcePath().toString().equals(project.getLocalPath())){
//            return projectRefactorIt;
//        }
        List<String> classPaths = null;
        if (!IDEController.runningTest()) {
            classPaths = new ArrayList(Arrays.asList(ClasspathUtil.getDefaultClasspath().split(";")));
            if (pomMaven.exists()) {
                classPaths.addAll(br.uff.ic.oceano.core.util.MavenUtil.getProjectClassPaths(project.getLocalPath()));
            }
        } else {
            Project projectRefactorIt = IDEController.getInstance().getActiveProject();
            if (projectRefactorIt != null && new File(projectRefactorIt.getPaths().getSourcePath().toString()).equals(new File(project.getLocalPath()))) {
                return projectRefactorIt;
            } else {
                classPaths = br.uff.ic.oceano.core.util.MavenUtil.getProjectClassPaths(project.getLocalPath());
            }
        }
        return new Project(project.getName(), new LocalSourcePath(project.getLocalPath()), new LocalClassPath(classPaths), null);
    }

    public static Revision getRevisionWithAllSourceChanded(Revision revision) throws IOException {
        Set<VersionedItem> changedFiles = new HashSet<VersionedItem>();

        List<File> allProjectFiles = addAllFiles(new File(revision.getLocalPath()));
        String basePath = revision.getLocalPath();
        int lenthBasePath = basePath.endsWith(System.getProperty("file.separator")) ? basePath.length() : basePath.length() - 1;
        for (File file : allProjectFiles) {
            VersionedItem versionedItem = new VersionedItem();
            versionedItem.setType('M');
            versionedItem.setRevision(revision);
            versionedItem.setItem(new Item(file.getAbsolutePath().substring(lenthBasePath)));
            changedFiles.add(versionedItem);
        }
        revision.setChangedFiles(changedFiles);
        return revision;
    }

    public static List<File> addAllFiles(File file) throws IOException {
        List<File> files = new ArrayList<File>();
        if (file.isDirectory()) {
            if (!file.getName().equals(".svn") && !file.getName().equals("target")) {
                for (File file1 : file.listFiles()) {
                    if (file1.isDirectory()) {
                        if (!file1.getName().equals(".svn")) {
                            files.addAll(addAllFiles(file1));
                        }
                    } else {
                        files.add(file1);
                    }
                }
            }
        } else {
            files.add(file);
        }
        return files;
    }
//    public static List<File> addAllFiles(File file) {
//        List<File> files = new ArrayList<File>();
//        if (file.isDirectory()) {
//            files.addAll(Arrays.asList(file.listFiles()));
//            for (File subFile : file.listFiles()) {
//                files.addAll(addAllFiles(subFile));
//            }
//        }else{
//            files.add(file);
//        }
//        return files;
//    }
}
