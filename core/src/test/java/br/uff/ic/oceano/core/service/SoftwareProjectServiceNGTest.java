/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class SoftwareProjectServiceNGTest extends AbstractNGTest{
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        JPAUtil.startUp();
    }

    /**
     * Teste de método getAll, da classe SoftwareProjectService.
     */
    @Test
    public void testGetAll() {
        println("getAll");
        SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
        projectService.getAll();
    }    

}