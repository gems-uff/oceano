/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.controller;

import br.uff.ic.oceano.ostra.service.ChartValue;
import br.uff.ic.oceano.controller.*;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.ostra.service.MetricChartService;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.ListDataModel;


import javax.servlet.ServletContext;

/**
 *
 * @author wallace
 */
public class MonitoringChartBean extends BaseBean {

    public static final int CHART_HEIGHT = 500;
    public static final int CHART_LENGHT = 1000;
    private String ERROR_LOADING_PROJECT = "There was an error loading the selected project. Please try that operation again.";
    public String PAGINA_LIST_PROJECTS = "def:/privado/ostra/chart/listProject";
    private final String PAGINA_DETAIL_PROJECT = "def:/privado/ostra/chart/detailChart";
    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
    private ListDataModel tableProject;
    private SoftwareProject currentProject;
    private Metric currentMetric;
    private SelectOneDataModel<String> projectExtractedMetricNames;
    private List<ElementSet> histogramValues;
    private boolean showChart = false;
    private boolean showHistogram = false;
    private String tag;
    private String chartPath;
    private MetricChartService metricChartService = new MetricChartService();
    private ChartValue chartValue = null;
    private ChartValue deltaChart = null;
    private ChartValue histogramValue = null;
    private String histogramSets;

    public MonitoringChartBean() {
        super("MonitoringChartBean");
        sessao.setPerfilOstra();
    }

    public String returnListProjects() {
        return PAGINA_LIST_PROJECTS;
    }

    public String detailProject() {
        try {
            this.currentProject = (SoftwareProject) tableProject.getRowData();
            this.currentProject = projectService.getProjectToDetailById(this.currentProject.getId());
            //showTableProjectMetric = false;
            initializeProjectExtractedMetricNames();

            return PAGINA_DETAIL_PROJECT;

        } catch (ServiceException ex) {
            error(ERROR_LOADING_PROJECT);
            return null;
        }
    }

    private void initializeProjectExtractedMetricNames() {
        Output.println("Initializing metrics list");
        List<Metric> projectExtractedMetrics = metricService.getMetricsByProject(this.currentProject);
        List<String> metricNames = new ArrayList<String>(projectExtractedMetrics.size());        
        for (Metric metric : projectExtractedMetrics) {
            if(!metric.isFromProject()){
                continue;
            }
            metricNames.add(metric.getName());
            Output.println("metric = " + metric);
        }        
        this.projectExtractedMetricNames = new SelectOneDataModel<String>(metricNames);
    }

    public String updateMetricChart() {
        try {
            System.out.println("Updating Metric Chart");
            
            this.currentMetric = MetricService.getMetricByName(projectExtractedMetricNames.getSelecao());
            
            ServletContext sc = (ServletContext) getContexto().getExternalContext().getContext();

            chartValue = metricChartService.getChartValue(currentProject, currentMetric, CHART_LENGHT, CHART_HEIGHT, false, sc);

            this.chartPath = chartValue.getChartPath();
            this.tag = chartValue.getTag();

            System.out.println("chartPath: " + chartPath);

            List<Double> minMaxList = metricChartService.getSoftwareProjectMetricMinMaxValues(currentProject, currentMetric);
            double maxValue = minMaxList.get(1);
            double minValue = minMaxList.get(0);
            double range = maxValue - minValue;
            System.out.println("Max value: " + maxValue);
            System.out.println("Min value: " + minValue);
            range = range / 5;
            histogramValues = new ArrayList<ElementSet>();
            histogramValues.add(new ElementSet(String.valueOf(minValue)));
            for (int i = 1; i < 5; i++) {
                histogramValues.add(new ElementSet(String.valueOf(minValue + ((i) * range))));
            }
            histogramValues.add(new ElementSet(String.valueOf(maxValue)));
            histogramSets = new String("5");

            updateHistogramChart();

            showChart = true;

            return null;
        } catch (ServiceException ex) {
            error(ERROR_LOADING_PROJECT);
            return null;
        }
    }

    public String updateNumberOfSets() {
        List<Double> minMaxList = metricChartService.getSoftwareProjectMetricMinMaxValues(currentProject, currentMetric);
        double maxValue = minMaxList.get(1);
        double minValue = minMaxList.get(0);
        double range = maxValue - minValue;
        System.out.println("Max value: " + maxValue);
        System.out.println("Min value: " + minValue);
        int numberOfSets = Integer.valueOf(histogramSets);
        range = range / numberOfSets;

        histogramValues = new ArrayList<ElementSet>();
        histogramValues.add(new ElementSet(String.valueOf(minValue)));
        for (int i = 1; i < numberOfSets; i++) {
            histogramValues.add(new ElementSet(String.valueOf(minValue + ((i) * range))));
        }
        histogramValues.add(new ElementSet(String.valueOf(maxValue)));
        updateHistogramChart();
        return null;
    }

    public String updateHistogramChart() {
        try {
            System.out.println("updateHistogramChart");
            int numberOfSets = Integer.valueOf(histogramSets);
            double doubleHistogram[] = new double[numberOfSets + 1];

            for (int i = 0; i < (numberOfSets + 1); i++) {
                doubleHistogram[i] = Double.valueOf(histogramValues.get(i).getValue());
            }
            System.out.println("upateHistogram");

            ServletContext sc = (ServletContext) getContexto().getExternalContext().getContext();
            histogramValue = metricChartService.getHistogramValue(currentProject, currentMetric, numberOfSets, doubleHistogram, CHART_LENGHT, CHART_HEIGHT, sc);
            showHistogram = true;
            return null;
        } catch (Exception ex) {
            error(ERROR_LOADING_PROJECT);
            return null;
        }

    }

    public String updateValores() {

        return null;
    }

    public ChartValue getChartValue() {
        return chartValue;
    }

    public ChartValue getDeltaChart() {
        return deltaChart;
    }

    public String getChartPath() {
        return chartPath;
    }

    public String getTag() {
        return tag;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public boolean isShowHistogram() {
        return showHistogram;
    }

    public SelectOneDataModel<String> getProjectExtractedMetricNames() {
        return projectExtractedMetricNames;
    }

    public SoftwareProject getCurrentProject() {
        return currentProject;
    }

    public String getCurrentProjectIntegrity() {
        System.out.println("getCurrentProjectIntegrity");
        Double integrity = 1D;

        if (currentProject != null && !currentProject.getRevisions().isEmpty()) {
            int total = currentProject.getRevisions().size();
            Double sum = 0d;
            for (Revision revision : currentProject.getRevisions()) {
                if (revision.getCannotCompile() != null && revision.getCannotCompile() == true) {
                    sum++;
                }
            }
            integrity = sum / total;
            System.out.println("sum = " + sum);
            System.out.println("total = " + total);
            System.out.println("integrity = " + integrity);
            integrity = 1 - integrity;
            System.out.println("integrity = " + integrity);
            integrity *= 100;
            System.out.println("integrity = " + integrity);
        }
        return Math.round(integrity) + "";
    }

    public String getUncompiledRevisionsFromCurrentProject() {
        Integer count = 0;
        if (currentProject != null && !currentProject.getRevisions().isEmpty()) {
            for (Revision revision : currentProject.getRevisions()) {
                if (revision.getCannotCompile() != null && revision.getCannotCompile() == true) {
                    count++;
                }
            }
        }
        return count.toString();
    }

    public ChartValue getHistogramValue() {
        return histogramValue;
    }

    public ListDataModel getTableProject() {
        if (tableProject == null) {
            tableProject = new ListDataModel(projectService.getAll());
        }
        return tableProject;
    }

    public List<ElementSet> getHistogramValues() {
        return histogramValues;
    }

    public void setHistogramValues(List<ElementSet> histogramValues) {
        this.histogramValues = histogramValues;
    }

    public String getHistogramSets() {
        return histogramSets;
    }

    public void setHistogramSets(String histogramSets) {
        this.histogramSets = histogramSets;
    }
}
