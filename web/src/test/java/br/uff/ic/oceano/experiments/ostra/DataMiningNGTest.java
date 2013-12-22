/*
 *
 */
package br.uff.ic.oceano.experiments.ostra;

import br.uff.ic.oceano.CppProjectsHelper;
import br.uff.ic.oceano.controller.SessaoDoUsuario;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.transiente.Language;
import br.uff.ic.oceano.core.service.RevisionService;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.ostra.controller.DataMiningBean;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import br.uff.ic.oceano.ostra.service.DataMiningResultService;
import br.uff.ic.oceano.ostra.service.OstraMetricService;
import br.uff.ic.oceano.util.DateUtil;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.util.file.Archive;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.view.SelectableItem;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;

/**
 *
 *
 */
public class DataMiningNGTest extends AbstractNGTest {

    private int maxRules = 1000;
    private double minSupport = 0.01;    
    private int maxBadResults = 2;
    private DataMiningBean dataMiningBean;    
    private SoftwareProject neoPz;

    @BeforeClass
    @Override
    public void beforeClass() {
        try {
            boolean clearOldResults = false;
            if (clearOldResults) {
                cleanOldResults();
            }

            startTimer();
            neoPz = CppProjectsHelper.getDBNeoPZProject();
            
            double percentOfRevisionsToMine = 100.0; //0.25 ~ 10, 1% - 49
            dataMiningBean = createBean(neoPz, percentOfRevisionsToMine);
            
            dataMiningBean.configureDataMining();
            endTimer("Preparing database time cost");
            print(dataMiningBean.getDataBaseSnapshot());
        } catch (Exception ex) {
            fail(ex.getMessage(), ex);
        }
    }
    
    @Test
    public void testNeoPzDataMiningLeverage() throws Exception {
        String assocRuleMetric;
        double minMetric;
        
        assocRuleMetric = "Conviction";
        minMetric = 0.5; //0.5, ..., 1, ...inf (best rules)
        runDatamining(assocRuleMetric, minMetric);
        
        assocRuleMetric = "Lift";
        minMetric = 1.1; // [0, inf)
        runDatamining(assocRuleMetric, minMetric);
                
        assocRuleMetric = "Confidence";
        minMetric = 0.1; // > 1 is the highest (means 100% sure)
        runDatamining(assocRuleMetric, minMetric);
        
        assocRuleMetric = "Leverage";
        minMetric = -0.25;// [−0.25, 0.25]
        runDatamining(assocRuleMetric, minMetric);
    }
    
//------------------------------------------------------------------------------    
    /**
     * ~1 minutes/result
     * @throws Exception 
     */
    //@Test
    public void testNeoPzDataMiningConvictionLoop() throws Exception {
        //chances of the rule is wrong
        //low is better
        String assocRuleMetric = "Conviction";
        //range 0.5, ...1.. inf
        //higher the better 

        DataMiningResult result;
        List<DataMiningResult> mined = new LinkedList<DataMiningResult>();
        //go from low to high, since highest isn't known
        double conviction = 20;
        do {
            //remove bad results
            removeBadResults(mined);

            //Mining                    
            result = runDatamining(assocRuleMetric, conviction);
            
            if(result != null){
                mined.add(result);
            }

            conviction += 1;
            
        } while (result != null && result.getNumberOfMinedPatterns() != 0);
    }

    /**
     * ~15 minutes/result
     * @throws Exception 
     */
    //@Test
    public void testNeoPzDataMiningLiftLoop() throws Exception {
        String assocRuleMetric = "Lift";

        List<DataMiningResult> mined = new LinkedList<DataMiningResult>();
        DataMiningResult result;
        //1.0 means that the rule is invalid. A does not interfere with B
        //go from high to low to avoid memory problems
        double lift = 2.0;
        do {
            //remove bad results
            removeBadResults(mined);

            //Mining                    
            result = runDatamining(assocRuleMetric, lift);
            
            if(result != null){
                mined.add(result);
            }

            lift += 0.1;
        } while (result != null && result.getNumberOfMinedPatterns() != 0);
    }

    /**
     * Takes too long
     * @throws Exception 
     */
    //@Test
    public void testNeoPzDataMiningConfidenceLoop() throws Exception {
        String assocRuleMetric = "Confidence";

        List<DataMiningResult> mined = new LinkedList<DataMiningResult>();
        //Confidence of 100% is the highest
        //range 0-100
        double confidence = 1.0;
        DataMiningResult result;
        do {
            //remove bad results
            removeBadResults(mined);
            
            //Mining                    
            result = runDatamining(assocRuleMetric, confidence);
            
            if(result != null){
                mined.add(result);
            }
            confidence += 0.5;
            
        } while (result != null && result.getNumberOfMinedPatterns() != 0 && confidence <= 100.0);
    }

    /**
     * Leverage metric is takes too long (http://michael.hahsler.net/research/association_rules/measures.html#leverage)
     * @throws Exception 
     */    
    //@Test
    public void testNeoPzDataMiningLeverageLoop() throws Exception {
        String assocRuleMetric = "Leverage";
        //Difference between support and expected support if rule head 
        //and body were independent
        //leverage is a lower bound for support
        List<DataMiningResult> mined = new LinkedList<DataMiningResult>();
        DataMiningResult result;
        double leverage = 1.0;
        do {
            //remove bad results
            removeBadResults(mined);

            //Mining                    
            result = runDatamining(assocRuleMetric, leverage);
            
            if(result != null){
                mined.add(result);
            }
            
            leverage += 1.0;            
        } while (result != null && result.getNumberOfMinedPatterns() != 0 && leverage <= 100.0);
    }

    private ListDataModel getSelectedMetrics() throws ServiceException {
        OstraMetricService ostraMetricService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricService.class);

        List<SelectableItem> metrics = new LinkedList<SelectableItem>();
        List<Metric> ignoredMetrics = getIgnoredMetrics(ostraMetricService);

        List<Metric> allMetrics = ostraMetricService.getAll();
        for (Metric metric : allMetrics) {
            if (ignoredMetrics.contains(metric)) {
                continue;
            }

            if (!metric.isFromProject()) {
                continue;
            }
//            if (metric.isDerived()) {
//                continue;
//            }

            if (!MetricService.isLanguageAvailable(Language.CPP, metric)) {
                continue;
            }

            if (!isAvgValueDiffZero(metric)) {
                continue;
            }

            metrics.add(new SelectableItem(metric, true));
        }

        println("Metrics selected: " + metrics.size());
        for (SelectableItem metric : metrics) {
            println(metric.getItem().toString());
        }

        return new ListDataModel(metrics);
    }

    private DataMiningBean createBean(SoftwareProject project, double percentRevisions) throws ServiceException {
        SessaoDoUsuario sessao = new SessaoDoUsuario();
        DataMiningBean bean = new DataMiningBean(sessao) {
            @Override
            protected void info(String message) {
                overloadedLog("info", message);
            }

            @Override
            protected void warn(String message) {
                overloadedLog("warn", message);
            }

            @Override
            protected void error(String message) {
                overloadedLog("error", message);
            }

            private void overloadedLog(String type, String message) {
                println("Overloaded " + type + ":" + message);
            }
        };

        //Setting project as selected for mining
        List<SelectableItem> projects = new LinkedList<SelectableItem>();
        projects.add(new SelectableItem(project, true));
        ListDataModel list = new ListDataModel(projects);
        bean.setTableSelectProjects(list);

        //defining revisions to be mined
        RevisionService revisionService = ObjectFactory.getObjectWithDataBaseDependencies(RevisionService.class);
        Set<Revision> revisions = revisionService.getByProject(project);
        List<Revision> sorted = new LinkedList<Revision>(revisions);
        Collections.sort(sorted);
        double revCount = sorted.size() * percentRevisions / 100.0;
        int start = (int) (sorted.size() - revCount); //
        int end = sorted.size(); //sublist is exclusive at the end
        println("Revisions mined from " + start + " to " + end + " (" + (end - start) + ")");
        sorted = sorted.subList(start, end);
        project.setRevisions(new HashSet<Revision>(sorted));

        //setting standard parameters for mining
        bean.setCalculateStandardDeviation(true);
        bean.setIgnoreRevisionsThatDontCompile(true); // avoid rule with compile=true
        bean.setTableSelectMetrics(getSelectedMetrics());

        return bean;
    }

    private String toString(DataMiningResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("Data mining result\n");
        sb.append("Time: ").append(result.getFormatedMinedInTime()).append("\n");

        sb.append("Description\n").append(result.getDescription());

        sb.append("result").append("\n");
        List<String> results = result.getResultAsStringList();
        if(results == null){
            sb.append("Null collection").append("\n");
        } else if(results.isEmpty()){
            sb.append("Collection empty").append("\n");
        } else{
            for (String string : results) {
                sb.append(string).append("\n");
            }
        }
        return sb.toString();
    }

    private void print(DataBaseSnapshot baseSnapshot) {

        System.out.println("");
        System.out.println("Instancias");
        System.out.println("_________________________________________________");
        for (String instance : baseSnapshot.getInstances()) {
            System.out.println(instance);
        }

        System.out.println("");
        System.out.println("");
        System.out.println("Attributes");
        System.out.println("_________________________________________________");
        for (String attribute : baseSnapshot.getAttributes()) {
            System.out.println("attribute: " + attribute);
        }
    }

    private void setAssociationRuleMetric(String assRuleMetric, DataMiningBean bean) {
        //1 = Confidence
        //2 = Lift
        //3 = Leverage
        //4 = Conviction
        boolean found = false;
        for (SelectItem selectItem : bean.getComboAssociationRuleMetric().getListaSelecao()) {
            String assRule = selectItem.getLabel();
            if (assRuleMetric.compareTo(assRule) == 0) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Associantion Rule not found: " + assRuleMetric);

        bean.getComboAssociationRuleMetric().setSelecao(assRuleMetric);
        bean.updateMetricType();
    }

    private DataMiningResult runDatamining(String assocRuleMetric, double minMetric) {
        setAssociationRuleMetric(assocRuleMetric, dataMiningBean);
        setMiningControl(minMetric, dataMiningBean);
            
        Output.clearLog();
        Output.println("Mining with " + assocRuleMetric);
        Output.println("Support:" + NumberUtil.format(dataMiningBean.getMiningControl().getMinSup()));
        Output.println("Metric type:" + dataMiningBean.getMiningControl().getMetricType());
        Output.println("Metric value:" + NumberUtil.format(dataMiningBean.getMiningControl().getMinMetric()));

        try {
            //Mining
            startTimer("Mining association rules");
            dataMiningBean.mineAssociationRules();
            endTimer("mineAssociationRules time cost");
        } catch (Exception ex) {
            println(ex.getMessage());
            return null;
        }

        //save to database
        startTimer("Saving datamining");
        dataMiningBean.saveDataMiningResult();
        endTimer("saveDataMiningResult time cost");

        //print result
        DataMiningResult result = dataMiningBean.getCurrentDataMiningResult();
        Output.println(toString(result));
        String fileName = ".\\target\\assocRuleMetric_" + DateUtil.currentFile()+".txt";
        Archive arc = new Archive(fileName);
        arc.openAppendAndClose(Output.getLog());  
        
        return result;
    }

    private void addMetric(List<Metric> ignoredMetrics, OstraMetricService ostraMetricService, String metricName) throws ServiceException {
        try {
            ignoredMetrics.add(ostraMetricService.getMetric(metricName));
            return;
        } catch (ServiceException ex) {
            //not the name of the metric
        }
        //trying acronym
        try {
            ignoredMetrics.add(ostraMetricService.getByAcronym(metricName));
        } catch (ObjetoNaoEncontradoException ex) {
            System.out.println("Not found metric: " + metricName);
//            throw new ServiceException( ex);
        }
    }

    private void removeBadResults(List<DataMiningResult> dataMiningResults) {
        DataMiningResultService dataMiningResultService = ObjectFactory.getObjectWithDataBaseDependencies(DataMiningResultService.class);
        if (dataMiningResults.size() > maxBadResults) {
            DataMiningResult dataMiningResult = dataMiningResults.remove(0); //remove worst
            try {
                dataMiningResultService.delete(dataMiningResult);
            } catch (ObjetoNaoEncontradoException ex) {
                fail(ex.getMessage(), ex);
            }
        }
    }

    private boolean isAvgValueDiffZero(Metric metric) throws ServiceException {
        final MeasurementService ms = ObjectFactory.getObjectWithDataBaseDependencies(MeasurementService.class);
        Double value = ms.getAvgValue(metric, neoPz);
        boolean diffZero = !NumberUtil.isZero(value);
        println("Avg of " + metric.getName() + ": " + NumberUtil.format(value) + " is diff zero: " + diffZero);
        return diffZero;
    }

    private List<Metric> getIgnoredMetrics(OstraMetricService ostraMetricService) throws ServiceException {
        List<Metric> ignoredMetrics = new LinkedList<Metric>();

//        //Calculating        
//        //addMetric(ignoredMetrics, ostraMetricService, "Lines Of Code"); //same as "Lines of Code Total"
//        //addMetric(ignoredMetrics,ostraMetricService,"Lines of Code Total");
//        //addMetric(ignoredMetrics,ostraMetricService,"average component dependency (ACD)");
//        //addMetric(ignoredMetrics,ostraMetricService,"max depth of type inheritance");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of components");        
//        //addMetric(ignoredMetrics,ostraMetricService,"cumulative component dependency (CCD)");
//        //addMetric(ignoredMetrics,ostraMetricService,"normalized cumulative component dependency (NCCD)");
//        //addMetric(ignoredMetrics,ostraMetricService,"cumulative component dependency for balanced binary tree");
//        //addMetric(ignoredMetrics,ostraMetricService,"cumulative component dependency for cyclically dependent graph");
//        //addMetric(ignoredMetrics,ostraMetricService,"max depth of package hierarchy");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing compilation unit dependencies");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing layer dependencies");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing package dependencies");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing subsystem dependencies");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing type dependencies");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of project external compilation units");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of project external packages");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of project external types");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of project internal compilation units");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of project internal packages");
//        //addMetric(ignoredMetrics,ostraMetricService,"number of project internal types");
//        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal layers with a relational cohesion >= 1.0");
//        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal packages with a relational cohesion >= 1.0");
//        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal subsystems with a relational cohesion >= 1.0");
//
//        //Not calculating or not project metric
//        //intersting why not extracting
//        addMetric(ignoredMetrics, ostraMetricService, "number of compilation unit cycles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of compilation unit tangles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of concrete types");
//        addMetric(ignoredMetrics, ostraMetricService, "afferent (incoming) coupling (Ca)");
//        addMetric(ignoredMetrics, ostraMetricService, "afferent (incoming) coupling (Ca) - project external");
//        addMetric(ignoredMetrics, ostraMetricService, "abstractness (A)");
//        addMetric(ignoredMetrics, ostraMetricService, "average usage of assertions per class");
//        addMetric(ignoredMetrics, ostraMetricService, "contains accessible types but no incoming outer package dependencies exist");
//        addMetric(ignoredMetrics, ostraMetricService, "depends upon");
//        addMetric(ignoredMetrics, ostraMetricService, "depth of class inheritance");
//        addMetric(ignoredMetrics, ostraMetricService, "depth of interface inheritance");
//        addMetric(ignoredMetrics, ostraMetricService, "depth of package hierarchy");
//        addMetric(ignoredMetrics, ostraMetricService, "distance (D)");
//        addMetric(ignoredMetrics, ostraMetricService, "efferent (outgoing) coupling (Ce)");
//        addMetric(ignoredMetrics, ostraMetricService, "instability (I)");
//        addMetric(ignoredMetrics, ostraMetricService, "more package external than internal relations exist");
//        addMetric(ignoredMetrics, ostraMetricService, "no dependencies detected");
//        addMetric(ignoredMetrics, ostraMetricService, "no incoming dependencies detected");
//        addMetric(ignoredMetrics, ostraMetricService, "number of abstract types (Na)");
//        addMetric(ignoredMetrics, ostraMetricService, "number of accessible types");
//        addMetric(ignoredMetrics, ostraMetricService, "number of assertions");
//        addMetric(ignoredMetrics, ostraMetricService, "number of children (NOC)");
//        addMetric(ignoredMetrics, ostraMetricService, "number of contained packages");
//        addMetric(ignoredMetrics, ostraMetricService, "number of contained subsystems");
//        addMetric(ignoredMetrics, ostraMetricService, "number of external type relations");
//        addMetric(ignoredMetrics, ostraMetricService, "number of forbidden outgoing compilation unit dependencies");
//        addMetric(ignoredMetrics, ostraMetricService, "number of forbidden outgoing dependencies");
//        addMetric(ignoredMetrics, ostraMetricService, "number of forbidden outgoing package dependencies");
//        addMetric(ignoredMetrics, ostraMetricService, "number of incoming dependencies");
//        addMetric(ignoredMetrics, ostraMetricService, "number of incoming dependencies - project external");
//        addMetric(ignoredMetrics, ostraMetricService, "number of internal type relations");
//        addMetric(ignoredMetrics, ostraMetricService, "number of layer cycles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of layer tangles");
//
//        addMetric(ignoredMetrics, ostraMetricService, "number of not assigned packages");
//        addMetric(ignoredMetrics, ostraMetricService, "number of outgoing dependencies");
//        addMetric(ignoredMetrics, ostraMetricService, "number of outgoing dependencies to project external");
//        addMetric(ignoredMetrics, ostraMetricService, "number of outgoing vertical slice dependencies");
//
//        addMetric(ignoredMetrics, ostraMetricService, "number of package cycles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of package external relations");
//        addMetric(ignoredMetrics, ostraMetricService, "number of package internal relations");
//        addMetric(ignoredMetrics, ostraMetricService, "number of package tangles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of project internal vertical slices");
//        addMetric(ignoredMetrics, ostraMetricService, "number of subsystem cycles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of subsystem tangles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of type cycles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of type tangles");
//        addMetric(ignoredMetrics, ostraMetricService, "number of types (Nc)");
//        addMetric(ignoredMetrics, ostraMetricService, "number of vertical slice tangles");
//        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal vertical slices with a relational cohesion >= 1.0");
//        addMetric(ignoredMetrics, ostraMetricService, "relational cohesion (RC)");
//        addMetric(ignoredMetrics, ostraMetricService, "the most external relations exist with package");
//
//        addMetric(ignoredMetrics, ostraMetricService, "Number of Overridden Methods");
//        addMetric(ignoredMetrics, ostraMetricService, "Abstractness");
//        addMetric(ignoredMetrics, ostraMetricService, "Cyclomatic Complexity");
//        addMetric(ignoredMetrics, ostraMetricService, "Average Number Of Ancestors");
//        addMetric(ignoredMetrics, ostraMetricService, "Class Interface Size");
//        addMetric(ignoredMetrics, ostraMetricService, "Cohesion Among Methods In Class");
//        addMetric(ignoredMetrics, ostraMetricService, "Data Access");
//        addMetric(ignoredMetrics, ostraMetricService, "Direct Class Coupling");
//        //addMetric(ignoredMetrics, ostraMetricService, "Design Size In Classes");
//        addMetric(ignoredMetrics, ostraMetricService, "Lack Of Cohesion Of Methods");
//        addMetric(ignoredMetrics, ostraMetricService, "Measure Of Aggregation");
//        addMetric(ignoredMetrics, ostraMetricService, "Measure Of Functional Abstraction");
//        addMetric(ignoredMetrics, ostraMetricService, "Method Lines Of Code");
//        addMetric(ignoredMetrics, ostraMetricService, "Number Of Attributes");
//        addMetric(ignoredMetrics, ostraMetricService, "Number Of Hierarchies");
//        addMetric(ignoredMetrics, ostraMetricService, "Number of Interfaces");
//        addMetric(ignoredMetrics, ostraMetricService, "Number Of Methods");
//        addMetric(ignoredMetrics, ostraMetricService, "Number Of Polymorphic Methods");
//        addMetric(ignoredMetrics, ostraMetricService, "Number Of Static Methods");
//        addMetric(ignoredMetrics, ostraMetricService, "Number Of Static Attributes");
//        addMetric(ignoredMetrics, ostraMetricService, "Total Cyclomatic Complexity");
//
//        //Derived metrics
//        addMetric(ignoredMetrics, ostraMetricService, "Effectiveness");
//        addMetric(ignoredMetrics, ostraMetricService, "Extendability");
//        addMetric(ignoredMetrics, ostraMetricService, "Flexibility");
//        addMetric(ignoredMetrics, ostraMetricService, "Functionality");
//        addMetric(ignoredMetrics, ostraMetricService, "Reusability");
//        addMetric(ignoredMetrics, ostraMetricService, "Understandability");


        //addMetric(ignoredMetrics, ostraMetricService, "Average Number Of Ancestors");
        //addMetric(ignoredMetrics, ostraMetricService, "AverageComponentDependency");
        //addMetric(ignoredMetrics, ostraMetricService, "CumulativeComponentDependency");
        addMetric(ignoredMetrics, ostraMetricService, "CumulativeComponentDependencyForBalancedBinaryTree");
        addMetric(ignoredMetrics, ostraMetricService, "CumulativeComponentDependencyForCyclicallyDependentGraph");
        //addMetric(ignoredMetrics, ostraMetricService, "Design Size In Classes");
        //addMetric(ignoredMetrics, ostraMetricService, "Lines of Code Total");
        addMetric(ignoredMetrics, ostraMetricService, "MaxDepthOfPackageHierarchy");
        addMetric(ignoredMetrics, ostraMetricService, "MaxDepthOfTypeInheritance");
        addMetric(ignoredMetrics, ostraMetricService, "NormalizedCumulativeComponentDependency");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfComponents");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfForbiddenOutgoingCompilationUnitDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfForbiddenOutgoingPackageDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfForbiddenOutgoingSubsystemDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfForbiddenOutgoingTypeDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfNotAssignedPackages");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfNotImplementedSubsystems");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfOutgoingCompilationUnitDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfOutgoingLayerDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfOutgoingPackageDependencies");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfOutgoingSubsystemDependencies");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfOutgoingTypeDependencies");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectExternalCompilationUnits");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectExternalPackages");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectExternalSubsystems");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectExternalTypes");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectInternalCompilationUnits");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectInternalPackages");
        addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectInternalSubsystems");
        //addMetric(ignoredMetrics, ostraMetricService, "NumberOfProjectInternalTypes");
        addMetric(ignoredMetrics, ostraMetricService, "PercentageOfProjectInternalLayersWithARelationalCohesionGreaterThanOne");
        addMetric(ignoredMetrics, ostraMetricService, "PercentageOfProjectInternalPackagesWithARelationalCohesionGreaterThanOne");
        addMetric(ignoredMetrics, ostraMetricService, "PercentageOfProjectInternalSubsystemsWithARelationalCohesionGreaterThanOne");

        return ignoredMetrics;
    }

    private void cleanOldResults() {
        DataMiningResultService dataMiningResultService = ObjectFactory.getObjectWithDataBaseDependencies(DataMiningResultService.class);

        List<DataMiningResult> tableDataMiningResults = dataMiningResultService.getAll();

        for (DataMiningResult dataMiningResult : tableDataMiningResults) {
            try {
                dataMiningResultService.delete(dataMiningResult);
            } catch (ObjetoNaoEncontradoException ex) {
                fail(ex.getMessage(), ex);
            }
        }
    }

    private void setMiningControl(Double metricValue, DataMiningBean dataMiningBean) {
        dataMiningBean.getMiningControl().setVerboseMode(true);
        dataMiningBean.getMiningControl().setMinSup(minSupport);
        dataMiningBean.getMiningControl().setMinMetric(metricValue);
        dataMiningBean.getMiningControl().setMaxRules(maxRules);
    }
}