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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javancss.Javancss;

/**
 *
 * @author Daniel
 */
public class LinesOfCodeExtractorJava extends AbstractMetricExtractor {

    private JavaRevisionTool revTool = new JavaRevisionTool();

    public LinesOfCodeExtractorJava() {
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
            double LOC = 0;
            for (String filePath : revTool.getSourceFiles(revision)) {
                LOC += extractMetric(revision, filePath).getDoubleValue();
            }
            return createMetricValue(revision, LOC);
        } catch (Exception ex) {
            throw new MetricException(ex);
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
            Reader reader = new BufferedReader(new FileReader(path));
            final Javancss javancss = new Javancss(reader);
            MetricValue result = createMetricValue(revision, javancss.getLOC());
            //explicit release file reader
            reader.close();
            return result;
        } catch (Exception ex) {
            throw new MetricException(ex);
        }

    }
}
