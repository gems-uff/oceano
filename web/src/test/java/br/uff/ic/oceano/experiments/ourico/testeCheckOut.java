/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.experiments.ourico;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ourico.service.CheckOutService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author marapao
 */
public class testeCheckOut {

     public CheckOutService checkOutService = ObjectFactory.getObjectWithDataBaseDependencies(CheckOutService.class);
     public SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);

    @BeforeClass
    public static void setUpClass() throws Exception {        
        JPAUtil.setCurrentPersistenceUnit(JPAUtil.PERSISTENCE_UNIT_LOCAL);        
        JPAUtil.startUp();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void informacoesAutobranch() throws ObjetoNaoEncontradoException{
        //        List<CheckOut> checkOuts = checkOutService.getAll();
        //
        //        for (CheckOut checkOut : checkOuts) {
        //            System.out.println(checkOut.getAutobranch());
        //        }
        //        Project bySuperRepository = projectService.getBySuperRepository("http://10.0.0.102/svn/trunk");
        //        Project projectByExtendedUrl = projectService.getProjectByExtendedUrl("http://10.0.0.102/svn/trunk");
        //
        //        System.out.println(projectByExtendedUrl.getName());
        //        System.out.println(bySuperRepository.getName());
//        List<SoftwareProject> ouricoProject = projectService.isOuricoProject();

//        for (SoftwareProject project : ouricoProject) {
//            System.out.println(project.getRepositoryUrl());
//        }
    }

    @Test
    public void selectMax() throws ObjetoNaoEncontradoException{
        Long maxAutobranch = checkOutService.getMaxAutobranch();
        System.out.println("maxAutobranch = " + maxAutobranch);


        
    }


}
