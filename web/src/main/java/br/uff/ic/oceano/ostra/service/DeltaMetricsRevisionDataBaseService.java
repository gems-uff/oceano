/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.service.RevisionService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ostra.discretizer.DayOfWeekDiscretizer;
import static br.uff.ic.oceano.ostra.discretizer.DiscretizerFactory.getDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.HourOfDayDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NumberOfFilesDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.RoundOfDayDiscretizer;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.Output;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DanCastellani
 */
public class DeltaMetricsRevisionDataBaseService {

    private static final String ATTRIBUTE_INSTANCE_ID = "project-revision";
    private static final String ATTRIBUTE_REVISION_DATE = "rdate";
    private static final String ATTRIBUTE_REVISION_DATE_DAY_OF_WEEK = "rday";
    private static final String ATTRIBUTE_REVISION_DATE_HOUR = "rhour";
    private static final String ATTRIBUTE_REVISION_DATE_ROUND = "rRound";
    private static final String ATTRIBUTE_REVISION_COMMITER = "rcommiter";
    private static final String ATTRIBUTE_REVISION_COMPILES = "rcompile";
    private static final String ATTRIBUTE_NUMBER_OF_CHANGED_FILES = Constantes.PREFIX_ATTRIBUTE_NUMBER + "files";
    private static final String ATTRIBUTE_PREFIX_DELTA_AVG = Constantes.PREFIX_DELTA_METRIC_AVARAGE;
    private static final String ATTRIBUTE_PREFIX_DELTA_SD = Constantes.PREFIX_DELTA_METRIC_STANDARD_DEVIATON;
    //
    private Set<String> attributeNames;
    private Map<Item, Map<Metric, Double>> mapWithLastMetricValueForItem;
    private Map<Metric, Double> mapWithLastValueForEachMetric;
    private Map<Revision, Map<String, String>> instanceAttributes;
    private List<MetricValue> metricValuesToPersist;
    private RevisionService revisionService;
    private MetricValueService metricValueService;
    private DataBaseSnapshot dataBaseSnapshot;
    private boolean calculateStandardDeviation = false;
    private boolean usesOnlyCompilingRevisions = false;
    //
    private Map<String, Discretizer> discretizersMap;
    private boolean useDiscretizers = false;
    private List<Metric> metricsToConsider;

    public DeltaMetricsRevisionDataBaseService() {
        revisionService = ObjectFactory.getObjectWithDataBaseDependencies(RevisionService.class);
        metricValueService = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueService.class);
    }

    public synchronized DataBaseSnapshot buildDeltaMetricsDataBase(List<SoftwareProject> projects, List<Discretizer> discretizers, boolean calculateStandardDeviation, boolean usesOnlyCompilingRevisions, List<Metric> metricsListToConsider, boolean calculateDeltaMetrics) throws ServiceException {
        this.calculateStandardDeviation = calculateStandardDeviation;
        this.usesOnlyCompilingRevisions = usesOnlyCompilingRevisions;
        this.metricsToConsider = metricsListToConsider;

        initialize();
        dataBaseSnapshot = new DataBaseSnapshot();

        for (SoftwareProject project : projects) {
            buildDeltaMetricsDataBase(project.getRevisions(), calculateDeltaMetrics);
        }

        saveMetricValuesForRevisions();

//        System.out.println("--------------------------------------------------");
//        System.out.println("attributeNames = " + attributeNames);
//        System.out.println("--------------------------------------------------");
        dataBaseSnapshot.getAttributes().addAll(attributeNames);

        initializeDiscretizers(discretizers);

        //the instances must be create after all the processing.
        //This is needed 'cause may exist metrics that some instance doesn't have.
        //And we just know all the metrics after all the instances be processed.
        populateDataBaseSnapshotWithInstances();

        return dataBaseSnapshot;
    }

    private void buildDeltaMetricsDataBase(Set<Revision> revisions, boolean calculateDeltaMetrics) throws ServiceException {

        List<Revision> revisionsList = new ArrayList<Revision>(revisions);        
        Collections.sort(revisionsList);
        //>debuging
        //TODO remove after testing
        revisionsList = revisionsList.subList(0, 30);
        //<debuging

        mapWithLastMetricValueForItem = new HashMap<Item, Map<Metric, Double>>();
        mapWithLastValueForEachMetric = new HashMap<Metric, Double>();

        for (Revision revision : revisionsList) {
            //skips revisions that doesnt compile
            boolean cannotCompile = revision.getCannotCompile() == null ? true : revision.getCannotCompile();
            if (usesOnlyCompilingRevisions && cannotCompile) {
                continue;
            }
            //Output.println(">>>>>>>>> Processando revisão: " + revision);

            if (calculateDeltaMetrics) {
                revision = revisionService.getWithVersionedItemsAndItemsAndMetricValues(revision);
            } else {
                revision = revisionService.getWithChangedFiles(revision);
            }

            //instance id
            addAttributeValue(ATTRIBUTE_INSTANCE_ID, revision.getProject().getConfigurationItem().getName() + "-r" + revision.getNumber(), revision);
            
            final String commitDate = DateUtil.format(revision.getCommitDate());
            addAttributeValue(ATTRIBUTE_REVISION_DATE, commitDate, revision);            
            addAttributeValue(ATTRIBUTE_REVISION_DATE_DAY_OF_WEEK, commitDate, revision);
            addAttributeValue(ATTRIBUTE_REVISION_DATE_HOUR, commitDate, revision);
            addAttributeValue(ATTRIBUTE_REVISION_DATE_ROUND, commitDate, revision);
            
            //revision commiter
            addAttributeValue(ATTRIBUTE_REVISION_COMMITER, revision.getCommiter(), revision);
            //does it compiles
            if (!usesOnlyCompilingRevisions) {
                Boolean compiles = !(revision.getCannotCompile() != null && revision.getCannotCompile());
                addAttributeValue(ATTRIBUTE_REVISION_COMPILES, compiles.toString(), revision);
            }
            //number of commited files
            if (revision.getChangedFiles() != null) {
                addAttributeValue(ATTRIBUTE_NUMBER_OF_CHANGED_FILES, revision.getChangedFiles().size() + "", revision);
            } else {
                addAttributeValue(ATTRIBUTE_NUMBER_OF_CHANGED_FILES, Constantes.ATTRIBUTE_NOT_KNOWN_SYMBOL, revision);
            }

            Output.println(">>>>>>>>>>>>>>>>>>>> gerando delta values for r-" + revision.getNumber()+"(" +revision+") #files "+ revision.getChangedFiles().size());
            
            //delta metrics
            if (calculateDeltaMetrics) {
                calculateAndAddDeltaMetricValuesForMetricsOfVersionedItems(revision);
            }
            calculateDeltaForProjectMetrics(revision);
        }
    }

    private Double getLastItemMetricValue(final Item actualItem, Metric metricToCalculateDelta) {
        final Map<Metric, Double> mapWithMetricsAndValuesForActualItem = mapWithLastMetricValueForItem.get(actualItem);
        if (mapWithMetricsAndValuesForActualItem == null) {
            return 0d;
        }
        final Double metricValue = mapWithMetricsAndValuesForActualItem.get(metricToCalculateDelta);
        if (metricValue == null) {
            return 0d;
        }
        return metricValue;
    }

    private void initialize() {
        instanceAttributes = new LinkedHashMap<Revision, Map<String, String>>();
        attributeNames = new LinkedHashSet<String>();
        metricValuesToPersist = new LinkedList<MetricValue>();
    }

    private void addAttributeValue(String attributeName, String attributeValue, Revision revision) {
        attributeNames.add(attributeName);

        if (!instanceAttributes.containsKey(revision)) {
            instanceAttributes.put(revision, new HashMap<String, String>());
        }
        instanceAttributes.get(revision).put(attributeName, attributeValue);

    }

    private void calculateAndAddDeltaMetricValuesForMetricsOfVersionedItems(Revision revision) throws ServiceException {
        //Output.println("\nCalculating delta for revision: " + revision);

        Set<Metric> setOfMetricsToCalculatedTheDelta = new HashSet<Metric>();
        Map<Metric, Set<VersionedItemMetricValue>> mapWithMetricValuesForEachMetricOfThisRevision = new HashMap<Metric, Set<VersionedItemMetricValue>>();

        //get the metric to calculate its delta
        for (VersionedItem versionedItem : revision.getChangedFiles()) {
            for (VersionedItemMetricValue versionedItemMetricValue : versionedItem.getMetricValues()) {
                Metric extractedMetric = versionedItemMetricValue.getMetric();
                if (!this.metricsToConsider.contains(extractedMetric)) {
                    continue;
                }
                setOfMetricsToCalculatedTheDelta.add(extractedMetric);

                if (!mapWithMetricValuesForEachMetricOfThisRevision.containsKey(extractedMetric)) {
                    mapWithMetricValuesForEachMetricOfThisRevision.put(extractedMetric, new HashSet<VersionedItemMetricValue>());
                }
                mapWithMetricValuesForEachMetricOfThisRevision.get(extractedMetric).add(versionedItemMetricValue);
            }
        }

        //calculate and store the delta for each metric
        for (Metric metricToCalculateDelta : setOfMetricsToCalculatedTheDelta) {
            Set<VersionedItemMetricValue> setOfMetricValuesOfTheActualMetric = mapWithMetricValuesForEachMetricOfThisRevision.get(metricToCalculateDelta);
            double deltaValueForActualMetric = 0d;
            System.out.print(".");

            //calculates the sum of deltas for each item
            for (VersionedItemMetricValue versionedItemMetricValue : setOfMetricValuesOfTheActualMetric) {
                Double actualItemMetricValue = versionedItemMetricValue.getDoubleValue();
                final char changeType = versionedItemMetricValue.getVersionedItem().getType();
                final Item actualItem = versionedItemMetricValue.getVersionedItem().getItem();

                final Double lastValueOfTheActualMetricForTheActualItem = getLastItemMetricValue(actualItem, metricToCalculateDelta);
                if (changeType == VersionedItem.TYPE_MODIFIED
                        || changeType == VersionedItem.TYPE_REPLACED
                        || changeType == VersionedItem.TYPE_ADDED
                        || changeType == VersionedItem.TYPE_DELETED) {
                    deltaValueForActualMetric += (actualItemMetricValue - lastValueOfTheActualMetricForTheActualItem);

                } else {
                    throw new ServiceException("Change type " + changeType + " not knwon for versionedItem " + versionedItemMetricValue.getVersionedItem());
                }

                updateLastMetricValueForActualItemAndMetric(actualItem, metricToCalculateDelta, actualItemMetricValue);
            }
            //calculate the average os the delta
            deltaValueForActualMetric /= setOfMetricValuesOfTheActualMetric.size();

            markToSaveDeltaMetricAndAddItsAttribute(revision, metricToCalculateDelta, deltaValueForActualMetric, false);

            if (calculateStandardDeviation) {
                final Double sdValue = getStandardDeviationForMetric(setOfMetricValuesOfTheActualMetric, deltaValueForActualMetric);
                markToSaveDeltaMetricAndAddItsAttribute(revision, metricToCalculateDelta, sdValue, true);
            }
        }

        //>Debugging
//        Output.println("Metrics values of revision");
//        for (Map.Entry<Metric, Set<VersionedItemMetricValue>> entry : mapWithMetricValuesForEachMetricOfThisRevision.entrySet()) {
//            Metric metric = entry.getKey();
//            Output.println("Metric: " + metric.getName());
//
//            Set<VersionedItemMetricValue> set = entry.getValue();
//            for (VersionedItemMetricValue versionedItemMetricValue : set) {
//                Output.println("Vers. item: " + versionedItemMetricValue.getDoubleValue() + "(" + NumberUtil.format(versionedItemMetricValue.getDoubleValue()) + ")");
//            }
//        }
//        Output.println("Delta last value mapping result");
//        for (Map.Entry<Item, Map<Metric, Double>> entry : mapWithLastMetricValueForItem.entrySet()) {
//            Output.print("Item: " + entry.getKey()+ "\t");
//            for (Map.Entry<Metric, Double> entry2 : entry.getValue().entrySet()) {
//                Output.print(entry2.getKey() + ": " +entry2.getValue()+ "\t");
//            }
//        }
//        
//        Output.println("Delta mapping result");
//        for (Map.Entry<Revision, Map<String, String>> entry : instanceAttributes.entrySet()) {
//            Output.print("Revision: " + entry.getKey()+ "\t");
//            final Map<String, String> mapAtt2Valu = entry.getValue();
//            for (Map.Entry<String, String> entry2 : mapAtt2Valu.entrySet()) {
//                Output.print(entry2.getKey() + ": " +entry2.getValue() + "\t");
//            }
//            Output.println("");
//        }
        //<Debugging
    }

    private void updateLastMetricValueForActualItemAndMetric(final Item actualItem, Metric metricToCalculateDelta, Double actualItemMetricValue) {
        if (!mapWithLastMetricValueForItem.containsKey(actualItem)) {
            mapWithLastMetricValueForItem.put(actualItem, new HashMap<Metric, Double>());
        }
        mapWithLastMetricValueForItem.get(actualItem).put(metricToCalculateDelta, actualItemMetricValue);
    }

    private void markToSaveDeltaMetricAndAddItsAttribute(final Revision revision, final Metric metric, final double value, final boolean standartDeviation) {
        if (standartDeviation) {
            addAttributeValue(ATTRIBUTE_PREFIX_DELTA_SD + metric.getName(), NumberUtil.format(value), revision);
        } else {
            final MetricValue deltaMetricValue = new MetricValue();
            deltaMetricValue.setRevision(revision);
            deltaMetricValue.setMetric(metric);
            deltaMetricValue.setDoubleValue(value);
            deltaMetricValue.setDelta(true);
            metricValuesToPersist.add(deltaMetricValue);

            addAttributeValue(ATTRIBUTE_PREFIX_DELTA_AVG + metric.getName(), NumberUtil.format(value), revision);
        }
    }

    private Double getStandardDeviationForMetric(final Collection<VersionedItemMetricValue> versionedItemMetricValues, final double avgValue) {
        double varianceSum = 0d;

//        System.out.println("avgValue = " + avgValue);
//        System.out.println("deltaValues = " + deltaValues);
        for (VersionedItemMetricValue versionedItemMetricValue : versionedItemMetricValues) {
            double delta = versionedItemMetricValue.getDoubleValue();
            varianceSum += Math.pow((delta - avgValue), 2);
//            System.out.println("    varianceSum = " + varianceSum);
        }

        if (versionedItemMetricValues.size() > 0) {
            final double variance = varianceSum / versionedItemMetricValues.size();
//            System.out.println("variance = " + variance);
//            System.out.println("1. sd = " + Math.pow(variance, 2));
            return Math.sqrt(variance);

        } else {
//            System.out.println("2. sd = " + 0);
            return 0D;
        }
    }

    /**
     * This method populates a snapshot of the actual processed database (just
     * the projects indicated) with all its metric values. When a metric value
     * is missing for a given revision it puts an known symbol that indicates
     * the missing value, otherwise put the respective metric value.
     */
    private void populateDataBaseSnapshotWithInstances() {
        Output.println("Populating database snapshot");
        for (Revision revision : instanceAttributes.keySet()) {
            Output.println("Revision: " + revision);
            final StringBuilder sb = new StringBuilder();
            for (Iterator<String> it = attributeNames.iterator(); it.hasNext();) {
                final String attributeName = it.next();
                final String attributeValue = instanceAttributes.get(revision).get(attributeName);

                if (attributeValue == null) {
                    sb.append(Constantes.ATTRIBUTE_NOT_KNOWN_SYMBOL);

                } else {
                    final Discretizer discret = discretizersMap.get(attributeName);
                    if (useDiscretizers && discret != null) {
                        final String result = discret.discretize(attributeValue);
                        sb.append(result);
                        Output.println(attributeName + ": " + attributeValue + "=>" + result);
                    } else {
                        sb.append(attributeValue);
                    }
                }

                if (it.hasNext()) {
                    sb.append(Constantes.ATTRIBUTE_SEPARATOR);
                }
            }

            Output.println(">> Instance: " + sb.toString());
            dataBaseSnapshot.getInstances().add(sb.toString());
        }
    }

    @Transacional
    private void saveMetricValuesForRevisions() {
        for (MetricValue metricValue : metricValuesToPersist) {
            try {
                metricValue.setId(metricValueService.getMetricValueId(metricValue));
            } catch (ServiceException ex) {
                //nice! It's a new metricValue! =D
            }
            Output.println("metricValue = " + metricValue);

            metricValue.setDelta(true);
            metricValueService.save(metricValue);
        }
    }

    /**
     * Initializes the discretizer's list validating it and updating the
     * attribute's names when necessary.
     *
     * @param discretizers
     * @throws ServiceException
     */
    private void initializeDiscretizers(List<Discretizer> discretizers) throws ServiceException {
        useDiscretizers = discretizers != null && !discretizers.isEmpty();
        if (!useDiscretizers) {
            return;
        }
        validateDiscretizers(discretizers);

        this.discretizersMap = new HashMap<String, Discretizer>();
        for (Discretizer discretizer : discretizers) {
            this.discretizersMap.put(discretizer.getAttributeTarget(), discretizer);
        }
    }

    /**
     * Verify if the discretizers list hasn't duplicated discretizers for the
     * same attribute or a discretizer references a target not known.
     *
     * @param discretizers
     * @throws ServiceException
     */
    private void validateDiscretizers(List<Discretizer> discretizers) throws ServiceException {
        for (int i = 0; i < discretizers.size() - 1; i++) {
            final Discretizer discretizer = discretizers.get(i);
            if (!attributeNames.contains(discretizer.getAttributeTarget())) {
                final String msg = "Attribute target not known " + discretizer.getAttributeTarget() + " of " + discretizer.getClass().getCanonicalName();
                Logger.getLogger(DeltaMetricsRevisionDataBaseService.class.getName()).log(Level.WARNING, msg);
            }
            for (int j = i + 1; j < discretizers.size(); j++) {
                final Discretizer anotherDiscretizer = discretizers.get(j);
                if (discretizer.getAttributeTarget().equals(anotherDiscretizer.getAttributeTarget())) {
                    throw new ServiceException("Cant deal with two discretizers for the same attribute: " + discretizer.getAttributeTarget());
                }
            }
        }
    }

    private void calculateDeltaForProjectMetrics(Revision revision) {
        //Output.println("\nCalculating Delta for Project Metrics of revision: "+revision);
        
        final List<MetricValue> metricValuesOfTheCurrentRevision = metricValueService.getByRevision(revision);

        for (MetricValue metricValue : metricValuesOfTheCurrentRevision) {
            final Metric metric = metricValue.getMetric();
            if (!this.metricsToConsider.contains(metric)) {
                continue;
            }///
            Output.print(metric.getName()+":");
            Double deltaValue;
            if (metricValue.isDelta()) {
                //already is a delta value                
                deltaValue = metricValue.getDoubleValue();
                Output.println( "delta" + deltaValue);
            } else {
                //verifica se existe um mv que seja delta para a mesma métrica.
                //caso exista, continue para a proxima metrica.
                boolean hasADeltaMetricValue = false;
                for (final MetricValue mvo : metricValuesOfTheCurrentRevision) {
                    if (mvo.isDelta() && mvo.getMetric().equals(metricValue.getMetric())) {
                        hasADeltaMetricValue = true;
                        break;
                    }
                }
                if (hasADeltaMetricValue) {
                    //Output.println("Metric already has delta value, continuing.");
                    Output.println("delta exists");
                    continue;
                }

                final Double lastValue = mapWithLastValueForEachMetric.get(metricValue.getMetric());
                //Output.println("Metric previous value:"+lastValue);
                Output.print("previous:"+lastValue);
                final Double currentValue = metricValue.getDoubleValue();
                Output.print(" current:"+currentValue);
                if (lastValue == null || NumberUtil.isNAN(lastValue)) {
                    //last value for metric not know, so new value is the delta.
                    deltaValue = currentValue;
                } else if (currentValue == null || NumberUtil.isNAN(currentValue)) {
                    //no new value for metric, so no changes.
                    deltaValue = 0.0;
                } else {
                    //last and current values known.
                    deltaValue = currentValue - lastValue;
                }
                
                mapWithLastValueForEachMetric.put(metricValue.getMetric(), currentValue);
                markToSaveDeltaMetricAndAddItsAttribute(revision, metric, deltaValue, false);
            }
            
            final String strValue = String.valueOf(deltaValue);
            //Output.println("Metric delta value:"+deltaValue + "("+strValue+")");
            Output.println(" delta:"+deltaValue);
            addAttributeValue(ATTRIBUTE_PREFIX_DELTA_AVG + metricValue.getMetric().getName(), strValue, revision);
            
        }
        
    }

    public static List<Discretizer> getDefaultDiscretizers() throws ServiceException {
        List<Discretizer> discretizers = new ArrayList<Discretizer>();

        discretizers.add(getDiscretizer(ATTRIBUTE_NUMBER_OF_CHANGED_FILES, NumberOfFilesDiscretizer.class));
        discretizers.add(getDiscretizer(ATTRIBUTE_REVISION_DATE_DAY_OF_WEEK, DayOfWeekDiscretizer.class));
        discretizers.add(getDiscretizer(ATTRIBUTE_REVISION_DATE_HOUR, HourOfDayDiscretizer.class));
        discretizers.add(getDiscretizer(ATTRIBUTE_REVISION_DATE_ROUND, RoundOfDayDiscretizer.class));

        return discretizers;
    }
}
