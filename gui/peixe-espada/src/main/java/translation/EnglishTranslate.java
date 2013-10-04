/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package translation;

/**
 *
 * @author Joao
 */
public class EnglishTranslate extends Translate {

    public EnglishTranslate() {
    }

    @Override
    public String startingWork() {
        return "[Starting work] - Sending Message to the Orchestrator Agent";
    }

    @Override
    public String suggestedRefactorings(String refactorings) {
        return "    Suggested Refactorings: " + refactorings;
    }

    @Override
    public String firstMeasurement() {
        return "\n***** FIRST MEASUREMENT USED FOR NORMALIZATION *****";
    }

    @Override
    public String normalizedValue() {
        return "\n Normalized Value: 1 *****\n\n";
    }

    @Override
    public String normalized() {
        return "\n***** NORMALIZED *****";
    }

    @Override
    public String measuredValue(Double value) {
        return "\n[Working] - Measured Value: " + value;
    }    
        
    @Override
    public String oldValue(Double value) {
        return "[Working] - Old Value: " + value;
    }

    @Override
    public String metricValues() {
        return "     Metric **Values**:";
    }

    @Override
    public String startingPlan() {
        return "  [Starting Refactoring Plan]";
    }

    @Override
    public String checkingImplemented() {
        return "      [Checking Implemented Refactorings]";
    }

    @Override
    public String applying(String refactoring) {
        return "\n***********--> Applying: " + refactoring;
    }

    @Override
    public String startingTestPlan() {
        return "  [Starting Test Plan]";
    }
    
    @Override
    public String notImprovedWorsened() {
        return "    The refactoring has not improved or worsened the quality attribute. ";
    }

    @Override
    public String revertingChanges() {
        return "    Reverting changes\n";
    }

    @Override
    public String worsened() {
        return "    The refactoring has worsened the quality attribute. ";
    }
    
    @Override
    public String doingCheckout(String project) {
        return "    [Working] - doing checkout of project " + project;
    }
    
    @Override
    public String checkoutVersion(Long version) {
        return "          Checkout Version : " + version;
    }

    @Override
    public String finalRelatory(String path) {
        return "Final relatory created in: " + path;
    }

    @Override
    public String metricsRelatory(String path) {
        return "Metrics relatory created in: " + path;
    }

    @Override
    public String branchPath(String path) {
        return "Branch created in: " + path;
    }

    @Override
    public String noBranch(String path) {
        return "Do not: " + path;
    }

    @Override
    public String inative() {
        return "inative";
    }

    @Override
    public String requestMetrics(String qualityAttribute) {
        return "[Working] - Requesting all metrics and formula for: " 
                + qualityAttribute;
    }
    
    @Override
    public String formula() {
        return "     Formula: \n       (";
    }
    
    @Override
    public String metrics() {
        return "     Metrics:";
    }

    @Override
    public String branchCreated(String branch, Long commit) {
        return "    Branch Created: " + branch + " [" + commit + "]";
    }
    
    @Override
    public String commitingSuccessful() {
        return "        Commiting successful refactoring";
    }
    
    @Override
    public String conflictSelected() {
        return "\n      Conflict selected to be used for refactoring:";
    }
    
    @Override
    public String moveMember(String member) {
        return "    Member: " + member;
    }
    
    @Override
    public String moveTo(String to) {
        return "    To: " + to;
    }

    @Override
    public String unresolvableConflicts() {
        return "    [Unresolvable conflicts found]";
    }  
    
    @Override
    public StringBuilder foundConflicts(StringBuilder builder, int number) {
        builder.append("   [").
                append(number).
                append("] conflicts found to refactorings:");
        return builder;
    }
    
    @Override
    public String resolutionFail() {
        return "    Failure to resolve a conflict... ";
    }

    @Override
    public String newConflicts() {
        return "\n    New conflicts were found";
    }
    
    @Override
    public String conflictsResolved() {
        return "\n    All conflicts were resolved";
    }
    
    @Override
    public String appliedRefactoring() {
        return "\n     OK - Refactoring applied \n";
    }
    
    @Override
    public StringBuilder failedToApply(StringBuilder builder, String message) {
        builder.append("\n    The refactorings WEREN'T APPLIED").append(message);
        return builder;
    }

    @Override
    public String failedToApplyMessage() {
        return " (Failed to apply) \n";
    }
    
    @Override
    public String closePE() {
        return  "This operation has no return. Do you want to quit PeixeEspadaCliente?";
    }

    @Override
    public String aboutPE() {
        return
                "Universidade Federal Fluminense \n\n"
                + "Type: Implementation of Master dissertation \n\n"
                + "COURSE: "
                + "Master degree of Computer Science (1º/2009 to 1/2011)\n\n"
                + "Towards Software Rejuvenation via a Multi-Agent Approach\n"
                + "\n\n"
                + "STUDENT :\n\n"
                + "     Heliomar Kann da Rocha Santos \n\n"
                + "Undergraduate Research :\n\n"
                + "     João Felipe Nicolaci Pimentel\n\n"
                + "ORIENTED BY :\n\n"
                + "     Leonardo Gresta Paulino Murta and Viviane Torres da Silva \n\n"
                + "                                       2011";
    }
    
    @Override
    public String aboutPETitle() {
        return "Implementation of Master dissertation - UFF";
    }
    
    @Override
    public String backgroundRestore() {
        return "Backgroung\\Restore";
    }

    @Override
    public String about() {
        return "About";
    }

    @Override
    public String exit() {
        return "Exit";
    }

    @Override
    public String working() {
        return "Working";
    }

    @Override
    public String waitingWork() {
        return "Waiting work";
    }

    @Override
    public String sleeping() {
        return "Sleeping";
    }
    
    @Override
    public String testing() {
        return "Testing";
    }
    
    @Override
    public String fatalError() {
        return "\n    FATAL ERROR: ";
    }
    
    @Override
    public String fatalErrorFinishingWork() {
        return "\n\n    FATAL ERROR FINISHING WORK: ";
    }
    
    @Override
    public String causedBy(String message) {
        return "   Caused by: " + message;
    }

    @Override
    public String workAgent(Long agent) {
        return "Work Agent: " + agent;
    }

    @Override
    public String initialDate2min() {
        return "The Initial Date must begin at least 2 minutes after the current time";
    }

    @Override
    public String endDateBigger() {
        return "The End Date must be bigger than the Initial Date";
    }
    
    @Override
    public String requestBranch() {
        return "     Requesting Branch to the Orchestrator Agent";
    }
    
    @Override
    public String copyToBranch(String branch) {
        return "     Copying to Branch: " + branch;
    }
    
    @Override
    public String newBranch(String branch) {
        return "     New Branch created and updated: " + branch;
    }
    
    @Override
    public String informingFailure() {
        return "     Informing failure in refactoring to the Orchestrator Agent";
    }
    
    @Override
    public String agentAware() {
        return "     Orchestrator Agent aware";
    }

    @Override
    public String cannotCreateWorkspace(String path) {
        return "Can not create workspace in path: " + path;
    }
    
    @Override
    public String cannotCreatePath(String path) {
        return "Can not create path: " + path;
    }
    
    @Override
    public String informingSuccess() {
        return "     Informing SUCCESS in refactoring to the Orchestrator Agent";
    }
    
    @Override
    public String informingNoChange() {
        return "     Informing 'NO CHANGE' of quality attribute in refactoring to the Orchestrator Agent";
    }

    @Override
    public String foundSymptoms(int number) {
        return number + " found symptoms";
    }

    @Override
    public String milliseconds(long number) {
        return number + " milliseconds";
    }

    @Override
    public String symptom(int number) {
        return "Symptom: " + number;
    }

    @Override
    public String removed(String text) {
        return text + " removed";
    }

    @Override
    public String refactoringCompleted(String refactoring) {
        return "The "+refactoring+" refactorings were completed";
    }

    @Override
    public String refactoringNotCompleted(String refactoring) {
        return "The "+refactoring+" refactorings were not completed due to the time exhausted";
    }

    @Override
    public String notRefactored(String message) {
        return "Not refactored: " + message;
    }
    
    @Override
    public String error(String message) {
        return "Error: " + message;
    }

    @Override
    public String foundResolutions(int number) {
        return "[" + number + "] resolutions";
    }
    
    @Override
    public String idealRefactoring() {
        return "\n    Ideal refactoring chosen.";
    }
    
    @Override
    public String changingTheCode() {
        return "\n    Changing the code";
    }
    
    @Override
    public String brokenResolution() {
        return "\n The resolution broke the code";
    }
    
    @Override
    public String commitingChanges() {
        return "\n    Commiting changes";
    }
}
