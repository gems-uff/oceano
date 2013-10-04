/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;

/**
 * @author DHeraclio
 *
 */
public interface IMetricExtractor {

    public MetricValue extractMetric(Revision revision) throws MetricException;

    public MetricValue extractMetric(Revision revision, String path) throws MetricException;

    public void setMetricManager(MetricManager metricManager);

    public Language getLanguage();

}
