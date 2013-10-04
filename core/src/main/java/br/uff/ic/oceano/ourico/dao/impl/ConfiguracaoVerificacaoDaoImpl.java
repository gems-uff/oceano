/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.ourico.dao.ConfiguracaoVerificacaoDao;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import java.util.List;

/**
 *
 * @author marapao
 */
public class ConfiguracaoVerificacaoDaoImpl extends JPADaoGenerico<ConfiguracaoVerificacao, Long> implements ConfiguracaoVerificacaoDao{

    public ConfiguracaoVerificacaoDaoImpl() {
        super(ConfiguracaoVerificacao.class);
    }




    @MetodoRecuperaLista
    public List<ConfiguracaoVerificacao> get() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
