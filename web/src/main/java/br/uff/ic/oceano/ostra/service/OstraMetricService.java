package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.*;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.RevisionService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.ostra.exception.CompilerException;
import br.uff.ic.oceano.ostra.model.Item;
import br.uff.ic.oceano.ostra.model.VersionedItem;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import br.uff.ic.oceano.util.Output;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * //TODO Should be moved to core, since web is a front end.
 */
public class OstraMetricService extends MetricService {

    //Control
    private boolean retryingCompilation = false;
    private boolean retryingCheckout = true;
    private boolean usingUpdateRevision = false;
    private boolean CONTINUE_TO_NEXT_REVISION_IF_THROWS_EXCEPTION = false;
    private boolean STOP_REVISIONS_MEASUREMENT_WITH_METRICS_EXCEPTION = true;
    private int revisionThreshold = 100;
    //
    protected VCSService vCSService;
    protected ProjectUserService projectUserService;
    protected RevisionService revisionService;
    protected ItemService itemService;
    protected LogService logService;
    protected MetricValueService metricValueService;
    protected VersionedItemService versionedItemService;
    protected VersionedItemMetricValueService versionedItemMetricValueService;
    protected MeasurementService measurementService;
    protected Set<MetricManager> fontMetrics;
    protected Set<MetricManager> compiledMetrics;

    public OstraMetricService() {
        super();
        measurementService = ObjectFactory.getObjectWithDataBaseDependencies(MeasurementService.class);
        vCSService = ObjectFactory.getObjectWithDataBaseDependencies(VCSService.class);
        projectUserService = ObjectFactory.getObjectWithDataBaseDependencies(ProjectUserService.class);
        revisionService = ObjectFactory.getObjectWithDataBaseDependencies(RevisionService.class);
        itemService = ObjectFactory.getObjectWithDataBaseDependencies(ItemService.class);
        metricValueService = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueService.class);
        versionedItemService = ObjectFactory.getObjectWithDataBaseDependencies(VersionedItemService.class);
        versionedItemMetricValueService = ObjectFactory.getObjectWithDataBaseDependencies(VersionedItemMetricValueService.class);
        logService = ObjectFactory.getObjectWithDataBaseDependencies(LogService.class);
    }

    /**
     * //TODO break down in methods //TODO find progress pattern to use, for
     * future web dispatch feature
     *
     * @param project
     * @param oceanoUser
     * @param metricsToExtract
     * @throws ServiceException
     */
    public void extractAndSaveMetricsFromAllFilesInProjectRevisions(SoftwareProject project, OceanoUser oceanoUser, List<MetricManager> metricsToExtract) throws ServiceException {
        loadMetrics(metricsToExtract);

        Output.println("Pegando usuário... ");
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(project, oceanoUser);
        Output.println("Login: <" + projectUser.getLogin() + ">");

        String revisionsPath = null;
        try {
            Output.println("Pegando revisões de : " + project.getRepositoryUrl());
            Set<Revision> revisions = vCSService.getRevisions(project, projectUser);

            Output.println("Processando " + revisions.size() + " revisões");

            if (revisions.size() < getRevisionThreshold()) {
                Output.println("<<< This Project has less than " + getRevisionThreshold() + " revisions. Will be Skipped. >>>");
                return;
            }

            int count = 0;
            String lastRevisionPath = "";
            Boolean firstRevision = true;
            for (Revision revision : revisions) {
                count++;
                Output.clearLog();
                Output.println("Revision " + revision.getNumber());
                Output.println("Comitted files: " + (revision.getChangedFiles() != null ? revision.getChangedFiles().size() : 0));

                Output.println("WARNING: stopping at 20 revisions");
                if(count==20)break;
                
                //Dont save revisions without alterations at source files
                if (!RevisionUtil.get().hasSourceFileInChangedFiles(revision)) {
                    Output.println("    Revision has no changed source files");
                    continue;
                }

                processRevision(revision);

                if (hasMeasuredMetrics(revision)) {
                    Output.println("    Metrics extracted. Skipping to next revision.");
                    continue;
                }

                try {
                    Output.println(DateUtil.format(Calendar.getInstance()) + " [" + count + "/" + revisions.size() + "]>>Checking revision " + revision + "... ");

                    Output.println("Identifying metrics to extract...");

                    final Set<MetricManager> fontMetricsToExtract = new HashSet<MetricManager>();
                    final Set<MetricManager> compiledMetricsToExtract = new HashSet<MetricManager>();
                    prepareMetricsToExtract(revision, fontMetricsToExtract, compiledMetricsToExtract);

                    if (compiledMetricsToExtract.isEmpty() && fontMetricsToExtract.isEmpty()) {
                        Output.println("    All metrics have been extracted from this revision. Skipping to next revision.");
                        continue;
                    }

                    if (!revisionShouldBeMeasured(revision, fontMetricsToExtract, compiledMetricsToExtract)) {
                        Output.println("Skipping to next revision.       ");
                        continue;
                    }
                    Output.println("");

                    ////////////////////////////////////////////////////////////////
                    //TODO preparing the workspace should be a method, not this.
                    boolean tryAgain = false;
                    boolean failToTryAgain = false;
                    int timesTrying = 0;
                    do {
                        timesTrying++;

                        try {
                            if (firstRevision) {
                                String path = revision.getProject().getConfigurationItem().getName().replace(" ", "_");
                                path = PathUtil.getWellFormedPath(ApplicationConstants.DIR_BASE_CHECKOUTS, path);
                                revision.setLocalPath(path);
                                PathUtil.mkDirs(path);
                                vCSService.doCheckout(revision, projectUser, false);
                                revisionsPath = revision.getLocalPath();
                                Output.println("revisionsPath = " + revisionsPath);
                                firstRevision = false;

                            } else {
                                if (isUsingUpdateRevision()) {
                                    revision.setLocalPath(revisionsPath);
                                    vCSService.doUpdate(revision, projectUser, false);
                                } else {
                                    FileUtils.deleteDirectory(lastRevisionPath);
                                    //TODO path shoulb the same always
                                    //TODO not saving the path for new loop to delete
                                    String path = revision.getProject().getConfigurationItem().getName().replace(" ", "_") + "-r" + revision.getNumber();
                                    path = PathUtil.getWellFormedPath(ApplicationConstants.DIR_BASE_CHECKOUTS, path);
                                    revision.setLocalPath(path);                                    
                                    vCSService.doCheckout(revision, projectUser, false);
                                }
                            }
                        } catch (VCSException ex) {
                            Output.println("");
                            if (timesTrying < 2) {
                                Output.println("    (!) There was a problem getting the code. Lets try again...");
                                //clean and try again
                                if (FileUtils.deleteDirectory(ApplicationConstants.DIR_BASE_CHECKOUTS)) {
                                    //its the first revision again
                                    firstRevision = true;
                                }
                                Output.println("    trying one more time...");
                                tryAgain = true;

                            } else {
                                failToTryAgain = true;
                                Output.println("    (!) There was a problem getting the code. And the problem happened again in the second time. Skipping to next revision...");
                            }
                        }
                    } while (isRetryingCheckout() && tryAgain && timesTrying == 1);
                    if (failToTryAgain) {
                        continue;
                    }
                    //finished preparing workspace
                    ////////////////////////////////////////////////////////////////

                    extractAndSaveMetricsForRevision(revision, fontMetricsToExtract, compiledMetricsToExtract);

                    Output.println("OK. Metrics extracted sucessfully.");

                } catch (ServiceException ex) {
                    Output.println(ex.getMessage());

                    if (CONTINUE_TO_NEXT_REVISION_IF_THROWS_EXCEPTION) {
                        Output.println("===========================> Service Exception: " + ex.getClass() + " : " + ex.getMessage());

                        boolean cleaned = FileUtils.deleteDirectory(ApplicationConstants.DIR_BASE_CHECKOUTS);
                        if (cleaned) {
                            //its the first revision again
                            firstRevision = true;
                        }
                        continue;
                    } else {
                        throw new ServiceException(ex);
                    }
                } finally {
                    logService.log(revision, Output.getLog().toString());
                }
                lastRevisionPath = revision.getLocalPath();
            }

        } catch (Exception ex) {
            throw new ServiceException(ex);
        }

        if (revisionsPath != null) {
            FileUtils.deleteDirectory(revisionsPath);
        }
    }

    private void loadMetrics(List<MetricManager> metricsToExtract) {
        fontMetrics = new HashSet<MetricManager>();
        compiledMetrics = new HashSet<MetricManager>();

        for (MetricManager mm : metricsToExtract) {
            if (mm.getMetric().isExtractsFromFont()) {
                fontMetrics.add(mm);
            } else {
                compiledMetrics.add(mm);
            }
        }

        Output.println("----------------------------------------------------");
        Output.println("Métricas compiladas:");
        for (MetricManager metricManager : compiledMetrics) {
            int type = metricManager.getMetric().getExtratcsFrom();
            Output.println(metricManager.getName() + " - " + (type == Metric.EXTRACTS_FROM_PROJECT ? "Project" : (type == Metric.EXTRACTS_FROM_PACKAGE ? "Package" : "File")));
        }
        Output.println("");
        Output.println("Métricas não compiladas:");
        for (MetricManager metricManager : fontMetrics) {
            int type = metricManager.getMetric().getExtratcsFrom();
            Output.println(metricManager.getName() + " - " + (type == Metric.EXTRACTS_FROM_PROJECT ? "Project" : (type == Metric.EXTRACTS_FROM_PACKAGE ? "Package" : "File")));
        }
        Output.println("----------------------------------------------------");
    }

    private boolean revisionShouldBeMeasured(Revision revision, Set<MetricManager> fontMetricManagers, Set<MetricManager> compiledMetricManagers) throws ServiceException {
        boolean mustExtractFileMetrics = false;
        boolean mustExtractPackageMetrics = false;
        boolean mustExtractProjectMetrics = false;
        boolean mustExtracCompiledMetrics = false;
        boolean mustExtracFontMetrics = false;

        for (MetricManager metricManager : compiledMetricManagers) {
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_FILE) {
                mustExtractFileMetrics = true;
            }
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_PACKAGE) {
                mustExtractPackageMetrics = true;
            }
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
                mustExtractProjectMetrics = true;
            }
            mustExtracFontMetrics |= metricManager.getMetric().isExtractsFromFont();
            mustExtracCompiledMetrics |= !metricManager.getMetric().isExtractsFromFont();
        }

        for (MetricManager metricManager : fontMetricManagers) {
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_FILE) {
                mustExtractFileMetrics = true;
            }
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_PACKAGE) {
                mustExtractPackageMetrics = true;
            }
            if (metricManager.getMetric().getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
                mustExtractProjectMetrics = true;
            }
            mustExtracFontMetrics |= metricManager.getMetric().isExtractsFromFont();
            mustExtracCompiledMetrics |= !metricManager.getMetric().isExtractsFromFont();
        }

        boolean hasSourceFilesFromChangesFiles;
        boolean hasPackagesFromChangesFiles;
        try {
            hasSourceFilesFromChangesFiles = !RevisionUtil.get().getSourceFilesFromChangedFiles(revision).isEmpty();
            hasPackagesFromChangesFiles = !RevisionUtil.get().getPackagesFromChangedFiles(revision).isEmpty();
        } catch (Exception ex) {
            throw new ServiceException("Fail to check for alterations on revision.", ex);
        }

        if (!hasSourceFilesFromChangesFiles && !hasPackagesFromChangesFiles) {
            Output.print("    There's no source or package alteration to measure.  ");
            return false;
        }

        mustExtractFileMetrics &= !hasSourceFilesFromChangesFiles;
        mustExtractPackageMetrics &= !hasPackagesFromChangesFiles;

        if (!mustExtractFileMetrics && !mustExtractPackageMetrics && !mustExtractProjectMetrics) {
            Output.print("    There's no metrics to extract.             ");
            return false;
        }

        try {
            Revision persistentRevision = revisionService.getByNumberAndProject(revision.getNumber(), revision.getProject());
            revision.setCannotCompile(persistentRevision.getCannotCompile());
            revision.setId(persistentRevision.getId());

            final boolean failForCompilation = !isRetryingCompilation() && revision.getCannotCompile() != null && revision.getCannotCompile() && mustExtracCompiledMetrics;
            if (failForCompilation && !mustExtracFontMetrics) {
                Output.print("    The compilation of this revision failed before. ");
                return false;
            }

        } catch (ObjetoNaoEncontradoException ex) {
            //This revision was never measured, so lets work on it!
        }

        Output.print("    Measuring revision...                   ");
        return true;
    }

    private boolean hasMeasuredMetrics(Revision revision) throws ServiceException {
        for (MetricManager metricManager : fontMetrics) {
            if (measurementService.isMeasured(metricManager, revision)) {
                return true;
            }
        }
        for (MetricManager metricManager : compiledMetrics) {
            if (measurementService.isMeasured(metricManager, revision)) {
                return true;
            }
        }

        return false;
    }

    private void prepareMetricsToExtract(Revision revision, Set<MetricManager> fontMetricsToExtract, Set<MetricManager> compiledMetricsToExtract) throws ServiceException {
        //TODO clean up after tests
        Output.println("WARNING: ignoring several metrics");
        for (MetricManager metricManager : fontMetrics) {
            if (!measurementService.isMeasured(metricManager, revision)) {
                //TODO remove this if after tests
                if(metricManager.getMetric().getName().equals(MetricEnumeration.TLOC.getName())){
                    fontMetricsToExtract.add(metricManager);
                }
            }
        }

        Output.println("WARNING: ignoring compiled metrics");
        for (MetricManager metricManager : compiledMetrics) {
            if (!measurementService.isMeasured(metricManager, revision)) {
                //TODO uncomment this if after tests
                //compiledMetricsToExtract.add(metricManager);
            }
        }
    }

    private void extractAndSaveMetricsForRevision(Revision revision, Set<MetricManager> fontMetricsToExtract, Set<MetricManager> compiledMetricsToExtract) throws ServiceException {

        Output.println("Extraindo " + (fontMetricsToExtract.size() + compiledMetricsToExtract.size()) + " metricas da revisão " + revision.getNumber() + ": " + fontMetricsToExtract + " (font), " + compiledMetricsToExtract + "(compiled).");

        Collection<VersionedItemMetricValue> versionedItemMetricValues = new LinkedList<VersionedItemMetricValue>();
        Collection<MetricValue> revisionMetricValues = new LinkedList<MetricValue>();

        //font
        Output.println("    Extracting font metrics...");
        final List<VersionedItemMetricValue> extractedMetricsFromVersionedItems = measurementService.extractMetricsFromVersionedItems(revision, fontMetricsToExtract, STOP_REVISIONS_MEASUREMENT_WITH_METRICS_EXCEPTION);
        versionedItemMetricValues.addAll(extractedMetricsFromVersionedItems);
        revisionMetricValues.addAll(measurementService.extractProjectMetricsOnly(revision, fontMetricsToExtract));

        //compiled
        Output.println("    Extracting compiled metrics...");
        versionedItemMetricValues.addAll(measurementService.extractMetricsFromVersionedItems(revision, compiledMetricsToExtract, STOP_REVISIONS_MEASUREMENT_WITH_METRICS_EXCEPTION));
        revisionMetricValues.addAll(measurementService.extractProjectMetricsOnly(revision, compiledMetricsToExtract));

        saveMetricsForRevisionVersionedItems(revision, versionedItemMetricValues, revisionMetricValues);
    }

    @Transacional
    private void saveMetricsForRevisionVersionedItems(Revision revision, Collection<VersionedItemMetricValue> versionedItemMetricValues, Collection<MetricValue> revisionMetricValues) throws ServiceException {

        Output.println("    Saving metrics... ");

        if (versionedItemMetricValues.isEmpty()) {
            Output.println("        There's no VersionedItem Metricvalues to save.");
        }
        if (revisionMetricValues.isEmpty()) {
            Output.println("        There's no Project MetricValues to save.");
        }

        if (versionedItemMetricValues.isEmpty() && revisionMetricValues.isEmpty()) {
            return;
        }

        final SoftwareProject revisionProject = revision.getProject();
        for (VersionedItemMetricValue versionedItemMetricValue : versionedItemMetricValues) {

            if (versionedItemMetricValue == null) {
                Output.println("Found null metric result, ignoring it");
                continue;
            }

            //recover the item id or create it
            final Item item = versionedItemMetricValue.getVersionedItem().getItem();

            itemService.save(item, revisionProject);

            //save the versioned item
            versionedItemService.save(versionedItemMetricValue.getVersionedItem());

            //save the versioned item metric value
            versionedItemMetricValueService.save(versionedItemMetricValue);
            Output.println("         " + versionedItemMetricValue.getMetric() + " (" + versionedItemMetricValue.getVersionedItem().getItem().getPath() + ") = " + versionedItemMetricValue.getDoubleValue());

        }

        //TODO: verify if this commented line changes the expected behavior.
//        revisionMetricValues.addAll(getRevisionMetricValuesFromVersionedItemMetricValues(versionedItemMetricValues));

        for (MetricValue value : revisionMetricValues) {

            if (value == null) {
                Output.println("Found null metric result, ignoring it");
                continue;
            }

            //updates the revision
            value.setRevision(revision);

            metricValueService.save(value);
            Output.println("         " + value.getMetric() + " (" + revision.getProject().getConfigurationItem().getName() + revision.getNumber() + ") = " + value.getDoubleValue());
        }
    }

    /**
     * This method calculates the metricvalues of non project metrics for a
     * given project.
     *
     * NOTE THAT THIS METHOD SHOULD NOT RECEIVE ANY PROJECT METRICS. Although it
     * has a protection to it. As project metrics already have a metricvalue, if
     * this method calculates one it will replace the measured value with zero,
     * ever.
     *
     * @param project
     * @param metrics
     */
    public void calculateRevisionMetricValuesFromVersionedItemMetricValues(SoftwareProject project, List<Metric> metrics) {
        //it is not possible to calculate metricvalues from project metrics, because they are already in that granularity
        List<Metric> nonProjectMetrics = new ArrayList<Metric>(metrics.size());
        for (Metric metric : metrics) {
            if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
                continue;
            }
            nonProjectMetrics.add(metric);
        }
        if (nonProjectMetrics.isEmpty()) {
            System.out.println("There are no metrics to calculate.");
            return;
        }

        //A map to store the last value of each item
        Map<Metric, Map<Item, Double>> mapNewestItemMeasurement = new HashMap<Metric, Map<Item, Double>>();
        for (Metric metric : nonProjectMetrics) {
            mapNewestItemMeasurement.put(metric, new HashMap<Item, Double>());
        }

        System.out.println("Get revisions...");
        Set<Revision> revisions = revisionService.getByProject(project);
        int count = 1;
        int total = revisions.size();
        for (Revision revision : revisions) {
            System.out.println("\n" + count++ + "/" + total + " Revision = " + revision);

            for (Metric metric : nonProjectMetrics) {
//                Output.println("    " + metric);

                //get the list of items modified in this revision
                List<VersionedItemMetricValue> vimvs = versionedItemMetricValueService.getByRevisionAndMetric(revision, metric);

//                Output.println("    Updating the measured items:" + vimvs.size());
                //for each of them, update the map with the last measured value
                for (VersionedItemMetricValue vimv : vimvs) {
//                    Output.println("    >>> vimv = " + vimv);
                    if (vimv.getVersionedItem().getType() == VersionedItem.TYPE_DELETED) {
                        mapNewestItemMeasurement.get(vimv.getMetric()).remove(vimv.getVersionedItem().getItem());
                    } else {
                        mapNewestItemMeasurement.get(vimv.getMetric()).put(vimv.getVersionedItem().getItem(), vimv.getDoubleValue());
                    }
                }
//                Output.println("    Calculating the value of revision");
                //after update the map, it contains the last value of each item.

                // ================ início do novo método para o metric manager que deve contabilizar para MetricValue os VIMVs
                Double sum = 0d;
                int numberOfConsederedArtifacts = 0;
                StringBuilder strSoma = new StringBuilder();
                for (Double oneValue : mapNewestItemMeasurement.get(metric).values()) {
//                    soma.append(" + ").append(oneValue);
                    if (!oneValue.isNaN()) {
                        sum += oneValue;
                        numberOfConsederedArtifacts++;
                    }
                }
                if (metric.getAcronym().equals(MetricEnumeration.ACC.getAcronym())
                        || metric.getAcronym().equals(MetricEnumeration.CAM.getAcronym())
                        || metric.getAcronym().equals(MetricEnumeration.DAM.getAcronym())
                        || metric.getAcronym().equals(MetricEnumeration.RMA.getAcronym())) {
                    sum /= numberOfConsederedArtifacts;
                }
                MetricValue mv = new MetricValue(revision, metric, sum);
                // ================ fim do novo método

                strSoma.append("    |").append(mapNewestItemMeasurement.get(metric).values().size()).append("|").append(" = ").append(mv);
                Output.println("    " + strSoma.toString());

                metricValueService.save(mv);
            }
        }
    }

    /**
     * @return the usingUpdateRevision
     */
    public boolean isUsingUpdateRevision() {
        return usingUpdateRevision;
    }

    /**
     * @param usingUpdateRevision the usingUpdateRevision to set
     */
    public void setUsingUpdateRevision(boolean usingUpdateRevision) {
        this.usingUpdateRevision = usingUpdateRevision;
    }

    /**
     * @return the revisionThreshold
     */
    public int getRevisionThreshold() {
        return revisionThreshold;
    }

    /**
     * @param revisionThreshold the revisionThreshold to set
     */
    public void setRevisionThreshold(int revisionThreshold) {
        this.revisionThreshold = revisionThreshold;
    }

    /**
     * @return the retryingCheckout
     */
    public boolean isRetryingCheckout() {
        return retryingCheckout;
    }

    /**
     * @param retryingCheckout the retryingCheckout to set
     */
    public void setRetryingCheckout(boolean retryingCheckout) {
        this.retryingCheckout = retryingCheckout;
    }

    /**
     * @return the retryingCompilation
     */
    public boolean isRetryingCompilation() {
        return retryingCompilation;
    }

    /**
     * @param retryingCompilation the retryingCompilation to set
     */
    public void setRetryingCompilation(boolean retryingCompilation) {
        this.retryingCompilation = retryingCompilation;
    }

    private void processRevision(Revision revision) throws CompilerException {
        try {
            CompilerService.compile(revision);
            revision.setCannotCompile(false);
        } catch (CompilerException ex) {
            revision.setCannotCompile(true);
        }

        try {
            Revision dbRevision = revisionService.getByNumberAndProject(revision.getNumber(), revision.getProject());
            revision.setId(dbRevision.getId());
        } catch (ObjetoNaoEncontradoException ex) {
            // its ok, the revision is just new. So, lets save it!
            revisionService.save(revision);
        }
    }
}
