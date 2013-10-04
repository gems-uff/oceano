/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.TaskDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.ostra.model.Task;
import java.util.List;

/**
 *
 * @author daniel
 */
public class TaskDaoImpl extends JPADaoGenerico<Task, Long> implements TaskDao {

    public TaskDaoImpl() {
        super(Task.class);
    }

    @MetodoRecuperaLista
    public List<Task> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Task> getScheduledTasks() {
        throw new MetodoInterceptadoException();
    }
}
