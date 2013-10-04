/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 *
 * @author João Felipe
 */
public class ReflectionUtils {
    public static Object getStaticField(Class cls, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(cls);
    }

    public static void setStaticField(Class cls, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(cls, value);
    }

    public static void setFinalStaticField(Class cls, String fieldName, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(cls, value);
    }

    public static Object instantiatePrivate(Class cls, Object... initArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor[] cons = cls.getDeclaredConstructors();
        cons[0].setAccessible(true);
        return cons[0].newInstance(initArgs);
    }
}
