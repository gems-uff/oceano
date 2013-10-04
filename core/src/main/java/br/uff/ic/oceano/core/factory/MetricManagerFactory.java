/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.factory;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.Tool;
import br.uff.ic.oceano.core.tools.metrics.DerivedMetricManager;
import br.uff.ic.oceano.core.tools.metrics.DirectMetricManager;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import br.uff.ic.oceano.core.tools.metrics.extractors.AbstractMetricExtractor;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.util.Output;
import java.util.*;

public class MetricManagerFactory implements ToolFactory {

    private class SavedOrderComparator implements Comparator<Metric> {

        public int compare(Metric o1, Metric o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
    private static final String name = "Metrics Factory";
    private static final String rationale = "A Factory that instantiates some Metrics to characterizes the system";
    private static MetricManagerFactory metricsFactory;
    private List<Metric> derivedMetrics;
    private Map<String, MetricManager> metricManagersByMetricName;

    /**
     * Here comes the Tools that this factory provides
     */
    private MetricManagerFactory() {
    }

    public static MetricManagerFactory getInstance() {
        MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
        return getInstance(metricService.getAll());
    }

    public static MetricManagerFactory getInstance(List<Metric> metrics) {
        if (metricsFactory == null) {
            metricsFactory = new MetricManagerFactory();
            //loading metrics outside constructor void stack over flow during derived metrics loading
            metricsFactory.load(metrics);
        }
        return metricsFactory;
    }

    private void load(List<Metric> metrics) {
        Output.println("-------------------------- Loading Metrics ----------------------");

        derivedMetrics = new LinkedList<Metric>();
        metricManagersByMetricName = new LinkedHashMap<String, MetricManager>();

        for (Metric metric : metrics) {
            if (metric.isDerived()) {
                //for loading latter
                derivedMetrics.add(metric);
            } else {
                //base Metrics must be loaded first
                loadMetric(metric);
            }
        }

        //derived metric must be loaded in the saved order
        //This is needed because one derived metric may depend on another derived metric.
        //So, we need to load them in the saved order to guarantee that the previous metrics are already loaded and dont run into a metric not found error.
        // Coloquei esse IF porque um atributo de qualidade jah tem todas metrica necessarias para seu calculo, e dava NULLPOINTER aqui para o PE
        if (derivedMetrics != null) {
            Collections.sort(derivedMetrics, new SavedOrderComparator());
            for (Metric metric : derivedMetrics) {
                loadMetric(metric);
            }
        }

        Output.println("------------------------- DONE ---------------------------------");
    }

    public String getName() {
        return name;
    }

    public String getRationale() {
        return rationale;
    }

    public Collection<MetricManager> getMetricManagers() {
        return metricManagersByMetricName.values();
    }

    public MetricManager getMetricManager(Metric metric) {
        return getMetricByName(metric.getName());
    }

    public MetricManager getMetricByName(String metricName) {
        return metricManagersByMetricName.get(metricName);
    }

    public Tool getTool(Class classe) {
        for (MetricManager mm : metricManagersByMetricName.values()) {
            if (mm.getClass().equals(classe)) {
                return mm;
            }
        }
        return null;
    }

    public Collection<Tool> getTools() {
        return new ArrayList<Tool>(getMetricManagers());
    }

    public Set<MetricManager> getMetricManagersThatUseFontFile() {
        Set<MetricManager> returnSet = new HashSet<MetricManager>();

        for (MetricManager metricManager : getMetricManagers()) {
            //we dont want compiled metrics
            if (!metricManager.getMetric().isExtractsFromFont()) {
                continue;
            }
            returnSet.add(metricManager);
        }

        return returnSet;
    }

    public Set<MetricManager> getMetricManagersThatUseCompiledFile() {
        Set<MetricManager> returnSet = new HashSet<MetricManager>();

        for (MetricManager metricManager : getMetricManagers()) {
            //we dont want font metrics
            if (metricManager.getMetric().isExtractsFromFont()) {
                continue;
            }
            returnSet.add(metricManager);
        }

        return returnSet;
    }

    private void loadMetric(Metric metric) {
        try {
            Output.print(" > Loading: " + metric.getName());

            MetricManager metricManager;
            if (metric.isDerived()) {
                metricManager = new DerivedMetricManager(metric);
            } else {
                DirectMetricManager dmm = new DirectMetricManager(metric);

                //registering extractors avalaible
                for (MetricExtractor metricExt : metric.getMetricExtractors()) {
                    AbstractMetricExtractor amm = createExtractor(metric, metricExt);
                    dmm.registerExtractor(amm);
                }
                metricManager = dmm;
            }
            Output.print("      loaded");

            //Register metric manager
            metricManagersByMetricName.put(metric.getName(), metricManager);
            Output.println("  done!");

        } catch (MetricException ex) {
            Output.println("------------------------------> Metric Manager exception: " + metric);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Create AbstractMetricExtractor instance from MetricExtractor data.
     *
     * @param metric
     * @param metricExt
     * @return
     */
    private AbstractMetricExtractor createExtractor(Metric metric, MetricExtractor metricExt) {
        try {
            Class extractorClass = getClass().getClassLoader().loadClass(metricExt.getMetricExtractorClass());
            Language language = Language.valueOf(metricExt.getLanguage());

            AbstractMetricExtractor metricExtractorCreated = (AbstractMetricExtractor) extractorClass.newInstance();
            metricExtractorCreated.setMetric(metric);
            metricExtractorCreated.setLanguage(language);

            return metricExtractorCreated;

        } catch (Exception ex) {
            Output.println("------------------------------> Metric Manager exception: " + metric + " : " + ex.getMessage());
            throw new RuntimeException(ex);
        }

    }

    public static List<MetricManager> getQmoodMetrics() {
        List<MetricManager> qmoodMetrics = new ArrayList<MetricManager>();
        for (String metricName : Arrays.asList(QMOOD.QMOOD_METRICS)) {
            qmoodMetrics.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(metricName));
        }
        return qmoodMetrics;
    }

    public static List<MetricManager> getQmoodQualityAttributes() {
        List<MetricManager> qmoodQualityAttributes = new ArrayList<MetricManager>();
        for (String metricName : Arrays.asList(QMOOD.QMOOD_QUALITY_ATTRIBUTES)) {
            MetricManager mm = (MetricManager) MetricManagerFactory.getInstance().getMetricByName(metricName);
            if (mm != null) {
                qmoodQualityAttributes.add(mm);
            }
        }
        return qmoodQualityAttributes;
    }
}
