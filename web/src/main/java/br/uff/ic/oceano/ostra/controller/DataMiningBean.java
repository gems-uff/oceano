package br.uff.ic.oceano.ostra.controller;

import br.uff.ic.oceano.controller.*;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.core.service.SoftwareProjectService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.util.Logger;
import br.uff.ic.oceano.ostra.controle.Constantes;
import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import br.uff.ic.oceano.ostra.discretizer.DayOfWeekDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.Discretizer;
import br.uff.ic.oceano.ostra.discretizer.DiscretizerFactory;
import br.uff.ic.oceano.ostra.discretizer.HourOfDayDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NegativePositiveDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.NumberOfFilesDiscretizer;
import br.uff.ic.oceano.ostra.discretizer.RoundOfDayDiscretizer;
import br.uff.ic.oceano.ostra.exception.DataMiningException;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import br.uff.ic.oceano.view.SelectableItem;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import br.uff.ic.oceano.ostra.service.behaviortable.BehaviorTableService;
import br.uff.ic.oceano.ostra.service.DataMiningResultService;
import br.uff.ic.oceano.ostra.service.DeltaMetricsRevisionDataBaseService;
import br.uff.ic.oceano.ostra.service.behaviortable.Behavior;
import br.uff.ic.oceano.ostra.tools.datamining.AprioriTool;
import br.uff.ic.oceano.ostra.tools.datamining.DataMiningTool;
import br.uff.ic.oceano.ostra.tools.datamining.util.DatabaseToArffService;
import br.uff.ic.oceano.view.SelectOneDataModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author daniel
 */
public class DataMiningBean extends BaseBean {

    //Messages
    private String ERROR_NO_PROJECT_SELECTED = "It's needed at least one project to mine.";
    private String ERROR_LOADING_DATA_MINING_RESULT = "Ops, there was an error loading this result. Please try again.";
    private String ERROR_LOADING_PROJECTS = "Ops, there was an error loading the projects. Please try again.";
    private String SUCCESS_SAVING_DATA_MINING_RESULT = "the mined patters was sucessfully saved.";
    //Pages
    private String PAGE_LIST_DATAMINING = "def:/privado/ostra/datamining/listDataMining";
    private String PAGE_NEW_DATA_BASE = "def:/privado/ostra/datamining/database/newDatabase";
    private String PAGE_CONFIG_DATA_MINING = "def:/privado/ostra/datamining/configDataMining";
    private String PAGE_DETAIL_DATA_MINING_RESULT = "def:/privado/ostra/datamining/detailDataMiningResult";
    private String PAGE_BEHAVIOR_TABLE = "def:/privado/ostra/datamining/behaviorTable";
    //Control
    private DataMiningControl miningControl = ObjectFactory.getObjectWithDataBaseDependencies(DataMiningControl.class);
    private ListDataModel tableSelectProjects;
    private List<SoftwareProject> selectedProjects;
    private ListDataModel tableSelectMetrics;
    private ListDataModel tableDataMiningResults;
    private DataBaseSnapshot dataBaseSnapshot;
    private DataMiningResult currentDataMiningResult;
    private List<Discretizer> discretizers;
    private List<Metric> metricsToConsider;
    private boolean calculateStandardDeviation = false;
    private boolean ignoreRevisionsThatDontCompile = true;
    private boolean showMinARMetric = false;
    private String tableBehaviorMetric;
    //config data mining
    private SelectOneDataModel<String> comboAssociationRuleMetric;
    //filter
    private List<String> allPossibleAttributes;
    private SelectOneDataModel<String> comboUnusedAttributes;
    private List<String> precedentAttributes;
    private List<String> consequentAttributes;
    private List<DataMiningPattern> filteredMinedPatterns;
    private boolean considerLimitSizes;
    private SelectOneDataModel<Integer> comboPrecedentSize;
    private SelectOneDataModel<Integer> comboConsequentSize;
    //Services
    private DataMiningTool apriori = ObjectFactory.getObjectWithDataBaseDependencies(AprioriTool.class);
    private MetricService metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
    private SoftwareProjectService projectService = ObjectFactory.getObjectWithDataBaseDependencies(SoftwareProjectService.class);
    private DeltaMetricsRevisionDataBaseService deltaMetricsRevisionDataBaseService = ObjectFactory.getObjectWithDataBaseDependencies(DeltaMetricsRevisionDataBaseService.class);
    private DataMiningResultService dataMiningResultService = ObjectFactory.getObjectWithDataBaseDependencies(DataMiningResultService.class);
    private BehaviorTableService behaviorTableService = ObjectFactory.getObjectWithDataBaseDependencies(BehaviorTableService.class);

    public DataMiningBean() {
        super("DataMiningBean");
        sessao.setPerfilOstra();
    }
    
    public DataMiningBean(SessaoDoUsuario sessao) {
        super("DataMiningBean",sessao);
        sessao.setPerfilOstra();
    }

    public String pageListDataMining() {
        return PAGE_LIST_DATAMINING;
    }

    public String pageNewDataBase() {
        return PAGE_NEW_DATA_BASE;
    }

    public String deleteDataMiningResult() {
        currentDataMiningResult = (DataMiningResult) tableDataMiningResults.getRowData();
        try {
            Logger.info("Deleting the dataMinig result.");
            dataMiningResultService.delete(currentDataMiningResult);

            info("Data sucessfully removed.");
        } catch (ObjetoNaoEncontradoException ex) {
            error("Was not possible to remove this data. Try to remove it again.");
        }
        currentDataMiningResult = null;
        tableDataMiningResults = null;
        return null;
    }

    public String downloadARFF() {
        String arffContent = null;
        if (currentDataMiningResult != null && currentDataMiningResult.getArff() != null) {
            arffContent = currentDataMiningResult.getArff();
        } else {
            arffContent = getDataBaseAsArff();
        }

        FacesContext context = getContexto();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setHeader("Content-disposition",
                "attachment; filename= " + this.currentDataMiningResult.getDescription().replace(" ", "_") + ".arff");
        response.setContentLength(arffContent.length());
        try {
            response.getOutputStream().write(arffContent.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
            context.responseComplete();

            return null;
        } catch (IOException e) {
            error("There was a problem generating the arff file. Please try again.");
            return null;
        }
    }

    public String detailDataMiningResult() {
        Long currentDataMiningResultsId = ((DataMiningResult) tableDataMiningResults.getRowData()).getId();
        try {
//            Logger.getLogger(DataMiningBean.class.getName()).log(Level.FINE, "Getting the data minig result to detail.");
            Logger.info("Getting the data minig result to detail.");
            currentDataMiningResult = dataMiningResultService.getToDetailById(currentDataMiningResultsId);

            Logger.info("Initialyzing filters.");
            initializeFilters();
            updateFilteredPatternList();

//            System.out.println("return to page.");

            showBehaviorTable();

            return PAGE_DETAIL_DATA_MINING_RESULT;

        } catch (ObjetoNaoEncontradoException ex) {
            error(ERROR_LOADING_DATA_MINING_RESULT);
            return null;
        }
    }

    public String downloadArffFile() {
        error("Sorry but this functionality is not yet implemented. Wait a next version.");
        return null;
    }

    public String getDataBaseAsArff() {
        try {
            return DatabaseToArffService.dataBaseToARFF(dataBaseSnapshot, discretizers);
        } catch (ServiceException ex) {
            return null;
        }
    }

    public String configureDataMining() {
        Logger.info("Initializing selected projects.");
        initializeSelectedProjects();

        if (selectedProjects.isEmpty()) {
            error(ERROR_NO_PROJECT_SELECTED);
            return null;
        }

        try {
            Logger.info("Initializing discretizers.");
            initializeDiscretizers();
            initializeSelectedMetrics();

            //build the dataBaseSnapshot that we will mine later.
            Logger.info("Building database snapshot.");
            dataBaseSnapshot = deltaMetricsRevisionDataBaseService.buildDeltaMetricsDataBase(selectedProjects, discretizers, calculateStandardDeviation, ignoreRevisionsThatDontCompile, metricsToConsider, false);
        } catch (ServiceException ex) {
            error(ex.getMessage());
            return null;
        }

        Logger.info("Creating a DataMiningControl.");
        miningControl = new DataMiningControl();

        Logger.debug("Returning to page.");
        return PAGE_CONFIG_DATA_MINING;
    }

    private void initializeSelectedMetrics() {
        metricsToConsider = new LinkedList<Metric>();
        for (SelectableItem si : ((List<SelectableItem>) tableSelectMetrics.getWrappedData())) {
//            System.out.println("si = " + si);
            if (si.isSelected()) {
//                System.out.println("    metrica selecionada = " + si.getItem());
                metricsToConsider.add((Metric) si.getItem());
            }
        }
    }

    private void initializeComboConsequentSize() {
        Set<Integer> consequentSizes = new HashSet<Integer>();
        int max = Integer.MIN_VALUE;
        for (DataMiningPattern dataMiningPattern : currentDataMiningResult.getDataMiningPatterns()) {
            consequentSizes.add(dataMiningPattern.getConsequentSize());
            if (dataMiningPattern.getConsequentSize() > max) {
                max = dataMiningPattern.getConsequentSize();
            }
        }

        comboConsequentSize = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(consequentSizes, max);
    }

    private void initializeComboPrecedentSize() {
        Set<Integer> precedentSizes = new HashSet<Integer>();
        int max = Integer.MIN_VALUE;
        for (DataMiningPattern dataMiningPattern : currentDataMiningResult.getDataMiningPatterns()) {
            precedentSizes.add(dataMiningPattern.getPrecedentSize());
            if (dataMiningPattern.getPrecedentSize() > max) {
                max = dataMiningPattern.getPrecedentSize();
            }
        }
        comboPrecedentSize = SelectOneDataModel.criaComObjetoSelecionadoSemTextoInicial(precedentSizes, max);
    }

    private void initializeSelectedProjects() {
        selectedProjects = new ArrayList<SoftwareProject>();
        for (SelectableItem selectableItem : (List<SelectableItem>) tableSelectProjects.getWrappedData()) {
            if (selectableItem.isSelected()) {
                final SoftwareProject selectedProject = (SoftwareProject) selectableItem.getItem();
                selectedProjects.add(selectedProject);
            }
        }
    }

    private void initializeDiscretizers() throws ServiceException {
        Set<String> setOfMetricNames = new HashSet<String>();
        for (SoftwareProject project : selectedProjects) {
            for (Metric metric : metricService.getMetricsByProject(project)) {
                setOfMetricNames.add(metric.getName());
            }
        }

        discretizers = new ArrayList<Discretizer>();
        for (String metricName : setOfMetricNames) {
            discretizers.add(DiscretizerFactory.getDiscretizer(Constantes.PREFIX_DELTA_METRIC_AVARAGE + metricName, NegativePositiveDiscretizer.class));
            discretizers.add(DiscretizerFactory.getDiscretizer(Constantes.PREFIX_DELTA_METRIC_STANDARD_DEVIATON + metricName, NegativePositiveDiscretizer.class));
        }
        discretizers.add(DiscretizerFactory.getDiscretizer("rday", DayOfWeekDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rhour", HourOfDayDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("rRound", RoundOfDayDiscretizer.class));
        discretizers.add(DiscretizerFactory.getDiscretizer("#files", NumberOfFilesDiscretizer.class));
    }

    public String mineAssociationRules() {
        if (comboAssociationRuleMetric.isTextoInicialSelecionado()) {
            error("Please select a association rule metric.");
            return null;
        }

        Logger.info("Starting to Mine database");

        try {
            Logger.info("Mining association rules with Apriori.");
            currentDataMiningResult = apriori.mine(getDataBaseAsArff(), miningControl);
            currentDataMiningResult.prepare();

            Logger.info("Writing data mining result description.");
            String description = "Projects: ";
            for (SoftwareProject project : selectedProjects) {
                description += project.getConfigurationItem().getName() + ",";
            }
            description = description.substring(0, description.length() - 1) + ".";
            description += " RA Metric: " + currentDataMiningResult.getRuleMetricName();
            currentDataMiningResult.setDescription(description);

            Logger.info("Initializyng filters.");
            initializeFilters();
            showBehaviorTable();

            info("The mineration was sucessfull. Mined patterns will be shown.");
            return PAGE_DETAIL_DATA_MINING_RESULT;

        } catch (DataMiningException ex) {            
            //System.out.println("ex = " + ex);
            ex.printStackTrace();
            error(ex.getMessage());
            return null;
        }
    }

    public String saveDataMiningResult() {
        Logger.info("Saving data mining result.");
        dataMiningResultService.save(currentDataMiningResult);

        info(SUCCESS_SAVING_DATA_MINING_RESULT);
        return PAGE_LIST_DATAMINING;
    }

    /**
     * @return the miningControl
     */
    public DataMiningControl getMiningControl() {
        return miningControl;
    }

    /**
     * @param miningControl the miningControl to set
     */
    public void setMiningControl(DataMiningControl miningControl) {
        this.miningControl = miningControl;
    }

    /**
     * @return the tableSelectProjects
     */
    public ListDataModel getTableSelectProjects() {
        Logger.info("Loading project table");
        if (tableSelectProjects == null) {
            try {
                List<SelectableItem> selectableProjects = new ArrayList<SelectableItem>();
                for (SoftwareProject project : projectService.getProjectsByOceanoUser(sessao.getUsuarioCorrente())) {
                    selectableProjects.add(new SelectableItem(projectService.getProjectToDetailById(project.getId())));
                }
                tableSelectProjects = new ListDataModel(selectableProjects);

            } catch (ServiceException ex) {
                //should be impossible to happen, but...
                error(ERROR_LOADING_PROJECTS);
                return null;
            }
        }
        return tableSelectProjects;
    }

    /**
     * @param tableSelectProjects the tableSelectProjects to set
     */
    public void setTableSelectProjects(ListDataModel tableSelectProjects) {
        this.tableSelectProjects = tableSelectProjects;
    }

    /**
     * @return the tableSelectMetrics
     */
    public ListDataModel getTableSelectMetrics() {
        if (tableSelectMetrics == null) {
            initializeMetrics();
            List<SelectableItem> items = new ArrayList<SelectableItem>();
            for (Metric metric : metricService.getAll()) {
                items.add(new SelectableItem(metric, metricsToConsider.contains(metric)));
            }
            tableSelectMetrics = new ListDataModel(items);
        }
        return tableSelectMetrics;
    }

    /**
     * @param tableSelectMetrics the tableSelectMetrics to set
     */
    public void setTableSelectMetrics(ListDataModel tableSelectMetrics) {
        this.tableSelectMetrics = tableSelectMetrics;
    }

    /**
     * @return the calculateStandardDeviation
     */
    public boolean isCalculateStandardDeviation() {
        return calculateStandardDeviation;
    }

    /**
     * @param calculateStandardDeviation the calculateStandardDeviation to set
     */
    public void setCalculateStandardDeviation(boolean calculateStandardDeviation) {
        this.calculateStandardDeviation = calculateStandardDeviation;
    }

    /**
     * @return the dataBaseSnapshot
     */
    public DataBaseSnapshot getDataBaseSnapshot() {
        return dataBaseSnapshot;
    }

    /**
     * @param dataBaseSnapshot the dataBaseSnapshot to set
     */
    public void setDataBaseSnapshot(DataBaseSnapshot dataBaseSnapshot) {
        this.dataBaseSnapshot = dataBaseSnapshot;
    }

    /**
     * @return the currentDataMiningResult
     */
    public DataMiningResult getCurrentDataMiningResult() {
        return currentDataMiningResult;
    }

    /**
     * @param currentDataMiningResult the currentDataMiningResult to set
     */
    public void setCurrentDataMiningResult(DataMiningResult currentDataMiningResult) {
        this.currentDataMiningResult = currentDataMiningResult;
    }

    /**
     * @return the tableDataMiningResults
     */
    public ListDataModel getTableDataMiningResults() {
        if (tableDataMiningResults == null) {
            Logger.info("Loagind data mining results from database.");
            tableDataMiningResults = new ListDataModel(dataMiningResultService.getAll());
        }
        return tableDataMiningResults;
    }

    /**
     * @param tableDataMiningResults the tableDataMiningResults to set
     */
    public void setTableDataMiningResults(ListDataModel tableDataMiningResults) {
        this.tableDataMiningResults = tableDataMiningResults;
    }

    /**
     * @return the unusedAttributes
     */
    public SelectOneDataModel<String> getComboUnusedAttributes() {
        if (comboUnusedAttributes == null) {
//            allPossibleAttributes = new ArrayList<String>();
//
//            List<Metric> metrics = metricService.getAll();
//            for (Metric metric : metrics) {
//                allPossibleAttributes.add(Constantes.PREFIX_DELTA_METRIC_AVARAGE + metric.getName());
//            }
//            for (Metric metric : metrics) {
//                allPossibleAttributes.add(Constantes.PREFIX_DELTA_METRIC_STANDARD_DEVIATON + metric.getName());
//            }
//            allPossibleAttributes.add("rdate");
//            allPossibleAttributes.add("#files");
//            allPossibleAttributes.add("rcommiter");

            allPossibleAttributes = this.currentDataMiningResult.getAttributes();

            updateComboUnusedAttributes();
        }
        return comboUnusedAttributes;
    }

    private void updateComboUnusedAttributes() {
        List<String> possibleAttributes = new ArrayList<String>();
        for (String attribute : allPossibleAttributes) {
            if (precedentAttributes.contains(attribute)) {
                continue;
            }
            if (consequentAttributes.contains(attribute)) {
                continue;
            }

            possibleAttributes.add(attribute);
        }
        comboUnusedAttributes = new SelectOneDataModel<String>(possibleAttributes);
    }

    /**
     * @param unusedAttributes the unusedAttributes to set
     */
    public void setComboUnusedAttributes(SelectOneDataModel<String> unusedAttributes) {
        this.comboUnusedAttributes = unusedAttributes;
    }

    public String addPrecedentAttribute() {
        final String selectedAttribute = comboUnusedAttributes.getSelecao();
        if (selectedAttribute == null) {
            error("Select an attribute first.");
            return null;
        }
        precedentAttributes.add(selectedAttribute);

        updateFilteredPatternList();
        updateComboUnusedAttributes();
        return null;
    }

    public String addConsequentAttribute() {
        final String selectedAttribute = comboUnusedAttributes.getSelecao();
        if (selectedAttribute == null) {
            error("Select an attribute first.");
            return null;
        }
        consequentAttributes.add(selectedAttribute);

        updateFilteredPatternList();
        updateComboUnusedAttributes();
        return null;
    }

    /**
     * @return the filteredMinedPatterns
     */
    public List<DataMiningPattern> getFilteredMinedPatterns() {
        if (filteredMinedPatterns == null) {
            filteredMinedPatterns = currentDataMiningResult.getDataMiningPatterns();
        }
        return filteredMinedPatterns;
    }

    /**
     * @param filteredMinedPatterns the filteredMinedPatterns to set
     */
    public void setFilteredMinedPatterns(List<DataMiningPattern> filteredMinedPatterns) {
        this.filteredMinedPatterns = filteredMinedPatterns;
    }

    public String updateFilteredPatternList() {
        List<DataMiningPattern> newFilteredPatternList = new LinkedList<DataMiningPattern>();
        for (DataMiningPattern dataMiningPattern : currentDataMiningResult.getDataMiningPatterns()) {
            //attribute verification
            final String precedent = dataMiningPattern.getPrecedent();
            boolean validPattern = true;
            for (String attribute : getPrecedentAttributes()) {
                if (!precedent.contains(attribute)) {
                    validPattern = false;
                }
            }
            final String consequent = dataMiningPattern.getConsequent();
            for (String attribute : getConsequentAttributes()) {
                if (!consequent.contains(attribute)) {
                    validPattern = false;
                }
            }
            
            if(!isConsiderLimitSizes() ){
                if (validPattern) {
                    newFilteredPatternList.add(dataMiningPattern);
                }
                continue;
            }
            
            //size verification
            Integer maxPrecedentSize = comboPrecedentSize.getObjetoSelecionado();
            if (maxPrecedentSize == null) {
                maxPrecedentSize = Integer.MAX_VALUE;
            }
            Integer maxConsequentSize = comboConsequentSize.getObjetoSelecionado();
            if (maxConsequentSize == null) {
                maxConsequentSize = Integer.MAX_VALUE;
            }
            if (dataMiningPattern.getPrecedentSize() > maxPrecedentSize || dataMiningPattern.getConsequentSize() > maxConsequentSize) {
                validPattern = false;
            }

            if (validPattern) {
                newFilteredPatternList.add(dataMiningPattern);
            }
        }

        filteredMinedPatterns = newFilteredPatternList;
        return null;
    }

    public String cleanFilters() {
        initializeFilters();
        updateComboUnusedAttributes();
        updateFilteredPatternList();

        return null;
    }

    private void initializeFilters() {
        precedentAttributes = new ArrayList<String>();
        consequentAttributes = new ArrayList<String>();
        initializeComboPrecedentSize();
        initializeComboConsequentSize();

        filteredMinedPatterns = null;
    }

    /**
     * @return the precedentAttributes
     */
    public List<String> getPrecedentAttributes() {
        return precedentAttributes;
    }

    /**
     * @param precedentAttributes the precedentAttributes to set
     */
    public void setPrecedentAttributes(List<String> precedentAttributes) {
        this.precedentAttributes = precedentAttributes;
    }

    /**
     * @return the consequentAttributes
     */
    public List<String> getConsequentAttributes() {
        return consequentAttributes;
    }

    /**
     * @param consequentAttributes the consequentAttributes to set
     */
    public void setConsequentAttributes(List<String> consequentAttributes) {
        this.consequentAttributes = consequentAttributes;
    }

    /**
     * @return the comboPrecedentSize
     */
    public SelectOneDataModel<Integer> getComboPrecedentSize() {
        if (comboPrecedentSize == null) {
            initializeComboPrecedentSize();
        }
        return comboPrecedentSize;
    }

    /**
     * @param comboPrecedentSize the comboPrecedentSize to set
     */
    public void setComboPrecedentSize(SelectOneDataModel<Integer> comboPrecedentSize) {
        this.comboPrecedentSize = comboPrecedentSize;
    }

    /**
     * @return the comboConsequentSize
     */
    public SelectOneDataModel<Integer> getComboConsequentSize() {
        if (comboConsequentSize == null) {
            initializeComboConsequentSize();
        }
        return comboConsequentSize;
    }

    /**
     * @param comboConsequentSize the comboConsequentSize to set
     */
    public void setComboConsequentSize(SelectOneDataModel<Integer> comboConsequentSize) {
        this.comboConsequentSize = comboConsequentSize;
    }

    public Integer getFilteredMinedPatternsSize() {
        if (filteredMinedPatterns == null) {
            return 0;
        }
        return filteredMinedPatterns.size();
    }

    /**
     * @return the comboAssociationRuleMetric
     */
    public SelectOneDataModel<String> getComboAssociationRuleMetric() {
        if (comboAssociationRuleMetric == null) {
            comboAssociationRuleMetric = SelectOneDataModel.criaComTextoInicialPersonalizado(Arrays.asList(DataMiningControl.getPossibleMetricTypes()), "Select an Association Rules metric");
        }
        return comboAssociationRuleMetric;
    }

    /**
     * @param comboAssociationRuleMetric the comboAssociationRuleMetric to set
     */
    public void setComboAssociationRuleMetric(SelectOneDataModel<String> comboAssociationRuleMetric) {
        this.comboAssociationRuleMetric = comboAssociationRuleMetric;
    }

    public void updateMetricType() {
        final String selectedMetricName = comboAssociationRuleMetric.getSelecao();
        miningControl.setMetricType(selectedMetricName);
        setShowMinARMetric(true);
    }

    /**
     * @return the showMinARMetric
     */
    public boolean isShowMinARMetric() {
        return showMinARMetric;
    }

    /**
     * @param showMinARMetric the showMinARMetric to set
     */
    public void setShowMinARMetric(boolean showMinARMetric) {
        this.showMinARMetric = showMinARMetric;
    }

    private void initializeMetrics() {
        //TODO: change for page input
        metricsToConsider = new ArrayList<Metric>();
        for (MetricManager mm : MetricManagerFactory.getQmoodQualityAttributes()) {
            metricsToConsider.add(mm.getMetric());
        }
    }
    ////////////////////////////////////////////////////////////////////////////
    private Behavior[][] tableBehavior;

    public String showBehaviorTable() {
        if (currentDataMiningResult == null) {
            currentDataMiningResult = (DataMiningResult) tableDataMiningResults.getRowData();

            if (currentDataMiningResult.getId() != null) {
                try {
                    currentDataMiningResult = dataMiningResultService.getToDetailById(currentDataMiningResult.getId());
                } catch (ObjetoNaoEncontradoException ex) {
                    error("Please try again.");
                    return null;
                }
            }
        }

        tableBehavior = behaviorTableService.buildTable(currentDataMiningResult);
        tableBehaviorMetric = currentDataMiningResult.getRuleMetricName();
        return PAGE_BEHAVIOR_TABLE;
    }

    /**
     * @return the tableBehavior
     */
    public Behavior[][] getTableBehavior() {
        return tableBehavior;
    }

    /**
     * @param tableBehavior the tableBehavior to set
     */
    public void setTableBehavior(Behavior[][] tableBehavior) {
        this.tableBehavior = tableBehavior;
    }

    /**
     * @return the tableBehaviorMetric
     */
    public String getTableBehaviorMetric() {
        return tableBehaviorMetric;
    }

    /**
     * @param tableBehaviorMetric the tableBehaviorMetric to set
     */
    public void setTableBehaviorMetric(String tableBehaviorMetric) {
        this.tableBehaviorMetric = tableBehaviorMetric;
    }

    /**
     * @return the ignoreRevisionsThatDontCompile
     */
    public boolean isIgnoreRevisionsThatDontCompile() {
        return ignoreRevisionsThatDontCompile;
    }

    /**
     * @param ignoreRevisionsThatDontCompile the ignoreRevisionsThatDontCompile to set
     */
    public void setIgnoreRevisionsThatDontCompile(boolean ignoreRevisionsThatDontCompile) {
        this.ignoreRevisionsThatDontCompile = ignoreRevisionsThatDontCompile;
    }

    /**
     * @return the considerLimitSizes
     */
    public boolean isConsiderLimitSizes() {
        return considerLimitSizes;
    }

    /**
     * @param considerLimitSizes the considerLimitSizes to set
     */
    public void setConsiderLimitSizes(boolean considerLimitSizes) {
        this.considerLimitSizes = considerLimitSizes;
    }
}
