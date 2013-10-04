/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class NumberUtil {

    public static final int DEFAULT_DECIMALS = 3;
    /**
     * This number was created to compare two doubles ignoring the machine error.
     * As Java machine error is around 10^-6, we're using 10^-5.
     */
    public static final double EPSILON = 1 / Math.pow(10, 5);

    public static boolean isEquivalent(Double a, Double b) {
        if(a == null && b == null){
            return true;
        } else if(a == null && b != null){
            return false;
        } else if(a != null && b == null){
            return false;
        }
        return isEquivalent(a.doubleValue(), b.doubleValue());
    }
    
    public static boolean isEquivalent(double a, double b) {
        return Math.abs(a - b) <= EPSILON;
    }

    public static String format(Float value) {
        return format(value, DEFAULT_DECIMALS);
    }

    public static String format(Float value, int decimals) {
        if (value == null) {
            return null;
        }

        return format(value.doubleValue(), decimals);
    }

    public static String format(Double value) {
        return format(value, DEFAULT_DECIMALS);
    }

    public static String format(Double value, int decimals) {
        if (value == null) {
            return null;
        }
        final int dotIndice = value.toString().indexOf(".");
        final int commaIndice = value.toString().indexOf(",");

        Integer numberSizeWithDecimals = null;
        if (dotIndice >= 0) {
            numberSizeWithDecimals = dotIndice + decimals;
        } else if (commaIndice >= 0) {
            numberSizeWithDecimals = commaIndice + decimals;
        }

        if (numberSizeWithDecimals != null && numberSizeWithDecimals < value.toString().length()) {
            return value.toString().substring(0, numberSizeWithDecimals);
        }
        return value.toString();
    }

    public static Double roundDecimal(Double value) {
        return roundDecimal(value, DEFAULT_DECIMALS);
    }

    public static Double roundDecimal(Double value, int decimals) {
        double coeficient = Math.pow(10, decimals);
        return Math.round(value * coeficient) / coeficient;
    }

    public static Long longValueOf(Object object) {
        return Long.valueOf(String.valueOf(object));
    }

    public static Double ratio(Double value, Double size) {
        if(value == null){
            return new Double(0);
        } 
        if(size == null){
            return Double.NaN;
        }
        return ratio(value.doubleValue(),size.doubleValue());
    }
    
    public static Double ratio(double value, double size) {
        if(isZero(value)){
            return 0d;
        } 
        if(isZero(size)){
            return Double.NaN;
        }
        return roundDecimal(value/size);
    }

    public static boolean isZero(double value) {
        return isEquivalent(value,0d);
    }
    
    public static double parseDouble(String value) {
        return parseDouble(value,Double.NaN);
    }
    
    public static boolean isNAN(Double value) {
        if(value == null){
            return true;
        }
        return Double.isNaN(value);
    }

    public static double parseDouble(String value, double defaultValue) {
        try{
            return Double.parseDouble(value);
        }catch(Exception e){
            e.printStackTrace();
            return defaultValue;
        }
    }
    
    public static double sum(Collection<Double> values){
        if(values == null || values.isEmpty()){
            return 0L;
        }
        
        double sum = 0;
        for (Double value : values) {
            sum += value;
        }
        return sum;
    }

    public static double avg(Collection<Double> values) {
        if(values == null || values.isEmpty()){
            return 0L;
        }
        Double sum = sum(values);
        if(isZero(sum)){
            return 0L;
        }
        return sum/new Double(values.size());
    }
}
