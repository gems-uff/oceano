/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.Adapter;
import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.MetricHelper;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.util.NumberUtil;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class DependomenterExtractor extends AbstractMetricExtractor {

    //stored revisions
    private static final byte size = 2;
    //store revision add order
    private static List<Revision> order = new LinkedList<Revision>();
    //buffered processed revisions
    private static LinkedHashMap<Revision, Adapter> buffer = new LinkedHashMap<Revision, Adapter>(size);

    public DependomenterExtractor() {
    }

    /**
     * Returns average value for hole project revision.
     *
     * @param revision
     * @return
     * @throws MetricException
     */
    @Override
    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    /**
     * Returns value of paths package
     *
     * @param revision
     * @param path
     * @return
     * @throws MetricException
     */
    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            Adapter service = getService(revision);
            Double result;
            if (isCalculatedMetric(getMetric())) {
                result = calculateMetric(service, getMetric(), path);
            } else {
                result = service.getMetric(getMetric(), path);
            }
            return createMetricValue(revision, result);
        } catch (Exception ex) {
            throw new MetricException("Exception while extracting " + getMetric().getName(), ex);
        }
    }

    private Double calculateMetric(Adapter service, Metric metric, String path) throws MetricException {
        try {
            if (MetricEnumeration.DSC.same(metric)) {
                MetricEnum dependometer = MetricHelper.getDependometerMetric(MetricEnumeration.DSC);
                List<Double> values = service.getCompilationUnitValues(dependometer);
                return NumberUtil.sum(values);
            } else if (MetricEnumeration.ANA.same(metric)) {
                MetricEnum dependometer = MetricHelper.getDependometerMetric(MetricEnumeration.ANA);
                List<Double> values = service.getTypeValues(dependometer);
                return NumberUtil.avg(values);
            } else if (MetricEnumeration.NOH.same(metric)) {
                MetricEnum dependometer = MetricHelper.getDependometerMetric(MetricEnumeration.NOH);
                List<Double> values = service.getTypeValues(dependometer);
                int numHierarchies = 0;
                //count classes with DepthOfClassInheritance > 0
                for (Double value : values) {
                    if (value > 0) {
                        numHierarchies++;
                    }
                }
                return new Double(numHierarchies);
            } else if (MetricEnumeration.DCC.same(metric)) {
                return service.getMetric(metric, path);
            }
        } catch (Exception ex) {
            throw new MetricException(ex);
        }

        throw new MetricException("Metric not calculated: " + metric);
    }

    private boolean isCalculatedMetric(Metric metric) throws DependometerException {
        return (MetricEnumeration.DSC.same(metric)
                || MetricEnumeration.ANA.same(metric)
                || MetricEnumeration.NOH.same(metric)
                || MetricEnumeration.DCC.same(metric));
    }

    private synchronized Adapter getService(Revision revision) {
        Adapter adapter = buffer.get(revision);
        if (adapter == null) {
            adapter = new Adapter(revision);
            buffer.put(revision, adapter);
            order.add(revision);

            if (order.size() > size) {
                Revision oldest = order.remove(0);
                buffer.remove(oldest);
            }
        }
        return adapter;
    }
}
