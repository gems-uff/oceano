/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.SoftwareProjectDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.model.Item;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class SoftwareProjectDaoImpl extends JPADaoGenerico<SoftwareProject, Long> implements SoftwareProjectDao {

    public SoftwareProjectDaoImpl() {
        super(SoftwareProject.class);
    }

    @MetodoRecuperaUnico
    public SoftwareProject getProjectToDetailById(Long id) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public SoftwareProject getByRepositoryUrl(String repositoryUrl) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public SoftwareProject getByItem(Item item) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<SoftwareProject> getProjectsByOceanoUser(OceanoUser oceanoUser) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<SoftwareProject> getMavenProjectsByUser(OceanoUser oceanoUser) {
        throw new MetodoInterceptadoException();
    }

}
