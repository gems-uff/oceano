/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class OceanoUserService implements PersistenceService{

    private OceanoUserDao oceanoUserDao;

    public void setup() {
        oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);
    }

    public OceanoUserService() {
    }

    @Transacional
    public void save(OceanoUser user) {
        oceanoUserDao.inclui(user);
    }


    public OceanoUser getByLogin(String login) throws ObjetoNaoEncontradoException {
        return oceanoUserDao.getByLogin(login);
    }

    public OceanoUser autenticarUsuario(String login, String senha) throws ServiceException {
        OceanoUser u;
        try {
            u = oceanoUserDao.getByLogin(login);
        } catch (ObjetoNaoEncontradoException ex) {
            throw new ServiceException("Login ou senha inválidos");
        }
        if (!u.getPassword().equals(senha)) {
            throw new ServiceException("Login ou senha inválidos");
        }
        return u;
    }

    public List<OceanoUser> getAll(){
        List<OceanoUser> all = oceanoUserDao.getAll();
         return all;
    }
}
