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
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import br.uff.ic.oceano.ostra.controller.DataMiningBean;
import br.uff.ic.oceano.ostra.model.DataBaseSnapshot;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import br.uff.ic.oceano.ostra.service.DataMiningResultService;
import br.uff.ic.oceano.ostra.service.OstraMetricService;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.view.SelectableItem;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private int maxRules = 100;
    private double minSupport = 0.01;
    private boolean clearOldResults = true;
    private int maxBadResults = 2;
    private DataMiningBean dataMiningBean;
    private double percentOfRevisionsToMine = 5.0;
    private SoftwareProject neoPz;

    @BeforeClass
    @Override
    public void beforeClass() {
        try {
            if (!clearOldResults) {
                return;
            }

            DataMiningResultService dataMiningResultService = ObjectFactory.getObjectWithDataBaseDependencies(DataMiningResultService.class);

            List<DataMiningResult> tableDataMiningResults = dataMiningResultService.getAll();

            for (DataMiningResult dataMiningResult : tableDataMiningResults) {
                try {
                    dataMiningResultService.delete(dataMiningResult);
                } catch (ObjetoNaoEncontradoException ex) {
                    fail(ex.getMessage(), ex);
                }
            }

            startTimer();
            neoPz = CppProjectsHelper.getDBNeoPZProject();
            dataMiningBean = createBean(neoPz, percentOfRevisionsToMine);
            dataMiningBean.configureDataMining();
            endTimer("Preparing database time cost");
            print(dataMiningBean.getDataBaseSnapshot());
        } catch (Exception ex) {
            fail(ex.getMessage(),ex);
        }
    }

//    @Test
//    public void testNeoPzDataMiningConviction() throws Exception {
//        String assocRuleMetric = "Conviction";
//        
//        //setup mining        
//        setAssociationRuleMetric(assocRuleMetric, bean);
//        bean.getMiningControl().setVerboseMode(true);
//        bean.getMiningControl().setMinSup(minSupport);
//        bean.getMiningControl().setMinMetric(1.0); //Conviction
//        bean.getMiningControl().setMaxRules(maxRules);
//
//        runDatamining(assocRuleMetric, bean);
//    }
//    
//    @Test
//    public void testNeoPzDataMiningLift() throws Exception {
//        String assocRuleMetric = "Lift";        
//        //setup mining        
//        setAssociationRuleMetric(assocRuleMetric, bean);
//        bean.getMiningControl().setVerboseMode(true);
//        bean.getMiningControl().setMinSup(minSupport);
//        bean.getMiningControl().setMinMetric(1.0); //Lift
//        bean.getMiningControl().setMaxRules(maxRules);
//        //bean.getMiningControl().setMaxSup(1.0);
//
//        runDatamining(assocRuleMetric, bean);
//    }
//    @Test
//    public void testNeoPzDataMiningConfidence() throws Exception {
//        String assocRuleMetric = "Confidence";
//
//        SoftwareProject neoPz = CppProjectsHelper.getDBNeoPZProject();
//        DataMiningBean bean = createBean(neoPz, 100.0);
//
//        //setup mining        
//        setAssociationRuleMetric(assocRuleMetric, bean);
//        bean.getMiningControl().setVerboseMode(true);
//        bean.getMiningControl().setMinSup(minSupport);
//        bean.getMiningControl().setMinMetric(0.8); //confidence
//        bean.getMiningControl().setMaxRules(maxRules);
//        //bean.getMiningControl().setMaxSup(1.0);
//
//        runDatamining(assocRuleMetric, bean);
//    }
//    @Test
//    public void testNeoPzDataMiningLeverage() throws Exception {
//        String assocRuleMetric = "Leverage";
//
//        SoftwareProject neoPz = CppProjectsHelper.getDBNeoPZProject();
//        DataMiningBean bean = createBean(neoPz, 100.0);
//
//        //setup mining        
//        setAssociationRuleMetric(assocRuleMetric, bean);
//        bean.getMiningControl().setVerboseMode(true);
//        bean.getMiningControl().setMinSup(minSupport);
//        bean.getMiningControl().setMinMetric(0.8); //confidence
//        bean.getMiningControl().setMaxRules(maxRules);
//        //bean.getMiningControl().setMaxSup(1.0);
//
//        runDatamining(assocRuleMetric, bean);
//    }
    @Test
    public void testNeoPzDataMiningConvictionLoop() throws Exception {
        //chances of the rule is wrong
        //low is better
        String assocRuleMetric = "Conviction";
        //range 0.5, ...1.. inf
        //higher the better 

        DataMiningResult result;
        List<DataMiningBean> mined = new LinkedList<DataMiningBean>();
        //go from high to low to avoid memory problems
        //Start from 0.5% chance of being wrong
        double conviction = 10;
        do {
            //remove bad results
            removeBadResults(mined);

            //setup mining        
            setAssociationRuleMetric(assocRuleMetric, dataMiningBean);
            dataMiningBean.getMiningControl().setVerboseMode(true);
            dataMiningBean.getMiningControl().setMinSup(minSupport);
            dataMiningBean.getMiningControl().setMinMetric(conviction); //Conviction
            dataMiningBean.getMiningControl().setMaxRules(maxRules);
            result = runDatamining(assocRuleMetric, dataMiningBean);

            conviction -= 0.05;
            mined.add(dataMiningBean);
        } while (result.getNumberOfMinedPatterns() == 0 || conviction > 1);
    }

    @Test
    public void testNeoPzDataMiningLiftLoop() throws Exception {
        String assocRuleMetric = "Lift";

        List<DataMiningBean> mined = new LinkedList<DataMiningBean>();
        DataMiningResult result;
        //1.0 means that the rule is invalid. A does not interfere with B
        //go from high to low to avoid memory problems
        double lift = 7.0;
        do {
            //remove bad results
            removeBadResults(mined);

            //setup mining        
            setAssociationRuleMetric(assocRuleMetric, dataMiningBean);
            dataMiningBean.getMiningControl().setVerboseMode(true);
            dataMiningBean.getMiningControl().setMinSup(minSupport);
            dataMiningBean.getMiningControl().setMinMetric(lift); //Lift
            dataMiningBean.getMiningControl().setMaxRules(maxRules);
            //bean.getMiningControl().setMaxSup(1.0);

            result = runDatamining(assocRuleMetric, dataMiningBean);
            lift -= 0.1;
            mined.add(dataMiningBean);
        } while (result.getNumberOfMinedPatterns() == 0 || lift > 1.0);
    }

    @Test
    public void testNeoPzDataMiningConfidenceLoop() throws Exception {
        String assocRuleMetric = "Confidence";

        List<DataMiningBean> mined = new LinkedList<DataMiningBean>();
        //Confidence of 100% is the highest
        //range 0-100
        double confidence = 100.0;
        DataMiningResult result;
        do {
            //remove bad results
            removeBadResults(mined);

            //setup mining        
            setAssociationRuleMetric(assocRuleMetric, dataMiningBean);
            dataMiningBean.getMiningControl().setVerboseMode(true);
            dataMiningBean.getMiningControl().setMinSup(minSupport);
            dataMiningBean.getMiningControl().setMinMetric(confidence); //confidence
            dataMiningBean.getMiningControl().setMaxRules(maxRules);
            //bean.getMiningControl().setMaxSup(1.0);

            result = runDatamining(assocRuleMetric, dataMiningBean);
            confidence -= 1.0;
            mined.add(dataMiningBean);
        } while (result.getNumberOfMinedPatterns() == 0 || confidence > 5);
    }

    @Test
    public void testNeoPzDataMiningLeverageLoop() throws Exception {
        String assocRuleMetric = "Leverage";
        //Difference between support and expected support if rule head 
        //and body were independent
        //leverage is a lower bound for support
        List<DataMiningBean> mined = new LinkedList<DataMiningBean>();
        DataMiningResult result;
        double leverage = 100.0;
        do {
            //remove bad results
            removeBadResults(mined);

            //setup mining        
            setAssociationRuleMetric(assocRuleMetric, dataMiningBean);
            dataMiningBean.getMiningControl().setVerboseMode(true);
            dataMiningBean.getMiningControl().setMinSup(minSupport);
            dataMiningBean.getMiningControl().setMinMetric(leverage); //leverage
            dataMiningBean.getMiningControl().setMaxRules(maxRules);
            //bean.getMiningControl().setMaxSup(1.0);

            result = runDatamining(assocRuleMetric, dataMiningBean);
            leverage -= 1.0;
            mined.add(dataMiningBean);
        } while (result.getNumberOfMinedPatterns() == 0 || leverage > 0);
    }

    private ListDataModel getSelectedMetrics() throws ServiceException {
        OstraMetricService ostraMetricService = ObjectFactory.getObjectWithDataBaseDependencies(OstraMetricService.class);
        List<SelectableItem> metrics = new LinkedList<SelectableItem>();
        List<Metric> allMetrics = ostraMetricService.getAll();
//        println("Metrics available");
//        for (Metric metric : allMetrics) {
//            println(metric.toString());
//        }

        List<Metric> ignoredMetrics = new LinkedList<Metric>();

        //Calculating        
        //addMetric(ignoredMetrics, ostraMetricService, "Lines Of Code"); //same as "Lines of Code Total"
        //addMetric(ignoredMetrics,ostraMetricService,"Lines of Code Total");
        //addMetric(ignoredMetrics,ostraMetricService,"average component dependency (ACD)");
        //addMetric(ignoredMetrics,ostraMetricService,"max depth of type inheritance");
        //addMetric(ignoredMetrics,ostraMetricService,"number of components");        
        //addMetric(ignoredMetrics,ostraMetricService,"cumulative component dependency (CCD)");
        //addMetric(ignoredMetrics,ostraMetricService,"normalized cumulative component dependency (NCCD)");
        //addMetric(ignoredMetrics,ostraMetricService,"cumulative component dependency for balanced binary tree");
        //addMetric(ignoredMetrics,ostraMetricService,"cumulative component dependency for cyclically dependent graph");
        //addMetric(ignoredMetrics,ostraMetricService,"max depth of package hierarchy");
        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing compilation unit dependencies");
        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing layer dependencies");
        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing package dependencies");
        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing subsystem dependencies");
        //addMetric(ignoredMetrics,ostraMetricService,"number of outgoing type dependencies");
        //addMetric(ignoredMetrics,ostraMetricService,"number of project external compilation units");
        //addMetric(ignoredMetrics,ostraMetricService,"number of project external packages");
        //addMetric(ignoredMetrics,ostraMetricService,"number of project external types");
        //addMetric(ignoredMetrics,ostraMetricService,"number of project internal compilation units");
        //addMetric(ignoredMetrics,ostraMetricService,"number of project internal packages");
        //addMetric(ignoredMetrics,ostraMetricService,"number of project internal types");
        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal layers with a relational cohesion >= 1.0");
        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal packages with a relational cohesion >= 1.0");
        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal subsystems with a relational cohesion >= 1.0");

        //Not calculating or not project metric
        //intersting why not extracting
        addMetric(ignoredMetrics, ostraMetricService, "number of compilation unit cycles");
        addMetric(ignoredMetrics, ostraMetricService, "number of compilation unit tangles");
        addMetric(ignoredMetrics, ostraMetricService, "number of concrete types");
        addMetric(ignoredMetrics, ostraMetricService, "afferent (incoming) coupling (Ca)");
        addMetric(ignoredMetrics, ostraMetricService, "afferent (incoming) coupling (Ca) - project external");
        addMetric(ignoredMetrics, ostraMetricService, "abstractness (A)");
        addMetric(ignoredMetrics, ostraMetricService, "average usage of assertions per class");
        addMetric(ignoredMetrics, ostraMetricService, "contains accessible types but no incoming outer package dependencies exist");
        addMetric(ignoredMetrics, ostraMetricService, "depends upon");
        addMetric(ignoredMetrics, ostraMetricService, "depth of class inheritance");
        addMetric(ignoredMetrics, ostraMetricService, "depth of interface inheritance");
        addMetric(ignoredMetrics, ostraMetricService, "depth of package hierarchy");
        addMetric(ignoredMetrics, ostraMetricService, "distance (D)");
        addMetric(ignoredMetrics, ostraMetricService, "efferent (outgoing) coupling (Ce)");
        addMetric(ignoredMetrics, ostraMetricService, "instability (I)");
        addMetric(ignoredMetrics, ostraMetricService, "more package external than internal relations exist");
        addMetric(ignoredMetrics, ostraMetricService, "no dependencies detected");
        addMetric(ignoredMetrics, ostraMetricService, "no incoming dependencies detected");
        addMetric(ignoredMetrics, ostraMetricService, "number of abstract types (Na)");
        addMetric(ignoredMetrics, ostraMetricService, "number of accessible types");
        addMetric(ignoredMetrics, ostraMetricService, "number of assertions");
        addMetric(ignoredMetrics, ostraMetricService, "number of children (NOC)");
        addMetric(ignoredMetrics, ostraMetricService, "number of contained packages");
        addMetric(ignoredMetrics, ostraMetricService, "number of contained subsystems");
        addMetric(ignoredMetrics, ostraMetricService, "number of external type relations");
        addMetric(ignoredMetrics, ostraMetricService, "number of forbidden outgoing compilation unit dependencies");
        addMetric(ignoredMetrics, ostraMetricService, "number of forbidden outgoing dependencies");
        addMetric(ignoredMetrics, ostraMetricService, "number of forbidden outgoing package dependencies");
        addMetric(ignoredMetrics, ostraMetricService, "number of incoming dependencies");
        addMetric(ignoredMetrics, ostraMetricService, "number of incoming dependencies - project external");
        addMetric(ignoredMetrics, ostraMetricService, "number of internal type relations");
        addMetric(ignoredMetrics, ostraMetricService, "number of layer cycles");
        addMetric(ignoredMetrics, ostraMetricService, "number of layer tangles");

        addMetric(ignoredMetrics, ostraMetricService, "number of not assigned packages");
        addMetric(ignoredMetrics, ostraMetricService, "number of outgoing dependencies");
        addMetric(ignoredMetrics, ostraMetricService, "number of outgoing dependencies to project external");
        addMetric(ignoredMetrics, ostraMetricService, "number of outgoing vertical slice dependencies");

        addMetric(ignoredMetrics, ostraMetricService, "number of package cycles");
        addMetric(ignoredMetrics, ostraMetricService, "number of package external relations");
        addMetric(ignoredMetrics, ostraMetricService, "number of package internal relations");
        addMetric(ignoredMetrics, ostraMetricService, "number of package tangles");
        addMetric(ignoredMetrics, ostraMetricService, "number of project internal vertical slices");
        addMetric(ignoredMetrics, ostraMetricService, "number of subsystem cycles");
        addMetric(ignoredMetrics, ostraMetricService, "number of subsystem tangles");
        addMetric(ignoredMetrics, ostraMetricService, "number of type cycles");
        addMetric(ignoredMetrics, ostraMetricService, "number of type tangles");
        addMetric(ignoredMetrics, ostraMetricService, "number of types (Nc)");
        addMetric(ignoredMetrics, ostraMetricService, "number of vertical slice tangles");
        addMetric(ignoredMetrics, ostraMetricService, "percentage of project internal vertical slices with a relational cohesion >= 1.0");
        addMetric(ignoredMetrics, ostraMetricService, "relational cohesion (RC)");
        addMetric(ignoredMetrics, ostraMetricService, "the most external relations exist with package");

        addMetric(ignoredMetrics, ostraMetricService, "Number of Overridden Methods");
        addMetric(ignoredMetrics, ostraMetricService, "Abstractness");
        addMetric(ignoredMetrics, ostraMetricService, "Cyclomatic Complexity");
        addMetric(ignoredMetrics, ostraMetricService, "Average Number Of Ancestors");
        addMetric(ignoredMetrics, ostraMetricService, "Class Interface Size");
        addMetric(ignoredMetrics, ostraMetricService, "Cohesion Among Methods In Class");
        addMetric(ignoredMetrics, ostraMetricService, "Data Access");
        addMetric(ignoredMetrics, ostraMetricService, "Direct Class Coupling");
        //addMetric(ignoredMetrics, ostraMetricService, "Design Size In Classes");
        addMetric(ignoredMetrics, ostraMetricService, "Lack Of Cohesion Of Methods");
        addMetric(ignoredMetrics, ostraMetricService, "Measure Of Aggregation");
        addMetric(ignoredMetrics, ostraMetricService, "Measure Of Functional Abstraction");
        addMetric(ignoredMetrics, ostraMetricService, "Method Lines Of Code");
        addMetric(ignoredMetrics, ostraMetricService, "Number Of Attributes");
        addMetric(ignoredMetrics, ostraMetricService, "Number Of Hierarchies");
        addMetric(ignoredMetrics, ostraMetricService, "Number of Interfaces");
        addMetric(ignoredMetrics, ostraMetricService, "Number Of Methods");
        addMetric(ignoredMetrics, ostraMetricService, "Number Of Polymorphic Methods");
        addMetric(ignoredMetrics, ostraMetricService, "Number Of Static Methods");
        addMetric(ignoredMetrics, ostraMetricService, "Number Of Static Attributes");
        addMetric(ignoredMetrics, ostraMetricService, "Total Cyclomatic Complexity");

        //Derived metrics
        addMetric(ignoredMetrics, ostraMetricService, "Effectiveness");
        addMetric(ignoredMetrics, ostraMetricService, "Extendability");
        addMetric(ignoredMetrics, ostraMetricService, "Flexibility");
        addMetric(ignoredMetrics, ostraMetricService, "Functionality");
        addMetric(ignoredMetrics, ostraMetricService, "Reusability");
        addMetric(ignoredMetrics, ostraMetricService, "Understandability");

        //Total 102 metrics
        for (Metric metric : allMetrics) {
//            if (ignoredMetrics.contains(metric)) {
//                continue;
//            }

//            if (!metric.isFromProject()) {
//                continue;
//            }
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

    private void print(DataMiningResult result) {
        println("Data mining result");
        println("Time: " + result.getFormatedMinedInTime());

        println("Description");
        println(result.getDescription());

        toOutput("result", result.getResultAsStringList());
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

    private DataMiningResult runDatamining(String assocRuleMetric, DataMiningBean bean) {
        println("Mining with " + assocRuleMetric);

//        startTimer();        
//        DataMiningControl mc = bean.getMiningControl(); //configureDataMining() resets DataMiningControl instance
//        bean.configureDataMining();
//        bean.setMiningControl(mc);
//        endTimer("configureDataMining time cost");
//        print(bean.getDataBaseSnapshot());


        //Mining
        startTimer("Mining association rules");
        bean.mineAssociationRules();
        endTimer("mineAssociationRules time cost");

        //save to database
        startTimer("Saving datamining");
        bean.saveDataMiningResult();
        endTimer("saveDataMiningResult time cost");

        //Save result to file
        startTimer();
        toDatedTxtFile(".\\target\\miningResult-" + assocRuleMetric, bean.getDataBaseAsArff());
        endTimer("Save datamining time cost");

        //print result
        DataMiningResult result = bean.getCurrentDataMiningResult();
        print(result);

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

    private void removeBadResults(List<DataMiningBean> mined) {
        if (mined.size() >= maxBadResults) {
            DataMiningBean beanTemp = mined.remove(0); //remove worst
            beanTemp.deleteDataMiningResult();
        }
    }
    
    private boolean isAvgValueDiffZero(Metric metric) throws ServiceException {        
        final MeasurementService ms = ObjectFactory.getObjectWithDataBaseDependencies(MeasurementService.class);
        Double value = ms.getAvgValue(metric, neoPz);
        println("Avg of "+ metric.getName() + ": " + NumberUtil.format(value));
        return !NumberUtil.isZero(value);
    }
}