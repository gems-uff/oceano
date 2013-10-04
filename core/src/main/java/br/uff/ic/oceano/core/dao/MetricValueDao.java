/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface MetricValueDao extends DaoGenerico<MetricValue, Long> {

    public List<MetricValue> getAll();

    public List<MetricValue> getAllSortedByConfigurationAndMetric();

    public Long getMetricValueId(Revision revision, Metric metric, boolean delta) throws ObjetoNaoEncontradoException;

    public long countByRevisionAndMetric(Revision revision, Metric metric) throws Exception;

    public List<MetricValue> getByRevision(Revision revision);

    public List<MetricValue> getValuesByProjectAndMetric(SoftwareProject softwareProject, Metric metric, boolean isDelta);

    public MetricValue getByRevisionMetricAndDelta(Revision revision, Metric metric, boolean delta) throws ObjetoNaoEncontradoException;
}
