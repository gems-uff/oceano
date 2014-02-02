/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service.metric;

import br.uff.ic.oceano.core.control.ApplicationConstants;
import br.uff.ic.oceano.core.dao.OceanoUserDao;
import br.uff.ic.oceano.core.dao.impl.OceanoUserDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.exception.VCSException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.*;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.service.ProjectUserService;
import br.uff.ic.oceano.core.service.RevisionService;
import br.uff.ic.oceano.core.service.vcs.VCSService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.tools.revision.RevisionUtil;
import br.uff.ic.oceano.ostra.service.ItemService;
import br.uff.ic.oceano.ostra.service.LogService;
import br.uff.ic.oceano.ostra.service.VersionedItemMetricValueService;
import br.uff.ic.oceano.ostra.service.VersionedItemService;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.util.SystemUtil;
import br.uff.ic.oceano.util.file.FileUtils;
import br.uff.ic.oceano.util.file.PathUtil;
import java.util.*;

/**
 *
 */
public class OstraMetricService1 extends MetricService {

    protected VCSService vCSService;
    protected ProjectUserService projectUserService;
    protected RevisionService revisionService;
    protected ItemService itemService;
    protected LogService logService;
    protected MetricValueService metricValueService;
    protected VersionedItemService versionedItemService;
    protected VersionedItemMetricValueService versionedItemMetricValueService;
    protected MeasurementService measurementService;
    private OceanoUserDao oceanoUserDao;

    public OstraMetricService1() {
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
        oceanoUserDao = ObjectFactory.getObjectWithDataBaseDependencies(OceanoUserDaoImpl.class);
    }

    public void execute(Request request) throws ServiceException {
        try {
            for (SoftwareProject project : request.getProjects()) {
                process(project, request);
            }
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    private void process(SoftwareProject project, Request request) throws Exception {
        Configuration config = request.getConfiguration();

        Set<Revision> revisions = getRevisions(project, request);
        if (revisions.size() < config.getRevisionThreshold()) {
            Output.println("<<< This Project has less than " + config.getRevisionThreshold() + " revisions. Will be Skipped. >>>");
            return;
        }

        long count = 0;
        for (Revision revision : revisions) {
            Output.println("Processing " + count++ + "/" + revisions.size() + " at " + DateUtil.format(Calendar.getInstance()));
            process(revision, request);
        }
    }

    private Set<Revision> getRevisions(SoftwareProject project, Request request) throws Exception {

        OceanoUser oceanoUser = oceanoUserDao.getByLogin(request.getUserLogin());

        Output.println("Loading user... ");
        ProjectUser projectUser = projectUserService.getByProjectAndOceanoUser(project, oceanoUser);
        request.setProjectUser(projectUser);
        Output.println("Loaded user: " + projectUser.getLogin() + "");

        Output.println("Loading revisions from " + project.getRepositoryUrl());
        Set<Revision> revisions = vCSService.getRevisions(project, projectUser);
        Output.println("Loaded " + revisions.size() + " revisions");

        return revisions;
    }

    private void process(Revision revision, Request request) throws Exception {
        Output.println("Revision " + revision.getNumber());
        Output.println("Comitted files: " + (revision.getChangedFiles() != null ? revision.getChangedFiles().size() : 0));

        List<MetricManager> metricsToExtract = loadMetrics(revision, request);
        if (metricsToExtract.isEmpty()) {
            Output.println("No metrics to extract");
            return;
        }
        request.setProjectMetrics(metricsToExtract);

        try {
            loadRevision(revision, request);
        } catch (VCSException ex) {
            Output.println("There was a problem getting the code. Skipping to next revision...");
        }
        
        if (!RevisionUtil.get().getSourceFilesFromChangedFiles(revision).isEmpty()) {
            Output.println("No changes in source files");
            return;
        }
        
        extractMetrics(revision,request);

        unloadRevision(revision, request);
    }

    private List<MetricManager> loadMetrics(Revision revision, Request request) throws Exception {
        List<String> metricsNames = request.getMetrics();
        List<MetricManager> metrics = new LinkedList<MetricManager>();

        MetricManagerFactory fact = MetricManagerFactory.getInstance();
        for (String name : metricsNames) {
            MetricManager mng = fact.getMetricByName(name);

            if (mng.isLanguageSupported(revision.getProject().getLanguage())) {
                continue;
            }

            if (versionedItemMetricValueService.isMeasured(revision, mng.getMetric())) {
                continue;
            }

            if (metricValueService.isMeasured(revision, mng.getMetric())) {
                continue;
            }

            metrics.add(mng);
        }
        return metrics;
    }

    private void loadRevision(Revision revision, Request request) throws Exception {
        Revision persistentRevision = revisionService.getByNumberAndProject(revision.getNumber(), revision.getProject());
        revision.setId(persistentRevision.getId());

        Configuration config = request.getConfiguration();

        String path = getPath(revision, request);
        revision.setLocalPath(path);

        if (config.isUsingUpdateRevision()) {
            vCSService.doUpdate(revision, request.getProjectUser(), false);
        } else {
            if (PathUtil.exists(path)) {
                FileUtils.deleteDirectory(path);
            }
            PathUtil.mkDirs(path);
            vCSService.doCheckout(revision, request.getProjectUser(), false);
        }
    }

    private void unloadRevision(Revision revision, Request request) throws Exception {
        Configuration config = request.getConfiguration();
        if (config.isUsingUpdateRevision()) {
            return;
        }

        //Delete checkout dir
        String path = getPath(revision, request);
        FileUtils.deleteDirectory(path);
    }

    private String getPath(Revision revision, Request request) throws Exception {
        Configuration config = request.getConfiguration();
        String projectName = revision.getProject().getConfigurationItem().getName().replace(" ", "_");
        String path = PathUtil.getWellFormedPath(SystemUtil.getTempDirectory(), projectName);
        if (!config.isUsingUpdateRevision()) {
            path = PathUtil.getWellFormedPath(path, String.valueOf(revision.getNumber()));
        }
        return path;
    }

    private void extractMetrics(Revision revision, Request request) {
        //create list of elements(files, classes, packages, the project)
        //run each metric through all element
            //save result
        //how to know if project already has the metrics before this point
           //not necessary. Check before extract
    }
}
