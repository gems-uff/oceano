/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import java.util.List;

/**
 *
 * @author dheraclio
 */
public interface MetricExtractorDao extends DaoGenerico<MetricExtractor, Long> {

    public List<MetricExtractor> getAll();

    public List<MetricExtractor> getMetricExtractorsByMetric(Metric metric);
}
