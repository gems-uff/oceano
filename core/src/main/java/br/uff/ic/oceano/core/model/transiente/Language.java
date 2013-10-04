package br.uff.ic.oceano.core.model.transiente;

/**
 *
 * @author Daniel
 */
public enum Language {
    JAVA,
    CPP;

    public static boolean isJava(Language language) {
       return JAVA.equals(language);
    }
    
    public static boolean isCpp(Language language) {
       return CPP.equals(language);
    }
        
    public boolean isSame(String lang){
        return (this.name().compareTo(lang)==0);
    }
}
