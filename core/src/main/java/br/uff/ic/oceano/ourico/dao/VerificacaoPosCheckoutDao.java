/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.ourico.model.CheckOut;
import br.uff.ic.oceano.ourico.model.VerificacaoPosCheckout;
import java.util.List;

/**
 *
 * @author marapao
 */

public interface VerificacaoPosCheckoutDao extends DaoGenerico<VerificacaoPosCheckout, Long> {

    public List<VerificacaoPosCheckout> getNaoVerificado();
    public VerificacaoPosCheckout getByCheckout(CheckOut checkOut) throws ObjetoNaoEncontradoException;
}
