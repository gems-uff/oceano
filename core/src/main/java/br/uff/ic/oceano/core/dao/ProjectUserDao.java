/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface ProjectUserDao extends DaoGenerico<ProjectUser, Long>{


    public ProjectUser getByProjectAndOceanoUser(SoftwareProject project, OceanoUser oceanoUser) throws ObjetoNaoEncontradoException;
    public ProjectUser getByProjectAndLogin(SoftwareProject project, String login) throws ObjetoNaoEncontradoException;

    public List<ProjectUser> getByOceanoUser(OceanoUser oceanoUser);

}
