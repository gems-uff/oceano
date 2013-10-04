/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.ourico.dao.CheckOutDao;
import br.uff.ic.oceano.ourico.dao.impl.CheckOutDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author marapao
 */
public class CheckOutService implements PersistenceService{

    private CheckOutDao checkOutDao;

    public void setup(){
        checkOutDao = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutDaoImpl.class);
    }

    public CheckOutService() {
    }

    @Transacional
    public void save(CheckOut co) {
        if (co.getIdCheckout() == null) {
            checkOutDao.inclui(co);
        } else {
            checkOutDao.altera(co);
        }
    }

    public CheckOut getbyAutobranch(Long autobranch) throws ObjetoNaoEncontradoException {
        return checkOutDao.getByAutobranch(autobranch);
    }

    public List<CheckOut> getAll() {
        return checkOutDao.getAll();
    }

    public Long getMaxAutobranch() throws ObjetoNaoEncontradoException {
        return checkOutDao.getMaxAutobranch();
    }

    public CheckOut getCheckOutwithMaxAutobranch() throws ObjetoNaoEncontradoException {
        return checkOutDao.getCheckOutwithMaxAutobranch();
    }

    public CheckOut getPorAutobranchComLock(String autobranch) throws ObjetoNaoEncontradoException{
            return checkOutDao.getPorAutobranchComLock(Long.parseLong(autobranch));
    }
}
