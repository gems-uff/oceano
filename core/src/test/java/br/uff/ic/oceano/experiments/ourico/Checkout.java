/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.service.OceanoUserService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class Checkout {

    private OceanoUserService oceanoUserService = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);


    @BeforeClass
    public static void setUpClass() throws Exception {
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_LOCAL);
        JPAUtil.startUp();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

 @Test
    public void oceanoUser() throws ObjetoNaoEncontradoException{
        
//        List<OceanoUser> oceanoUsers = oceanoUserService.getAll();
        OceanoUser oceanoUser = oceanoUserService.getByLogin("gleiph");

        System.out.println(oceanoUser.getEmail());
//        for (OceanoUser oceanoUser : oceanoUsers) {
//            System.out.println(oceanoUser.getName());
//        }

    }
}
