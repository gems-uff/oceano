/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util.file;

import br.uff.ic.oceano.util.Output;
import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author kann
 */
public class FileUtils {

    /**
     * Reads texts file to String.
     *
     * @author dheraclio From original code in
     * http://stackoverflow.com/questions/4716503/best-way-to-read-a-text-file
     * @param path Text file path
     * @return String full file content
     */
    public static String readFile(String path) throws Exception {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            String result = IOUtils.toString(inputStream);
            inputStream.close();
            return result;
        }catch(Exception ex){
            throw new Exception("Fail to read file " + path, ex);
        }
    }
    
    /**
     * Return lines from text file
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static List<String> getLines(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        List<String> lines = new LinkedList<String>();
        String line = "";
        while ((line = in.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    /**
     * Copy a File The renameTo method does not allow action across NFS mounted
     * filesystems this method is the workaround
     *
     * @param fromFile The existing File
     * @param toFile The new File
     * @return <code>true</code> if and only if the renaming succeeded;
     * <code>false</code> otherwise
     */
    public static boolean copy(File fromFile, File toFile) {
        try {
            FileInputStream in = new FileInputStream(fromFile);
            FileOutputStream out = new FileOutputStream(toFile);
            BufferedInputStream inBuffer = new BufferedInputStream(in);
            BufferedOutputStream outBuffer = new BufferedOutputStream(out);

            int theByte = 0;

            while ((theByte = inBuffer.read()) > -1) {
                outBuffer.write(theByte);
            }

            outBuffer.close();
            inBuffer.close();
            out.close();
            in.close();

            // cleanupif files are not the same length
            if (fromFile.length() != toFile.length()) {
                toFile.delete();

                return false;
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Extrai um arquivo ZIP apontado no caminho zipOrign para o
     * arquivo/diretorio de destino destign
     *
     * @param zip arquivo compactado
     * @param folder
     * @return
     */
    public static boolean extractZip(File zip, File folder) {
        try {
            InputStream is = new FileInputStream(zip);
            ZipInputStream zi = new ZipInputStream(is);
            ZipEntry ze = null;
            ZipFile zf = new ZipFile(zip);
            while ((ze = zi.getNextEntry()) != null) {
                File newFile = new File(folder, ze.getName());
                copyFile(zf.getInputStream(ze), newFile);
            }
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    /**
     * Copia de um ImputStream para um Arquivo a ser criado no caminho
     * pathFileDestign
     *
     * @param is
     * @param pathFileDestign
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean copyFile(InputStream is, File newFile) throws FileNotFoundException, IOException {
        FileOutputStream out = new FileOutputStream(newFile);
        BufferedInputStream inBuffer = new BufferedInputStream(is);
        BufferedOutputStream outBuffer = new BufferedOutputStream(out);
        int theByte = 0;

        while ((theByte = inBuffer.read()) > -1) {
            outBuffer.write(theByte);
        }

        outBuffer.close();
        inBuffer.close();
        out.close();
        is.close();
        return true;
    }

    // If targetLocation does not exist, it will be created.
    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * Move a File The renameTo method does not allow action across NFS mounted
     * filesystems this method is the workaround
     *
     * @param fromFile The existing File
     * @param toFile The new File
     * @return <code>true</code> if and only if the renaming succeeded;
     * <code>false</code> otherwise
     */
    public static boolean move(File fromFile, File toFile) {
        if (fromFile.renameTo(toFile)) {
            return true;
        }

        // delete if copy was successful, otherwise move will fail
        if (copy(fromFile, toFile)) {
            return fromFile.delete();
        }

        return false;
    }

    public static boolean deleteDirectory(String dir) {
        return deleteDirectory(new File(dir));
    }
    
    public static boolean deleteDirectory(File dir) {
        try {
            if(dir == null){
                return false;
            } else if (!dir.exists()){
                return true;
            }
            org.apache.commons.io.FileUtils.forceDelete(dir);            
        } catch (Exception ex) {
            Output.println("Fail to delete directory on first try: " + ex.getMessage());            
            Output.println("Retrying with different approach");            
        }
        
        final int retryCount = 5;
        for (int i = 0; i < retryCount; i++) {
            try {
                if (tryDeleteDirectory(dir)) {
                    return true;
                }
                Thread.sleep(500);
            } catch (Exception ex) {
                continue;
            }
        }
        return false;
    }

    /**
     * warning: do not use deleteDirectory method, to avoid overflow.
     * @param dir
     * @return
     * @throws Exception 
     */
    private static boolean tryDeleteDirectory(File dir) throws Exception {
        SecurityManager security = System.getSecurityManager();
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (!tryDeleteDirectory(new File(dir, children[i]))) {
                    return false;
                }
            }
        } 
        //verify authorization to delete
        if (security != null) {
            security.checkDelete(dir.getPath());
        }
        
        return dir.delete();
    }

    /**
     * Verify if the file has only commented lines. Works only for JavaFiles.
     *
     * @return
     */
    public static boolean hasOnlyCommentLines(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            boolean lookingForCloseBlockOfComment = false;
            boolean comentedLine;

            while ((line = br.readLine()) != null) {
                comentedLine = false;

                if (!lookingForCloseBlockOfComment) {
                    if (line.startsWith("/*") || line.startsWith("/**")) {
                        lookingForCloseBlockOfComment = true;
                        comentedLine = true;
                    } else if (line.startsWith("//")) {
                        comentedLine = true;
                    }

                } else {
                    if (line.endsWith("*/")) {
                        lookingForCloseBlockOfComment = false;
                        comentedLine = true;
                    }
                }
                if (!comentedLine) {
                    return false;
                }
            }
            return true;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Verify if the file has only commented lines. Works only for JavaFiles.
     *
     * @return
     */
    public static boolean hasOnlyCommentLines(String filename) {
        return hasOnlyCommentLines(new File(filename));
    }

    /**
     * Return all files in the fileOrFolder directory which ends with the
     * endPattern. If a null endPattern is specified, all files will return.
     *
     * @param fileOrFolder
     * @param endPattern - null to return everything
     * @return
     */
    public static Set<String> getAllFilesInFolderAndSubFolders(final File fileOrFolder) {
        final String endPattern = "";
        return getAllFilesInFolderAndSubFolders(fileOrFolder, endPattern);
    }

    /**
     * Return all files in the fileOrFolder directory which ends with the
     * endPattern. If a null endPattern is specified, all files will return.
     *
     * @param fileOrFolder
     * @param endPattern - empty to return everything
     * @return
     */
    public static Set<String> getAllFilesInFolderAndSubFolders(final File fileOrFolder, final String endPattern) {
        Set<String> filenames = new HashSet<String>();

        if (fileOrFolder.isFile()) {
            String nome = fileOrFolder.toString();
            if (endPattern == null || endPattern.isEmpty() || nome.endsWith(endPattern)) {
                filenames.add(PathUtil.getWellFormedPath(nome));
            }
        } else {
            if (fileOrFolder.listFiles() != null) {
                for (File subItem : fileOrFolder.listFiles()) {
                    filenames.addAll(getAllFilesInFolderAndSubFolders(subItem, endPattern));
                }
            }
        }

        return filenames;
    }

    /**
     * Return all files in the fileOrFolder directory which ends with the a
     * element in endPatterns collection. If a null endPattern is specified, all
     * files will return.
     *
     * @param fileOrFolder
     * @param endPatterns - empty to return everything
     * @return
     */
    public static List<String> getAllFilesInFolderAndSubFolders(final File fileOrFolder, final List<String> endPatterns) {
        List<String> filenames = new LinkedList<String>();

        if (fileOrFolder.isFile()) {
            String nome = fileOrFolder.toString();
            if (endPatterns == null || endPatterns.isEmpty()) {
                filenames.add(PathUtil.getWellFormedPath(nome));
            } else {
                for (String endPattern : endPatterns) {
                    if (nome.endsWith(endPattern)) {
                        filenames.add(PathUtil.getWellFormedPath(nome));
                        break;
                    }
                }
            }
        } else {
            if (fileOrFolder.listFiles() != null) {
                for (File subItem : fileOrFolder.listFiles()) {
                    filenames.addAll(getAllFilesInFolderAndSubFolders(subItem, endPatterns));
                }
            }
        }

        return filenames;
    }

    /**
     * Return all files in the fileOrFolder directory which ends with the
     * endPattern. If a null endPattern is specified, all files will return.
     *
     * @param fileOrFolder
     * @param endPattern - null to return everything
     * @return
     */
    public static List<String> getAllFoldersAndSubFoldersEndsWith(final File fileOrFolder, final String endPattern) {
        List<String> folderNames = new LinkedList<String>();

        if (fileOrFolder.isDirectory()) {
            String nome = fileOrFolder.toString();
            if (endPattern == null || endPattern.isEmpty() || nome.endsWith(endPattern)) {
                folderNames.add(PathUtil.getWellFormedPath(nome));
            }

            if (fileOrFolder.listFiles() != null) {
                for (File subItem : fileOrFolder.listFiles()) {
                    folderNames.addAll(getAllFoldersAndSubFoldersEndsWith(subItem, endPattern));
                }
            }
        }

        return folderNames;
    }

    public static List<String> getAllFoldersAndSubFoldersContains(final File fileOrFolder, final String contains) {
        List<String> folderNames = new LinkedList<String>();

        if (fileOrFolder.isDirectory()) {
            String nome = fileOrFolder.toString();
            if (contains == null || contains.isEmpty() || nome.contains(contains)) {
                folderNames.add(PathUtil.getWellFormedPath(nome));
            }

            if (fileOrFolder.listFiles() != null) {
                for (File subItem : fileOrFolder.listFiles()) {
                    folderNames.addAll(getAllFoldersAndSubFoldersContains(subItem, contains));
                }
            }
        }

        return folderNames;
    }
}
