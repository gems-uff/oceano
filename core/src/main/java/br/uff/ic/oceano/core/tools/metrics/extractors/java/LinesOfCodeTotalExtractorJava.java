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
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import java.io.File;
import javancss.Javancss;

/**
 * refactored by dheraclio
 *
 * @author DanCastellani
 */
public class LinesOfCodeTotalExtractorJava extends AbstractMetricExtractor {

    private JavaRevisionTool revTool = new JavaRevisionTool();

    public LinesOfCodeTotalExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        try {
            double sumLoc = 0;
            for (String filePath : revTool.getSourceFiles(revision)) {
                Double classLoc = extractLOC(revision, filePath).getDoubleValue();
                sumLoc += classLoc;
            }
            return createMetricValue(revision, sumLoc);
        } catch (Exception ex) {
            throw new MetricException(ex);
        }
    }

    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        return extractMetric(revision);
    }

    private MetricValue extractLOC(Revision revision, String path) throws MetricException {
        final Javancss javancss = new Javancss(new File(path));
        return createMetricValue(revision, javancss.getLOC());
    }
}
