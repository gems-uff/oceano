/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ourico.dao.ProjectConfigurationDao;
import br.uff.ic.oceano.ourico.dao.impl.ProjectConfigurationDaoImpl;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;

/**
 *
 * @author marapao
 */
public class ProjectConfigurationService implements PersistenceService{

    private ProjectConfigurationDao projectConfigurationDao;

    public void setup(){
        projectConfigurationDao = ObjectFactory.getObjectWithDataBaseDependencies(ProjectConfigurationDaoImpl.class);
    }

    @Transacional
    public void save(ProjectConfiguration projectConfiguration) {
        if (projectConfiguration.getId() == null) {
            projectConfigurationDao.inclui(projectConfiguration);
        } else {
            projectConfigurationDao.altera(projectConfiguration);
        }

    }

    public ProjectConfiguration getByProject(SoftwareProject project) throws ObjetoNaoEncontradoException{
        return projectConfigurationDao.getByProject(project);
    }

}
