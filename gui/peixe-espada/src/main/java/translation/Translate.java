/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package translation;

/**
 *
 * @author Joao
 */
public abstract class Translate {
    
    private static Translate translate = null;
    
    public static Translate getTranslate() {
        if (translate == null) {
            translate = new EnglishTranslate();
        } 
        return translate;
    }
    
    public abstract String startingWork();
    public abstract String suggestedRefactorings(String refactorings);
    public abstract String firstMeasurement();
    public abstract String normalizedValue();
    public abstract String normalized();
    public abstract String measuredValue(Double value);
    public abstract String oldValue(Double value);
    public abstract String metricValues();
    public String metricValuesItem(String acronym, Double value){
        return "\n       -" + acronym + " : " + value;
    }
    public abstract String startingPlan();
    public abstract String checkingImplemented();
    public abstract String applying(String refactoring);
    public abstract String startingTestPlan();
    public abstract String notImprovedWorsened();
    public abstract String revertingChanges();
    public abstract String worsened();
    public abstract String doingCheckout(String project);
    public abstract String checkoutVersion(Long version);
    public String workspace(String path) {
        return "          Workspace : " + path;
    }
    public String finishWorkSeparatorStart() {
        return "\n*\n*";
    }
    public String finishWorkSeparatorEnd() {
        return "*\n*";
    }
    public abstract String finalRelatory(String path);
    public abstract String metricsRelatory(String path);
    public abstract String branchPath(String path);
    public abstract String noBranch(String path);
    public abstract String inative();
    public abstract String requestMetrics(String qualityAttribute);
    public abstract String formula();
    public abstract String metrics();
    public String metricItem(String acronym, String name){
        return "\n       -" + acronym + " - " + name;
    }
    public String formulaEnd(){
        return " )\n";
    }
    public abstract String branchCreated(String branch, Long commit);
    public abstract String commitingSuccessful();
    public abstract String conflictSelected();
    public StringBuilder conflictDescription(StringBuilder builder, String description) {
        builder.append("\n      ->").append(description);
        return builder;
    }
    public StringBuilder resolutionDescription(StringBuilder builder, String description) {
        builder.append("\n        =>").append(description);
        return builder;
    }
    public StringBuilder resolutionItem(StringBuilder builder, String name) {
        builder.append("\n              .").append(name);
        return builder;
    }
    public abstract String moveMember(String member);
    public abstract String moveTo(String to);
    public abstract String unresolvableConflicts();
    public abstract StringBuilder foundConflicts(StringBuilder builder, int number);
    public abstract String resolutionFail();
    public abstract String newConflicts();
    public abstract String conflictsResolved();
    public abstract String appliedRefactoring();
    public abstract StringBuilder failedToApply(StringBuilder builder, String message);
    public abstract String failedToApplyMessage();
    public abstract String closePE();
    public abstract String aboutPE();
    public abstract String aboutPETitle();
    public abstract String backgroundRestore();
    public abstract String about();
    public abstract String exit();
    public abstract String working();
    public abstract String waitingWork();
    public abstract String sleeping();
    public abstract String testing();
    public abstract String fatalError();
    public abstract String fatalErrorFinishingWork();
    public abstract String causedBy(String message);
    public String stackItem(String message){
        return "      " + message;
    }
    public abstract String workAgent(Long agent);
    public abstract String initialDate2min();
    public abstract String endDateBigger();
    public abstract String requestBranch();
    public abstract String copyToBranch(String branch);
    public abstract String newBranch(String branch);
    public abstract String informingFailure();
    public abstract String agentAware();
    public abstract String cannotCreateWorkspace(String path);
    public abstract String cannotCreatePath(String path);
    public abstract String informingSuccess();
    public abstract String informingNoChange();
    public abstract String foundSymptoms(int number);
    public abstract String milliseconds(long number);
    public abstract String symptom(int number);
    public abstract String removed(String text);
    public abstract String refactoringCompleted(String refactoring);
    public abstract String refactoringNotCompleted(String refactoring);
    public abstract String notRefactored(String message);
    public abstract String error(String message);
    public abstract String foundResolutions(int number);
    public abstract String idealRefactoring();
    public abstract String changingTheCode();
    public abstract String brokenResolution();
    public abstract String commitingChanges();
}
