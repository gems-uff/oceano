/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.controller;

import br.uff.ic.oceano.controller.BaseBean;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.ConfiguracaoVerificacao;
import br.uff.ic.oceano.ourico.service.ConfiguracaoVerificacaoService;
import java.util.List;

/**
 *
 * @author marapao
 */
public class OuricoVerificationBean extends BaseBean {

    private ConfiguracaoVerificacaoService configuracaoVerificacaoService = ObjectFactory.getObjectWithDataBaseDependencies(ConfiguracaoVerificacaoService.class);

    public OuricoVerificationBean() {
        super("OuricoVerificationBean");
    }
    private ConfiguracaoVerificacao currentConfiguracaoVerificacao;
 
    private String confirmacaoSenhaEmail;

    //caminho ate a pagina
    String caminho = "def:/privado/ourico/verification/includingDatas";

    public String doCadastrar() {
        List<ConfiguracaoVerificacao> list = configuracaoVerificacaoService.get();

        if ((list != null) && (!list.isEmpty())) {
            currentConfiguracaoVerificacao = list.get(0);
        } else {
            currentConfiguracaoVerificacao = new ConfiguracaoVerificacao();
        }
        return caminho;
    }

    /**
     * @return the configuracaoVerificacaoService
     */
    public ConfiguracaoVerificacaoService getConfiguracaoVerificacaoService() {
        return configuracaoVerificacaoService;
    }

    /**
     * @param configuracaoVerificacaoService the configuracaoVerificacaoService to set
     */
    public void setConfiguracaoVerificacaoService(ConfiguracaoVerificacaoService configuracaoVerificacaoService) {
        this.configuracaoVerificacaoService = configuracaoVerificacaoService;
    }


    public String getConfirmacaoSenhaEmail() {
        return confirmacaoSenhaEmail;
    }

    /**
     * @param confirmacaoSenhaEmail the confirmacaoSenhaEmail to set
     */
    public void setConfirmacaoSenhaEmail(String confirmacaoSenhaEmail) {
        this.confirmacaoSenhaEmail = confirmacaoSenhaEmail;
    }


    public String doSave() {

        if (!currentConfiguracaoVerificacao.getSenhaEmail().equals(confirmacaoSenhaEmail)) {
            info("Senha e Confimação de senha não são iguais");
            return null;
        }

        info("Configuração realizada com sucesso.");
        configuracaoVerificacaoService.save(currentConfiguracaoVerificacao);

        return null;
    }

    public String doCancel() {

        currentConfiguracaoVerificacao = new ConfiguracaoVerificacao();

        return null;
    }

    /**
     * @return the currentConfiguracaoVerificacao
     */
    public ConfiguracaoVerificacao getCurrentConfiguracaoVerificacao() {
        return currentConfiguracaoVerificacao;
    }

    /**
     * @param currentConfiguracaoVerificacao the currentConfiguracaoVerificacao to set
     */
    public void setCurrentConfiguracaoVerificacao(ConfiguracaoVerificacao currentConfiguracaoVerificacao) {
        this.currentConfiguracaoVerificacao = currentConfiguracaoVerificacao;
    }
}
