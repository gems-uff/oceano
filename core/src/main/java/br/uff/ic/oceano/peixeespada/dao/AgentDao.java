/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.core.model.OceanoUser;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public interface AgentDao extends DaoGenerico<Agent, Long>{

    public List<Agent> getByOceanoUserAndStateActive(OceanoUser oceanUser, boolean active);

    public List<Agent> getActiveByOceanoUserAndTimeInterval(OceanoUser oceanoUser, Date initDate, Date endDate);

}
