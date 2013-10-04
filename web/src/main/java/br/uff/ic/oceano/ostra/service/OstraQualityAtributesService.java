/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.tools.metrics.MetricException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import java.util.*;

/**
 * Refactored by dheraclio
 * @author DanCastellani
 */
public class OstraQualityAtributesService extends OstraMetricService {

    private static final boolean CALCULATE_QA_VALUES_AGAIN = true;
    List<MetricManager> qualityAttribytes;
    List<MetricManager> qmoodMetrics;
    Map<MetricManager, Double> firstValues;
    Map<MetricManager, Double> lastValues;

    public OstraQualityAtributesService() {
        super();
    }

    public void calculateQualityAttributes(SoftwareProject project) throws MetricException, ServiceException, VCSException {
        initializeQualityAttributes();
        initializeQmoodMetrics();
        firstValues = new HashMap<MetricManager, Double>();
        lastValues = new HashMap<MetricManager, Double>();

        ArrayList<Revision> revisions = new ArrayList<Revision>(revisionService.getByProject(project));
        Collections.sort(revisions);
        boolean firstRevision = true;
        for (Revision revision : revisions) {
            if (revision.getCannotCompile()) {
                System.out.println("revision " + revision + " cannot compile. Skipping...");
                continue;
            }
            boolean hasAllQMoodMetrics = checkIfRevisionHasAllAMoodMetrics(revision);

            if (!hasAllQMoodMetrics) {
                continue;
            }
            calculateQualityAtributeValues(revision, firstRevision);

            for (MetricManager qualittAttribute : qualityAttribytes) {
                MetricValue deltaQa;
                try {
                    MetricValue qaValue = metricValueService.getByRevisionMetricAndDelta(revision, qualittAttribute.getMetric(), false);

                    if (firstRevision) {
                        deltaQa = new MetricValue(revision, qualittAttribute.getMetric(), qaValue.getDoubleValue());

                    } else {
//                        final boolean actualValueIsInvalid = qaValue.getDoubleValue() == Double.NaN;
//                        if (actualValueIsInvalid) {
//                            continue;
//                        }

                        deltaQa = new MetricValue(revision, qualittAttribute.getMetric(), qaValue.getDoubleValue() - lastValues.get(qualittAttribute));
                    }
                    lastValues.put(qualittAttribute, qaValue.getDoubleValue());
                    System.out.println("delta " + deltaQa);

                } catch (ObjetoNaoEncontradoException ex) {
                    //can't happen
                }
            }

            if (firstRevision) {
                firstRevision = false;
            }
        }
    }

    private void calculateQualityAtributeValues(Revision revision, boolean firstRevision) throws MetricException {
        for (MetricManager qualityAttribute : qualityAttribytes) {
            MetricValue qaValue = null;
            Long idQaValue = null;
            try {
//                System.out.println("revision.getId() = " + revision.getId());
//                System.out.println("qualityAttribute.getMetric().getId() = " + qualityAttribute.getMetric().getId());
                qaValue = metricValueService.getByRevisionMetricAndDelta(revision, qualityAttribute.getMetric(), false);
                idQaValue = qaValue.getId();
                //
            } catch (ObjetoNaoEncontradoException ex) {
            }

            if (CALCULATE_QA_VALUES_AGAIN || idQaValue == null) {
                qaValue = qualityAttribute.extractMetric(revision);
                qaValue.setDelta(false);
                System.out.print(qaValue.getMetric() + "    REAL = " + qaValue);
                if (firstRevision) {
                    //as we must normalize the values, first/first = 1
                    qaValue.setDoubleValue(1d);

                } else {
                    //as we must normalize the values, actual/first
                    final double actualValue = qaValue.getDoubleValue();
                    final double baseValue = firstValues.get(qualityAttribute);
                    qaValue.setDoubleValue(actualValue / baseValue);
                }
                //must save if it was extracted now
                qaValue.setId(idQaValue);
//                metricValueService.save(qaValue);
            }

            if (firstRevision) {
                //we must store the first value, even if we must not calculate again the values.
                firstValues.put(qualityAttribute, qaValue.getDoubleValue());
            }
            System.out.println("    NORMALIZADO: " + qaValue);
        }
    }

    private void initializeQualityAttributes() {
        qualityAttribytes = new ArrayList();
        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName("Method Cyclomatic Complexity Density"));
//        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(QMOOD.QA_EFFECTIVENESS));
//        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(QMOOD.QA_EXTENDABILITY));
//        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(QMOOD.QA_FLEXIBILITY));
//        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(QMOOD.QA_FUNCTIONALITY));
//        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(QMOOD.QA_REUSABILITY));
//        qualityAttribytes.add((MetricManager) MetricManagerFactory.getInstance().getMetricByName(QMOOD.QA_UNDERSTANDABILITY));
    }

    private void initializeQmoodMetrics() {
        qmoodMetrics = MetricManagerFactory.getQmoodMetrics();
        qmoodMetrics.add(MetricManagerFactory.getInstance().getMetricByName(MetricEnumeration.TCC.getName()));
    }

    private boolean checkIfRevisionHasAllAMoodMetrics(Revision revision) throws ServiceException, VCSException {
        System.out.print("\n=== revision = " + revision);
        revision.setLocalPath(null);
        boolean hasAllQmoodMetrics = true;

        for (MetricManager qmoodMetric : qmoodMetrics) {

            if (qmoodMetric.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
                try {
                    metricValueService.getByRevisionMetricAndDelta(revision, qmoodMetric.getMetric(), false);
                    System.out.print(".");
                } catch (ObjetoNaoEncontradoException ex) {
                    hasAllQmoodMetrics = false;
                    //The metric should be extracted before the quality attribute calculation
                }

            } else {
                List<VersionedItemMetricValue> vimvs = versionedItemMetricValueService.getByRevisionAndMetric(revision, qmoodMetric.getMetric());
                if (vimvs == null || vimvs.isEmpty()) {
                    hasAllQmoodMetrics = false;
                    //The metric should be extracted before the quality attribute calculation
                } else {
                    System.out.print("!");
                }
            }
        }
        System.out.println("    hasAllQmoodMetrics = " + hasAllQmoodMetrics + " >>     compila:" + !revision.getCannotCompile());
        return hasAllQmoodMetrics;
    }
}
