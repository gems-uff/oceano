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
import java.util.Collection;

/**
 *
 * @author Daniel
 */
public class LinesOfCodeTotalExtractorCpp extends AbstractMetricExtractor {

    private static CPPRevisionTool cppTool = new CPPRevisionTool();

    public LinesOfCodeTotalExtractorCpp() {
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
            Collection<String> files = cppTool.getSourceFiles(revision);
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
        try {
            double count = 0;
            for (String filePath : cppTool.getSourceFiles(path)) {
                count += getLOC(filePath);
            }
            return createMetricValue(revision, count);
        } catch (Exception ex) {
            throw new MetricException("Fail to extract metric", ex);
        }
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
