/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.RevisionDao;
import br.uff.ic.oceano.core.dao.impl.RevisionDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heliomar
 */
public class RevisionService implements PersistenceService {

    private RevisionDao revisionDao;
    private VCSService vCSService;

    public void setup() {
        revisionDao = ObjectFactory.getObjectWithDataBaseDependencies(RevisionDaoImpl.class);
        vCSService = ObjectFactory.getObjectWithDataBaseDependencies(VCSService.class);
    }

    public RevisionService() {
    }

    public Revision getConfigurationByCaminhoLocal(String caminhoLocal) throws ObjetoNaoEncontradoException {
        return revisionDao.getByCaminhoLocal(caminhoLocal);
    }

    @Transacional
    public void save(Revision revision) /*throws ServiceException*/ {

        try {
            revisionDao.getByNumberAndProject(revision.getNumber(), revision.getProject());

        } catch (ObjetoNaoEncontradoException ex) {
            // it is OK
        }

        final int MESSAGE_MAX_SIZE = 255;
        if (revision.getMessage() != null && revision.getMessage().length() > MESSAGE_MAX_SIZE) {
            revision.setMessage(revision.getMessage().substring(0, 250) + "...");
        }

        if (revision.getId() == null) {
            revisionDao.inclui(revision);
        } else {
            revisionDao.altera(revision);
        }
    }

    public Revision getByNumberAndProject(Long number, SoftwareProject project) throws ObjetoNaoEncontradoException {
        return revisionDao.getByNumberAndProject(number, project);
    }

    public Revision getHEADByProject(ProjectUser projectUser) throws ServiceException {
        Long lastRevision = null;
        Revision revision = null;
        try {
            lastRevision = vCSService.getNumberOfHEADRevision(null);
        } catch (VCSException ex) {
            throw new ServiceException(ex);
        }

        try {
            revision = revisionDao.getByNumberAndProject(lastRevision, projectUser.getProject());
        } catch (ObjetoNaoEncontradoException ex) {
            try {
                revision = vCSService.doCheckout(null, projectUser, false);
            } catch (VCSException ex1) {
                throw new ServiceException(ex);
            }
            revisionDao.inclui(revision);
        }
        return revision;
    }

    public Revision getWithVersionedItemsAndItemsAndMetricValues(Revision revision) throws ServiceException {
        try {
            return revisionDao.getWithRevisionedItemsAndItemsAndMetricValues(revision);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    public Set<Revision> getByProject(SoftwareProject project) {
        return revisionDao.getByProject(project);
    }

    public Revision getWithChangedFiles(Revision revision) throws ServiceException {
        try {
            return revisionDao.getWithChangedFiles(revision);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }

    public Revision getById(Long id) throws ServiceException {
        try {
            return revisionDao.getPorId(id);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException(ex);
        }
    }
}
