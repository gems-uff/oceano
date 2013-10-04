package br.uff.ic.oceano.util;

import java.util.Map;

/**
 *
 * @author Daniel
 */
public class SystemUtil {

    public static final String FILESEPARATOR = System.getProperty("file.separator");


    public static String getTempDirectory(){
        try {
            return System.getenv("TEMP");
        } catch (java.security.AccessControlException ace) {
            return null;
        }
    }

    public static String getUniqueTempDirectory() {
        return getTempDirectory()+ FILESEPARATOR + System.currentTimeMillis() + FILESEPARATOR;
    }

    public static String getJavaClassPath() throws Exception{
        final String classpath = System.getProperty("java.class.path");
        if(classpath == null){
            throw new Exception("No java classpath set");
        }else if (classpath.isEmpty()){
            throw new Exception("Java classpath empty");
        }
        return classpath;
    }
    
    public static String[] getEnvironmentVariables() {
        final Map<String, String> env = System.getenv();
        String[] variaveis = new String[env.keySet().size()];
        int pos = 0;
        for (String chave : env.keySet()) {
            variaveis[pos] = chave + "=" + env.get(chave);
            pos++;
        }
        return variaveis;
    }
    
    public static String getOS(){
        return System.getenv("OS");
    }
    
    public static boolean isWindows(){
        return getOS() != null && getOS().equals("Windows_NT");
    }
}
