/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.Estado;
import br.uff.ic.oceano.ourico.service.EstadoService;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class BDPersistente {

    @BeforeClass
    public static void setUpClass() throws Exception {        
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_LOCAL);
        JPAUtil.startUp();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void informacoesAutobranch(){
        EstadoService estadoService = ObjectFactory.getObjectWithDataBaseDependencies(EstadoService.class);
        List<Estado> estados = estadoService.getByAutobranch(35l);

        for (Estado estado : estados) {
            System.out.println(estado.getAutobranch()+" "+estado.getInicio()+" "+estado.getFim()+" "+estado.getDescricao());
        }

    }

}
