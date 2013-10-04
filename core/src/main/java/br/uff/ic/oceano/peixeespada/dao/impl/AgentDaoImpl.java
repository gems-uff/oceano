/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.dao.impl;

import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.peixeespada.dao.AgentDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class AgentDaoImpl extends JPADaoGenerico<Agent, Long> implements AgentDao {

    public AgentDaoImpl(){
        super(Agent.class);
    }

    @MetodoRecuperaLista
    public List<Agent> getByOceanoUserAndStateActive(OceanoUser oceanUser, boolean active) {
        throw new MetodoInterceptadoException();

    }

    @MetodoRecuperaLista
    public List<Agent> getActiveByOceanoUserAndTimeInterval(OceanoUser oceanoUser, Date initDate, Date endDate) {
        throw new MetodoInterceptadoException();
    }

}
