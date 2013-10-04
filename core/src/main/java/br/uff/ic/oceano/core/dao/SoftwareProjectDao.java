/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.model.Item;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public interface SoftwareProjectDao extends DaoGenerico<SoftwareProject, Long>{


    public SoftwareProject getProjectToDetailById(Long id) throws ObjetoNaoEncontradoException;

    public SoftwareProject getByItem(Item item) throws ObjetoNaoEncontradoException;

    public SoftwareProject getByRepositoryUrl(String repositoryUrl) throws ObjetoNaoEncontradoException;
    
    public List<SoftwareProject> getProjectsByOceanoUser(OceanoUser oceanoUser);

    public List<SoftwareProject> getMavenProjectsByUser(OceanoUser oceanoUser);

}
