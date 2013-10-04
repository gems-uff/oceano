/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import java.util.List;

/**
 *
 * @author marapao
 */
public interface ConfiguracaoVerificacaoDao extends DaoGenerico<ConfiguracaoVerificacao, Long> {

    public List<ConfiguracaoVerificacao> get();
}
