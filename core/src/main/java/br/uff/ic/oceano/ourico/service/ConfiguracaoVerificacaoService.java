/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ourico.dao.ConfiguracaoVerificacaoDao;
import br.uff.ic.oceano.ourico.dao.impl.ConfiguracaoVerificacaoDaoImpl;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import java.util.List;

/**
 *
 * @author marapao
 */
public class ConfiguracaoVerificacaoService implements PersistenceService{

    private ConfiguracaoVerificacaoDao configuracaoVerificacaoDao;

    public void setup(){
        configuracaoVerificacaoDao = ObjectFactory.getObjectWithDataBaseDependencies(ConfiguracaoVerificacaoDaoImpl.class);
    }

    public ConfiguracaoVerificacaoService() {
    }

    @Transacional
    public void save(ConfiguracaoVerificacao configuracaoVerificacao) {
        if (configuracaoVerificacao.getId() == null) {
            configuracaoVerificacaoDao.inclui(configuracaoVerificacao);
        } else {
            configuracaoVerificacaoDao.altera(configuracaoVerificacao);
        }

    }

    public List<ConfiguracaoVerificacao> get(){
        return configuracaoVerificacaoDao.get();
    }
}
