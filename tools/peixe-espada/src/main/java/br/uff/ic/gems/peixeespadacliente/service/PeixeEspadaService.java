/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.service;

import br.uff.ic.gems.peixeespadacliente.configuration.Configuration;
import br.uff.ic.gems.peixeespadacliente.context.Constants;
import br.uff.ic.gems.peixeespadacliente.exception.RefactoringException;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.gems.peixeespadacliente.resolution.Resolution;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import br.uff.ic.gems.peixeespadacliente.tool.RefactoringTool;
import br.uff.ic.gems.peixeespadacliente.tool.factory.FactoryRefactoringTool;
import br.uff.ic.gems.peixeespadacliente.utils.FileUtils;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GEMS
 */
public class PeixeEspadaService {

    private LocalManagerAgent agentPeixeEspada;

    public PeixeEspadaService(LocalManagerAgent agentPeixeEspada) {
        this.agentPeixeEspada = agentPeixeEspada;
    }

    //ok
    public List<String> getRefactorings() {
        return new ArrayList<String>(RefactoringTool.classMap.keySet());
    }

    //ok
    public List<Symptom> getSymptoms(List<String> refactorings) throws RefactoringException {
        List<Symptom> result = new ArrayList<Symptom>();
        for (String strRefactoring : refactorings) {
            RefactoringTool refactoringTool = (RefactoringTool) FactoryRefactoringTool.getRefactoringTool(RefactoringTool.classMap.get(strRefactoring), agentPeixeEspada.getProjectVCS());
            result.addAll(refactoringTool.findAllSymptoms());
        }
        return result;
    }

    //indentify resolution without applying it 
    public List<Resolution> getResolutions(List<Symptom> symptoms) throws RefactoringException {
        List<Resolution> result = new ArrayList<Resolution>();
        for (Symptom symptom : symptoms) {
            result.addAll(symptom.generateResolutions(agentPeixeEspada, false));
        }
        return result;
    }


    public List<Configuration> refactor(List<List<Resolution>> solutions) throws VCSException, IOException {
        List<Configuration> configurations = new ArrayList<Configuration>();
        for (List<Resolution> solution : solutions) {
            Configuration configuration = new Configuration(solution);
            if (configuration.apply()) {

                File workspaceOriginal = new File(agentPeixeEspada.getProjectVCS().getLocalPath());
                String pathBaseModified = agentPeixeEspada.getBaseWorkspace() + Constants.MODIFIED_DIRECTORY;
                
                File modifiedWorkspace = Configuration.getAvaliablePath(pathBaseModified);
                FileUtils.copyDirectory(workspaceOriginal, modifiedWorkspace);

                configuration.setPath(modifiedWorkspace);
                configurations.add(configuration);
                
            }

            agentPeixeEspada.getProjectVCS().doReset();
        }
        return configurations;
    }

    public List<Configuration> measure(List<Configuration> configurations) throws ServiceException {
        for (Configuration configuration : configurations) {
            configuration.calculateQA(agentPeixeEspada);
        }
        return configurations;
    }
}
