/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.ostra.model.Task;
import java.util.List;

/**
 *
 * @author daniel
 */
//@NamedQueries({
//
//    @NamedQuery(name="ConfiguracaoDao.getByUsuario",query="select c from Configuracao c where c.usuario= ? order by c.dataCriacao")
//
//})
public interface TaskDao extends DaoGenerico<Task, Long>{

    public List<Task> getAll();

    public List<Task> getScheduledTasks();

}
