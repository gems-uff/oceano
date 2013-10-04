/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.service;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import br.uff.ic.oceano.core.tools.maven.MavenUtil;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.exception.CompilerException;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import br.uff.ic.oceano.ostra.service.VersionedItemMetricValueService;
import br.uff.ic.oceano.util.Output;
import java.io.File;
import java.util.*;

/**
 *
 * @author DanCastellani
 */
public class MeasurementService {

    private VersionedItemMetricValueService versionedItemMetricValueService;
    private MetricValueService metricValueService;

    public MeasurementService() {
        versionedItemMetricValueService = ObjectFactory.getObjectWithDataBaseDependencies(VersionedItemMetricValueService.class);
        metricValueService = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueService.class);
    }

    /**
     * This method extract all metrics from the parameter revision even if the
     * metric's target isn't a project. If the metric's target is a package or
     * file, the resultant metric is an average value for all the changedFiles.
     *
     * @param revision
     * @param metricManagers
     * @return
     * @throws ServiceException
     */
    public List<MetricValue> extractMetricsFromRevision(Revision revision, Collection<MetricManager> metricManagers) throws ServiceException {
        return extractProjectMetrics(revision, metricManagers, false);
    }

    /**
     * This method extracts the parameter metrics from the parameter revision,
     * but only the project metrics.
     *
     * @param revision
     * @param metricManagers
     * @return
     * @throws ServiceException
     */
    public List<MetricValue> extractProjectMetricsOnly(Revision revision, Collection<MetricManager> metricManagers) throws ServiceException {
        return extractProjectMetrics(revision, metricManagers, true);
    }

    private List<MetricValue> extractProjectMetrics(Revision revision, Collection<MetricManager> metricManagers, boolean dontExtractIfTargetIsNotProject) throws ServiceException {
        List<MetricValue> metricValues = new ArrayList<MetricValue>(metricManagers.size());

        for (MetricManager metricManager : metricManagers) {
            if (metricManager.getMetric().getExtratcsFrom() != Metric.EXTRACTS_FROM_PROJECT) {
                if (dontExtractIfTargetIsNotProject) {
                    continue;
                }
                Output.println("WARN[" + this.getClass().getSimpleName() + "] Extracting metric <" + metricManager.getMetric().getName() + "> from Project. But it's not from project by default.");
            }

            MetricValue value = extractMetric(metricManager, revision);
            if (value != null) {
                metricValues.add(value);
            }
        }

        return metricValues;
    }

    /**
     *
     * @param revision
     * @param metricManagers
     * @param stopWithExceptions True if want to stop with a single exception.
     * False if want to ignore if one metric throws exception and continue with
     * the other.
     *
     * @return
     * @throws ServiceException
     */
    public List<VersionedItemMetricValue> extractMetricsFromVersionedItems(Revision revision, Collection<MetricManager> metricManagers, boolean stopWithExceptions) throws ServiceException {
        Set<VersionedItem> versionedFiles;
        Set<VersionedItem> versionedPackages;
        try {
            versionedFiles = RevisionUtil.get().getSourceFilesFromChangedFiles(revision);
            versionedPackages = RevisionUtil.get().getPackagesFromChangedFiles(revision);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

        List<VersionedItemMetricValue> viMetricValues = new ArrayList<VersionedItemMetricValue>(metricManagers.size() * versionedFiles.size());

        //split metricManagers in files or packages
        Collection<MetricManager> fileMetricManagers = new HashSet<MetricManager>();
        Collection<MetricManager> packageMetricManagers = new HashSet<MetricManager>();
        for (MetricManager metricManager : metricManagers) {
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_FILE) {
                fileMetricManagers.add(metricManager);
            }

            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_PACKAGE) {
                packageMetricManagers.add(metricManager);
            }

        }

        //extract metrics from files
        Output.println("        Arquivos a serem analisados: " + versionedFiles);
        viMetricValues.addAll(extractVersionedItemMetricValues(versionedFiles, fileMetricManagers, stopWithExceptions));

        //extract metrics from packages
        Output.println("        Pacotes a serem analisados: " + versionedPackages.size());
        viMetricValues.addAll(extractVersionedItemMetricValues(versionedPackages, packageMetricManagers, stopWithExceptions));

        return viMetricValues;
    }

    /**
     *
     * @param versionedItems
     * @param metricManagers
     * @param stopWithExceptions True if want to stop with a single exception.
     * False if want to ignore if one metric throws exception and continue with
     * the other.
     * @return
     * @throws ServiceException
     */
    private List<VersionedItemMetricValue> extractVersionedItemMetricValues(final Set<VersionedItem> versionedItems, final Collection<MetricManager> metricManagers, boolean stopWithExceptions) throws ServiceException {
        List<VersionedItemMetricValue> viMetricValues = new LinkedList<VersionedItemMetricValue>();

        for (MetricManager metricManager : metricManagers) {
            try {
                Output.println("    Extracting " + metricManager.getName());
                viMetricValues.addAll(extractMetricValuesForVersionedItems(versionedItems, metricManager));

            } catch (ServiceException ex) {
                if (stopWithExceptions) {
                    throw ex;
                } else {
                    Output.println("[ERROR] Metric " + metricManager.getName() + " threw exception: " + ex.getClass().getName());
                    Output.append("Exception: \n" + ex.getMessage() + "\n ------------ end of exception");
                }
            }
        }
        return viMetricValues;
    }

    private List<VersionedItemMetricValue> extractMetricValuesForVersionedItems(final Set<VersionedItem> versionedItems, final MetricManager metricManager) throws ServiceException {
        List<VersionedItemMetricValue> metricValuesFromThisMetricManager = new ArrayList<VersionedItemMetricValue>(versionedItems.size());

        for (VersionedItem versionedItem : versionedItems) {
            VersionedItemMetricValue metricValue;
            if (versionedItem.getType() == VersionedItem.TYPE_ADDED || versionedItem.getType() == VersionedItem.TYPE_MODIFIED) {
                metricValue = this.extracMetricValueForVersionedItem(metricManager, versionedItem);
            } else {
                metricValue = versionedItemMetricValueService.createVersionedMetricWithZeroValue(metricManager, versionedItem);
            }
            if (metricValue != null) {
                metricValuesFromThisMetricManager.add(metricValue);
            }
        }

        return metricValuesFromThisMetricManager;
    }

    public VersionedItemMetricValue extracMetricValueForVersionedItem(MetricManager metricManager, VersionedItem versionedItem) throws ServiceException {
        final Revision revision = versionedItem.getRevision();

        String path = PathUtil.getWellFormedPath(revision.getLocalPath() + versionedItem.getItem().getPath());
        MetricValue metricValueExtracted = extractMetric(metricManager, revision, path);

        if (metricValueExtracted != null) {
            final VersionedItemMetricValue returningMetricValueForVersionedItem = new VersionedItemMetricValue();

            returningMetricValueForVersionedItem.setDoubleValue(metricValueExtracted.getDoubleValue());
            returningMetricValueForVersionedItem.setMetric(metricValueExtracted.getMetric());

            returningMetricValueForVersionedItem.setVersionedItem(versionedItem);

            return returningMetricValueForVersionedItem;

        } else {
            return null;
        }
    }

    /**
     * This method extract a metric for one revision. If the metric target is a
     * File or a Package it return the average value as the metricValue
     *
     * @param metricManager
     * @param revision
     * @return
     * @throws CompilerException
     * @throws ServiceException
     */
    public static MetricValue extractMetric(MetricManager metricManager, Revision revision) throws ServiceException {
        if (metricManager == null) {
            throw new ServiceException("Null parameter: metricManager");
        }
        if (metricManager.getMetric() == null) {
            throw new ServiceException("Metric not set");
        }

        int metricTarget = metricManager.getMetric().getExtratcsFrom();
        if (metricTarget == Metric.EXTRACTS_FROM_PROJECT) {
            return extractMetric(metricManager, revision, null);
        }

        MetricValue result = new MetricValue();
        result.setMetric(metricManager.getMetric());
        result.setDelta(false);
        result.setRevision(revision);
        result.setDoubleValue(0d);
        Double acumulatedValue = 0d;

        if (metricTarget == Metric.EXTRACTS_FROM_PACKAGE) {
            try {
                Collection<String> packages = RevisionUtil.get().getSourceClassPaths(revision);
                for (String packagePath : packages) {
                    MetricValue value = extractMetric(metricManager, revision, packagePath);
                    if (value != null) {
                        acumulatedValue += value.getDoubleValue();
                    }
                }
                result.setDoubleValue(NumberUtil.roundDecimal(acumulatedValue));
            } catch (Exception ex) {
                throw new ServiceException("Fail to extract metric for revision: " + revision, ex);
            }
        } else if (metricTarget == Metric.EXTRACTS_FROM_FILE) {
            try {
                Collection<String> files = RevisionUtil.get().getSourceFiles(revision);
                for (String filePath : files) {
                    MetricValue value = extractMetric(metricManager, revision, filePath);
                    if (value != null) {
                        acumulatedValue += value.getDoubleValue();
                    }
                }
                result.setDoubleValue(NumberUtil.roundDecimal(acumulatedValue));
            } catch (Exception ex) {
                throw new ServiceException("Fail to extract metric for revision: " + revision, ex);
            }
        } else {
            throw new ServiceException("Unknown metric target:" + metricTarget);
        }

        return result;
    }

    /**
     * This method extracts a metricValue for an especific Item, identified by
     * the filePath.
     *
     * @param metricManager
     * @param revision
     * @param path
     * @return
     * @throws CompilerException
     * @throws ServiceException
     */
    public static MetricValue extractMetric(MetricManager metricManager, Revision revision, String path) throws CompilerException, ServiceException {

        //Fix path
        if (path != null && path.isEmpty()) {
            throw new ServiceException("Variable path is empty. Set path to null or valid path");
        }

        //Fixed file separators
        path = path != null ? PathUtil.getWellFormedPath(path) : null;
        path = PathUtil.getAbsolutePathFromRelativetoCurrentPath(path);

        String revisionPath = revision.getLocalPath();
        revisionPath = revisionPath != null ? PathUtil.getWellFormedPath(revisionPath) : null;
        revisionPath = PathUtil.getAbsolutePathFromRelativetoCurrentPath(revisionPath);

        //Add revisionPath when necessary
        String fixedPath;
        if (path != null && !path.contains(revisionPath)) {
            fixedPath = revisionPath + path;
        } else if (path != null) {
            fixedPath = path;
        } else {
            //Use revision path when no path supplied
            fixedPath = revisionPath;
        }

        final Metric metricToExtract = metricManager.getMetric();
        if (!metricToExtract.isExtractsFromFont()) {
            if (revision.getProject().isMavenProject()) {
                fixedPath = fixedPath.replace(MavenUtil.MAVEN2_BASE_MAIN_SOURCE_FILES, MavenUtil.MAVEN2_BASE_COMPILED_FILES);
            }
            if (revision.getProject().getLanguage().equals(Language.JAVA)) {
                fixedPath = fixedPath.replace(Constantes.DOT_JAVA, Constantes.DOT_CLASS);
            }
        }

        //validating
        final File file = new File(fixedPath);
        if (!file.exists()) {
            if (metricToExtract.isExtractsFromFont()) {
                throw new ServiceException("The file [" + fixedPath + "] must exist!");
            } else {
                return MetricValue.createMetricValueWithZero(revision, metricToExtract);
                //Its a compiled metric and if the .java file is all comented will be no class for it.
                //Things will be like this 'cause i fond that problem and don't know a better way to get it done right now.
            }
        }
        if (metricToExtract.getExtratcsFrom() == Metric.EXTRACTS_FROM_FILE && !file.isFile()) {
            throw new ServiceException("The metric " + metricToExtract.getName() + " can only be extracted from a file! " + fixedPath);
        }
        if (metricToExtract.getExtratcsFrom() == Metric.EXTRACTS_FROM_PACKAGE && !file.isDirectory()) {
            throw new ServiceException("The metric " + metricToExtract.getName() + " can only be extracted from a package! " + fixedPath);
        }
        if (metricToExtract.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT && !file.isDirectory()) {
            throw new ServiceException("The metric " + metricToExtract.getName() + " can only be extracted from a project! " + fixedPath);
        }

        //must compile the revision and then extract the metric.
        if (!metricToExtract.isExtractsFromFont() && revision.getProject().getLanguage().equals(Language.JAVA)) {
            CompilerService.compile(revision);
        }

        try {
            return metricManager.extractMetric(revision, fixedPath);
        } catch (Exception ex) {
            throw new ServiceException(ex);
        } catch (Throwable tw) {
            //Don't think it's the best way, but any Exception or Runtime exception could come.
            throw new ServiceException(tw);
        }

    }

    public boolean isMeasured(MetricManager metricManager, Revision revision) throws ServiceException {
        switch (metricManager.getMetric().getExtratcsFrom()) {
            case Metric.EXTRACTS_FROM_FILE:
                return versionedItemMetricValueService.isMeasured(revision, metricManager.getMetric());

            case Metric.EXTRACTS_FROM_PACKAGE:
                return versionedItemMetricValueService.isMeasured(revision, metricManager.getMetric());

            case Metric.EXTRACTS_FROM_PROJECT:
                return metricValueService.isMeasured(revision, metricManager.getMetric());
        }
        return false;
    }

    public Double getAvgValue(Metric metric, SoftwareProject softwareProject) throws ServiceException {
        switch (metric.getExtratcsFrom()) {
            case Metric.EXTRACTS_FROM_FILE:
            case Metric.EXTRACTS_FROM_PACKAGE:
                double total = 0;
                double sum = 0;
                for (Revision revision : softwareProject.getRevisions()) {
                    List<VersionedItemMetricValue> valuesTemp = versionedItemMetricValueService.getByRevisionAndMetric(revision, metric);
                    total += valuesTemp.size();
                    for (VersionedItemMetricValue versionedItemMetricValue : valuesTemp) {
                        Double value = versionedItemMetricValue.getDoubleValue();
                        if (value != null && !NumberUtil.isNAN(value)) {
                            sum += value;
                        }
                    }
                }
                return NumberUtil.ratio(sum, total);

            case Metric.EXTRACTS_FROM_PROJECT:
                List<MetricValue> valuesTemp = metricValueService.getAbsoluteValuesByProjectAndMetric(softwareProject, metric);
                total = valuesTemp.size();
                sum = 0;
                for (MetricValue metricValue : valuesTemp) {
                    Double value = metricValue.getDoubleValue();
                    if (value != null && !NumberUtil.isNAN(value)) {
                        sum += value;
                    }
                }
                return NumberUtil.ratio(sum, total);
        }
        return null;
    }
}
