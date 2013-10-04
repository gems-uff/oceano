/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics;

import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.extractors.IMetricExtractor;
import java.util.EnumMap;

/**
 *
 * @author DanCastellani
 *
 * Revision by DHeraclio removed Tool interface added extractor mapping for
 * languages added metric attribute
 */
public class DirectMetricManager extends MetricManager {

    private EnumMap<Language, IMetricExtractor> extractors = new EnumMap<Language, IMetricExtractor>(Language.class);

    public DirectMetricManager(Metric metric) {
        super(metric);
    }

    public MetricValue extractMetric(Revision revision) throws MetricException {
        Language language = revision.getProject().getLanguage();

        IMetricExtractor extractor = extractors.get(language);

        if (extractor == null) {
            throw new MetricException("Metric " + getMetric() + " not available for " + language);
        }

        return extractor.extractMetric(revision);
    }

    public MetricValue extractMetric(Revision revision, String path) throws MetricException {
        Language language = revision.getProject().getLanguage();

        IMetricExtractor extractor = extractors.get(language);

        if (extractor == null) {
            throw new MetricException("Metric " + getMetric() + " not available for " + language);
        }

        return extractor.extractMetric(revision, path);
    }

    @Override
    public boolean isLanguageSupported(Language language) {
        return extractors.get(language) != null;
    }
    

    /**
     * Unregister metric extractor with this metric manager
     * @param language
     * @throws MetricException
     */
    public void unregisterExtractor(Language language) throws MetricException {
        IMetricExtractor extractor = extractors.remove(language);
        extractor.setMetricManager(null);
    }

    /**
     * Register metric extractor with this metric manager
     * @param language
     * @param extractor
     * @throws MetricException
     */
    public void registerExtractor(IMetricExtractor extractor) throws MetricException {
        extractor.setMetricManager(this);
        extractors.put(extractor.getLanguage(), extractor);
    }
}
