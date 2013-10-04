/*
 * BaseBean.java
 *
 * Created on 12 de Outubro de 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package br.uff.ic.oceano.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class BaseBean {

    protected Logger logger;
    protected SessaoDoUsuario sessao;
    public static final String PAGINA_PRINCIPAL = "def:/privado/principal";
    public static final String PAGINA_LOGIN = "def:/login";

    /**
     * Nome do logger desejado
     */
    public BaseBean(final String nomeDoLogger) {
        logger = Logger.getLogger(nomeDoLogger);
        getSessaoDoUsuario();
    }
    
    public BaseBean(final String nomeDoLogger,SessaoDoUsuario sessao) {
        logger = Logger.getLogger(nomeDoLogger);
        setSessaoDoUsuario(sessao);
    }

    /**
     * Retorna o contexto JSF
     */
    protected static FacesContext getContexto() {
        return FacesContext.getCurrentInstance();
    }

    /**
     * Retorna um bean do escopo JSF
     */
    @SuppressWarnings("deprecation")
    protected Object getBean(final String nome) {
        final FacesContext contexto = getContexto();
        return contexto.getApplication().getVariableResolver().resolveVariable(contexto, nome);
    }

    protected void fatal(final String message) {
        getContexto().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, message, ""));
    }

    protected void error(final String message) {
        getContexto().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, message, ""));
    }

    protected void info(final String message) {
        getContexto().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, ""));
    }

    protected void warn(final String message) {
        getContexto().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, ""));
    }

//    // Kann - 14-01-2008
//    protected String getMensagemBundled(String nome) {
//        return BeanUtils.getMessageResourceString(getContexto().getApplication().getMessageBundle(), nome, null, getContexto().getViewRoot().getLocale());
//    }
    // Kann - 14-01-2008
    public static List<SelectItem> getListaString(String nome) {
        String linha = getMessageResourceString(FacesContext.getCurrentInstance().getApplication().getMessageBundle(), nome, null, FacesContext.getCurrentInstance().getViewRoot().getLocale());

        String[] tokens = linha.split("#");
        ArrayList<SelectItem> lista = new ArrayList<SelectItem>(tokens.length);

        for (String elem : tokens) {
            elem = elem.toUpperCase();
            lista.add(new SelectItem(elem, elem));
        }

        return lista;
    }

    protected SessaoDoUsuario getSessaoDoUsuario() {
        if (sessao == null) {
            sessao = (SessaoDoUsuario) getBean("SessaoDoUsuario");
        }
        return sessao;
    }
    
    private void setSessaoDoUsuario(SessaoDoUsuario sessao) {
        this.sessao =sessao;
    }

    public static String getMessageResourceString(String key,Object params[]){
        String text = null;

        Locale  locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        String bundleName = "br.uff.ic.oceano.messages.Mensagens";

        ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));

        try {
            text = bundle.getString(key);
        } catch (MissingResourceException e) {
            text = "?? chave " + key + " não encontrada no arquivo de mensagens " + bundleName + " ??";
        }

        if (params != null) {
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params, new StringBuffer(), null).toString();
        }

        return text;
    }

    public static String getMessageResourceString(
            String bundleName,
            String key,
            Object params[],
            Locale locale) {

        String text = null;

        ResourceBundle bundle =
                ResourceBundle.getBundle(bundleName, locale,
                getCurrentClassLoader(params));

        try {
            text = bundle.getString(key);
        } catch (MissingResourceException e) {
            text = "?? chave " + key + " não encontrada no arquivo de mensagens " + bundleName + " ??";
        }

        if (params != null) {
            MessageFormat mf = new MessageFormat(text, locale);
            text = mf.format(params, new StringBuffer(), null).toString();
        }

        return text;
    }

    protected void invalidarBean(final String nome) {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).setAttribute(nome, null);
    }

    // Kann - 14-01-2008
    protected static ClassLoader getCurrentClassLoader(Object defaultObject) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = defaultObject.getClass().getClassLoader();
        }
        return loader;
    }

    public static Object getParametroRequest(String nomeParametro) {
        return getContexto().getExternalContext().getRequestParameterMap().get(nomeParametro);
    }

    
}
