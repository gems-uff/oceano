/*
 * FiltroSegurancaOrion.java
 *
 * Created on 12 de Outubro de 2007, 19:47
 */

package br.uff.ic.oceano.filtro;

import br.uff.ic.oceano.controller.SessaoDoUsuario;
import java.io.*;
import javax.servlet.http.*;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author  thiago
 * @version
 */

public class FiltroSeguranca implements Filter {
    
    private String PAGINA_LOGIN = "/login.oceano";
    
    private FilterConfig filterConfig = null;
    
    private Logger logger = Logger.getLogger( "FiltroSeguranca" );
    
    public FiltroSeguranca() {
    }
    
    /**
     *
     * @param request The servlet request we are processing
     * @param result The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest servReq = (HttpServletRequest) request;
        
        HttpSession session = servReq.getSession();
        
        if(logger.isDebugEnabled()) {
            logger.debug("Iniciando filtro de requisicao a :" + servReq.getContextPath());
        }
        
        SessaoDoUsuario sessao = (SessaoDoUsuario) session.getAttribute("SessaoDoUsuario");

//        String urlRequisitada = servReq.getRequestURI();
        // filtro normal
        if(sessao==null || sessao.getUsuarioCorrente()==null) {
            ((HttpServletResponse)response).sendRedirect(servReq.getContextPath() + PAGINA_LOGIN);
            if(logger.isDebugEnabled()) {
                logger.debug("Requisicao filtrada, chain negado. Redirecionando para :\"" 
                        + servReq.getContextPath() + PAGINA_LOGIN +"\"");
            }
            return;
        }

        if(logger.isDebugEnabled()) {
            logger.debug("Requisicao filtrada, chain autorizado.");
        }
        
        chain.doFilter(request, response);
    }
    
    
    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }
    
    
    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
    
    /**
     * Destroy method for this filter
     *
     */
    public void destroy() {
    }
    
    
    /**
     * Init method for this filter
     *
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }
    
}
