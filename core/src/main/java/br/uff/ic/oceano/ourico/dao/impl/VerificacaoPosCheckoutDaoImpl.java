/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ourico.dao.VerificacaoPosCheckoutDao;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.VerificacaoPosCheckout;
import java.util.List;

/**
 *
 * @author marapao
 */
public class VerificacaoPosCheckoutDaoImpl extends JPADaoGenerico<VerificacaoPosCheckout, Long> implements VerificacaoPosCheckoutDao{

    public VerificacaoPosCheckoutDaoImpl() {
        super(VerificacaoPosCheckout.class);
    }


    @MetodoRecuperaLista
    public List<VerificacaoPosCheckout> getNaoVerificado() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @MetodoRecuperaUnico
    public VerificacaoPosCheckout getByCheckout(CheckOut checkOut) throws ObjetoNaoEncontradoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
