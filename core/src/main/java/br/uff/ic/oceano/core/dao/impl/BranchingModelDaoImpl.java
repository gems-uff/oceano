/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.BranchingModelDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.model.BranchingModel;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public class BranchingModelDaoImpl extends JPADaoGenerico<BranchingModel, Long> implements BranchingModelDao {

    public BranchingModelDaoImpl() {
        super(BranchingModel.class);
    }
    
    @MetodoRecuperaLista
    public List<BranchingModel> getAll() {
        throw new MetodoInterceptadoException();
    }

}
