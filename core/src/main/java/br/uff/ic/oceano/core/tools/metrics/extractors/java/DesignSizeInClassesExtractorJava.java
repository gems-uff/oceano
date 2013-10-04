/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.core.tools.revision.JavaRevisionTool;
import java.util.Collection;

/**
 * refactored by dheraclio
 * @author wallace
 */
public class DesignSizeInClassesExtractorJava extends AbstractMetricExtractor {

    private JavaRevisionTool revTool = new JavaRevisionTool();

    public DesignSizeInClassesExtractorJava() {

    }

    @Override
    public MetricValue extractMetric(Revision configuration) throws MetricException {
        return this.extractMetric(configuration, configuration.getLocalPath());
    }

    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        Collection<String> fileNames;
        try {
            fileNames = revTool.getPathsFromCompiledJavaClasses(revision);
        } catch (Exception ex) {
            throw new MetricException(ex);
        }

        if (fileNames.isEmpty()) {
            return createMetricValue(revision, 0D);
        } else {
            return createMetricValue(revision, fileNames.size());
        }
    }
}
