/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.java;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.core.tools.metrics.util.ClassLoaderUtil;
import br.uff.ic.oceano.ostra.exception.CompilerException;
import java.util.List;

/**
 *
 * @author wallace
 *  Revision by DHeraclio
 *      converted to extractor
 */
public class AverageNumberOfAncestorsExtractorJava extends AbstractMetricExtractor {

    public AverageNumberOfAncestorsExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    @Override
    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {            
            List<Class> classes = ClassLoaderUtil.loadClasses(revision);                       
            if(classes == null || classes.isEmpty()){
                return createMetricValue(revision, 0D);
            }

            float count = 0;
            for (Class oneClass : classes) {
                Class superClass = oneClass.getSuperclass();
                while (superClass != null) {
                    superClass = superClass.getSuperclass();
                    count += 1;
                }
            }

            float ana = 0;
            if (!classes.isEmpty()) {
                ana = count / classes.size();
            }
            return createMetricValue(revision, ana);

        } catch (CompilerException ex) {
            throw new MetricException(ex);
        } catch (NoClassDefFoundError e) {
            throw new MetricException(e);
        } catch (Exception e) {
            throw new MetricException(e);
        }
    }
}
