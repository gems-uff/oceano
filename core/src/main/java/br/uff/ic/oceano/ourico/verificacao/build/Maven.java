/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.verificacao.build;

import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author marapao
 */
public class Maven implements Construcao {

    private String urlProjeto;
    private String pathSettings;
    private String repositorioLocal;

    public Maven() {
        this.pathSettings = "";
        this.urlProjeto = "";
        this.urlProjeto = "";
    }

    public Maven(String urlProjeto, String pathSettings, String repoLocal) {
        this.urlProjeto = urlProjeto;
        this.pathSettings = pathSettings;
        this.repositorioLocal = repoLocal;
    }

    public String getRepositorioLocal() {
        return repositorioLocal;
    }

    public void setRepositorioLocal(String repositorioLocal) {
        this.repositorioLocal = repositorioLocal;
    }

    public String getPathSettings() {
        return pathSettings;
    }

    public void setPathSettings(String pathSettings) {
        this.pathSettings = pathSettings;
    }

    public String getUrlProjeto() {
        return urlProjeto;
    }

    public void setUrlProjeto(String urlProjeto) {
        this.urlProjeto = urlProjeto;
    }

    @Override
    public List<Throwable> limpa() throws Exception {
        return executaAcoes("clean");

    }

    @Override
    public List<Throwable> instala() throws Exception {
        return executaAcoes("install");
    }

    @Override
    public List<Throwable> compila() throws Exception {
        return executaAcoes("compile");
    }

    @Override
    public List<Throwable> testa() throws Exception {
        return executaAcoes("test");

    }

    @Override
    public List<Throwable> executaAcoes(String ...objetivos) throws Exception {
        return MavenUtil.execute(urlProjeto.substring(0, urlProjeto.length()), Arrays.asList(objetivos), pathSettings);

    }
}
