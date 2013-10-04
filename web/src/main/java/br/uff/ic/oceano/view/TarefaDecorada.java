///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package br.uff.ic.oceano.view;
//
//import br.uff.ic.oceano.asf.agente.ConstantesAplicacao.ConstantesAplicacao;
//import framework.agent.Agent;
//import framework.mentalState.Message;
//import java.util.Date;
//
///**
// *
// * @author DanCastellani
// */
//public class TarefaDecorada {
//
//    private Message message;
//    private Agent fromAgent;
//    private Agent toAgent;
//    private String fromName;
//    private String toName;
//    private String performative;
//    private String content;
//    private Date sendTime;
//
//    public TarefaDecorada(Message message, Agent fromAgent, String toName) {
//        this.message = message;
//        this.fromAgent = fromAgent;
//        this.fromName = processaNome(fromAgent.getAgentName().getName());
//        this.toName = processaNome(toName);
//        this.performative = message.getPerformative();
//        if (message.getContent() != null) {
//            this.content = message.getContent().getClass().getSimpleName();
//        } else {
//            content = null;
//        }
//        this.sendTime = new Date();
//    }
//
//    private String processaNome(String nomeCompleto) {
//        String nomeRetorno = nomeCompleto.substring(ConstantesAplicacao.PREFIXO_NOME_AGENTE.length());
//        nomeRetorno = nomeRetorno.substring(0, nomeRetorno.indexOf("::"));
//        return nomeRetorno;
//    }
//
//    @Override
//    public String toString() {
//        return "-<FROM " + this.fromName + ", TO " + this.toName + ", PERFORMATIVE " + this.performative + ", CONTENT " + this.content + ">-";
//    }
//
//    /**
//     * @return the fromName
//     */
//    public String getFromName() {
//        return fromName;
//    }
//
//    /**
//     * @param fromName the fromName to set
//     */
//    public void setFromName(String fromName) {
//        this.fromName = fromName;
//    }
//
//    /**
//     * @return the toName
//     */
//    public String getToName() {
//        return toName;
//    }
//
//    /**
//     * @param toName the toName to set
//     */
//    public void setToName(String toName) {
//        this.toName = toName;
//    }
//
//    /**
//     * @return the performative
//     */
//    public String getPerformative() {
//        return performative;
//    }
//
//    /**
//     * @param performative the performative to set
//     */
//    public void setPerformative(String performative) {
//        this.performative = performative;
//    }
//
//    /**
//     * @return the content
//     */
//    public String getContent() {
//        return content;
//    }
//
//    /**
//     * @param content the content to set
//     */
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    /**
//     * @return the sendTime
//     */
//    public Date getSendTime() {
//        return sendTime;
//    }
//
//    /**
//     * @param sendTime the sendTime to set
//     */
//    public void setSendTime(Date sendTime) {
//        this.sendTime = sendTime;
//    }
//}
