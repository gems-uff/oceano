/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service.behaviortable;

import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.core.tools.metrics.expression.QMOOD;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DanCastellani
 */
public class BehaviorTableService implements PersistenceService {

    private static int TABLE_SIZE = 7;
    private static final String SYMBOL_PROPORTIONAL_BEHAVIOR = "+";
    private static final String SYMBOL_OPOSITE_BEHAVIOR = "-";
    private static final String SYMBOL_CONFLICTANT_BEHAVIOR = "0";
    private static Map<String, Integer> mapAttributeIndices;
    private static String[] attributeNames;

    public void setup() {
        mapAttributeIndices = new HashMap<String, Integer>();
        int indice = 1;
        //coloco tamanho = lenght + 1, pq a posição 0 ficará em branco
        attributeNames = new String[QMOOD.QMOOD_QUALITY_ATTRIBUTES.length + 1];
        for (String qaName : QMOOD.QMOOD_QUALITY_ATTRIBUTES) {
            attributeNames[indice] = qaName;
            mapAttributeIndices.put(qaName, indice++);
        }
    }

    private void updateAttributeIndiceMap(DataMiningResult currentDataMiningResult) {
        mapAttributeIndices = new HashMap<String, Integer>();

        int indice = 1;
        List<String> attributes = getAttributes(currentDataMiningResult);
        Collections.sort(attributes);
        for (String attributeName : attributes) {
            mapAttributeIndices.put(attributeName, indice++);
        }

        attributeNames = new String[mapAttributeIndices.keySet().size() + 1];
        for (String attributeName : mapAttributeIndices.keySet()) {
            MetricManager mm = (MetricManager) MetricManagerFactory.getInstance().getMetricByName(attributeName);
            System.out.println("mapAttributeIndices.get(attributeName) = " + mapAttributeIndices.get(attributeName));
            System.out.println("mm.getMetric().getAcronym() = " + mm.getMetric().getAcronym());
            attributeNames[mapAttributeIndices.get(attributeName)] = mm.getMetric().getAcronym();
        }

        TABLE_SIZE = attributeNames.length;
    }

    public synchronized Behavior[][] buildTable(DataMiningResult currentDataMiningResult) {
        updateAttributeIndiceMap(currentDataMiningResult);

        Behavior[][] behaviorTable = createDefaultTable(currentDataMiningResult);
        final String ruleMetricName = currentDataMiningResult.getRuleMetricName();

        for (DataMiningPattern dataMiningPattern : currentDataMiningResult.getDataMiningPatterns()) {
            if (dataMiningPattern.getSize() != 2) {
                continue;
            }
            final String pattern = dataMiningPattern.getPattern();
            if (pattern.contains("project-revision") 
                    || pattern.contains("rdate") 
                    || pattern.contains("rcommiter") 
                    || pattern.contains("#files")
                    || pattern.contains("rday") 
                    || pattern.contains("rhour") 
                    || pattern.contains("rcompile")) {
                continue;
            }

            final String precedentAttribute = DataMiningPattern.getAttribute(dataMiningPattern.getPrecedent());
            final String consequentAttribute = DataMiningPattern.getAttribute(dataMiningPattern.getConsequent());

            final Integer precedentIndice = mapAttributeIndices.get(precedentAttribute);
            final Integer consequentIndice = mapAttributeIndices.get(consequentAttribute);

            if (precedentIndice == null || consequentIndice == null) {
                continue;
            }
            final Behavior behavior = behaviorTable[precedentIndice][consequentIndice];
            behavior.getRules().add(dataMiningPattern);
            behavior.setValue(verifyBehaviors(behavior.getRules()));

            //update max rule metric value
            final Double highestValue = behavior.getHighestConfidence();
            final Double actualValue = getRuleMetric(ruleMetricName, dataMiningPattern);
            if (highestValue == null || actualValue > highestValue) {
                behavior.setHighestConfidence(actualValue);
            }
        }

        return behaviorTable;
    }

    private Behavior[][] createDefaultTable(DataMiningResult dataMiningResult) {
        Behavior[][] behaviorTable = new Behavior[TABLE_SIZE][TABLE_SIZE];
        for (int i = 0; i < behaviorTable.length; i++) {
            for (int j = 0; j < behaviorTable[i].length; j++) {
                if (j == 0 && i == 0) {
                    behaviorTable[i][j] = new Behavior(dataMiningResult);

                } else if (j == 0) {
                    behaviorTable[i][j] = new Behavior(attributeNames[i], dataMiningResult);
//                    behaviorTable[i][j].getRules().add(attributeNames[i]);

                } else if (i == 0) {
                    behaviorTable[i][j] = new Behavior(attributeNames[j], dataMiningResult);
//                    behaviorTable[i][j].getRules().add(attributeNames[j]);
                } else {
                    behaviorTable[i][j] = new Behavior(dataMiningResult);
                }

            }
        }
        return behaviorTable;
    }

    private String verifyBehavior(String oneValue, String otherValue) {
        if (oneValue.equals(otherValue)) {
            return SYMBOL_PROPORTIONAL_BEHAVIOR;
        } else {
            return SYMBOL_OPOSITE_BEHAVIOR;
        }
    }

    private String verifyBehaviors(List<DataMiningPattern> rules) {
        boolean proportionalBehavior = false;
        boolean opositeBehavior = false;
        for (DataMiningPattern rule : rules) {
            String behavior = verifyBehavior(DataMiningPattern.getValue(rule.getPrecedent()), DataMiningPattern.getValue(rule.getConsequent()));
            if (behavior.equals(SYMBOL_PROPORTIONAL_BEHAVIOR)) {
                proportionalBehavior = true;
            } else if (behavior.equals(SYMBOL_OPOSITE_BEHAVIOR)) {
                opositeBehavior = true;
            }
        }
        if (opositeBehavior && proportionalBehavior) {
            return SYMBOL_CONFLICTANT_BEHAVIOR;
        } else if (opositeBehavior) {
            return SYMBOL_OPOSITE_BEHAVIOR;
        } else {
            return SYMBOL_PROPORTIONAL_BEHAVIOR;
        }
    }

    private Double getRuleMetric(String ruleMetricName, DataMiningPattern dataMiningPattern) {
        if (ruleMetricName.equals("Confidence")) {
//            System.out.println("metric = Confidence");
            return dataMiningPattern.getConfidence();
        }
        if (ruleMetricName.equals("Lift")) {
//            System.out.println("metric = lift");
            return dataMiningPattern.getLift();
        }
        if (ruleMetricName.equals("Leverage")) {
//            System.out.println("metric = Leverage");
            return dataMiningPattern.getLeverage();
        }
        if (ruleMetricName.equals("Conviction")) {
//            System.out.println("metric = Conviction");
            return dataMiningPattern.getConviction();
        }
        return null;
    }

    private List<String> getAttributes(DataMiningResult dataMiningResult) {
        List<String> returningAttributes = new LinkedList<String>();

        final String arff = dataMiningResult.getArff();
        for (String line : arff.substring(0, arff.indexOf("@DATA")).split("\n")) {
            if (line.startsWith("@ATTRIBUTE")) {
                if (line.contains("project-revision") 
                        || line.contains("rdate") 
                        || line.contains("rcommiter") 
                        || line.contains("#files")
                        || line.contains("rday") 
                        || line.contains("rhour") 
                        || line.contains("rRound") 
                        || line.contains("rcompile")) {
                    continue;
                }
                String attributeName;
                if (line.contains("\"")) {
                    attributeName = line.substring(line.indexOf("\"") + 1);
                    attributeName = attributeName.substring(0, attributeName.indexOf("\""));
                } else {
                    attributeName = line.split(" ")[1];
                }
                attributeName = attributeName.substring("dAvg-".length());
//                System.out.println("attributeName = " + attributeName);
                returningAttributes.add(attributeName);
            }
        }
        return returningAttributes;
    }
}
