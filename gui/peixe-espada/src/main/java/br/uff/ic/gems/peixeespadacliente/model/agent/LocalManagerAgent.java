package br.uff.ic.gems.peixeespadacliente.model.agent;

import br.uff.ic.gems.peixeespadacliente.action.DoCheckout;
import br.uff.ic.gems.peixeespadacliente.action.DoFinishWork;
import br.uff.ic.gems.peixeespadacliente.action.DoGetMetricsToQualityAttribute;
import br.uff.ic.gems.peixeespadacliente.action.DoInitWork;
import br.uff.ic.gems.peixeespadacliente.action.DoRefactoring;
import br.uff.ic.gems.peixeespadacliente.action.demo.DoCheckoutDemoLocal;
import br.uff.ic.gems.peixeespadacliente.action.demo.DoInitWorkDemoLocal;
import br.uff.ic.gems.peixeespadacliente.action.demo.DoRefactoringDemoLocal;
import br.uff.ic.gems.peixeespadacliente.gui.JDesktopAgent;
import br.uff.ic.gems.peixeespadacliente.model.ProjectVCS;
import br.uff.ic.gems.peixeespadacliente.utils.Archive;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.gems.peixeespadacliente.utils.ProjectUtils;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.MetricQualityAttribute;
import br.uff.ic.oceano.core.model.OceanoUser;
import br.uff.ic.oceano.core.model.ProjectUser;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.core.model.Repository;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class LocalManagerAgent extends PeixeEspadaAgent implements Runnable {

    // constants
    public static final String STATUS_WORKING = Translate.getTranslate().working();
    public static final String STATUS_WAITING_SERVER = Translate.getTranslate().waitingWork();
    public static final String STATUS_SLEEPING = Translate.getTranslate().sleeping();
    // attributes
    private Long identifier;
    private String status;
    private String name;
    private List<String> sequenceRefactorings;
    private Date initDate;
    private Date endDate;
    // dependencies
    private Agent orchestratorAgent;
    private JDesktopAgent output;
    private String baseWorkspace;
    private ProjectVCS projectVCS;
    private ProjectUser projectUser;
    private Double currentQualityAttributeValue = 0d;
    private QualityAttribute firstQualityAttribute = new QualityAttribute();
    private QualityAttribute currentQualityWithoutNormalize = new QualityAttribute();
    private Archive finalRelatory;
    private Archive metricsRelatory;
    // booleans facilitators
    private boolean hasBranch;
    private Boolean improved;
    private boolean testing;
    private int testLevel = -1;
    private boolean running = true;
    private boolean stopped;
    //util
    private Revision revision;
    private boolean finalizeByError = false;
    // agents
    private MeterAgent meterAgent = new MeterAgent();

    public boolean isFinalizeByError() {
        return finalizeByError;
    }

    public ProjectVCS getProjectVCS() {
        return projectVCS;
    }

    public void setProjectVCS(ProjectVCS projectVCS) {
        this.projectVCS = projectVCS;
    }

    private LocalManagerAgent() {
    }

    public static LocalManagerAgent createToTests(ProjectVCS projectVCS) {
        LocalManagerAgent agentPeixeEspada = new LocalManagerAgent();
        agentPeixeEspada.status = Translate.getTranslate().testing();
        agentPeixeEspada.testing = true;
        agentPeixeEspada.projectVCS = projectVCS;

        Repository repository = new Repository();
        repository.setName("svn");

        ConfigurationItem ci = new ConfigurationItem();
        ci.setRepository(repository);
        ci.setName(projectVCS.getName());
        ci.setBranchPath("branches");
        try {
            ci.setBaseUrl(projectVCS.getRepositoryUrl().substring(0, projectVCS.getRepositoryUrl().lastIndexOf("/trunk")));
        } catch (Exception e) {
            ci.setBaseUrl(projectVCS.getRepositoryUrl());
        }

        ProjectUser projectUser = new ProjectUser();
        SoftwareProject softwareProject = new SoftwareProject();
        softwareProject.setRepositoryUrl(projectVCS.getRepositoryUrl());
        softwareProject.setConfigurationItem(ci);
        projectUser.setProject(softwareProject);
        projectUser.setLogin("xyz");
        projectUser.setPassword("xyz");

        agentPeixeEspada.projectUser = projectUser;
        agentPeixeEspada.projectVCS.setProjectUser(projectUser);

        JDesktopAgent.createToTest(agentPeixeEspada);

        return agentPeixeEspada;
    }

    public static LocalManagerAgent create(Date initDate, Date endDate, String OceanoUsername, String OceanoPassword) {
        LocalManagerAgent agentPeixeEspada = new LocalManagerAgent();
        agentPeixeEspada.status = LocalManagerAgent.STATUS_SLEEPING;
        agentPeixeEspada.initDate = initDate;
        agentPeixeEspada.endDate = endDate;
        OceanoUser oceanoUser = new OceanoUser();
        oceanoUser.setLogin(OceanoUsername);
        oceanoUser.setPassword(OceanoPassword);

        ProjectUser projectUser = new ProjectUser();
        projectUser.setOceanoUser(oceanoUser);

        agentPeixeEspada.projectUser = projectUser;

        return agentPeixeEspada;
    }

    public JDesktopAgent prepareOutput() throws PropertyVetoException {
        return JDesktopAgent.create(this);
    }

    public void prepareAgent(Agent orchestratorAgent) {
        this.orchestratorAgent = orchestratorAgent;
        this.projectUser.setProject(orchestratorAgent.getProject());
        projectVCS = new ProjectVCS();
        projectVCS.setName(projectUser.getProject().getConfigurationItem().getName());
        projectVCS.setRepositoryUrl(projectUser.getProject().getRepositoryUrl());
        projectVCS.setProjectUser(projectUser);
    }

    public boolean isTesting() {
        return testing;
    }

    @Override
    public void run() {
        if (testing) {
            planTesting();
        } else {
            plan();
        }
    }

    @Override
    public void planTesting() {
        try {
            new DoInitWorkDemoLocal().execute(this);
            new DoCheckoutDemoLocal().execute(this);
            new DoRefactoringDemoLocal().execute(this);
        } catch (Throwable ex) {
            output.appendMessage(Translate.getTranslate().fatalError());
            printError(ex);
        }
    }

    public void initializeTests() {
        try {
            new DoInitWorkDemoLocal().execute(this);
            new DoCheckoutDemoLocal().execute(this);
        } catch (Throwable ex) {
            output.appendMessage(Translate.getTranslate().fatalError());
            printError(ex);
        }
    }

    @Override
    public void plan() {
        try {
            new DoInitWork().execute(this);
            new DoCheckout().execute(this);
            new DoGetMetricsToQualityAttribute().execute(this);
            meterAgent.calculeteFirstQualityAttributeValue(this);

            // Criar branch e commitar classes
//            if (this.isTesting()) {
//                new DoCommitDemoLocal().execute(agentPeixeEspada);
//            } else {
//                new DoAskOrchestratorAgentAboutBranchAndCommit().createNewBranch(agentPeixeEspada);
//            }

            while (running) {
                new DoRefactoring().execute(this);
            }
        } catch (Throwable ex) {
            output.appendMessage(Translate.getTranslate().fatalError());
            printError(ex);
            finalizeByError = true;
        }
        try {
            new DoFinishWork().execute(this);
        } catch (ServiceException ex1) {
            output.appendMessage(Translate.getTranslate().fatalErrorFinishingWork());
            printError(ex1);
        }
        stopped = true;
    }

    public void printError(Throwable th) {
        Translate translate = Translate.getTranslate();
        output.appendMessage(translate.causedBy(th.getMessage()));
        for (StackTraceElement stackTraceElement : th.getStackTrace()) {
            output.appendMessage(translate.stackItem(stackTraceElement.toString()));
        }
        if (th.getCause() != null) {
            printError(th.getCause());
        }


    }

    public void printTestError(int level, Throwable th) {
        if (this.isTesting() && level <= this.testLevel) {
            this.printError(th);
        }
    }

    public Agent getOrchestratorAgent() {
        return orchestratorAgent;
    }

    public void setOrchestratorAgent(Agent orchestratorAgent) {
        this.orchestratorAgent = orchestratorAgent;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the name
     */
    public String getName() {
        if (name == null) {
            name = Translate.getTranslate().workAgent(identifier);
        }
        return name;
    }

    public JDesktopAgent getOutput() {
        return output;
    }

    public void setOutput(JDesktopAgent output) {
        this.output = output;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public ProjectUser getProjectUser() {
        return projectUser;
    }

    public void setProjectUser(ProjectUser projectUser) {
        this.projectUser = projectUser;
    }

    public boolean hasBranch() {
        return hasBranch;
    }

    /**
     * @return the initDate
     */
    public Date getInitDate() {
        return initDate;
    }

    /**
     * @param initDate the initDate to set
     */
    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the currentQualityAttributeValue
     */
    public Double getCurrentQualityAttributeValue() {
        return currentQualityAttributeValue;
    }

    /**
     * @param currentQualityAttributeValue the currentQualityAttributeValue to
     * set
     */
    public void setCurrentQualityAttributeValue(Double currentQualityAttributeValue) {
        this.currentQualityAttributeValue = currentQualityAttributeValue;
    }

    public Revision getDefaultRevision() throws IOException {
        revision = new Revision();
        revision.setProject(this.getOrchestratorAgent().getProject());
        revision.setLocalPath(projectVCS.getLocalPath());
        revision.setNumber(System.currentTimeMillis());
        revision = ProjectUtils.getRevisionWithAllSourceChanded(revision);
        return revision;
    }

    public void setHasImproved(Boolean hasImproved) {
        this.improved = hasImproved;
    }

    public Boolean hasImproved() {
        return improved;
    }

    public void setHasBranch(boolean hasBranch) {
        this.hasBranch = hasBranch;
    }

    /**
     * @return the sequenceRefactorings
     */
    public List<String> getSequenceRefactorings() {
        return sequenceRefactorings;
    }

    /**
     * @param sequenceRefactorings the sequenceRefactorings to set
     */
    public void setSequenceRefactorings(List<String> sequenceRefactorings) {
        this.sequenceRefactorings = sequenceRefactorings;
    }

    public void requestFinishWork() {
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isStopped() {
        return stopped;
    }

    /**
     * @return the firstQualityAttribute
     */
    public QualityAttribute getFirstQualityAttribute() {
        return firstQualityAttribute;
    }

    /**
     * @return the meterAgent
     */
    public MeterAgent getMeterAgent() {
        return meterAgent;
    }

    /**
     * @param meterAgent the meterAgent to set
     */
    public void setMeterAgent(MeterAgent meterAgent) {
        this.meterAgent = meterAgent;
    }

    /**
     * @return the finalRelatory
     */
    public Archive getFinalRelatory() {
        if (finalRelatory == null) {
            File localProject = new File(projectVCS.getLocalPath());
            File relatory = new File(localProject.getParent(), localProject.getName() + ".txt");
            relatory.delete();
            finalRelatory = new Archive(relatory.getAbsolutePath());
        }
        return finalRelatory;
    }

    public Archive getMetricsRelatory() {
        return metricsRelatory;
    }

    public void appendMetricRelatory() {
        StringBuilder values = new StringBuilder();
        for (MetricQualityAttribute metricQualityAttribute : currentQualityWithoutNormalize.getMetricQualityAttributes()) {
            values.append(metricQualityAttribute.getMetricValue()).append(";");
        }
        values.append(currentQualityWithoutNormalize.getCurrentValue());
        metricsRelatory.openAppendAndClose(values.toString());
        // helping the garbage collector ;-)
        values = null;
    }

    public QualityAttribute getCurrentQualityWithoutNormalize() {
        return currentQualityWithoutNormalize;
    }

    public void createNewMetricsRelatory(String refactoring) {
        File localProject = new File(projectVCS.getLocalPath());
        File relatory = new File(localProject.getParent(), "metrics_" + localProject.getName() + "_" + refactoring + "_" + orchestratorAgent.getQualityAttribute().getName() + ".csv");
        relatory.delete();
        metricsRelatory = new Archive(relatory.getAbsolutePath());
        StringBuilder header = new StringBuilder();
        StringBuilder firstValues = new StringBuilder();
        for (MetricQualityAttribute metricQualityAttribute : currentQualityWithoutNormalize.getMetricQualityAttributes()) {
            header.append(metricQualityAttribute.getMetric().getAcronym()).append(";");
            firstValues.append(metricQualityAttribute.getMetricValue()).append(";");
        }
        header.append(firstQualityAttribute.getName());
        firstValues.append(firstQualityAttribute.getCurrentValue());
        metricsRelatory.openAppendAndClose(header.toString());
        metricsRelatory.openAppendAndClose(firstValues.toString());
        // helping the garbage collector ;-)
        firstValues = null;
        header = null;
    }

    public LocalManagerAgent appendMessage(String text) {
        if (!text.isEmpty()) {
            this.output.appendMessage(text);
        }
        return this;
    }

    public void testMessage(int level, String message) {
        if (level <= this.testLevel) {
            this.output.appendMessage(message);
        }
    }

    public void setTestLevel(int level) {

        this.testLevel = level;
    }

    public String getBaseWorkspace() {
        return baseWorkspace;
    }

    public void setBaseWorkspace(String baseWorkspace) {
        this.baseWorkspace = baseWorkspace;
    }
}
