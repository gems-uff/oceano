package br.uff.ic.oceano.core.tools.metrics.extractors;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.transiente.Language;

/**
 *
 * @author Daniel
 */
public class ExtractorTestSet {

    private Metric metric;
    private Class metricExtractorClass;
    private Language language;

    public ExtractorTestSet(Metric metric, Class metricExtractorClass, Language language) {
        this.metric = metric;
        this.metricExtractorClass = metricExtractorClass;
        this.language = language;
    }


    public IMetricExtractor getMetricExtractor() {
        try {
            AbstractMetricExtractor metricExtractorCreated = (AbstractMetricExtractor) getMetricExtractorClass().newInstance();
            metricExtractorCreated.setMetric(getMetric());
            metricExtractorCreated.setLanguage(getLanguage());
            return metricExtractorCreated;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Metric getMetric() {
        return this.metric;
    }

    private Class getMetricExtractorClass() {
        return this.metricExtractorClass;
    }

    public Language getLanguage() {
        return this.language;
    }

    /**
     * @param metric the metric to set
     */
    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    /**
     * @param metricExtractorClass the metricExtractorClass to set
     */
    public void setMetricExtractorClass(Class metricExtractorClass) {
        this.metricExtractorClass = metricExtractorClass;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(Language language) {
        this.language = language;
    }
}
