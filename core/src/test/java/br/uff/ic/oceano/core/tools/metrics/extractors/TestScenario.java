package br.uff.ic.oceano.core.tools.metrics.extractors;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;

/**
 *
 * @author Daniel
 */
public class TestScenario {

    private Revision revision;
    private String path;
    private Double result;    
   

    public TestScenario(Revision revision, double result) {
        this(revision, null, new Double(result));
    }
    
    public TestScenario(Revision revision, Double result) {
        this(revision, null, result);
    }
        
    public TestScenario(Revision revision, String path, double result) {
        this(revision, path, new Double(result));
    }
    
    public TestScenario(Revision revision, String path, Double result) {
        this.revision = revision;
        this.path = path;
        this.result = result;
    }

    public Language getLanguage() {
        return getRevision().getProject().getLanguage();
    }

    public Revision getRevision() {
        return revision;
    }

    public Double getResult() {        
        return result;
    }
    
    public String getPath() {
        return path;
    }

    public final String getName() {
        if (getPath() != null) {
            return getPath();
        } else{
            return getRevision().getLocalPath();
        }
    }
}
