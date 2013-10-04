/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author DanCastellani
 */
public class DateUtil {

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
    private static final DateFormat fileFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static Date currentDate(){
        return new Date(System.currentTimeMillis());
    }

    public static String currentString(){
        return format(currentDate());
    }

    public static synchronized String format(Date date) {
        if(date == null){
            return null;
        }
        return dateFormat.format(date);
    }

    public static synchronized String format(Calendar calendar) {
        if(calendar == null){
            return null;
        }
        return dateFormat.format(calendar.getTime());
    }

    public static String toStringHour(int hours, int minutes, int seconds, int millis) {        
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

    /**
     * Returns current date as file name compatible name
     * @return current date as string
     */
    public static String currentFile() {
        return fileFormat.format(currentDate());
    }
}
