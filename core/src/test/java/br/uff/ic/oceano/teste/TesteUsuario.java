package br.uff.ic.oceano.teste;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.service.RevisionService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.OceanoUserService;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 *
 * @author Heliomar
 */
public class TesteUsuario {

    private OceanoUserDao usuarioDao = new OceanoUserDaoImpl();
    private OceanoUserService usuarioService;
    private SoftwareProjectService projectService;
    private RevisionService configurationService = ObjectFactory.getObjectWithDataBaseDependencies(RevisionService.class);

    public TesteUsuario() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("------------------------------ESTABELECENDO O JPA");
        JPAUtil.startUp();
        System.out.println("------------------------------JPA ESTABELECIDO");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    public void setUp() {
    }

//    @Test
    public void insere() {
        OceanoUserService us = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserService.class);
        OceanoUser u = new OceanoUser();
        u.setEmail("walabichas@gmail");
        u.setLogin("kann");
        u.setPassword("a");
        u.setName("Heliomar Kann da Rocha Santos");
        us.save(u);
        System.out.println("inseriu o kann");

        u = new OceanoUser();
        u.setEmail("walabichas@gmail");
        u.setLogin("dan");
        u.setPassword("a");
        u.setName("Daniel");
        us.save(u);
        System.out.println("inseriu o daniel");
    }

//    @Test
    public void insereProjeto() throws ObjetoNaoEncontradoException, ServiceException {
//        System.out.println("------------------------------> Criando projeto");
//        projectService = ObjectFactory.getObj(ProjectService.class);

//        SoftwareProject p = new SoftwareProject();
//        p.setName("Oceanp");
//        p.setRevision(configurationService.getConfigurationByCaminhoLocal("/oceano/trunk/"));
//        p.setNumberOfDownloads(0);
//        p.setNumberOfUsers(3);
//        p.setRelease("1.0 beta");

//        projectService.save(p);

//        System.out.println("------------------------------> Projeto salvo com sucesso");

    }

//    @Test
    public void login() throws ObjetoNaoEncontradoException, ServiceException {
//        System.out.println(usuarioService.autenticarUsuario("kann", "kann"));
        System.out.println(usuarioDao.getByLogin("dan"));
    }
}
