package br.uff.ic.oceano.util.file;

import java.io.FileReader;
import java.util.List;
import au.com.bytecode.opencsv.CSVReader;
import java.util.LinkedList;

/**
 *
 * 
 */
public abstract class CSVUtils {
    
    /**
     * Build T instance from line[]
     * @param <T> Class of created instances
     */
    public static interface Builder<T> {
        public T newInstance(final String[] line);
    }
    /**
     * 
     * @param <T>
     * @param path
     * @param delimiter
     * @param builder
     * @return
     * @throws Exception 
     */
    public static <T> List<T> getAll(final String path, final char delimiter, final Builder<T> builder) throws Exception{
        try{
            final List<T> result = new LinkedList<T>();
            final FileReader fr = new FileReader(path);
            final CSVReader reader = new CSVReader(fr,delimiter);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {                
                result.add(builder.newInstance(nextLine));
            }
            return result;
        } catch (Exception ex){
            throw new Exception("Error while reading CSV file!",ex);
        }
         
    }
    
    /**
     * 
     * @param path
     * @param delimiter
     * @return
     * @throws Exception 
     */
    public static List<String[]> getAll(final String path, final char delimiter) throws Exception{
        try{
            FileReader fr = new FileReader(path);
            CSVReader reader = new CSVReader(fr,delimiter);
            return reader.readAll();
        } catch (Exception ex){
            throw new Exception("Error while reading CSV file!",ex);
        }
    }    
    
}
