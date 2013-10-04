///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package br.uff.ic.oceano.aspectos;
//
//import br.uff.ic.oceano.asf.agente.ConstantesAplicacao.ConstantesAplicacao;
//import br.uff.ic.oceano.asf.ambiente.Ambiente;
//import br.uff.ic.oceano.asf.fabrica.FabricaAgente;
//import br.uff.ic.oceano.asf.papel.Desempenho;
//import br.uff.ic.oceano.asf.papel.Manutencao;
//import br.uff.ic.oceano.asf.papel.Papel;
//import br.uff.ic.oceano.asf.servico.SetupASF;
//import br.uff.ic.oceano.factory.ObjectFactory;
//import br.uff.ic.oceano.peixeespada.model.Transformacao;
//import br.uff.ic.oceano.model.transiente.Agente;
//import br.uff.ic.oceano.service.impl.TransformacaoService;
//import br.uff.ic.oceano.util.GerenciadorFluxos;
//import br.uff.ic.oceano.util.ThreadScheduling;
//import framework.agent.Agent;
//import framework.mentalState.Message;
//import framework.mentalState.belief.Belief;
//import framework.mentalState.belief.LeafBelief;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
///**
// *
// * @author Heliomar
// */
//public class Teste {
//
//    Agente agenteTransisnte = null;
//    public void main(String[] args) {
//
////        Signature sig = thisJoinPoint.getSignature();
////        Method metodo = ((MethodSignature)sig).getMethod();
////        if(metodo.getAnnotation(ContextoASF.class).registra()){
////
////        }
//        Agente agentTransient = null;
//        Ambiente oceano = SetupASF.ambienteOceano;
//            Papel agentRole = null;
//            int tipo = agentTransient.getMetricaAtual().getType();
//            if(tipo==Agente.PAPEL_MANUTENCAO){
//                agentRole = new Manutencao();
//            } else if(tipo == Agente.PAPEL_DESEMPENHO){
//                agentRole = new Desempenho();
//            }
//
//            agentTransient.setTipo(tipo);
//
//            TransformacaoService transformacaoService = ObjectFactory.getObj(TransformacaoService.class);
//
//            List<Transformacao> transformacoes = transformacaoService.getTransformacoesPorTipo(agentTransient.getTipo());
//            Collection<Belief> beliefs = new ArrayList<Belief>();
//            beliefs.add(new LeafBelief(ConstantesAplicacao.TRANSFORMACAO, ConstantesAplicacao.TRANSFORMACOES, transformacoes));
//            beliefs.add(new LeafBelief(ConstantesAplicacao.METRICA, ConstantesAplicacao.METRICA_ATUAL, agentTransient.getMetricaAtual()));
//            beliefs.add(new LeafBelief(ConstantesAplicacao.CONFIGURACAO, ConstantesAplicacao.CONFIGURACAO_ATUAL, agentTransient.getConfiguracaoAtual()));
//            beliefs.add(new LeafBelief(ConstantesAplicacao.CONFIGURACAO, ConstantesAplicacao.CONFIGURACOES_DERIVADAS, null));
//
//            agentRole.setBeliefs(beliefs);
//
//            Agent agentASF = FabricaAgente.getAgente(agentTransient.getNome(),agentRole, oceano, SetupASF.OrganizacaoPrincipal);
//
//            Thread threadASF = GerenciadorFluxos.registraASF_Agente(agentASF, agentTransient);
//            oceano.registerAgents(agentASF);
//
//            // Sending initial message
//            Message msgm = new Message("?" + agentASF.getAgentName().getName(), ConstantesAplicacao.CONFIGURACAO_ATUAL, agentASF.getAgentName(), agentASF.getAgentName());
//            msgm.setPerformative(ConstantesAplicacao.ACAO_EXTRAI_METRICAS_CONFIG_ATUAL);
//            agentASF.send(msgm);
//
//            // waiting the time to init the work
//            ThreadScheduling scheduling = new ThreadScheduling(threadASF, agentTransient.getDataInicio(), agentTransient.getDataTermino());
//            scheduling.start();
//
//    }
//}
