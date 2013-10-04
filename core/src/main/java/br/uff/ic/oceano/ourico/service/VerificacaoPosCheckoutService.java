/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ourico.dao.VerificacaoPosCheckoutDao;
import br.uff.ic.oceano.ourico.dao.impl.VerificacaoPosCheckoutDaoImpl;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.VerificacaoPosCheckout;
import java.util.List;

/**
 *
 * @author marapao
 */
public class VerificacaoPosCheckoutService implements PersistenceService {

    private VerificacaoPosCheckoutDao verificacaoPosCheckoutDao;

    public void setup() {
        verificacaoPosCheckoutDao = ObjectFactory.getObjectWithDataBaseDependencies(VerificacaoPosCheckoutDaoImpl.class);
    }

    public VerificacaoPosCheckoutService() {
    }

    @Transacional
    public void save(VerificacaoPosCheckout verificacaoPosCheckout) {
        if (verificacaoPosCheckout.getId() == null) {
            verificacaoPosCheckoutDao.inclui(verificacaoPosCheckout);
        } else {
            verificacaoPosCheckoutDao.altera(verificacaoPosCheckout);
        }

    }

    public List<VerificacaoPosCheckout> getNaoVerificado() {
        return verificacaoPosCheckoutDao.getNaoVerificado();
    }

    public VerificacaoPosCheckout getByCheckout(CheckOut checkOut) {
        try {
            return verificacaoPosCheckoutDao.getByCheckout(checkOut);
        } catch (ObjetoNaoEncontradoException ex) {
            return null;
        }
    }
}
