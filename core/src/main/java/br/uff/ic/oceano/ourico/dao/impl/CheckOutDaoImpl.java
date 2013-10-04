package br.uff.ic.oceano.ourico.dao.impl;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.ourico.dao.CheckOutDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.InfraestruturaException;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ourico.model.CheckOut;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

/**
 *
 * @author marapao
 */
public class CheckOutDaoImpl extends JPADaoGenerico<CheckOut, Long> implements CheckOutDao{

    public CheckOutDaoImpl() {
        super(CheckOut.class);
    }

    @MetodoRecuperaUnico
    public CheckOut getByAutobranch(Long autobranch) throws ObjetoNaoEncontradoException{
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<CheckOut> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @MetodoRecuperaUnico
    public Long getMaxAutobranch() throws ObjetoNaoEncontradoException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @MetodoRecuperaUnico
    public CheckOut getCheckOutwithMaxAutobranch() throws ObjetoNaoEncontradoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*public final T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException {
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            t = (T) em.find(tipo, id);

            if (t != null) {
                em.lock(t, LockModeType.READ);
                em.refresh(t);
            } else {
                throw new ObjetoNaoEncontradoException();
            }
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }

        return t;
    }*/

    public CheckOut getPorAutobranchComLock(Long autobranch) throws ObjetoNaoEncontradoException {


        EntityManager em = JPAUtil.getEntityManager();
        CheckOut t = null;
        try {
            t = (CheckOut) em.find(CheckOut.class , autobranch);

            if (t != null) {
                em.lock(t, LockModeType.READ);
                em.refresh(t);
            } else {
                throw new ObjetoNaoEncontradoException();
            }
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }

        return t;
    }

}
