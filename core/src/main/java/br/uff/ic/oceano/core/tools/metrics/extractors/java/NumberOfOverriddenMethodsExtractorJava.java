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
import java.util.List;

/**
 * refactored by dheraclio
 *
 * @author wallace
 */
public class NumberOfOverriddenMethodsExtractorJava extends AbstractMetricExtractor {

    public NumberOfOverriddenMethodsExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return extractMetricWithMetricService(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            List<Class> classes = ClassLoaderUtil.loadClasses(revision);

            int total = 0;
            for (Class clazz : classes) {
                java.lang.reflect.Method methods[] = clazz.getDeclaredMethods();
                Class superclass = clazz.getSuperclass();
                HashSet map = new HashSet();
                while (superclass != null) {
                    java.lang.reflect.Method supermethods[] = superclass.getDeclaredMethods();
                    for (int i = 0; i < methods.length; i++) {
                        String MethodString = methods[i].getName();
                        if (map.contains(i)) {
                            continue;
                        }
                        for (int j = 0; j < supermethods.length; j++) {
                            String superMethodString = supermethods[j].getName();
                            if (!superMethodString.equals(MethodString)) {
                                continue;
                            }
                            Class returntype1 = methods[i].getReturnType();
                            Class returntype2 = supermethods[j].getReturnType();
                            if (!returntype1.equals(returntype2)) {
                                continue;
                            }
                            Class[] parameter1 = methods[i].getParameterTypes();
                            Class[] parameter2 = supermethods[j].getParameterTypes();
                            if (parameter1.length != parameter2.length) {
                                continue;
                            }
                            int l = 0;
                            boolean parameter = true;
                            while ((l < parameter1.length) && parameter) {
                                if (!parameter1[l].equals(parameter2[l])) {
                                    parameter = false;
                                }
                                l++;
                            }
                            if (parameter) {
                                map.add(i);
                                total++;
                            }
                        }
                    }
                    superclass = superclass.getSuperclass();
                }
            }
            return createMetricValue(revision, total);
        } catch (NoClassDefFoundError ex) {
            throw new MetricException(ex);
        } catch (Exception ex) {
            throw new MetricException(ex);
        }
    }
}
