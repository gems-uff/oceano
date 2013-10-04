/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.teste;

import br.uff.ic.oceano.core.exception.VCSException;
import java.io.File;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitPacket;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 *
 * @author Heliomar
 */
public class TesteSvnKit {

    private static String SVN_PEIXE_ESPADA = "https://gems.ic.uff.br/svn/peixe-espada/trunk/";
    private static String SVN_SPRING_SECURITY = "https://springsecurityuff.googlecode.com/svn/trunk/";
    private static String SVN_SISTEMA_MULTI_AGENTES = "https://asf-ap-simulation.googlecode.com/svn/trunk/";
    private static String PASTA_WORKSPACE = "D:/TesteCheckoutSVN";
    private static String senha_ic = "animalboladao";

    public TesteSvnKit() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("ESTABELECENDO O SVN");
        /*
         * Para uso sobre http:// e https://
         */
        DAVRepositoryFactory.setup();
        /*
         * Para uso sobre svn:// e svn+xxx://
         */
        SVNRepositoryFactoryImpl.setup();
        /*
         * Para uso sobre  file:///
         */
        FSRepositoryFactory.setup();
        System.out.println("SVN ESTABELECIDO");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testCopyToAndCommitInCopy() throws VCSException{

        DefaultSVNOptions myOptions = new DefaultSVNOptions();

        SVNClientManager clientManager = SVNClientManager.newInstance(myOptions);
        File wp = new File("G:/Users/Heliomar/Documents/NetBeansProjects/JPADAOGenerico-Rep-Local-Branche/");

        SVNURL urlDst = null;
        try {
            urlDst = SVNURL.parseURIDecoded("file:///D:/repiduff/branches/peixeespada/JPADAOGenerico_1_5");
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }

        SVNCopySource copySource = new SVNCopySource(SVNRevision.WORKING, SVNRevision.WORKING, wp);
        SVNCopySource[] copySources = new SVNCopySource[]{copySource};

        try {
            clientManager.getCopyClient().doCopy(copySources, urlDst, false, true, true, "Message Commit", null);
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }

        try {
            clientManager.getUpdateClient().doSwitch(wp, urlDst, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false, true);
        } catch (SVNException ex) {
            try {
                clientManager.getWCClient().doCleanup(wp, true);
            } catch (SVNException ex1) {
                throw new VCSException(ex);
            }
            throw new VCSException(ex);
        }

//        try {
//            SVNStatus nStatus =clientManager.getStatusClient().doStatus(wp, false);
//            System.out.println(nStatus.getPropertiesStatus());
//            System.out.println(nStatus.getEntry());
////            clientManager.getWCClient().doAdd(wp, true, true, true, true);
//        } catch (SVNException ex) {
//            throw new VCSException(ex);
//        }
        try {
            clientManager.getWCClient().doAdd(wp, true, true, true, SVNDepth.INFINITY, true, true);
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }

        try {
            clientManager.getCommitClient().doCommit(new File[]{wp}, true, "mudanças", null, null, true, true, SVNDepth.INFINITY);
        } catch (SVNException ex) {
            throw new VCSException(ex);
        }
    }
    
//    @Test
    public void teste() throws SVNException {
        SVNURL url = SVNURL.parseURIDecoded(SVN_SPRING_SECURITY);
        SVNRepository repository = SVNRepositoryFactory.create(url, null);
//        ISVNEditor iSVNEditor =
//        repository.checkout(SVNRevision.HEAD.getNumber(), "kannnnnnn", true,ISVNEditor);

    }

//    private static void deletaArvoreArquivos(String path) {
//        File dir = new File(path);
//        if (dir.exists()) {
//            String[] children = dir.list();
//            if (children != null) {
//                for (int i = 0; i < children.length; i++) {
//                    File dir2 = new File(dir, children[i]);
//                    String[] netos = dir2.list();
//                    if (netos == null) {
//                        dir2.delete();
//                    } else {
//                        for (String caminho : netos) {
//                            deletaArvoreArquivos(caminho);
//                        }
//                    }
//                }
//            }
//        }
//    }
//    @Test
    public void testeCheckOut() throws SVNException {
        DefaultSVNOptions myOptions = new DefaultSVNOptions();
        SVNClientManager clientManager = SVNClientManager.newInstance(myOptions, "kann", senha_ic);

        SVNURL url = SVNURL.parseURIDecoded(SVN_SPRING_SECURITY);

        File dir = new File(PASTA_WORKSPACE);
        if (dir.exists()) {
            if (dir.list() != null) {
                throw new RuntimeException("Para Esse Teste O diretório [" + PASTA_WORKSPACE + "] deve estar vazio");
            }
        } else {
            dir.mkdir();
        }

        clientManager.createRepository(url, true);
        clientManager.getUpdateClient().doCheckout(url, dir, SVNRevision.UNDEFINED, SVNRevision.HEAD, true);

////         UPDATE
//        clientManager.getUpdateClient().doUpdate(dir, SVNRevision.HEAD, true);
//
////        COMMIT
//        clientManager.getCommitClient().doCommit(new File[] {new File(dir, "www")}, false, "message", false, true);

    }

//    @Test
//    public void logSVN() {
//        SVNClientImpl jniAPI = SVNClientImpl.newInstance();
//        byte[] contents = jniAPI.fileContent("http://svn.svnkit.com/repos/svnkit/branches/1.1.x/changelog.txt", Revision.HEAD);
//    }

//    @Test
    public void logModificacoesSVN() throws SVNException {

        ISVNAuthenticationManager aManager = SVNWCUtil.createDefaultAuthenticationManager("kann", senha_ic);

        SVNURL url = SVNURL.parseURIEncoded(SVN_PEIXE_ESPADA);
        SVNRepository repos = SVNRepositoryFactory.create(url);
        repos.setAuthenticationManager(aManager);

        long headRevision = repos.getLatestRevision();
        Collection<SVNDirEntry> entriesList = repos.getDir("", headRevision, null, (Collection) null);

        for (SVNDirEntry entry : entriesList) {
            System.out.println("Entrada: " + entry.getName());
            System.out.println("Última modificação da revisão: " + entry.getDate() +
                    " por " + entry.getAuthor());
            System.out.println("   --> MSG: "+entry.getCommitMessage());
        }
    }

}
