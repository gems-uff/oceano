/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package translation;

/**
 *
 * @author Joao
 */
public class PortugueseTranslate extends Translate {

    public PortugueseTranslate() {
    }

    @Override
    public String startingWork() {
        return "[Iniciarei o trabalho] - Enviando Mensagem ao Agente Orquestrador";
    }

    @Override
    public String suggestedRefactorings(String refactorings) {
        return "    Refactorings Sugeridos: " + refactorings;
    }

    @Override
    public String firstMeasurement() {
        return "\n***** PRIMEIRA MEDIÇÃO, UTILIZADA PARA NORMALIZAÇÃO *****";
    }

    @Override
    public String normalizedValue() {
        return "\n Valor Normalizado: 1 *****\n\n";
    }

    @Override
    public String normalized() {
        return "\n***** NORMALIZADO *****";
    }

    @Override
    public String measuredValue(Double value) {
        return "\n[Trabalhando] - Valor Medido: " + value;
    }
    
    @Override
    public String oldValue(Double value) {
        return "[Trabalhando] - Valor Antigo: " + value;
    }

    @Override
    public String metricValues() {
        return "     **Valores** Métricas:";
    }
    
    @Override
    public String startingPlan() {
        return "  [Iniciando Plano de Refatorações]";
    }

    @Override
    public String checkingImplemented() {
        return "      [Verificando Refatorações Implementadas]";
    }
    
    @Override
    public String applying(String refactoring) {
        return "\n***********--> Aplicando: " + refactoring;
    }

    @Override
    public String startingTestPlan() {
        return "  [Iniciando Plano de Teste]";
    }

    @Override
    public String notImprovedWorsened() {
        return "    A refatoração não melhorou nem piorou o atributo de qualidade. ";
    }

    @Override
    public String revertingChanges() {
        return "    Revertendo alterações\n";
    }

    @Override
    public String worsened() {
        return "    A refatoração piorou o atributo de qualidade. ";
    }

    @Override
    public String doingCheckout(String project) {
        return "    [Trabalhando] - fazendo checckout do projeto " + project;
    }

    @Override
    public String checkoutVersion(Long version) {
        return "          Versão do Checkout : " + version;
    }
    
    @Override
    public String finalRelatory(String path) {
        return "Relatório final criado em: " + path;
    }

    @Override
    public String metricsRelatory(String path) {
        return "Relatório de métricas criado em: " + path;
    }

    @Override
    public String branchPath(String path) {
        return "Branch criado em: " + path;
    }

    @Override
    public String noBranch(String path) {
        return "Branch não criado: " + path;
    }

    @Override
    public String inative() {
        return "inativo";
    }
    
    @Override
    public String requestMetrics(String qualityAttribute) {
        return "[Trabalhando] - Solicitando todas as métricas e fórmula para: " 
                + qualityAttribute;
    }

    @Override
    public String formula() {
        return "     Fórmula: \n       (";
    }

    @Override
    public String metrics() {
        return "     Métricas:";
    }

    @Override
    public String branchCreated(String branch, Long commit) {
        return "    Branch Criado: " + branch + " [" + commit + "]";
    }

    @Override
    public String commitingSuccessful() {
        return "        Commitando refatoração de sucesso";
    }

    @Override
    public String conflictSelected() {
        return "\n      Conflito Selecionado para ser utilizado na refatoração:";
    }

    @Override
    public String moveMember(String member) {
        return "    Membro: " + member;
    }
    
    @Override
    public String moveTo(String to) {
        return "    Para: " + to;
    }

    @Override
    public String unresolvableConflicts() {
        return "    [Conflitos insolucionaveis encontrados]";
    }

    @Override
    public StringBuilder foundConflicts(StringBuilder builder, int number) {
        builder.append("   [").
                append(number).
                append("] conflitos encontrados para refatorações:");
        return builder;
    }

    @Override
    public String resolutionFail() {
        return "    Falha em resolver algum conflito... ";
    }

    @Override
    public String newConflicts() {
        return "\n    Novos conflitos foram encontrados";
    }

    @Override
    public String conflictsResolved() {
        return "\n    Todos conflitos foram resolvidos";
    }

    @Override
    public String appliedRefactoring() {
        return "\n     OK - Refatoração aplicada \n";
    }

    @Override
    public StringBuilder failedToApply(StringBuilder builder, String message) {
        builder.append("\n    As refatorações NÃO FORAM APLICADAS").append(message);
        return builder;
    }

    @Override
    public String failedToApplyMessage() {
        return " (Falha ao aplicar) \n";
    }

    @Override
    public String closePE() {
        return  "Essa operação não tem volta, deseja encerrar o PeixeEspadaCliente?";
    }
    
    @Override
    public String aboutPE() {
        return
                "Universidade Federal Fluminense \n\n"
                + "Protótipo: Implementação da dissertação de Mestrado \n\n"
                + "CURSO: "
                + "Mestrado em Ciência da Computação (1º/2009 a 1/2011)\n\n"
                + "Rumo ao Rejuvenescimento do Software em uma abordadem Multi-Agente\n"
                + "\n\n"
                + "ESTUDANTE :\n\n"
                + "     Heliomar Kann da Rocha Santos \n\n"
                + "Iniciação Científica :\n\n"
                + "     João Felipe Nicolaci Pimentel\n\n"
                + "ORIENTADO POR :\n\n"
                + "     Leonardo Gresta Paulino Murta and Viviane Torres da Silva \n\n"
                + "                                       2011";
    }
    
    @Override
    public String aboutPETitle() {
        return "Implementação da dissertação de Mestrado - UFF";
    }

    @Override
    public String backgroundRestore() {
        return "Esconder\\Mostrar";
    }

    @Override
    public String about() {
        return "Sobre";
    }

    @Override
    public String exit() {
        return "Sair";
    }
    
    @Override
    public String working() {
        return "Trabalhando";
    }

    @Override
    public String waitingWork() {
        return "Esperando Trabalho";
    }

    @Override
    public String sleeping() {
        return "Dormindo";
    }
    
    @Override
    public String testing() {
        return "Testando";
    }

    @Override
    public String fatalError() {
        return "\n    ERRO FATAL: ";
    }
    
    @Override
    public String fatalErrorFinishingWork() {
        return "\n\n    ERRO FATAL FINALIZANDO TRABALHO: ";
    }

    @Override
    public String causedBy(String message) {
        return "   Causado por: " + message;
    }
    
    @Override
    public String workAgent(Long agent) {
        return "Agente Trabalhador: " + agent;
    }
    
    @Override
    public String initialDate2min() {
        return "A Data Inicial deve ser pelo menos 2 minutos depois da data atual";
    }
    
    @Override
    public String endDateBigger() {
        return "A Data Final deve ser maior do que a Data Inicial";
    }

    @Override
    public String requestBranch() {
        return "     Solicitando Branch para o Agente Orquestrador";
    }

    @Override
    public String copyToBranch(String branch) {
        return "     Copiando para Branch: " + branch;
    }

    @Override
    public String newBranch(String branch) {
        return "     Novo Branch Criado e atualizado: " + branch;
    }

    @Override
    public String informingFailure() {
        return "     Informando falha na refatoração ao Agente Orquestrador";
    }

    @Override
    public String agentAware() {
        return "     Agente Orquestrador ciente";
    }
    
    @Override
    public String cannotCreateWorkspace(String path) {
        return "Não pode criar workspace no diretório: " + path;
    }
    
    @Override
    public String cannotCreatePath(String path) {
        return "Não pode criar diretório: " + path;
    }

    @Override
    public String informingSuccess() {
        return "     Informando SUCESSO na refatoração ao Agente Orquestrador";
    }

    @Override
    public String informingNoChange() {
        return "     Informando 'NÃO ALTERAÇAO' do atributo de qualidade na refatoração ao Agente Orquestrador";
    }
    
    @Override
    public String foundSymptoms(int number) {
        return number + " sintomas encontrados";
    }
    
    @Override
    public String milliseconds(long number) {
        return number + " ms";
    }
    
    @Override
    public String symptom(int number) {
        return "Sintoma: " + number;
    }
    
    @Override
    public String removed(String text) {
        return text + " removida";
    }

    @Override
    public String refactoringCompleted(String refactoring) {
        return "As refatoraçoes de "+refactoring+" foram concluídas";
    }

    @Override
    public String refactoringNotCompleted(String refactoring) {
        return "As refatoraçoes de "+refactoring+" não foram concluídas por esgotamento de tempo";
    }
    
    @Override
    public String notRefactored(String message) {
        return "Não refatorado: " + message;
    }
    
    @Override
    public String error(String message) {
        return "Error: " + message;
    }
    
    @Override
    public String foundResolutions(int number) {
        return "[" + number + "] resoluções";
    }

    @Override
    public String idealRefactoring() {
        return "\n    Refatoração ideal escolhida.";
    }

    @Override
    public String changingTheCode() {
        return "\n    Alterando o codigo";
    }

    @Override
    public String brokenResolution() {
        return "\n A resolução de Conflito quebrou o código";
    }

    @Override
    public String commitingChanges() {
        return "\n    Commitando alterações";
    }
    
}
