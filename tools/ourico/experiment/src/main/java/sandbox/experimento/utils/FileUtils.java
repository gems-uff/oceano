package sandbox.experimento.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Util methods to manipulate files
 * 
 * @author murta
 */
public class FileUtils {

    /**
     * Provides the size of a file or directory (recursivelly)
     */
    public static long getSize(File file) {
        long size = file.length();

        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                size += getSize(subFile);
            }
        }

        return size;
    }

    /**
     * Provides the number of files in a directory (recursivelly)
     * @param countDirectory Indicates if directories should be counted
     */
    public static long getFileCount(File file, boolean countDirectory) {
        long fileCount = 0;

        if (!"CVS".equals(file.getName()) &&
            !".svn".equals(file.getName()) &&
            !".git".equals(file.getName()) &&
            !".hg".equals(file.getName()) ) {
            if (file.isDirectory()) {
                if (countDirectory) {
                    fileCount++;
//                    System.out.println(file.getName());
                }

                for (File subFile : file.listFiles()) {
                    fileCount += getFileCount(subFile, countDirectory);
                }
            } else {
//                    System.out.println(file.getName());
                fileCount++;
            }
        }

        return fileCount;
    }

    /**
     * Recursivelly delete a file or directory
     */
    public static void recursiveDelete(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    recursiveDelete(subFile);
                }
            }
            if (!file.delete()) {
                Logger.global.info("Could not delete file " + file);
            }
        }
    }

    /**
     * Copies a whole directory to a new location preserving the file dates.
     * @param srcDir
     * @param destDir
     * @throws IOException
     */
//    public static void copyDirectory(File srcDir, File destDir) throws IOException{
//        org.apache.commons.io.FileUtils.copyDirectory(srcDir, destDir);
//    }
}
