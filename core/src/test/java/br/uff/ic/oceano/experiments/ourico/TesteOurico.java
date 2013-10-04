//package br.uff.ic.oceano.bd.ourico;
//
///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//import br.uff.ic.oceano.core.control.ApplicationConstants;
//import br.uff.ic.oceano.core.dao.controle.JPAUtil;
//import br.uff.ic.oceano.core.exception.InfraestruturaException;
//import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
//import br.uff.ic.oceano.core.factory.ObjectFactory;
//import br.uff.ic.oceano.ourico.controle.ConstantesOurico;
//import br.uff.ic.oceano.ourico.model.CheckOut;
//import br.uff.ic.oceano.ourico.model.Estado;
//import br.uff.ic.oceano.ourico.service.CheckOutService;
//import br.uff.ic.oceano.ourico.service.EstadoService;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.List;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
///**
// *
// * @author Heliomar
// */
//public class TesteOurico {
//
////    public CheckOutService checkOutService = ObjectFactory.getObj(CheckOutService.class);
////    public EstadoService estadoService = ObjectFactory.getObj(EstadoService.class);
//
//    public TesteOurico() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//        ApplicationConstants.CURRENT_PERSISTENCE_UNIT = ApplicationConstants.PERSISTENCE_UNIT_MEMORY;
//        System.out.println("ESTABELECENDO O JPA");
//        JPAUtil.startUp();
//        System.out.println("JPA ESTABELECIDO");
//    }
//
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//    }
//
//    @Test(expectedExceptions = InfraestruturaException.class)
//    public void testeInsereNuloNoBd() {
//        CheckOut checkOut = new CheckOut();
//        checkOutService.save(checkOut);
//    }
//
//    @Test
//    public void insereEstado() {
//        Estado estado;
//
//
//        estado = new Estado();
//
//        Calendar c = new GregorianCalendar();
//
//
//
////        estado.setInicial(new Date(System.currentTimeMillis()));
//        estado.setInicial(c.getInstance().getTime());
//        estado.setAutobranch(new Long(1));
//        estado.setDescricao(ConstantesOurico.ANALISE_FISICA_2);
//        estado.setFim(c.getInstance().getTime());
//
//        estadoService.save(estado);
//    }
//
//    @Test(dependsOnMethods = "insereEstado")
//    public void recuperaEstado() {
//        List<Estado> estados = estadoService.getByAutobranch(1l);
//
//        for (Estado estado : estados) {
//
//            System.out.println(estado.getStringInicial() + " " + estado.getStringFim() + " " + estado.getDescricao() + " " + estado.getAutobranch());
//        }
//
//
//    }
//
//    @Test(dependsOnMethods = "insereEstado")
//    public void recuperaEstadoByAutobranchDescricao() throws ObjetoNaoEncontradoException {
//        Estado estado = estadoService.getByAutobranchDescricao(1l, ConstantesOurico.ANALISE_FISICA_2);
//        System.out.println("------------------------");
//        System.out.println(estado.getStringInicial() + " " + estado.getStringFim() + " " + estado.getDescricao() + " " + estado.getAutobranch());
//
//    }
//
//    @Test
//    public void testeInsereNormalNoBd() {
////        CheckOut checkOut = new CheckOut();
////        checkOut.setAutobranch(1l);
////        checkOut.setRevisao(2l);
////        checkOut.setUrlRepositorio("URL_ouriço1");
////        checkOut.setWorkspace("sdfsdf");
////        checkOut.setDiretorioAutobranch("/autbranch/");
////        checkOut.setDiretorioProtegido("/trunk/");
////        checkOut.setEstado("Testando");
////        checkOut.setPolitica("Teste");
////        checkOut.setProjeto("Teste");
////        checkOut.setUsuario("Tester");
////
////        checkOutService.save(checkOut);
////
////        checkOut = new CheckOut();
////        checkOut.setAutobranch(2l);
////        checkOut.setRevisao(2l);
////        checkOut.setUrlRepositorio("URL_ouriço2");
////        checkOut.setWorkspace("sdfsdf");
////        checkOut.setDiretorioAutobranch("/autbranch/");
////        checkOut.setDiretorioProtegido("/trunk/");
////        checkOut.setEstado("Testando");
////        checkOut.setPolitica("Teste");
////        checkOut.setProjeto("Teste");
////        checkOut.setUsuario("Tester");
////
////        checkOutService.save(checkOut);
//    }
//
//    @Test(dependsOnMethods = "testeInsereNormalNoBd")
//    public void testeSelecionadoBd() throws ObjetoNaoEncontradoException {
////        CheckOut checkOut = checkOutService.getbyAutobranch(1L);
////        System.out.println(checkOut.getUrlRepositorio());
//    }
//
//    @Test(dependsOnMethods = "testeInsereNormalNoBd")
//    public void testeSelecionaMaxAautobranch() throws ObjetoNaoEncontradoException {
//        Long checkOut = checkOutService.getMaxAutobranch();
//        System.out.println("Maior " + checkOut);
//
//    }
//
//    @Test(dependsOnMethods = "testeInsereNormalNoBd")
//    public void testeSelecionaMaxAutobranch() throws ObjetoNaoEncontradoException {
//        CheckOut checkOut = checkOutService.getCheckOutwithMaxAutobranch();
//        System.out.println("Maior " + checkOut.toString());
//
//    }
//}
