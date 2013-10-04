/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.RevisionDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaConjunto;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.OceanoUser;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Heliomar
 */
public class RevisionDaoImpl extends JPADaoGenerico<Revision, Long> implements RevisionDao {

    public RevisionDaoImpl() {
        super(Revision.class);
    }

    @MetodoRecuperaLista
    public List<Revision> getByUsuario(OceanoUser usuario) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Revision getByCaminhoLocal(String caminhoLocal) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Revision getByNumberAndProject(Long number, SoftwareProject project) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Revision getWithRevisionedItemsAndItemsAndMetricValues(Revision revision) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaConjunto
    public Set<Revision> getByProject(SoftwareProject project) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Revision getWithChangedFiles(Revision revision) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }
}
