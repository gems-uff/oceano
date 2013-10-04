/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.ProjectUserDao;
import br.uff.ic.oceano.core.dao.impl.ProjectUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DanCastellani
 */
public class ProjectUserService implements PersistenceService{

    private ProjectUserDao projetUserDao;

    public void setup() {
        projetUserDao = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserDaoImpl.class);
    }

    public ProjectUserService() {
    }

    @Transacional
    public void save(ProjectUser projectUser) throws ServiceException {

        if (projectUser.getId() == null) {
            try {
                projetUserDao.getByProjectAndOceanoUser(projectUser.getProject(), projectUser.getOceanoUser());
                throw new ServiceException("O cadastro do usuário selecionado nesse projeto já existe, realize a operação de alteração");
            } catch (ObjetoNaoEncontradoException ex) {
                projetUserDao.inclui(projectUser);
            }
        } else {
            try {
                projetUserDao.getPorIdComLock(projectUser.getId());
            } catch (ObjetoNaoEncontradoException ex) {
                throw new ServiceException("O vínculo foi removido no momento da operação de alteração");
            }
            projetUserDao.altera(projectUser);
        }
    }

    public ProjectUser getByProjectAndOceanoUser(SoftwareProject project, OceanoUser oceanoUser) throws ServiceException {
        try {
            return projetUserDao.getByProjectAndOceanoUser(project, oceanoUser);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    public ProjectUser getByProjectAndLogin(SoftwareProject project, String login) throws ServiceException{
        try {
            return projetUserDao.getByProjectAndLogin(project, login);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<ProjectUser> getByOceanoUser(OceanoUser oceanoUser){
        return projetUserDao.getByOceanoUser(oceanoUser);
    }

    @Transacional
    public void exclude(ProjectUser item) {
        try {
            item = projetUserDao.getPorIdComLock(item.getId());
        } catch (ObjetoNaoEncontradoException ex) {
            System.out.println("objeto já removido");
        }
        projetUserDao.exclui(item);
    }
}

