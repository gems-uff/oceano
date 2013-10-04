/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.BranchDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Branch;
import java.util.List;

/**
 *
 * @author Rafael Santos
 */
public class BranchDaoImpl extends JPADaoGenerico<Branch, Long> implements BranchDao {

    public BranchDaoImpl() {
        super(Branch.class);
    }
    
    @MetodoRecuperaLista
    public List<Branch> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Branch> getbyProject(Long id) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Branch getbyProjectName(Long idProject, String nameBranch) throws ObjetoNaoEncontradoException{
        throw new MetodoInterceptadoException();
    }

}
