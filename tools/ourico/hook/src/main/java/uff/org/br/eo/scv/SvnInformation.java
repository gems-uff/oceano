/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uff.org.br.eo.scv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.admin.SVNLookClient;

/**
 *
 * @author marapao
 */
public class SvnInformation {

    private String repository;
    private String version;
    private String loginSVN;
    private String senhaSVN;
    private List<String> changedDirectories;



    public List<String> getChangedDirectories() {
        return changedDirectories;
    }

    public void setChangedDirectories(List<String> changedDirectories) {
        this.setChangedDirectories(changedDirectories);
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public SvnInformation() {
        repository = null;
        version = null;
    }

    public SvnInformation(String repository, String version) {
        this.repository = repository;
        this.version = version;
        this.changedDirectories = new ArrayList<String>();

    }
    public SvnInformation(String repository, String version, String loginSVN, String senhaSVN) {
        this.repository = repository;
        this.version = version;
        this.changedDirectories = new ArrayList<String>();
        this.loginSVN = loginSVN;
        this.senhaSVN = senhaSVN;

    }

    public List<String> returnChangedDirectories() {

        if (getRepository() == null) {
            return null;
        }
        if (getVersion() == null) {
            return null;
        }

        File repositoryDir = new File(getRepository());
        changedDirectories svnChangedDirectories = new changedDirectories();

        ISVNAuthenticationManager svnManager = new BasicAuthenticationManager(loginSVN, senhaSVN);
        ISVNOptions svnOptions = new DefaultSVNOptions();
        SVNLookClient cliLook = new SVNLookClient(svnManager, svnOptions);

        try {
            cliLook.doGetChangedDirectories(repositoryDir, SVNRevision.create(Long.parseLong(getVersion())), svnChangedDirectories);
        } catch (SVNException ex) {
            Logger.getLogger(SvnInformation.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        

        return svnChangedDirectories.getDirectories();
    }

    public String retornaMensagem(){

        String result = null;
        
        if (getRepository() == null) {
            return null;
        }
        if (getVersion() == null) {
            return null;
        }



        ISVNAuthenticationManager svnManager = new BasicAuthenticationManager("", "");
        ISVNOptions svnOptions = new DefaultSVNOptions();
        SVNLookClient cliLook = new SVNLookClient(svnManager, svnOptions);

        File rep = new File(getRepository());
        try {
            result = cliLook.doGetLog(rep, SVNRevision.create(Long.parseLong(getVersion())));
        } catch (SVNException ex) {
            System.out.println("Problema ao fazer svnlook log");
            Logger.getLogger(SvnInformation.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return result;

    }

    /**
     * @return the loginSVN
     */
    public String getLoginSVN() {
        return loginSVN;
    }

    /**
     * @param loginSVN the loginSVN to set
     */
    public void setLoginSVN(String loginSVN) {
        this.loginSVN = loginSVN;
    }

    /**
     * @return the senhaSVN
     */
    public String getSenhaSVN() {
        return senhaSVN;
    }

    /**
     * @param senhaSVN the senhaSVN to set
     */
    public void setSenhaSVN(String senhaSVN) {
        this.senhaSVN = senhaSVN;
    }

   


}
