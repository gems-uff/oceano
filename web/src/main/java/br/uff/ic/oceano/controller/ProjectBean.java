package br.uff.ic.oceano.controller;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.Repository;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.service.ConfigurationItemService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.service.RepositoryService;
import br.uff.ic.oceano.ostra.service.OstraMetricValueService;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.ListDataModel;

/**
 *
 * @author daniel
 */
public class ProjectBean extends BaseBean {

    //Messages
    private String INFO_CREATE_PROJECT_SUCESSFUL = "Your project was successfully created.";
    private String ERROR_LOADING_PROJECT = "There was an error loading the selected project. Please try that operation again.";
    private String ERROR_CREATE_PROJECT_REPOSITORY_MISSING = "Please select a repository from the list befote creating you project.";
    //Pages
    private String PAGINA_FORM_PROJECT = "def:/privado/oceano/project/form";
    private String PAGINA_DETAIL_PROJECT = "def:/privado/oceano/project/detail";
    private String PAGINA_LIST_PROJECTS = "def:/privado/oceano/project/list";
    //Control
    private ListDataModel projectMetricsTable;
    private ListDataModel tableProject;
    private SelectOneDataModel<ConfigurationItem> selectConfigurationItem;
    private SelectOneDataModel<String> projectExtractedMetricNames;
    private SoftwareProject currentProject;
    private boolean showTableProjectMetric = false;
    //Services
    private ConfigurationItemService configurationItemService = ObjectFactory.getObjectWithDataBaseDependencies(ConfigurationItemService.class);
    private OstraMetricValueService ostraMetricValueService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricValueService.class);
    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private RepositoryService repositoryService = ObjectFactory.getObjectWithDataBaseDependencies(RepositoryService.class);
    private MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);

    public ProjectBean() {
        super("ProjectBean");
        sessao.setPerfilOceano();
    }

    public String newProject() {
        currentProject = new SoftwareProject();
//        currentProject.setTrunkFolder("trunk");
//        currentProject.setBranchesFolder("branches");
//        currentProject.setOuricoProject(false);

        return PAGINA_FORM_PROJECT;
    }

    public String saveNewProject() {
        if (selectConfigurationItem.getObjetoSelecionado() == null) {
            error(ERROR_CREATE_PROJECT_REPOSITORY_MISSING);
            return null;
        }
        try {

            currentProject.setConfigurationItem(selectConfigurationItem.getObjetoSelecionado());
            projectService.save(currentProject);
            this.tableProject = null;

        } catch (ServiceException ex) {
            error(ex.getMessage());
        }

        info(INFO_CREATE_PROJECT_SUCESSFUL);
        return PAGINA_LIST_PROJECTS;
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

    /**
     * @param currentProject the currentProject to set
     */
    public void setCurrentProject(SoftwareProject currentProject) {
        this.currentProject = currentProject;
    }

    public String getUncompiledRevisionsFromCurrentProject() {
        if (currentProject != null && !currentProject.getRevisions().isEmpty()) {
            return currentProject.getUncompiledRevisionCount().toString();
        } else {
            return "N/A";
        }
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
     * @return the selectRepository
     */
    public SelectOneDataModel<ConfigurationItem> getSelectConfigurationItem() {
        if (selectConfigurationItem == null) {
            selectConfigurationItem = new SelectOneDataModel<ConfigurationItem>(configurationItemService.getAll());
        }
        return selectConfigurationItem;
    }

    /**
     * @param selectRepository the selectRepository to set
     */
    public void setSelectRepository(SelectOneDataModel<ConfigurationItem> selectConfigurationItem) {
        this.selectConfigurationItem = selectConfigurationItem;
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
//
        List<Metric> projectExtractedMetrics = metricService.getMetricsByProject(this.currentProject);
        List<String> metricNames = new ArrayList<String>(projectExtractedMetrics.size());
        for (Metric metric : projectExtractedMetrics) {
            metricNames.add(metric.getName());
        }
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
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
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
