/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.service;

import br.uff.ic.oceano.peixeespada.dao.AgentDao;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.peixeespada.dao.impl.AgentDaoImpl;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heliomar
 */
public class AgentService implements PersistenceService{

    private AgentDao agentDao;

    public void setup(){
        agentDao = ObjectFactory.getObjectWithDataBaseDependencies(AgentDaoImpl.class);
    }

    public AgentService() {
    }

    public List<Agent> getListaCompleta() {
        return agentDao.getListaCompleta();
    }

    public List<Agent> getActiveByOceanoUser(OceanoUser oceanoUser) {
        return agentDao.getByOceanoUserAndStateActive(oceanoUser, true);
    }

    public List<Agent> getActiveByOceanoUserAndTimeInterval(OceanoUser oceanoUser, Date initDate, Date endDate) {
        return agentDao.getActiveByOceanoUserAndTimeInterval(oceanoUser, initDate, endDate);
    }


    @Transacional
    public synchronized Agent salve(Agent agent) throws ServiceException {
        return agentDao.inclui(agent);
    }

    @Transacional
    public void updateAgent(Agent agent) throws ServiceException {
        try {
            agentDao.getPorIdComLock(agent.getIdAgent());
        } catch (ObjetoNaoEncontradoException ex) {
            Logger.getLogger(AgentService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("O objeto a ser alterado não encontra-se mais no banco de dados");
        }
        agentDao.altera(agent);
    }

}

