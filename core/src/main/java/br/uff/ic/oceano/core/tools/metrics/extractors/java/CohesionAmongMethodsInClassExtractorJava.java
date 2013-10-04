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
import java.util.HashSet;
import java.util.Set;
import org.aspectj.apache.bcel.classfile.ClassFormatException;

/**
 *
 * @author wallace
 */
public class CohesionAmongMethodsInClassExtractorJava extends AbstractMetricExtractor {

    public CohesionAmongMethodsInClassExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {

        try {
            Class clazz = ClassLoaderUtil.loadClass(revision,path);
            java.lang.reflect.Method[] methods = clazz.getDeclaredMethods();

            Set totalUniqueTypes = new HashSet();
            Set uniqueParameterTypes = new HashSet();
            int sum = 0;
            for (int i = 0; i < methods.length; i++) {
                Class[] parameters = methods[i].getParameterTypes();
                for (int j = 0; j < parameters.length; j++) {
                    totalUniqueTypes.add(parameters[j].getName());
                    uniqueParameterTypes.add(parameters[j].getName());
                }
                //count only unique types
                sum += uniqueParameterTypes.size();
                uniqueParameterTypes.clear();
            }

            double aux2 = methods.length * totalUniqueTypes.size();
            double cam = 0;
            if (aux2 != 0) {
                cam = sum / aux2;
            } else {
                cam = Double.NaN;
            }
            return createMetricValue(revision, cam);

        } catch (ClassFormatException ex) {
            throw new MetricException(ex);
        } catch (Exception ex) {
            throw new MetricException(ex);
        }
    }
}
