/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util.file;

import br.uff.ic.oceano.util.SystemUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class PathUtil {

    public static String getCurrentAbsolutePath() {
        File file = new File(".");
        String path = file.getAbsolutePath();
        path = path.replace(".", "");
        return path;
    }

    /**
     * Returns absolute path from path relative to current path (.).
     *
     * @param refPath
     * @return
     */
    public static String getAbsolutePathFromRelativetoCurrentPath(String refPath) {

        if (refPath == null) {
            return refPath;
        }

        //Not a relative path
        if (!isRelativePath(refPath)) {
            return refPath;
        }

        //fix file separator
        refPath = getWellFormedPath(refPath);

        String path = getCurrentAbsolutePath() + refPath.replaceFirst(".", "");
        return getWellFormedPath(path);
    }

    /**
     * Retorna um <b>PATH bem formado</b> a partir de um Path base e vários
     * subdiretorios <br/> ex. basePath = c:\checkout <br/> path1 = workspace
     * <br/> path2 = oceano <br/> path3 = trunk <br/> <br/> <b>A saída será:
     * </b> c:\checkout\workspace\oceano\trunk
     *
     * @param basePath
     * @param paths
     * @return
     */
    public static synchronized String getWellFormedPath(String basePath, String... paths) {
        return getWellFormedURLOrLocalPath(SystemUtil.FILESEPARATOR, basePath, paths);
    }

    /**
     * Retorna uma <b>URL bem formada</b> a partir de uma URL base e vários
     * subdiretorios <br/> ex. baseURL = http://gems.ic.uff.br<br/> path1 =
     * oceano/<br/> path2 = /branches<br/> path3 = otimizacao<br/> <br/> <b>A
     * saída será: </b>http://gems.ic.uff.br/oceano/branches/otimizacao/
     *
     * @param baseUrl
     * @param paths
     * @return
     */
    public static synchronized String getWellFormedURL(String baseUrl, String... paths) {
        return getWellFormedURLOrLocalPath("/", baseUrl, paths);
    }

    private static synchronized String getWellFormedURLOrLocalPath(String separator, String baseUrl, String... paths) {
        StringBuilder sb = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith(separator)) {
            sb.append(separator);
        }
        for (String path : paths) {
            if (path.startsWith(separator)) {
                sb.append(path.substring(1));
            } else {
                sb.append(path);
            }
            if (!path.endsWith(separator)) {
                sb.append(separator);
            }
        }

        return sb.toString();

    }

    /**
     * Fix file separator on path list. Note: file separators may be mixed up.
     *
     * @param paths
     */
    public static void fixPathFileSeparator(List<String> paths) {
        for (int i = 0; i < paths.size(); i++) {
            paths.set(i, getWellFormedPath(paths.get(i)));
        }
    }

    /**
     * Fix file separators according to Operating System. Adds file separator to
     * end of directory paths when necessary.
     *
     * @param path
     * @return
     */
    public static synchronized String getWellFormedPath(String path) {
        if (path == null) {
            return null;
        }

        //fix file separator
        path = path.replace("/", SystemUtil.FILESEPARATOR).replace("\\", SystemUtil.FILESEPARATOR);

        //remove multiple file separators
        String multFileSep = SystemUtil.FILESEPARATOR + SystemUtil.FILESEPARATOR;
        while (path.contains(multFileSep)){
            path = path.replace(multFileSep, SystemUtil.FILESEPARATOR);
        }

        //add file separator to end of directory path
        File tempFile = new File(path);
        if (tempFile.exists() && tempFile.isDirectory() && !path.endsWith(SystemUtil.FILESEPARATOR)) {
            path += SystemUtil.FILESEPARATOR;
        }

        return path;
    }

    public static String getPathUntilLastDirectory(String path) throws IOException {
        //Fix path
        path = getWellFormedPath(path);

        //only works if file exists
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.getParent();
        }

        //String handling
        if (path.endsWith(SystemUtil.FILESEPARATOR)) {
            return path;
        } else {
            return path.substring(0, 1 + path.lastIndexOf(SystemUtil.FILESEPARATOR));
        }
    }

    /**
     *
     * @param path
     * @throws IOException
     */
    public static void mkDirs(String path) throws IOException {

        File file = new File(path);
        if (file.exists()) {
            return;
        }
        
        //fix if path to file
        path = getPathUntilLastDirectory(path);
        file = new File(path);
        
        //Create output path
        //one try is not enough
        int tryCount = 0;
        while (tryCount < 5 && !file.exists()) {
            file.mkdirs();
            file = new File(path);
            tryCount++;
        }

        //test if path was created
        if (!file.exists()) {
            throw new IOException("Path not created: " + path);
        }
    }

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }
    
    public static boolean isEmpty(String path) {
        return isEmpty(new File(path));
    }
    
    public static boolean isEmpty(File directory) {

        if (!directory.exists()) {
            return true;
        } else if (!directory.isDirectory()) {
            return false;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return true;
        }
        return (files.length == 0);
    }

    public static boolean isRelativePath(String path) {
        if (path == null) {
            return false;
        } else {
            return !new File(path).isAbsolute();
        }
    }

    public static String trimLastFileSeparator(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        
        //fix path before process
        path = getWellFormedPath(path);
        
        //trim
        if(path.endsWith(SystemUtil.FILESEPARATOR)){
            path = path.substring(0, path.length()-1);
        }
        
        return path;
    }

}
