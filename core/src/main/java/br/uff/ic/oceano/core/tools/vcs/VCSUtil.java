package br.uff.ic.oceano.core.tools.vcs;

import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.HashUtil;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 *
 * @author Daniel
 */
public class VCSUtil {

    public static boolean isVCSpath(String path) {
        return isGitPath(path) || isSVNPath(path);
    }

    public static boolean isSVNPath(String path) {
        final String svnRoot = ".svn";
        return path.toLowerCase().contains(svnRoot);
    }

    public static boolean isGitPath(String path) {
        final String gitRoot = ".git";
        return path.toLowerCase().contains(gitRoot);
    }

    public static String getMD5(Revision revision) throws VCSException {
        try {
            File file = new File(revision.getLocalPath());
            Set<String> paths = FileUtils.getAllFilesInFolderAndSubFolders(file);
            file = null; //release
            
            //Ignore VCS directory files
            final Predicate predicate = new Predicate() {
                 public boolean evaluate(Object input) {
                    if (input instanceof String) {
                        return !VCSUtil.isVCSpath((String) input);
                    } else {
                        return false;
                    }
                }
            };
            CollectionUtils.filter(paths, predicate);

            //unsorted collections result in different MD5.
            List sorted = new LinkedList(paths);
            Collections.sort(sorted);

            return HashUtil.getMD5(sorted);
        } catch (Exception ex) {
            throw new VCSException(ex);
        }
    }
}
