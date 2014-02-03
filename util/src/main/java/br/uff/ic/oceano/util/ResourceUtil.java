/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.util;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Heliomar
 */

public class ResourceUtil {

    protected static ClassLoader getClassLoader(final Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }
    
    protected static ClassLoader getClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ResourceUtil.class.getClass().getClassLoader();
        }
        return loader;
    }
    
    /**
     * 
     * @param name name of the file
     * @param refObject instance of a calling project class, used to recover its context.
     * @return 
     */
    public static File getResourceAsFile(final String name, final Object refObject){
        try{
        ClassLoader loader = getClassLoader(refObject);
        final URL url = loader.getResource(name);
        if(url == null){
            return null;
        }        
        return new File(url.toURI());
        }catch (Exception ex){
            return null;
        }
    }

    public static String getMessageResourceString(
            String bundleName,
            String key,
            Object params[],
            Locale locale) {

        String text = null;

        ResourceBundle bundle =
                ResourceBundle.getBundle(bundleName, locale,
                getClassLoader(params));

        try {
            text = bundle.getString(key);
        } catch (MissingResourceException e) {
            text = "?? mensagem \"" + key + "\" não encontrada ??";
        }

        if (params != null) {
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params, new StringBuffer(), null).toString();
        }

        return text;
    }

    public static ResourceBundle getResourceBundle(String bundleName) {
        return ResourceBundle.getBundle(bundleName,Locale.ROOT,
                getClassLoader(new Object[]{}));
    }

    public static String getStringBundleProperties(String properties, String keyBundle){
        String text= null;
        try {
            text = getResourceBundle(properties).getString(keyBundle);
        } catch (MissingResourceException e) {
            text = " ?? value [" + keyBundle + "] not found in propert bundle ?? ";
        }
        return text;
    }

}
