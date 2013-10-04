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
 * refactor by dheraclio
 *
 * @author wallace
 */
public class MeasureOfFunctionalAbstractionExtractorJava extends AbstractMetricExtractor {

    public MeasureOfFunctionalAbstractionExtractorJava() {
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        return this.extractMetric(revision, revision.getLocalPath());
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        try {
            float mfa = 0;
            List<Class> classes = ClassLoaderUtil.loadClasses(revision);
            if (classes == null || classes.isEmpty()){
                return createMetricValue(revision, mfa);
            }

            for (Class clazz : classes) {
                float methodsnum = numberofmethods(clazz);

                java.lang.reflect.Method methods[] = clazz.getDeclaredMethods();
                float inherited = methodsnum - (methods.length);

                if (methodsnum > 0) {
                    mfa += inherited / methodsnum;
                }
            }
            if (!classes.isEmpty()) {
                mfa = mfa / classes.size();
            }

            return createMetricValue(revision, mfa);
        } catch (NoClassDefFoundError ex) {
            throw new MetricException(ex);
        } catch (Exception ex) {
            throw new MetricException(ex);
        }

    }

    private int numberofmethods(Class clazz) {
        java.lang.reflect.Method methods[] = clazz.getDeclaredMethods();
        Class superclass =  clazz.getSuperclass();

        if (superclass != null) {
            return (methods.length - (numberofoverridden(superclass, methods))) + numberofmethods(superclass);
        } else {
            return methods.length;
        }
    }

    private int numberofoverridden(Class superclass, java.lang.reflect.Method methods[]) {
        int total = 0;
        int i, j, l;
        java.lang.reflect.Method supermethods[];
        Class[] parameter1, parameter2;
        Class returntype1, returntype2;
        HashSet map = new HashSet();
        boolean parameter;
        String MethodString, superMethodString;
        while (superclass != null) {
            i = 0;
            supermethods = superclass.getDeclaredMethods();
            while (i < methods.length) {
                MethodString = methods[i].getName();
                if (!map.contains(i)) {
                    j = 0;
                    while (j < supermethods.length) {
                        superMethodString = supermethods[j].getName();
                        if (superMethodString.equals(MethodString)) {
                            returntype1 = methods[i].getReturnType();
                            returntype2 = supermethods[j].getReturnType();
                            if (returntype1.equals(returntype2)) {
                                parameter1 = methods[i].getParameterTypes();
                                parameter2 = supermethods[j].getParameterTypes();
                                if (parameter1.length == parameter2.length) {
                                    l = 0;
                                    parameter = true;
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
                        }
                        j++;
                    }
                }
                i++;
            }

            superclass = superclass.getSuperclass();
        }
        return total;
    }
}
