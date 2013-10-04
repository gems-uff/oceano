/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.view;

import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.ConfigurationItem;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.util.file.PathUtil;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.peixeespada.model.Knowledge;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import br.uff.ic.oceano.peixeespada.service.AgentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Heliomar
 */
public class DecoratedOrchestratorAgent {

    private String name;
    private Revision revision;
    private List<Revision> dericRevisions;
    private List<Agent> workAgents;
    private Agent orchestrator;
    private Long serialBranch = 0l;
    private Long serialWorkerAgent = 0l;
    private AgentService agentService = ObjectFactory.getObjectWithDataBaseDependencies(AgentService.class);

    public DecoratedOrchestratorAgent(Agent agente) {
        workAgents = new ArrayList<Agent>();
        orchestrator = agente;
    }

    private synchronized Long getNextSerialWorkerAgent() {
        return ++serialWorkerAgent;
    }

    private synchronized void alterCyclesOfOrchestratorAgent(int improve) {
        orchestrator.setCycles(orchestrator.getCycles() + 1);
        if (improve == 1) {
            orchestrator.setSuccessCycles(orchestrator.getSuccessCycles() + 1);
        }else if (improve == -1) {
            orchestrator.setWorsenCycles(orchestrator.getWorsenCycles() + 1);
        }else if (improve == 0) {
            orchestrator.setNotImproveNorWorsenCycles(orchestrator.getNotImproveNorWorsenCycles() + 1);
        }

        try {
            agentService.updateAgent(orchestrator);
        } catch (ServiceException ex) {
            throw new RuntimeException(ex);
        }
    }

    private synchronized Long getNextSerialBranch() {
        return ++serialBranch;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the revision
     */
    public Revision getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Revision revision) {
        this.revision = revision;
    }

    /**
     * @return the dericRevisions
     */
    public List<Revision> getDericRevisions() {
        return dericRevisions;
    }

    /**
     * @param dericRevisions the dericRevisions to set
     */
    public void setDericRevisions(List<Revision> dericRevisions) {
        this.dericRevisions = dericRevisions;
    }

    /**
     * @return the workAgents
     */
    public List<Agent> getWorkAgents() {
        return workAgents;
    }

    /**
     * @param workAgents the workAgents to set
     */
    public void setWorkAgents(List<Agent> workAgents) {
        this.workAgents = workAgents;
    }

    /**
     * @return the orchestrator
     */
    public Agent getOrchestrator() {
        return orchestrator;
    }

    /**
     * @param orchestrator the orchestrator to set
     */
    public void setOrchestrator(Agent orchestrator) {
        this.orchestrator = orchestrator;
    }

    public Agent registraAgenteTrabalhador(Agent agenteTrabalhador) {
        agenteTrabalhador.setIdAgent(getNextSerialWorkerAgent());
        workAgents.add(agenteTrabalhador);
        return agenteTrabalhador;
    }

    public void desregistraAgenteTrabalhador(Long idAgenteTrabalhador) {
        Agent agenteTrabalhador = new Agent();
        agenteTrabalhador.setIdAgent(idAgenteTrabalhador);
        workAgents.remove(agenteTrabalhador);
    }

    public void alteraStatusAgenteTrabalhador(Long idAgenteTrabalhador, String status) {
        for (Agent agenteTrabalhador : workAgents) {
            if (agenteTrabalhador.getIdAgent().equals(idAgenteTrabalhador)) {
                agenteTrabalhador.setStatus(status);
                break;
            }
        }
    }

    public synchronized String getNextBranch(Long idAgenteTrabalhador) {
        Agent agenteTrabalhador = null;
        for (Agent agenteCorrente : workAgents) {
            if (agenteCorrente.getIdAgent().equals(idAgenteTrabalhador)) {
                agenteTrabalhador = agenteCorrente;
                break;
            }
        }

        ConfigurationItem ci = orchestrator.getProject().getConfigurationItem();
        return PathUtil.getWellFormedURL(ci.getBaseUrl(),
                ci.getBranchPath(),
                orchestrator.getQualityAttribute().getName().toLowerCase() + "_" + orchestrator.getIdAgent(),
                "workagent_" + agenteTrabalhador.getIdAgent() + "_" + System.currentTimeMillis());

    }

    public synchronized void alteraCiclosFailAgenteTrabalhador(long idAgenteTrabalhador, String refactoring) {
        alterCyclesOfOrchestratorAgent(-1);
        for (Agent agenteTrabalhador : workAgents) {
            if (agenteTrabalhador.getIdAgent().equals(idAgenteTrabalhador)) {
                agenteTrabalhador.setCycles(agenteTrabalhador.getCycles() + 1);
                agenteTrabalhador.setWorsenCycles(agenteTrabalhador.getWorsenCycles() + 1);
                break;
            }
        }
    }

    public synchronized void alteraCiclosSuccessAgenteTrabalhador(long idAgenteTrabalhador, String refactoring) {
        alterCyclesOfOrchestratorAgent(1);
        for (Agent agenteTrabalhador : workAgents) {
            if (agenteTrabalhador.getIdAgent().equals(idAgenteTrabalhador)) {
                agenteTrabalhador.setCycles(agenteTrabalhador.getCycles() + 1);
                agenteTrabalhador.setSuccessCycles(agenteTrabalhador.getSuccessCycles() + 1);
                break;
            }
        }
    }

    public synchronized void alteraCiclosNaoMelhorouNemPiorouAgenteTrabalhador(long idAgenteTrabalhador, String refactoring) {
        alterCyclesOfOrchestratorAgent(0);
        for (Agent agenteTrabalhador : workAgents) {
            if (agenteTrabalhador.getIdAgent().equals(idAgenteTrabalhador)) {
                agenteTrabalhador.setCycles(agenteTrabalhador.getCycles() + 1);
                agenteTrabalhador.setNotImproveNorWorsenCycles(agenteTrabalhador.getNotImproveNorWorsenCycles() + 1);
                break;
            }
        }
    }

    public String getRefactoring(long idAgenteTrabalhador) {
        return "um_refactoringAindaNaoUtilizado";
    }

    public List<String> getRefactorings(long idAgenteTrabalhador) {

        List<Refactoring> refactorings = ContextoAmbiente.getInstance().getRefactorings();
        List<String> strRefactorings = new ArrayList(refactorings.size());
        int[] sorteio = sorteio(refactorings.size());
        for (int i : sorteio) {
            strRefactorings.add(refactorings.get(sorteio[i]).getName());
        }
        return strRefactorings;
    }

    public int[] sorteio(int size){
        Random random = new Random();
        int[] retorno = new int[size];
        List<Integer> aSerSorteado = new ArrayList(size);
        for (int i = 0; i < retorno.length; i++) {
            aSerSorteado.add(i);
        }
        for (int i = retorno.length; i > 0 ; i--) {
            int r = random.nextInt(i);
            retorno[i-1] = aSerSorteado.remove(r);
        }
        return retorno;
    }
}
