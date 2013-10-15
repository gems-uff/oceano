/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import static br.uff.ic.oceano.core.tools.metrics.expression.QMOOD.*;
import br.uff.ic.oceano.util.NumberUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DanCastellani
 */
public class OstraQualityAtributesWithout_HardCoded_Service extends OstraMetricService {

    private static final boolean STOP_WITH_ERRORS = false;
    List<MetricManager> qualityAttributes;    
    List<MetricManager> qmoodMetrics;    

    public OstraQualityAtributesWithout_HardCoded_Service() {
        super();
        
        qmoodMetrics = MetricManagerFactory.getQmoodMetrics();
        qualityAttributes = MetricManagerFactory.getQmoodQualityAttributes();
    }

    public void calculateQualityAttributes(SoftwareProject project) throws MetricException, ServiceException {
               
        Map<String, Double> firstValues = new HashMap<String, Double>();
        System.out.print("Calculating Quality Attributes for project " + project);

        // I - get all revisions on time order
        ArrayList<Revision> revisions = new ArrayList<Revision>(revisionService.getByProject(project));
        Collections.sort(revisions);

        boolean firstRevision = true;
        for (Revision revision : revisions) {
            System.out.println("\n=== Revision: " + revision);
            
            //I.a ignore revisions that dont compile, because they dont have all the metrics
            //may not be true for non Java projects
//            boolean cannotCompile = revision.getCannotCompile()==null?true:revision.getCannotCompile();
//            if (cannotCompile) {
//                System.out.println("    ===> Cannot compile. Skipping...");
//                continue;
//            }
            
            //II - get and normalize the qmood metric values by the first revisions values
            System.out.println("    Getting QMOOD metrics ");
            final Map<String, Double> mapMetricValues = getQmoodMetricValues(revision);
            if (mapMetricValues.entrySet().size() < QMOOD.QMOOD_METRICS.length) {
                System.out.println("[!!!] Skipping revision: " + revision + ". Not enough metrics");
                continue;
            }
            final Map<String, Double> mapMetricValuesNormalized = new HashMap<String, Double>(mapMetricValues.size());
            //II.a store the first values
            if (firstRevision) {
                for (String qmoodMetric : mapMetricValues.keySet()) {
                    firstValues.put(qmoodMetric, mapMetricValues.get(qmoodMetric));
                    if (mapMetricValues.get(qmoodMetric) != 0D) {
                        mapMetricValuesNormalized.put(qmoodMetric, 1D);
                    } else {
                        mapMetricValuesNormalized.put(qmoodMetric, 0D);
                    }
                }
                firstRevision = false;
            } else {
                //II.b normalize
                System.out.println("    Normalizing values");
                for (String qmoodMetric : mapMetricValues.keySet()) {
                    //update the first value that can be used.
                    if (firstValues.get(qmoodMetric) == 0D) {
                        if (mapMetricValues.get(qmoodMetric) != 0D) {
                            System.out.println("=== Updating first value (was zero): " + qmoodMetric + " = " + mapMetricValues.get(qmoodMetric));
                            firstValues.put(qmoodMetric, mapMetricValues.get(qmoodMetric));
                        }
                    }
                    Double normalizedValue = mapMetricValues.get(qmoodMetric);
                    if (firstValues.get(qmoodMetric) != 0D) {
                        normalizedValue /= firstValues.get(qmoodMetric);
                    }
                    normalizedValue = NumberUtil.roundDecimal(normalizedValue);
                    mapMetricValuesNormalized.put(qmoodMetric, normalizedValue);
                }
            }
            System.out.println("        Original Metric Values  = " + mapMetricValues);
            System.out.println("        Normalized Metric Values = " + mapMetricValuesNormalized);


            //III - calculate and save the quality attributes
            System.out.println("    Calculating Quality Attributes");
            for (MetricManager qualityAttribute : qualityAttributes) {
                // III.a calculate quality atribute value
                Double calculatedQualityAttribute = calculateQualityAttribute(qualityAttribute.getMetric().getName(), mapMetricValuesNormalized);
                // III.b save it
                final MetricValue metricValueQualityAttribute = new MetricValue(revision, qualityAttribute.getMetric(), calculatedQualityAttribute);
                metricValueService.save(metricValueQualityAttribute);
                System.out.println("        QA: " + metricValueQualityAttribute);
            }
        }
    }

    private Double calculateQualityAttribute(String qmoodQualityAttribute, Map<String, Double> mvs) throws MetricException {
        if (qmoodQualityAttribute.equals(QA_EFFECTIVENESS)) {
            return 0.2 * mvs.get(ABSTRACTION) + 0.2 * mvs.get(ENCAPSULATION) + 0.2 * mvs.get(COMPOSITION) + 0.2 * mvs.get(INHERITANCE) + 0.2 * mvs.get(POLYMORPHISM);
        }
        if (qmoodQualityAttribute.equals(QA_EXTENDABILITY)) {
            return 0.5 * mvs.get(ABSTRACTION) - 0.5 * mvs.get(COUPLING) + 0.5 * mvs.get(INHERITANCE) + 0.5 * mvs.get(POLYMORPHISM);
        }
        if (qmoodQualityAttribute.equals(QA_FLEXIBILITY)) {
            return 0.25 * mvs.get(ENCAPSULATION) - 0.25 * mvs.get(COUPLING) + 0.5 * mvs.get(COMPOSITION) + 0.5 * mvs.get(POLYMORPHISM);
        }
        if (qmoodQualityAttribute.equals(QA_FUNCTIONALITY)) {
            return 0.12 * mvs.get(COHESION) + 0.22 * mvs.get(POLYMORPHISM) + 0.22 * mvs.get(MESSAGING) + 0.22 * mvs.get(DESIGN_SIZE) + 0.22 * mvs.get(HIERARCHIES);
        }
        if (qmoodQualityAttribute.equals(QA_REUSABILITY)) {
            return -0.25 * mvs.get(COUPLING) + 0.25 * mvs.get(COHESION) + 0.5 * mvs.get(MESSAGING) + 0.5 * mvs.get(DESIGN_SIZE);
        }
        if (qmoodQualityAttribute.equals(QA_UNDERSTANDABILITY)) {
            return -0.33 * mvs.get(ABSTRACTION) + 0.33 * mvs.get(ENCAPSULATION) - 0.33 * mvs.get(COUPLING) + 0.33 * mvs.get(COHESION) - 0.33 * mvs.get(POLYMORPHISM) - 0.33 * mvs.get(COMPLEXITY) - 0.33 * mvs.get(DESIGN_SIZE);
        }
        throw new MetricException("This is not a QMOOD Quality Attribute");
    }

    private Map<String, Double> getQmoodMetricValues(Revision revision) throws ServiceException {
        Map<String, Double> mapQmoodMetricValues = new HashMap<String, Double>(QMOOD_METRICS.length);

        for (MetricManager qmoodMetric : qmoodMetrics) {
            try {
                MetricValue metricValue = metricValueService.getByRevisionMetricAndDelta(revision, qmoodMetric.getMetric(), false);
                mapQmoodMetricValues.put(qmoodMetric.getMetric().getAcronym(), metricValue.getDoubleValue());

            } catch (ObjetoNaoEncontradoException ex) {
                if (STOP_WITH_ERRORS) {
                    throw new ServiceException("Revision " + revision + " dont have the metric: " + qmoodMetric);
                } else {
                    System.out.println("    >>>>>>>>>>>> Revision " + revision + " dont have the metric: " + qmoodMetric);
                }
            }
        }
        return mapQmoodMetricValues;
    }
}
