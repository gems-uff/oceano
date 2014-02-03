package br.uff.ic.oceano.util.test;

import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.util.ResourceUtil;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.util.file.Archive;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import static org.testng.Assert.*;
import org.joda.time.Period;

/**
 *
 * @author Daniel Heraclio
 */
public abstract class AbstractNGTest {
    
    private String startMessage;
    private Class testedClass;
    private Date timer;

    protected AbstractNGTest() {
        this(null);        
    }

    protected AbstractNGTest(Class<?> clazz) {
        testedClass = clazz;
        if(clazz != null){
            clazz = this.getClass();
        }
        startMessage = getName(clazz) + " tests running";
    }    
    
    public void beforeClass(){
        println(startMessage);
    }

    protected String getTestName(){
        return getName(this.getClass());
    }

    protected final String getName(Class<?> clazz){
        if(clazz == null){
            return "";
        }
        return clazz.getSimpleName();
    }

    protected final void println(final String message){
        System.out.println(message);
    }
    
    protected final void print(final String message){
        System.out.println(message);
    }
    
    protected void toOutput(String message, Collection<String> coll) {
        println(message);
        if(coll == null){
            println("Null collection");
        } else if(coll.isEmpty()){
            println("Collection empty");
        } else{
            for (String string : coll) {
                println(string);
            }
        }
    }
    
    protected final File getTestFile(final String name){
        return ResourceUtil.getResourceAsFile(name,this);
    }


    protected final String getCurrentPath(){
        String path = PathUtil.getCurrentAbsolutePath();
        path += "target" + SystemUtil.FILESEPARATOR;
        return path;
    }

    protected final String getTempPath(){
        String path = getCurrentPath();
        path += DateUtil.currentDate().getTime() + SystemUtil.FILESEPARATOR;
        return path;
    }

    protected final String createTempPath(){
        try {
            String path = getTempPath();
            PathUtil.mkDirs(path);
            return path;
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        return null;
    }
    
    /**
     * Creates fileName at dated temp directory with text as content.
     * @param String 
     */
    protected final void toTempFile(final String fileName, final String text){
        String filePath = getTempPath() + fileName;
        Archive arc = new Archive(filePath);
        arc.openAppendAndClose(text);
        
    }
    
    /**
     * Creates fileName at dated temp directory with text as content.
     * @param String 
     */
    protected final void toDatedTxtFile(final String fileName, final String text){        
        Archive arc = new Archive(fileName + " " + DateUtil.currentFile()+".txt");
        arc.openAppendAndClose(text);        
    }
    
    protected final void startTimer(final String msg){
        println(msg);        
        startTimer();
    }
    
    protected final void startTimer(){
        this.timer = DateUtil.currentDate();        
    }
    
    protected final void showTimer(final String msg){
        println(msg);
        showTimer();
    }
    
    protected final void showTimer(){
        if (this.timer == null){
            println("Timer not started");
            return;
        }
        
        Period per = new Period(DateUtil.currentDate().getTime(), timer.getTime());
        String result = DateUtil.toStringHour(per.getHours(), per.getMinutes(), per.getSeconds(), per.getMillis());
        
        println("Period: " + result);
    }
    
    protected final void endTimer(){        
        showTimer();
        this.timer = null;
    }    
    protected final void endTimer(final String message) {
        println(message);
        endTimer();
    }
}
