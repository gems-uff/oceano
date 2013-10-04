/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface VersionedItemMetricValueDao extends DaoGenerico<VersionedItemMetricValue, Long> {

    public long countByRevisionAndMetric(Revision revision, Metric metric);

    public List<VersionedItemMetricValue> getByRevisionAndMetric(Revision revision, Metric metric);

    public List<VersionedItemMetricValue> getByRevision(Revision revision);

    public VersionedItemMetricValue getNewestByItemAndMetricUntilRevision(Item item, Metric metric, Long revisionNumber);

}
