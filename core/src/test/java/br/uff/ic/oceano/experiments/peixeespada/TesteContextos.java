package br.uff.ic.oceano.experiments.peixeespada;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//import br.uff.ic.oceano.asf.servico.SetupASF;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Heliomar
 */
public class TesteContextos {

    public TesteContextos() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
    }

    @Test
    public void testeIntegracao() throws InterruptedException {
        JPAUtil.startUp();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  inicianco contexto ASF @@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
//        SetupASF.setup();
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  Contexto ASF INICIADO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ");
    }
}
