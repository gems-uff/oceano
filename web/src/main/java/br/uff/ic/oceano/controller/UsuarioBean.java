package br.uff.ic.oceano.controller;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.OceanoUserService;

/**
 *
 * @author Kann
 */
public class UsuarioBean extends BaseBean{

    //String da tela de login
    private String loginDigitado;
    private String senhaDigitada;
    //Variavel corrente
    private String PAGINA_CADASTRO = "def:/privado/oceano/user/form";

    //Cadastro de usuários Oceano
    private String confirmacaoSenha;


    //Listagem dos comboboxes

    private OceanoUserService usuarioService;
    private OceanoUser currentUsuario;


    public UsuarioBean() {
        super("UsuarioBean");
        currentUsuario = new OceanoUser();
        usuarioService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
        sessao.setPerfilOceano();
    }

    public String doCadastrar(){
        return PAGINA_CADASTRO;
    }

    public String doCancel(){
        currentUsuario = null;
        return PAGINA_PRINCIPAL;
    }
    
    public String doSave(){

        if(!confirmacaoSenha.equals(currentUsuario.getPassword())){
            error("A senha e confirmação de senha não conferem");
            return null;
        }

        usuarioService.save(currentUsuario);
        info("Usuário Cadastrado com Sucesso");


        currentUsuario = new OceanoUser();

        return null;
    }

    public String doLogar(){
        try {
            currentUsuario = usuarioService.autenticarUsuario(loginDigitado, senhaDigitada);
        } catch (ServiceException ex) {
            error(ex.getMessage());
            return null;
        }
        getSessaoDoUsuario().setUsuarioCorrente(currentUsuario);
        return PAGINA_PRINCIPAL;
    }

    public String doDeslogar(){
        getSessaoDoUsuario().invalidar();
        return PAGINA_LOGIN;
    }

    /**
     * @return the loginDigitado
     */
    public String getLoginDigitado() {
        return loginDigitado;
    }

    /**
     * @param loginDigitado the loginDigitado to set
     */
    public void setLoginDigitado(String loginDigitado) {
        this.loginDigitado = loginDigitado;
    }

    /**
     * @return the senhaDigitada
     */
    public String getSenhaDigitada() {
        return senhaDigitada;
    }

    /**
     * @param senhaDigitada the senhaDigitada to set
     */
    public void setSenhaDigitada(String senhaDigitada) {
        this.senhaDigitada = senhaDigitada;
    }

    /**
     * @return the confirmacaoSenha
     */
    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    /**
     * @param confirmacaoSenha the confirmacaoSenha to set
     */
    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }

    /**
     * @return the currentUsuario
     */
    public OceanoUser getCurrentUsuario() {
        return currentUsuario;
    }

    /**
     * @param currentUsuario the currentUsuario to set
     */
    public void setCurrentUsuario(OceanoUser currentUsuario) {
        this.currentUsuario = currentUsuario;
    }
}