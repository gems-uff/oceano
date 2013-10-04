/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.cpp;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.core.tools.metrics.extractors.cpp.easycount.EasyCountService;
import br.uff.ic.oceano.core.tools.metrics.extractors.cpp.easycount.EasyCountServiceException;
import br.uff.ic.oceano.core.tools.revision.CPPRevisionTool;
import br.uff.ic.oceano.core.tools.revision.RevisionTool;
import java.util.Set;

/**
 *
 * @author Daniel
 */
public class LinesOfCodeExtractorCpp extends AbstractMetricExtractor {

    private static RevisionTool cppTool = new CPPRevisionTool();

    public LinesOfCodeExtractorCpp() {
    }

    /**
     *
     * @param revision
     * @return
     * @throws MetricException
     */
    @Override
    public MetricValue extractMetric(Revision revision) throws MetricException {
        try {
            Set<String> files = cppTool.getSourceFiles(revision);
            double count = 0;
            for (String path : files) {
                count += getLOC(path);
            }
            return createMetricValue(revision, count);
        } catch (Exception ex) {
            throw new MetricException("Fail to extract metric", ex);
        }
    }

    /**
     *
     * @param revision
     * @param path
     * @return
     * @throws MetricException
     */
    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        return createMetricValue(revision, getLOC(path));
    }

    private double getLOC(String path) throws MetricException {
        try {
            EasyCountService easyCount = new EasyCountService();
            return easyCount.loc(path);
        } catch (EasyCountServiceException ex) {
            throw new MetricException(ex);
        }
    }
}
