/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.configuration;

import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.QualityAttribute;
import java.io.File;
import java.util.List;
import translation.Translate;

/**
 *
 * @author GEMS
 */
public class Configuration {
    
    private QualityAttribute resolutionQuality = null;
    private List<Resolution> solution;
    private StringBuilder builder;
    private File path;

    

    public Configuration(List<Resolution> solution) {
        this.solution = solution;
        this.builder = new StringBuilder();
    }
     
    //modifiedPath is a source directory where modified project would be stored
    public static File getAvaliablePath(String modifiedPath) {
        File workspace = null;

        int index = 0;
        do {
            workspace = new File(modifiedPath + "_" + (index++));
        } while (workspace.exists());

        return workspace;
    }
    
    public boolean apply() {
        for (Resolution resolution : solution) {
            try {
                resolution.apply(builder);
            } catch (RefactoringException ex) {
                return false;
            }
        }
        return true;
    }
    
    public QualityAttribute getResolutionQuality() {
        return resolutionQuality;
    }
    
    public void setResolutionQuality(QualityAttribute resolutionQuality) {
        this.resolutionQuality = resolutionQuality;
    }
    
    public void calculateQA(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        QualityAttribute calculadeAttribute = new QualityAttribute();

        if (agentPeixeEspada.isTesting()) {
            calculadeAttribute.setCurrentValue(1.0D);
        } else {
            try {
                calculadeAttribute = agentPeixeEspada.getMeterAgent().calculateNormalizedQualityAttributeValue(agentPeixeEspada);
            } catch (Exception ex) {
                throw new ServiceException(ex);
            }
        }

        this.setResolutionQuality(calculadeAttribute);
    }
    
    
    
    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
