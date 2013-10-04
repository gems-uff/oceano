/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.ourico.model.VerificacaoPosCheckout;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import br.uff.ic.oceano.ourico.service.VerificacaoPosCheckoutService;
import java.util.List;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class VerificacaoPost {

    private VerificacaoPosCheckoutService verificacaoPosCheckoutService = ObjectFactory.getObjectWithDataBaseDependencies(VerificacaoPosCheckoutService.class);
    private CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);

    @BeforeClass
    public void before(){        
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_LOCAL);
        JPAUtil.startUp();
    }

    @AfterClass
    public void after(){

    }

    @Test
    public void insere() throws ObjetoNaoEncontradoException{

        VerificacaoPosCheckout verificacaoPosCheckout = new VerificacaoPosCheckout();
        
        verificacaoPosCheckout.setSemantica(false);
        verificacaoPosCheckout.setSintatica(false);
        verificacaoPosCheckout.setVerificado(false);
        verificacaoPosCheckout.setVerificando(false);
        verificacaoPosCheckout.setCheckOut(checkOutService.getbyAutobranch(new Long("1")));

        verificacaoPosCheckoutService.save(verificacaoPosCheckout);

    }

    @Test(dependsOnMethods="insere")
    public void recupera(){
        List<VerificacaoPosCheckout> naoVerificado = verificacaoPosCheckoutService.getNaoVerificado();
        for (VerificacaoPosCheckout verificacaoPosCheckout : naoVerificado) {
            System.out.println(verificacaoPosCheckout.getSemantica().toString());
        }
    }
}
