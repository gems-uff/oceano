/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.SoftwareProject;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface MetricDao extends DaoGenerico<Metric, Long> {

    public Metric getByNome(String nome) throws ObjetoNaoEncontradoException;

    public List<Metric> getAll();

    public List<Metric> getMetricsByProjectRevisions(SoftwareProject project);
    public List<Metric> getMetricsByProjectVersionedItems(SoftwareProject project);

    public Metric getByAcronym(String acronym)throws ObjetoNaoEncontradoException;
}
