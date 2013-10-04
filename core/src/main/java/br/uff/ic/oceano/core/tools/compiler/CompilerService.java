/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.compiler;

import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.ostra.exception.CompilerException;

/**
 *
 */
public class CompilerService {

    /**
     * //TODO refactor to multiple build tools.
     * @param revision
     * @throws CompilerException 
     */
    public synchronized static void compile(Revision revision) throws CompilerException {
        try {
            if (revision.getProject().isMavenProject()) {
                MavenUtil.compile(revision);
            } else if (revision.getProject().getLanguage().equals(Language.CPP)){
                return;                
            } else {
                throw new CompilerException("Unsupported project type.");
            }
        } catch (Exception ex) {
            throw new CompilerException(ex);
        }
    }
}
