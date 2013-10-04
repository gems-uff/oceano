/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.verificacao.politicas;

import br.uff.ic.oceano.ourico.rcs.Subversion;
import br.uff.ic.oceano.ourico.verificacao.build.Maven;
import org.tmatesoft.svn.core.SVNException;

/**
 *
 * @author marapao
 */
public interface Politica {

    public void preProcessamento(String repAutobranch, String wsAutobranch, Subversion svn, boolean update) throws SVNException;

    public boolean posProcessamento(Subversion svn, String wsProtegido, String wsAutobranch, String repProtegido, String repAutobranch, StringBuffer log) throws SVNException;

    public boolean verificacao(Maven mvn, Subversion svn, String repProtegido, String wsVerificado, StringBuffer log, Long autobranch);

}
