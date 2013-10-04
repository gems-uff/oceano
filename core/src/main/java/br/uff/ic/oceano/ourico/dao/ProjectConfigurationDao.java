/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;

/**
 *
 * @author marapao
 */
public interface  ProjectConfigurationDao extends DaoGenerico<ProjectConfiguration, Long> {

    public ProjectConfiguration getByProject(SoftwareProject project) throws ObjetoNaoEncontradoException;

}
