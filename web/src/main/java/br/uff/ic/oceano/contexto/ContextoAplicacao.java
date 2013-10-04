/*
 * ContextoSpring.java
 *
 * Created on 12 de Outubro de 2007, 17:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package br.uff.ic.oceano.contexto;

import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.util.CargaDefaultWeb;
import br.uff.ic.oceano.util.file.FileUtils;
import java.io.File;
import java.util.EventListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

/**
 *
 * @author Kann
 */
public class ContextoAplicacao implements EventListener, ServletContextListener {

    private Logger logger = Logger.getLogger(ConstantesAplicacao.class);

    public ContextoAplicacao() {
        super();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        JPAUtil.startUp();

        ConstantesAplicacao.DIR_BASE_JNLP = sce.getServletContext().getRealPath(ConstantesAplicacao.DIR_BASE_JNLP) + "/";
        ConstantesAplicacao.DIR_FILE_INFO_UPLOAD = ConstantesAplicacao.DIR_BASE_JNLP + "info.txt";
        
        if (JPAUtil.isRunningOnMemoryDB()) {
            if (!CargaDefaultWeb.isDefaultDataInserted()) {
                System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%EXECUTANDO CARGA PARA BANCO EM MEMÓRIA %%%%%%%%%%%%%%%%%%%%%%%\n");
                CargaDefaultWeb.insertDefaultData();
            }
        } 
        
        logger.info("******** Carregando Agentes Peixe-Espada ***********");
        ContextoAmbiente.getInstance().registraAllAtiveAgents();
        logger.info("******** Agentes Peixe-Espada carregador corretamente ***********");

        if (ApplicationConstants.CLEAN_CHECKOUT_DIRECTORY) {
            File dirBaseCheckout = new File(ConstantesAplicacao.DIR_BASE_CHECKOUTS);
            FileUtils.deleteDirectory(dirBaseCheckout);
            System.out.println(">>>> Cleaning checkout directory... " + dirBaseCheckout.delete());
        }
        
        //Force loading metrics now to speed up metric charts later
        MetricManagerFactory.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.shutdown();
    }
}
