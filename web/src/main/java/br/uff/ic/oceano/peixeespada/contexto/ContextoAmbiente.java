/*
 * ContextoSpring.java
 *
 * Created on 12 de Outubro de 2007, 17:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.contexto;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.RefactoringService;
import br.uff.ic.oceano.peixeespada.model.Agent;
import br.uff.ic.oceano.peixeespada.model.Knowledge;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import br.uff.ic.oceano.peixeespada.service.AgentService;
import br.uff.ic.oceano.peixeespada.service.KnowledgeService;
import br.uff.ic.oceano.view.DecoratedOrchestratorAgent;
import br.uff.ic.oceano.view.QualityAttributeRefactoring;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kann
 */
public class ContextoAmbiente {

    // list of agent context
    private List<Knowledge> KnowledgeAcumulado = new ArrayList<Knowledge>();
    private List<DecoratedOrchestratorAgent> agentesAtivos = new ArrayList<DecoratedOrchestratorAgent>();
    private List<Refactoring> refactorings = null;
    //maps
    private static Map<QualityAttributeRefactoring, Knowledge> mapaKnowledge = new HashMap<QualityAttributeRefactoring, Knowledge>();
    private static Map<Long, DecoratedOrchestratorAgent> mapOrchestratorAgent = new HashMap<Long, DecoratedOrchestratorAgent>();
    private static Map<String, Refactoring> mapRefactorings = new HashMap<String, Refactoring>();
    //services
    private AgentService agentService = ObjectFactory.getObjectWithDataBaseDependencies(AgentService.class);
    private KnowledgeService knowledgeService = ObjectFactory.getObjectWithDataBaseDependencies(KnowledgeService.class);
    private RefactoringService refactoringService = ObjectFactory.getObjectWithDataBaseDependencies(RefactoringService.class);
    // singleton
    private static ContextoAmbiente interno;

    public void registraAllAtiveAgents() {

//        FacesContext contexto = FacesContext.getCurrentInstance();
//        SessaoDoUsuario sessaoDoUsuario = (SessaoDoUsuario) contexto.getApplication().getVariableResolver().resolveVariable(contexto, "SessaoDoUsuario");
//        List<Agent> agents = agentService.getActiveByOceanoUser(sessaoDoUsuario.getUsuarioCorrente());
        // paliativo, trocar para o método comentado acima quando a abordgem estiver pronta
        List<Agent> agents = agentService.getListaCompleta();
        mapOrchestratorAgent = new HashMap<Long, DecoratedOrchestratorAgent>();
        agentesAtivos = new ArrayList<DecoratedOrchestratorAgent>();
        for (Agent agent : agents) {
            registraAgente(agent);
        }

        List<Refactoring> listRefactoring = refactoringService.getAll();
        mapRefactorings = new HashMap<String, Refactoring>();
        for (Refactoring refactoring : listRefactoring) {
            mapRefactorings.put(refactoring.getName(), refactoring);
        }

        KnowledgeAcumulado = knowledgeService.getListaCompleta();
        mapaKnowledge = new HashMap<QualityAttributeRefactoring, Knowledge>();
        for (Knowledge knowledge : KnowledgeAcumulado) {
            QualityAttributeRefactoring qarp = new QualityAttributeRefactoring();
            qarp.setQualityAtributte(knowledge.getQualityAttribute());
            qarp.setRefactoring(knowledge.getRefactoring());
            mapaKnowledge.put(qarp, knowledge);
        }
    }

    public Knowledge getKnowledge(QualityAttributeRefactoring mt) {
        return mapaKnowledge.get(mt);
    }

    public synchronized void updateKnowledge(QualityAttributeRefactoring mt, Knowledge Knowledge) {
        mapaKnowledge.remove(mt);
        mapaKnowledge.put(mt, Knowledge);
        KnowledgeAcumulado = new ArrayList(mapaKnowledge.values());
    }

    public void registraAgente(Agent agente) {
        alteraRegistroAgentes(true, agente);
    }

    public void registraAgenteTrabalhador(Agent agenteTrabalhador, Long idAgenteOrquestrador) {
        mapOrchestratorAgent.get(idAgenteOrquestrador).registraAgenteTrabalhador(agenteTrabalhador);
    }

    public void desregistraAgenteTrabalhador(Long idAgenteTrabalhador, Long idAgenteOrquestrador) {
        mapOrchestratorAgent.get(idAgenteOrquestrador).desregistraAgenteTrabalhador(idAgenteTrabalhador);
    }

    public void alteraStatusAgenteTrabalhador(Long idAgenteTrabalhador, Long idAgenteOrquestrador, String status) {
        mapOrchestratorAgent.get(idAgenteOrquestrador).alteraStatusAgenteTrabalhador(idAgenteTrabalhador, status);
    }

    public String nextBranch(Long idAgenteTrabalhador, Long idAgenteOrquestrador) {
        return mapOrchestratorAgent.get(idAgenteOrquestrador).getNextBranch(idAgenteTrabalhador);
    }

    public void desregistraAgente(Agent agente) {
        alteraRegistroAgentes(false, agente);
    }

    public synchronized void alteraRegistroAgentes(boolean adiciona, Agent agente) {
        if (adiciona) {
            DecoratedOrchestratorAgent decoratedOrchestratorAgent = new DecoratedOrchestratorAgent(agente);
            mapOrchestratorAgent.put(agente.getIdAgent(), decoratedOrchestratorAgent);
            agentesAtivos.add(decoratedOrchestratorAgent);
        } else {
            agentesAtivos.remove(mapOrchestratorAgent.remove(agente.getIdAgent()));

        }
    }

    public static synchronized ContextoAmbiente getInstance() {
        if (interno == null) {
            interno = new ContextoAmbiente();
        }
        return interno;
    }

    /**
     * @return the KnowledgeAcumulado
     */
    public List<Knowledge> getKnowledgeAcumulado() {
        return KnowledgeAcumulado;
    }

    /**
     * @param KnowledgeAcumulado the KnowledgeAcumulado to set
     */
    public void setKnowledgeAcumulado(List<Knowledge> KnowledgeAcumulado) {
        this.KnowledgeAcumulado = KnowledgeAcumulado;
    }

    public List<DecoratedOrchestratorAgent> getListaAgentes() {
        return agentesAtivos;
    }

    public void updateKnoleged(Agent orchestratorAgent, String refactoring, int improve) throws ServiceException {
        QualityAttributeRefactoring attributeRefactoringProject = new QualityAttributeRefactoring();
        attributeRefactoringProject.setQualityAtributte(orchestratorAgent.getQualityAttribute());
        attributeRefactoringProject.setRefactoring(mapRefactorings.get(refactoring));
        Knowledge knowledge = getKnowledge(attributeRefactoringProject);
        if (knowledge == null) {
            knowledge = new Knowledge();
            knowledge.setQualityAttribute(attributeRefactoringProject.getQualityAtributte());
            knowledge.setRefactoring(attributeRefactoringProject.getRefactoring());
            if (improve == 1) {
                knowledge.setTotalSuccess(1);
            } else if (improve == -1) {
                knowledge.setTotalWorsen(1);
            } else if (improve == 0) {
                knowledge.setTotalNotImproveNorWorsen(1);
            }
            knowledge.setTotalUsed(1);
            knowledgeService.salvar(knowledge);
        } else {
            knowledge = knowledgeService.alterarConhecimento(knowledge, improve);
        }
        updateKnowledge(attributeRefactoringProject, knowledge);

    }

    public void alteraCiclosFailAgenteTrabalhador(long idAgenteTrabalhador, long idAgenteOrquestrador, String refactoring) throws ServiceException {
        DecoratedOrchestratorAgent decoratedOrchestratorAgent = mapOrchestratorAgent.get(idAgenteOrquestrador);
        updateKnoleged(decoratedOrchestratorAgent.getOrchestrator(), refactoring, -1);
        decoratedOrchestratorAgent.alteraCiclosFailAgenteTrabalhador(idAgenteTrabalhador, refactoring);
    }

    public void alteraCiclosNaoMelhorouNemPioroulAgenteTrabalhador(long idAgenteTrabalhador, long idAgenteOrquestrador, String refactoring) throws ServiceException {
        DecoratedOrchestratorAgent decoratedOrchestratorAgent = mapOrchestratorAgent.get(idAgenteOrquestrador);
        updateKnoleged(decoratedOrchestratorAgent.getOrchestrator(), refactoring, 0);
        decoratedOrchestratorAgent.alteraCiclosNaoMelhorouNemPiorouAgenteTrabalhador(idAgenteTrabalhador, refactoring);
    }

    public void alteraCiclosSucessAgenteTrabalhador(long idAgenteTrabalhador, long idAgenteOrquestrador, String refactoring) throws ServiceException {
        DecoratedOrchestratorAgent decoratedOrchestratorAgent = mapOrchestratorAgent.get(idAgenteOrquestrador);
        updateKnoleged(decoratedOrchestratorAgent.getOrchestrator(), refactoring, 1);
        decoratedOrchestratorAgent.alteraCiclosSuccessAgenteTrabalhador(idAgenteTrabalhador, refactoring);
    }

    public String solicitaRefatoracao(long idAgenteTrabalhador, long idAgenteOrquestrador) {
        return mapOrchestratorAgent.get(idAgenteOrquestrador).getRefactoring(idAgenteTrabalhador);
    }

    public List<String> solicitaRefatoracoes(long idAgenteTrabalhador, long idAgenteOrquestrador) {
        return mapOrchestratorAgent.get(idAgenteOrquestrador).getRefactorings(idAgenteTrabalhador);
    }

    /**
     * @return the refactorings
     */
    public List<Refactoring> getRefactorings() {
        if (refactorings == null) {
            refactorings = refactoringService.getAll();
        }
        return refactorings;
    }

    /**
     * @param refactorings the refactorings to set
     */
    public void setRefactorings(List<Refactoring> refactorings) {
        this.refactorings = refactorings;
    }
}
