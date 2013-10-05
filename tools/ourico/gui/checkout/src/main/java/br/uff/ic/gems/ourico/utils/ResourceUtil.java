/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gems.ourico.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Heliomar
 */

public class ResourceUtil {

    protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }

    public static String getMessageResourceString(
            String bundleName,
            String key,
            Object params[],
            Locale locale) {

        String text = null;

        ResourceBundle bundle =
                ResourceBundle.getBundle(bundleName, locale,
                getCurrentClassLoader(params));

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
        return ResourceBundle.getBundle(bundleName,Locale.getDefault(),
                getCurrentClassLoader(new Object[]{}));
    }

    public static String getStringBundleProperties(String keyBundle){
        String text= null;
        try {
            text = getResourceBundle("br.uff.ic.gems.peixeespadacliente.property.properties").getString(keyBundle);
        } catch (MissingResourceException e) {
            text = " ?? value [" + keyBundle + "] not found in propert bundle ?? ";
        }
        return text;
    }
}
