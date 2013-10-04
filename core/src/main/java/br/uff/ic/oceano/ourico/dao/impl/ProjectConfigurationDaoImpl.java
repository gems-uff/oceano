/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ourico.dao.ProjectConfigurationDao;
import br.uff.ic.oceano.ourico.model.ProjectConfiguration;

/**
 *
 * @author marapao
 */
public class ProjectConfigurationDaoImpl extends JPADaoGenerico<ProjectConfiguration, Long> implements ProjectConfigurationDao {

    public ProjectConfigurationDaoImpl() {
        super(ProjectConfiguration.class);
    }



    @MetodoRecuperaUnico
    public ProjectConfiguration getByProject(SoftwareProject project) throws ObjetoNaoEncontradoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
