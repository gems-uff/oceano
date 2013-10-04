package br.uff.ic.oceano.ourico.dao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ourico.model.CheckOut;
import java.util.List;

/**
 *
 * @author marapao
 */
public interface CheckOutDao extends DaoGenerico<CheckOut, Long> {
    
    public CheckOut getByAutobranch(Long autobranch) throws ObjetoNaoEncontradoException;
    public List<CheckOut> getAll();
    public Long getMaxAutobranch() throws ObjetoNaoEncontradoException;
    public CheckOut getCheckOutwithMaxAutobranch() throws ObjetoNaoEncontradoException;
    public CheckOut getPorAutobranchComLock(Long autobranch) throws ObjetoNaoEncontradoException;



}
