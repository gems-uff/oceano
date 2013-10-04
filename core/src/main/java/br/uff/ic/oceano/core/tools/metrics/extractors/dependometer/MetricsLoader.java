package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.MetricHelper;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.tools.metrics.service.MetricExtractorService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.util.Output;
import com.valtech.source.dependometer.app.core.common.MetricEnum;
import java.util.List;

/**
 * //TODO fix metrics classification. Dependometer allows some metrics for
 * project and package, maybe file too. Replicate for each support Demands
 * remodel of Metric class
 *
 * @author Daniel
 */
public class MetricsLoader {

    private static MetricsLoader self;
    private MetricService metricService;
    private MetricExtractorService metricExtractorService;

    private MetricsLoader() {
        metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
        metricExtractorService = ObjectFactory.getObjectWithDataBaseDependencies(MetricExtractorService.class);
    }

    public static MetricsLoader getSelf() {
        if (self == null) {
            self = new MetricsLoader();
        }
        return self;
    }

    public static void load() throws DependometerException {
        Output.println("Inserting dependometer metrics");

        try {

            getSelf().addProjectMetrics();
            getSelf().addPackageMetrics();
            getSelf().addFileMetrics();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addProjectMetrics() throws DependometerException {
        Output.println("Inserting project metrics");

        List<MetricEnum> metrics = MetricHelper.getValidProjectMetrics();
        for (MetricEnum metricEnum : metrics) {
            addProjectMetric(metricEnum);
        }
    }

    private void addPackageMetrics() throws DependometerException {
        Output.println("Inserting package metrics");

        List<MetricEnum> metrics = MetricHelper.getValidPackageMetrics();
        for (MetricEnum metricEnum : metrics) {
            addPackageMetric(metricEnum);
        }
    }

    private void addFileMetrics() throws DependometerException {
        Output.println("Inserting file metrics");

        List<MetricEnum> metrics = MetricHelper.getValidCompilationUnitMetrics();
        for (MetricEnum metricEnum : metrics) {
            addCompilationUnitMetric(metricEnum);
        }

        metrics = MetricHelper.getValidTypeMetrics();
        for (MetricEnum metricEnum : metrics) {
            addTypeMetric(metricEnum);
        }
    }

    /**
     *
     * @param metricEnum
     * @throws DependometerException
     */
    private void addProjectMetric(MetricEnum metricEnum) throws DependometerException {

        Output.println("Addition metric " + metricEnum);

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            Output.println("Ignoring addition of non numeric metric " + metricEnum);
            return;
        }

        Metric metric = createMetric(metricEnum, Metric.EXTRACTS_FROM_PROJECT);
        saveMetric(metric);
    }

    private void addCompilationUnitMetric(MetricEnum metricEnum) throws DependometerException {

        Output.println("Addition metric " + metricEnum);

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            Output.println("Ignoring addition of non numeric metric " + metricEnum);
            return;
        }

        Metric metric = createMetric(metricEnum, Metric.EXTRACTS_FROM_FILE);
        saveMetric(metric);
    }

    private void addPackageMetric(MetricEnum metricEnum) throws DependometerException {
        Output.println("Addition metric " + metricEnum);

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            Output.println("Ignoring addition of non numeric metric " + metricEnum);
            return;
        }

        Metric metric = createMetric(metricEnum, Metric.EXTRACTS_FROM_PACKAGE);
        saveMetric(metric);
    }

    private void addTypeMetric(MetricEnum metricEnum) throws DependometerException {

        Output.println("Addition metric " + metricEnum);

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            Output.println("Ignoring addition of non numeric metric " + metricEnum);
            return;
        }
    }

    private void addSubsystemMetric(MetricEnum metricEnum) throws DependometerException {

        Output.println("Addition metric " + metricEnum);

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            Output.println("Ignoring addition of non numeric metric " + metricEnum);
            return;
        }
    }

    private void addLayerMetric(MetricEnum metricEnum) throws DependometerException {

        Output.println("Addition metric " + metricEnum);

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            Output.println("Ignoring addition of non numeric metric " + metricEnum);
            return;
        }
    }

    private Metric createMetric(MetricEnum metricEnum, int target) throws DependometerException {

        if (!MetricHelper.isNumberMetric(metricEnum)) {
            throw new DependometerException("Not a number metric: " + metricEnum);
        }

        String name;
        String acronym;
        String description;

        //Translate metrics names
        MetricEnumeration oceanoMetric = MetricHelper.getOceanoMetric(metricEnum);
        if (oceanoMetric != null) {
            name = oceanoMetric.getName();
            acronym = oceanoMetric.getAcronym();
            description = oceanoMetric.getDescription();
        } else {
            name = metricEnum.name();
            acronym = metricEnum.getDisplayName();
            description = MetricHelper.getDescription(metricEnum);
        }

        Metric metric = new Metric();
        metric.setName(name);
        metric.setAcronym(acronym);
        metric.setDescription(description);
        metric.setType(Metric.TYPE_FLOAT);
        metric.setExtratcsFrom(target);
        metric.setExtractsFromFont(true); //dependometer does it own compilation if necessary  

        return metric;
    }

    private void saveMetric(final Metric metric) throws DependometerException {
        
        //may be a overloaded
        if (!existsMetric(metric)) {
            metricService.save(metric);
        }
        //may be a extractor for different language
        insertMetricExtractor(metric.getAcronym());
    }

    private boolean existsMetric(final Metric metric) throws DependometerException {
        try {
            return metricService.getByAcronym(metric.getAcronym()) != null;
        } catch (ObjetoNaoEncontradoException ex) {
            return false;
        }
    }

    private void insertMetricExtractor(String metricAcronym) throws DependometerException {
        try {
            Metric metric = metricService.getByAcronym(metricAcronym);
            if (metric == null) {
                throw new DependometerException("Metric " + metricAcronym + " not found");
            }

            //CPP
            MetricExtractor metricExtractor = new MetricExtractor();
            metricExtractor.setMetric(metric);
            metricExtractor.setLanguage(Language.CPP.name());
            metricExtractor.setMetricExtractorClass(DependomenterExtractor.class.getCanonicalName());
            if (!existsMetricExtractor(metricExtractor)) {
                metricExtractorService.save(metricExtractor);
            }

            //JAVA
            metricExtractor = new MetricExtractor();
            metricExtractor.setMetric(metric);
            metricExtractor.setLanguage(Language.JAVA.name());
            metricExtractor.setMetricExtractorClass(DependomenterExtractor.class.getCanonicalName());
            if (!existsMetricExtractor(metricExtractor)) {
                metricExtractorService.save(metricExtractor);
            }
        } catch (ObjetoNaoEncontradoException ex) {
            throw new DependometerException("Fail to save metric extractor for " + metricAcronym, ex);
        }
    }

    private boolean existsMetricExtractor(MetricExtractor metricExtractor) throws DependometerException {
        try {
            List<MetricExtractor> metricExtractors = metricExtractorService.getMetricExtractorsByMetric(metricExtractor.getMetric());
            for (MetricExtractor mext : metricExtractors) {
                if (!mext.getLanguage().equals(metricExtractor.getLanguage())) {
                    continue;
                }
                //exist a extractor for the language
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new DependometerException("Fail to verify existency of metric extractor", ex);
        }
    }
}
