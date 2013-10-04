/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.ProjectUserDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.ProjectUser;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class ProjectUserDaoImpl extends JPADaoGenerico<ProjectUser, Long> implements ProjectUserDao {

    public ProjectUserDaoImpl() {
        super(ProjectUser.class);
    }

    @MetodoRecuperaUnico
    public ProjectUser getByProjectAndOceanoUser(SoftwareProject project, OceanoUser oceanoUser) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public ProjectUser getByProjectAndLogin(SoftwareProject project, String login) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<ProjectUser> getByOceanoUser(OceanoUser oceanoUser) {
        throw new MetodoInterceptadoException();
    }
}
