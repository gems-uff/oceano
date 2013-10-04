/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
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
//@NamedQueries({
//
//    @NamedQuery(name="ConfiguracaoDao.getByUsuario",query="select c from Configuracao c where c.usuario= ? order by c.dataCriacao")
//
//})
public interface RevisionDao extends DaoGenerico<Revision, Long> {

    public Revision getByCaminhoLocal(String caminhoLocal) throws ObjetoNaoEncontradoException;

    public List<Revision> getByUsuario(OceanoUser usuario);

    public Revision getByNumberAndProject(Long number, SoftwareProject project) throws ObjetoNaoEncontradoException;

    public Revision getWithRevisionedItemsAndItemsAndMetricValues(Revision revision) throws ObjetoNaoEncontradoException;

    public Set<Revision> getByProject(SoftwareProject project);

    public Revision getWithChangedFiles(Revision revision) throws ObjetoNaoEncontradoException;
}
