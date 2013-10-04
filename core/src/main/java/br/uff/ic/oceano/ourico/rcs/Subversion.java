/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.rcs;

//import java.io.File;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.internal.wc.admin.SVNAdminAreaFactory;
import org.tmatesoft.svn.core.wc.ISVNConflictHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNConflictChoice;
import org.tmatesoft.svn.core.wc.SVNConflictDescription;
import org.tmatesoft.svn.core.wc.SVNConflictReason;
import org.tmatesoft.svn.core.wc.SVNConflictResult;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNMergeFileSet;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNRevisionRange;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 *
 * @author marapao
 */
public class Subversion {

    SVNClientManager cliente = null;

    public static void iniciaRepositorio(String url) {

        String[] partes = url.split(":");

        if (partes.length > 1) {
            if (partes[0].equals("http") || partes[0].equals("https")) {
                DAVRepositoryFactory.setup();
            } else if (partes[0].equals("file")) {
                FSRepositoryFactory.setup();
            } else {
                SVNRepositoryFactoryImpl.setup();
            }
        } else {
            throw new IndexOutOfBoundsException("URL inválida.");
        }
    }

    public Subversion() {
        System.out.println("construtor");
//        cliente = SVNClientManager.newInstance(null, "marapao", "Xcx3xy5T");
        cliente = SVNClientManager.newInstance();
        
        System.out.println("fim construtor");
    }

    public Subversion(String urlAutenticada, String Login, String Senha) {
        DefaultSVNOptions defaultSVNOptions = new DefaultSVNOptions();
        cliente = SVNClientManager.newInstance(defaultSVNOptions, Login, Senha);
        iniciaRepositorio(urlAutenticada);
    }

    public Long copia(String urlOrigem, String urlDestino, SVNRevision revision) throws SVNException {

        //criando SVNURL
        SVNURL origem = null, destino = null;
        origem = SVNURL.parseURIEncoded(urlOrigem);
        destino = SVNURL.parseURIEncoded(urlDestino);

        SVNCopySource[] copySource = {new SVNCopySource(revision, revision, origem)};
        SVNProperties sVNProperties = new SVNProperties();
        SVNCommitInfo sVNCommitInfo = null;


        //realizando a cópia

//        cliente.getCopyClient().doCopy(origem, SVNRevision.HEAD, destino, false, "realizando cópia da ultima versăo do trunk");
        sVNCommitInfo = cliente.getCopyClient().doCopy(copySource, destino, false, true, true, "Copiando do diretorio protegido para o autobranch", sVNProperties);

        return sVNCommitInfo.getNewRevision();
    }

    public Long checkout(String urlOrigem, String endDestino) throws SVNException {

        SVNURL origem;
        File destino;

        origem = SVNURL.parseURIEncoded(urlOrigem);
        destino = new File(endDestino);
        destino.mkdirs();


        return cliente.getUpdateClient().doCheckout(origem, destino, SVNRevision.HEAD, SVNRevision.HEAD, true);
//        return cliente.getUpdateClient().doCheckout(origem, destino, SVNRevision.WORKING, SVNRevision.HEAD, SVNDepth.INFINITY, false);

                
    }

    public SVNCommitInfo checkin(String workspace, String message) throws SVNException{

        File [] paths = {new File(workspace)};
        return cliente.getCommitClient().doCommit(paths, false, message, null, null, false, true, SVNDepth.INFINITY);


    }

    public List<String> mergePath(String url1, String url2, String destino) throws SVNException {


        //SVNWCClient wCClient = cliente.getWCClient();
        SVNWCClient wCClient = SVNClientManager.newInstance().getWCClient();

        SVNDiffClient diffClient = cliente.getDiffClient();

        //criando o DefaultOptions
        DefaultSVNOptions svnOptions = (DefaultSVNOptions) diffClient.getOptions();
        ConflictResolverHandler con = new ConflictResolverHandler();
        svnOptions.setConflictHandler(con);


        File ws1 = new File(url1);
        File ws2 = new File(url2);
        File wsDestino = new File(destino);
        wCClient.doSetWCFormat(ws1, SVNAdminAreaFactory.WC_FORMAT_14);
        wCClient.doSetWCFormat(ws2, SVNAdminAreaFactory.WC_FORMAT_14);
        wCClient.doSetWCFormat(wsDestino, SVNAdminAreaFactory.WC_FORMAT_14);


        cliente.getDiffClient().doMerge(ws1, SVNRevision.HEAD, ws2, SVNRevision.HEAD, wsDestino, SVNDepth.INFINITY, true, false, false, false);

        return con.getConflitos();


    }

    //usar essa aqui preferencialmente
    public List<String> mergePath(String url1, String workspace) throws SVNException {


        SVNWCClient wCClient = SVNClientManager.newInstance().getWCClient();
        SVNDiffClient diffClient = cliente.getDiffClient();
        SVNRevisionRange range = new SVNRevisionRange(SVNRevision.create(1), SVNRevision.HEAD);

        //criando o DefaultOptions
        DefaultSVNOptions svnOptions = (DefaultSVNOptions) diffClient.getOptions();
        ConflictResolverHandler con = new ConflictResolverHandler();
        svnOptions.setConflictHandler(con);

//        diffClient.doMerge(SVNURL.parseURIEncoded(url1), SVNRevision.HEAD, Collections.singleton(range), new File(workspace), SVNDepth.INFINITY, true, false, false, false);
        diffClient.doMerge(SVNURL.parseURIEncoded(url1), SVNRevision.UNDEFINED, Collections.singleton(range), new File(workspace), SVNDepth.INFINITY, true, false, false, false);

        return con.getConflitos();


    }
   
    public List<String> mergePathReintegrate(String url1, String workspace) throws SVNException {


        SVNWCClient wCClient = SVNClientManager.newInstance().getWCClient();
        SVNDiffClient diffClient = cliente.getDiffClient();
        SVNRevisionRange range = new SVNRevisionRange(SVNRevision.create(1), SVNRevision.HEAD);

        //criando o DefaultOptions
        DefaultSVNOptions svnOptions = (DefaultSVNOptions) diffClient.getOptions();
        ConflictResolverHandler con = new ConflictResolverHandler();
        svnOptions.setConflictHandler(con);

        diffClient.doMergeReIntegrate(SVNURL.parseURIEncoded(url1),SVNRevision.HEAD, new File(workspace), false);
        return con.getConflitos();


    }

    public SVNCommitInfo criaRamo(String urlRamo, String mensagem) throws SVNException {


        SVNURL url = SVNURL.parseURIEncoded(urlRamo);
        System.out.println("Criando ramo  " + urlRamo);


        return cliente.getCommitClient().doMkDir(new SVNURL[]{url}, mensagem);

    }

    private static class ConflictResolverHandler implements ISVNConflictHandler {

        List<String> conflitos;

        public ConflictResolverHandler() {
            this.conflitos = new LinkedList<String>();
        }

        public List<String> getConflitos() {
            return conflitos;
        }

        public void setConflitos(List<String> conflitos) {
            this.conflitos = conflitos;
        }


        @Override
        public SVNConflictResult handleConflict(SVNConflictDescription conflictDescription) throws SVNException {

            SVNConflictReason reason = conflictDescription.getConflictReason();
            SVNMergeFileSet mergeFiles = conflictDescription.getMergeFiles();

//            SVNConflictChoice choice = SVNConflictChoice.THEIRS_FULL;
//            if (reason == SVNConflictReason.EDITED) {
//                //If the reason why conflict occurred is local edits, chose local version of the file
//                //Otherwise the repository version of the file will be chosen.
//                choice = SVNConflictChoice.MINE_FULL;
//            }

            conflitos.add(mergeFiles.getWCFile().toString());
//            System.out.println("Automatically resolving conflict for " + mergeFiles.getWCFile()
//                    + ", choosing " + (choice == SVNConflictChoice.MINE_FULL ? "local file" : "repository file"));
//            return new SVNConflictResult(choice, mergeFiles.getResultFile());
            return null;
        }
    }

    public String getURL(String path) throws SVNException{

        File workspace = new File(path);
        SVNInfo info = cliente.getWCClient().doInfo(workspace, SVNRevision.WORKING);
        return info.getURL().toString();
    }

    public void update(String pathWorkspace, SVNRevision revision) throws SVNException{

        File workspace = new File(pathWorkspace);
        cliente.getUpdateClient().doUpdate(workspace, revision, SVNDepth.INFINITY, true,true);
    }
}
