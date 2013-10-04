package br.uff.ic.gems.peixeespadacliente.service;

import br.uff.ic.gems.peixeespadacliente.context.Constants;
import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricQualityAttribute;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.core.model.Repository;
import br.uff.ic.oceano.core.service.ProtocolService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Heliomar
 */
public class JSONService {

    public static final String CMD_NODE_SCHEDULING = "cmd=Scheduling&";
    public static final String CMD_NODE_SCHEDULING_REQUEST = "cmd=Scheduling_Request&";
    public static final String CMD_NODE_AVAILABLE = "cmd=Avaiable&";
    public static final String CMD_NODE_UNAVAILABLE = "cmd=Unavaiable&";
    public static final String CMD_NODE_USE_REFACTORING = "cmd=Use_refactoring&";
    public static final String CMD_NODE_USE_METRICS = "cmd=Use_metrics&";
    public static final String CMD_NODE_KNOLEGED = "cmd=Use_metrics&";
    public static final String CMD_SUCCESS_REFACTORING = "cmd=Sucess_Refactoring&";
    public static final String CMD_NOT_IMPROVE_NOR_WORSEN_REFACTORING = "cmd=Not_Improve_nor_Worsen Refactoring&";
    public static final String CMD_NEXT_BRANCH_TO_SUCESS_REFACTORING = "cmd=Branch_Sucess_Refactoring&";
    public static final String CMD_FAIL_REFACTORING = "cmd=Fail_Refactoring&";
    public static final String CMD_GET_QUALITY_ATTRIBUTE_WITH_METRICS = "cmd=Get_QualityAttribute_With_Metrics&";
    public static final String CMD_EXCEPTION = "cmd=exception";
    public ProtocolService protocolService = ObjectFactory.getObjectWithoutDataBaseDependencies(ProtocolService.class);

    public String schedulingRequest(String username, String password) throws ServiceException {
        StringBuffer message = new StringBuffer();
        message.append(CMD_NODE_SCHEDULING_REQUEST);
        message.append("username=");
        message.append(username);
        message.append("&");
        message.append("password=");
        message.append(password);
        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public String getNextBranchtoSucessRefactoring(LocalManagerAgent agentPeixeEspada, String refactoring) throws ServiceException {
        StringBuilder message = new StringBuilder();
        message.append(CMD_NEXT_BRANCH_TO_SUCESS_REFACTORING);
        message.append("idWorkAgent=");
        message.append(agentPeixeEspada.getIdentifier());
        message.append("&");
        message.append("refactoring=");
        message.append(refactoring);
        message.append("&");
        message.append("idOrchestratorAgent=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getIdAgent());
        String strMessage = protocolService.getMessageServer(message, Constants.URL_OCEANO);
        JSONObject jSONObject = null;
        try {
            jSONObject = new JSONObject(strMessage);
            return jSONObject.getString("branch");
        } catch (JSONException ex) {
            Logger.getLogger(JSONService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException(ex);
        }

    }

    public String agentAvaiable(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        StringBuffer message = new StringBuffer();
        message.append(CMD_NODE_AVAILABLE);
        message.append("statusAgent=");
        message.append(LocalManagerAgent.STATUS_WAITING_SERVER);
        message.append("&");
        message.append("idWorkAgent=");
        message.append(agentPeixeEspada.getIdentifier());
        message.append("&");
        message.append("idOrchestratorAgent=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getIdAgent());
        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public String agentWorking(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        StringBuffer message = new StringBuffer();
        message.append(CMD_NODE_AVAILABLE);
        message.append("statusAgent=");
        message.append(LocalManagerAgent.STATUS_WORKING);
        message.append("&");
        message.append("idWorkAgent=");
        message.append(agentPeixeEspada.getIdentifier());
        message.append("&");
        message.append("idOrchestratorAgent=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getIdAgent());
        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public String agentUnavaiable(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        StringBuilder message = new StringBuilder();
        message.append(CMD_NODE_UNAVAILABLE);
        message.append("idWorkAgent=");
        message.append(agentPeixeEspada.getIdentifier());
        message.append("&");
        message.append("idOrchestratorAgent=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getIdAgent());
        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public List<Agent> getListOrchestratorAgents(LocalManagerAgent agentPeixeEspada) throws ServiceException {

        String message = schedulingRequest(agentPeixeEspada.getProjectUser().getOceanoUser().getLogin(), agentPeixeEspada.getProjectUser().getOceanoUser().getPassword());

        JSONArray jSONArray = null;
        JSONObject jSONObjectMain = null;
        try {
            jSONObjectMain = new JSONObject(message);
            jSONArray = new JSONArray(jSONObjectMain.getString("orchestratorAgents"));
            agentPeixeEspada.getProjectUser().getOceanoUser().setId(jSONObjectMain.getLong("idOceanUser"));
        } catch (JSONException ex) {
            throw new ServiceException(ex);
        }

        int lenght = jSONArray.length();
        List<Agent> agents = new ArrayList<Agent>(lenght);

        try {
            for (int i = 0; i < lenght; i++) {
                Agent agent = new Agent();
                JSONObject jSONObject = jSONArray.getJSONObject(i);

                agent.setIdAgent(jSONObject.getLong("idAgent"));

                QualityAttribute attribute = new QualityAttribute();
                attribute.setName(jSONObject.getString("role"));
                attribute.setId(jSONObject.getLong("idQualityAttribute"));
                agent.setQualityAttribute(attribute);

                Repository repository = new Repository();
                repository.setName(jSONObject.getString("nameRepository"));

                ConfigurationItem configurationItem = new ConfigurationItem();
                configurationItem.setName(jSONObject.getString("project"));
                configurationItem.setRepository(repository);

                SoftwareProject project = new SoftwareProject();
                project.setConfigurationItem(configurationItem);
                project.setRepositoryUrl(jSONObject.getString("urlProject"));
                project.setId(jSONObject.getLong("idProject"));
                project.setMavenProject(jSONObject.getBoolean("mavenProject"));
                agent.setProject(project);

                agents.add(agent);
            }
        } catch (JSONException ex) {
            throw new ServiceException("Error while parting the JSON data [" + ex.getMessage() + "]");
        }
        return agents;
    }

    public LocalManagerAgent getWorkAgentScheduled(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        JSONObject jSONObject = null;
        String message = scheduling(agentPeixeEspada);

        try {
            jSONObject = new JSONObject(message);
        } catch (JSONException ex) {
            throw new ServiceException(ex);
        }

        try {
            agentPeixeEspada.setIdentifier(jSONObject.getLong("idWorkAgent"));
            agentPeixeEspada.getProjectUser().setId(jSONObject.getLong("idProjectUser"));
            agentPeixeEspada.getProjectUser().setPassword(jSONObject.getString("passwordVCS"));
            agentPeixeEspada.getProjectUser().setLogin(jSONObject.getString("usernameVCS"));
        } catch (JSONException ex) {
            throw new ServiceException("Error while parting the JSON data [" + ex.getMessage() + "]");
        }
        return agentPeixeEspada;
    }

    private String scheduling(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        StringBuffer message = new StringBuffer();
        message.append(CMD_NODE_SCHEDULING);
        message.append("dateInitial=");
        message.append(sdf.format(agentPeixeEspada.getInitDate()));
        message.append("&");
        message.append("dateEnd=");
        message.append(sdf.format(agentPeixeEspada.getEndDate()));
        message.append("&");
        message.append("statusAgent=");
        message.append(LocalManagerAgent.STATUS_SLEEPING);
        message.append("&");
        message.append("idOrchestratorAgent=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getIdAgent());
        message.append("&");
        message.append("idProject=");
        message.append(agentPeixeEspada.getProjectUser().getProject().getId());
        message.append("&");
        message.append("idOceanUser=");
        message.append(agentPeixeEspada.getProjectUser().getOceanoUser().getId());
        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public String sendMessageAboutRefactoring(LocalManagerAgent agentPeixeEspada, String refactoring, String statusRefactoring) throws ServiceException {
        StringBuilder message = new StringBuilder();
        message.append(statusRefactoring);
        message.append("idWorkAgent=");
        message.append(agentPeixeEspada.getIdentifier());
        message.append("&");
        message.append("refactoring=");
        message.append(refactoring);
        message.append("&");
        message.append("idOrchestratorAgent=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getIdAgent());
        return protocolService.getMessageServer(message, Constants.URL_OCEANO);
    }

    public QualityAttribute getQualityAttributeWithMetricsAndFactors(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        StringBuilder message = new StringBuilder();
        message.append(CMD_GET_QUALITY_ATTRIBUTE_WITH_METRICS);
        message.append("idQualityAttribute=");
        message.append(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute().getId());
        String response = protocolService.getMessageServer(message, Constants.URL_OCEANO);


        JSONArray jSONArray = null;
        try {
            jSONArray = new JSONArray(response);
        } catch (JSONException ex) {
            throw new ServiceException(ex);
        }

        QualityAttribute qualityAttribute = agentPeixeEspada.getOrchestratorAgent().getQualityAttribute();
        int lenght = jSONArray.length();
        List<MetricQualityAttribute> metricsFactors = new ArrayList<MetricQualityAttribute>(lenght);

        try {
            for (int i = 0; i < lenght; i++) {
                MetricQualityAttribute metricFactor = new MetricQualityAttribute();
                JSONObject jSONObject = jSONArray.getJSONObject(i);

                Metric metric = new Metric();
                metric.setAcronym(jSONObject.getString("acronym"));
                metric.setDescription(jSONObject.getString("description"));
                metric.setExtractsFromFont(jSONObject.getBoolean("extractsFromFont"));
                metric.setExtratcsFrom(jSONObject.getInt("extratcsFrom"));
                //metric.setMetricManagerClass(jSONObject.getString("metricManagerClass"));
                metric.setName(jSONObject.getString("metricName"));
                metric.setPreRelease(jSONObject.getBoolean("preRelease"));
                metric.setType(jSONObject.getInt("type"));

                metricFactor.setMetric(metric);
                metricFactor.setFactor((float) jSONObject.getDouble("factor"));

                metricsFactors.add(metricFactor);
            }
            qualityAttribute.setMetricQualityAttributes(metricsFactors);
        } catch (JSONException ex) {
            throw new ServiceException("Error while parting the JSON data [" + ex.getMessage() + "]");
        }

        return qualityAttribute;
    }

    List<String> getRefactorings(String response) throws ServiceException {
        JSONArray jSONArray = null;
        try {
            jSONArray = new JSONArray(response);
        } catch (JSONException ex) {
            throw new ServiceException(ex);
        }

        int length = jSONArray.length();
        List<String> refactorings = new ArrayList<String>(length);
        try {
            for (int i = 0; i < length; i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                refactorings.add(jSONObject.getString("refactoringName"));
            }
        } catch (JSONException ex) {
            throw new ServiceException(ex);
        }
        return refactorings;

    }
}
