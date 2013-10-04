package br.uff.ic.oceano.ostra.controller;

import br.uff.ic.oceano.controller.*;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ostra.service.OstraMetricValueService;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.ListDataModel;

/**
 *
 * @author daniel
 */
public class MonitoringBean extends BaseBean {

    //Messages
    private String ERROR_LOADING_PROJECT = "There was an error loading the selected project. Please try that operation again.";
    //Pages
    private String PAGINA_DETAIL_PROJECT = "def:/privado/ostra/monitoring/detail";
    private String PAGINA_LIST_PROJECTS = "def:/privado/ostra/monitoring/list";
    //Control
    private ListDataModel projectMetricsTable;
    private ListDataModel tableProject;
    private SelectOneDataModel<String> projectExtractedMetricNames;
    private SoftwareProject currentProject;
    private boolean showTableProjectMetric = false;
    //Services
    private OstraMetricValueService ostraMetricValueService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricValueService.class);
    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);

    public MonitoringBean() {
        super("MonitoringBean");
        sessao.setPerfilOstra();
    }

    public String detailProject() {
        try {
            this.currentProject = (SoftwareProject) tableProject.getRowData();
            this.currentProject = projectService.getProjectToDetailById(this.currentProject.getId());
            showTableProjectMetric = false;
            initializeProjectExtractedMetricNames();

            return PAGINA_DETAIL_PROJECT;

        } catch (ServiceException ex) {
            error(ERROR_LOADING_PROJECT);
            return null;
        }
    }

    public String returnListProjects() {
        this.currentProject = null;
        return PAGINA_LIST_PROJECTS;
    }

    /**
     * @return the currentProject
     */
    public SoftwareProject getCurrentProject() {
        return currentProject;
    }

    public String getUncompiledRevisionsFromCurrentProject() {
        Integer count = 0;
        if (currentProject != null && !currentProject.getRevisions().isEmpty()) {
            for (Iterator<Revision> it = currentProject.getRevisions().iterator(); it.hasNext();) {
                Revision revision = it.next();
                if (revision.getCannotCompile()!=null && revision.getCannotCompile()==true) {
                    count++;
                }
            }
        }
        return count.toString();
    }

    /**
     * @param currentProject the currentProject to set
     */
    public void setCurrentProject(SoftwareProject currentProject) {
        this.currentProject = currentProject;
    }

    /**
     * @return the tableProject
     */
    public ListDataModel getTableProject() {
        if (tableProject == null) {
            tableProject = new ListDataModel(projectService.getAll());
            for (SoftwareProject project : projectService.getAll()) {
                System.out.println("project = " + project);
            }
        }
        return tableProject;
    }

    /**
     * @param tableProject the tableProject to set
     */
    public void setTableProject(ListDataModel tableProject) {
        this.tableProject = tableProject;
    }

    /**
     * @return the projectMetricsTable
     */
    public ListDataModel getProjectMetricsTable() {
        return projectMetricsTable;
    }

    /**
     * @param projectMetricsTable the projectMetricsTable to set
     */
    public void setProjectMetricsTable(ListDataModel projectMetricsTable) {
        this.projectMetricsTable = projectMetricsTable;
    }

    private void initializeProjectExtractedMetricNames() {
        List<Metric> projectExtractedMetrics = metricService.getMetricsByProject(this.currentProject);
        List<String> metricNames = new ArrayList<String>(projectExtractedMetrics.size());
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        for (Metric metric : projectExtractedMetrics) {
            metricNames.add(metric.getName());
            System.out.println("metric = " + metric);
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        this.projectExtractedMetricNames = new SelectOneDataModel<String>(metricNames);
    }

    /**
     * @return the projectExtractedMetricNames
     */
    public SelectOneDataModel<String> getProjectExtractedMetricNames() {
        return projectExtractedMetricNames;
    }

    /**
     * @param projectExtractedMetricNames the projectExtractedMetricNames to set
     */
    public void setProjectExtractedMetricNames(SelectOneDataModel<String> projectExtractedMetricNames) {
        showTableProjectMetric = true;
        this.projectExtractedMetricNames = projectExtractedMetricNames;
    }

    public String updateProjectMetricsTable() {
        this.projectMetricsTable = new ListDataModel(ostraMetricValueService.getProjectMetricsToDetail(currentProject, projectExtractedMetricNames.getSelecao()));
        this.showTableProjectMetric = true;

        System.out.println(projectMetricsTable.getRowCount());
        System.out.println("Mostrar tabela de metricas");
        return null;
    }

    /**
     * @return the showTableProjectMetric
     */
    public boolean isShowTableProjectMetric() {
        return showTableProjectMetric;
    }

    /**
     * @param showTableProjectMetric the showTableProjectMetric to set
     */
    public void setShowTableProjectMetric(boolean showTableProjectMetric) {
        this.showTableProjectMetric = showTableProjectMetric;
    }
}
