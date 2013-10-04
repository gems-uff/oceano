/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.SoftwareProjectDao;
import br.uff.ic.oceano.core.dao.impl.SoftwareProjectDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class SoftwareProjectService implements PersistenceService{

    private SoftwareProjectDao projectDao;

    public void setup(){
        projectDao = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectDaoImpl.class);
    }

    public SoftwareProjectService() {
    }

    public SoftwareProject getById(Long id) throws ObjetoNaoEncontradoException {
        return projectDao.getPorId(id);
    }

    public List<SoftwareProject> getProjectsByOceanoUser(OceanoUser oceanoUser){
        return projectDao.getProjectsByOceanoUser(oceanoUser);
    }


    @Transacional
    public void save(SoftwareProject project) throws ServiceException {
        try {
            projectDao.getByRepositoryUrl(project.getRepositoryUrl());

            throw new ServiceException("Can't exists two projects with the same repository url.");
        } catch (ObjetoNaoEncontradoException ex) {
            // it is OK
        }
        //the configuration is not setted

        if (project.getId() == null) {
            projectDao.inclui(project);
        } else {
            projectDao.altera(project);
        }
    }

    public List<SoftwareProject> getAll() {
        return projectDao.getListaCompleta();
    }

    public SoftwareProject getProjectToDetailById(Long id) throws ServiceException {
        try {
            return projectDao.getProjectToDetailById(id);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<SoftwareProject> getMavenProjectsByUser(OceanoUser oceanoUser){
        return projectDao.getMavenProjectsByUser(oceanoUser);
    }

    public SoftwareProject getByRepositoryUrl(String repositoryUrl) throws ObjetoNaoEncontradoException{
        return projectDao.getByRepositoryUrl(repositoryUrl);
    }

    public SoftwareProject getByUrl(String url){

        if(!url.endsWith("/"))
            url = url.concat("/");
        List<SoftwareProject> all = getAll();
        for (SoftwareProject softwareProject : all) {
            if(url.startsWith(softwareProject.getRepositoryUrl()))
                return softwareProject;
        }
        return null;
    }


}

