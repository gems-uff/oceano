package br.uff.ic.oceano.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 *
 * @author dheraclio
 */
public class HashUtil {

    /**
     * Originally from
     * http://www.velocityreviews.com/forums/t131917-sha1-hash-generator-in-hex.html
     *
     * @param object
     * @return
     */
    public static String sha1(Object object) throws Exception {
        if (object == null) {
            throw new Exception("Object is null.");
        }

        String input = String.valueOf(object);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
        md.reset();

        byte[] buffer = input.getBytes();
        md.update(buffer);

        byte[] digest = md.digest();
        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hexStr;
    }

    /**
     * Create MD5 Hex of a file. Origial code on
     * http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public static String getFileMD5(String filename) throws Exception {
        if(filename == null){
            throw new Exception("File path is null.");
        }

        if (!new File(filename).isFile()) {
            throw new Exception("Not a file path: " + filename);
        }
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.reset();

        InputStream fis = new FileInputStream(filename);
        byte[] buffer = new byte[1024];
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();

        byte[] bytes = complete.digest();
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * Create MD5 from files in path list
     * Warning: Collection order affects result.
     * @param paths
     * @return
     */
    public static String getMD5(Collection<String> paths) throws Exception {
        if(paths == null){
            throw new Exception("Paths collection is null.");
        }

        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.reset();

        for (final String path : paths) {
            File file = new File(path);
            if (!file.isFile()) {            
                throw new Exception("Not a file path: " + path);
            } else if (!file.exists()) {          
                continue;
            } else if (!file.canRead()) {          
                continue;
            }
            InputStream fis = null;
            try{
                fis = new FileInputStream(path);
            }catch(FileNotFoundException ex){
                continue;
            }
            
            byte[] buffer = new byte[1024];
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            fis.close();
        }

        byte[] bytes = complete.digest();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
