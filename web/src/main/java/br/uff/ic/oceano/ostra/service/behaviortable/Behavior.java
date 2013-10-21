/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service.behaviortable;

import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class Behavior {

    private DataMiningResult dataMiningResult;
    private List<DataMiningPattern> rules = new ArrayList<DataMiningPattern>();
    private String value = "";
    private Double highestConfidence = 0D;
    //prefix
    private static final String PROPORTIONAL = "proportional_";
    private static final String INVERSE = "inverse_";
    private static final String NEUTRAL = "neutral";
    //proportional
    private static final String PROPORTIONAL_MINUS = PROPORTIONAL + "minus";
    private static final String PROPORTIONAL_PLUS = PROPORTIONAL + "plus";
    private static final String PROPORTIONAL_BOTH = PROPORTIONAL + "both";
    //inverse
    private static final String INVERSE_MINUS_PLUS = INVERSE + "minus_plus";
    private static final String INVERSE_PLUS_MINUS = INVERSE + "plus_minus";
    private static final String INVERSE_BOTH = INVERSE + "both";
    private static final String INVERSE_NEUTRAL_MINUS = INVERSE + "neutral_minus";
    private static final String INVERSE_NEUTRAL_PLUS = INVERSE + "neutral_plus";
    //undefined
    private static final String UNDEFINED = "undefined";
    private static final String NO_BEHAVIOR = "no_behavior";

    public String getFormatedRules() {
        String s = "";
        for (DataMiningPattern rule : rules) {
            s += rule.toString() + "<br/>";
        }
        return s;
    }

    public String getBehaviorName() {
        return getBehaviorName_Real();
    }

    public String getBehaviorName_Real() {
        List<String> behaviors = new LinkedList<String>();
        for (DataMiningPattern dataMiningPattern : rules) {
            behaviors.add(getSimpleBehaviorName(dataMiningPattern));
        }
        final boolean hasNeutralBehavior = existsBehavior(behaviors, NEUTRAL);
        final boolean hasProportionalBehavior = existsBehavior(behaviors, PROPORTIONAL);
        final boolean hasInverseBehavior = existsBehavior(behaviors, INVERSE);

        if (hasInverseBehavior && hasProportionalBehavior) {
            return UNDEFINED;
        }
        if (hasInverseBehavior) {
            if (hasNeutralBehavior) {
                //because neutral is proportional (0 -> 0)
                return UNDEFINED;
            } else {
                final boolean hasInverseMinusPlus = existsBehavior(behaviors, INVERSE_MINUS_PLUS);
                final boolean hasInversePlusMinus = existsBehavior(behaviors, INVERSE_PLUS_MINUS);
                final boolean hasInverseNeutralMinus = existsBehavior(behaviors, INVERSE_NEUTRAL_MINUS);
                final boolean hasInverseNeutralPlus = existsBehavior(behaviors, INVERSE_NEUTRAL_PLUS);

//                if (hasInverseMinusPlus && hasInversePlusMinus) {
//                    return INVERSE_BOTH;
//                }
                if (hasInverseMinusPlus) {
                    return INVERSE_MINUS_PLUS;
                }
                if (hasInversePlusMinus) {
                    return INVERSE_PLUS_MINUS;
                }
                if (hasInverseNeutralMinus && hasInverseNeutralPlus) {
                    DataMiningPattern betterPattern = null;
                    final String dataMiningMetricName = dataMiningResult.getRuleMetricName();
                    for (DataMiningPattern currentDataMiningPattern : rules) {
                        if (betterPattern == null) {
                            betterPattern = currentDataMiningPattern;
                        }
                        if (betterPattern.getMetricValue(dataMiningMetricName) < currentDataMiningPattern.getMetricValue(dataMiningMetricName)) {
                            betterPattern = currentDataMiningPattern;

                        } else if (betterPattern.getMetricValue(dataMiningMetricName) == currentDataMiningPattern.getMetricValue(dataMiningMetricName)) {
                            if (betterPattern.getSupport() < currentDataMiningPattern.getSupport()) {
                                betterPattern = currentDataMiningPattern;
                            }
                        }
                    }
                    return getSimpleBehaviorName(betterPattern);
//                    return INVERSE_BOTH;
                }
                if (hasInverseNeutralMinus) {
                    return INVERSE_NEUTRAL_MINUS;
                }
                if (hasInverseNeutralPlus) {
                    return INVERSE_NEUTRAL_PLUS;
                }
            }
        } else if (hasProportionalBehavior) {
            final boolean hasProportionalMinus = existsBehavior(behaviors, PROPORTIONAL_MINUS);
            final boolean hasProportionalPlus = existsBehavior(behaviors, PROPORTIONAL_PLUS);

            //as for proportional behavior, the neutral doesnt influence, we will ignore it.

            if (hasProportionalMinus && hasProportionalPlus) {
                DataMiningPattern betterPattern = null;
                final String dataMiningMetricName = dataMiningResult.getRuleMetricName();
                for (DataMiningPattern currentDataMiningPattern : rules) {
                    if (betterPattern == null) {
                        betterPattern = currentDataMiningPattern;
                    }
                    if (betterPattern.getMetricValue(dataMiningMetricName) < currentDataMiningPattern.getMetricValue(dataMiningMetricName)) {
                        betterPattern = currentDataMiningPattern;
                    } else if (betterPattern.getMetricValue(dataMiningMetricName) == currentDataMiningPattern.getMetricValue(dataMiningMetricName)) {
                        if (betterPattern.getSupport() < currentDataMiningPattern.getSupport()) {
                            betterPattern = currentDataMiningPattern;
                        }
                    }
                }
                return getSimpleBehaviorName(betterPattern);
//                return PROPORTIONAL_BOTH;
            }
            if (hasProportionalMinus) {
                return PROPORTIONAL_MINUS;
            }
            if (hasProportionalPlus) {
                return PROPORTIONAL_PLUS;
            }
        } else if (hasNeutralBehavior) {
            return NEUTRAL;
        }
        return NO_BEHAVIOR;
    }

    private String getSimpleBehaviorName(DataMiningPattern rule) {
        final String behavior = getRuleBehavior(rule);

        if (behavior.equals("--")) {
            return PROPORTIONAL_MINUS;
        } else if (behavior.equals("++")) {
            return PROPORTIONAL_PLUS;
        } else if (behavior.equals("00")) {
            return NEUTRAL;
        } else if (behavior.equals("-+")) {
            return INVERSE_MINUS_PLUS;
        } else if (behavior.equals("-0")) {
            return INVERSE_NEUTRAL_MINUS;
        } else if (behavior.equals("+-")) {
            return INVERSE_PLUS_MINUS;
        } else if (behavior.equals("+0")) {
            return INVERSE_NEUTRAL_PLUS;
        } else if (behavior.equals("0+")) {
            return INVERSE_NEUTRAL_PLUS;
        } else if (behavior.equals("0-")) {
            return INVERSE_NEUTRAL_MINUS;
        } else {
            return NO_BEHAVIOR;
        }
    }

    public Behavior(DataMiningResult dataMiningResult) {
        this.dataMiningResult = dataMiningResult;
    }

    public Behavior(String value, DataMiningResult dataMiningResult) {
        this.value = value;
        this.dataMiningResult = dataMiningResult;
    }

    /**
     * @return the rules
     */
    public List<DataMiningPattern> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(List<DataMiningPattern> rules) {
        this.rules = rules;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the value
     */
    public String getShortValue() {
        if (value.length() >= 3) {
            return value.substring(0, 3);
        } else {
            return value;
        }
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * @return the highestConfidence
     */
    public Double getHighestConfidence() {
        return highestConfidence;
    }

    /**
     * @return the highestConfidence
     */
    public String getFormatedHighestConfidence() {
        return NumberUtil.format(highestConfidence);
    }

    /**
     * @param highestConfidence the highestConfidence to set
     */
    public void setHighestConfidence(Double highestConfidence) {
        this.highestConfidence = highestConfidence;
    }

    private static String getRuleBehavior(DataMiningPattern rule) {
        return DataMiningPattern.getValue(rule.getPrecedent()) + DataMiningPattern.getValue(rule.getConsequent());
    }

    private boolean existsBehavior(List<String> behaviors, String prefix) {
        for (String behavior : behaviors) {
            if (behavior.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public String getAverageSuport() {
        double sum = 0;
        for (DataMiningPattern rule : rules) {
            sum += rule.getSupport()!=null?rule.getSupport():0.0;
        }
        return NumberUtil.format((rules != null && !rules.isEmpty()) ? sum / rules.size() : sum);
    }

    public String getAverageConfidence() {
        double sum = 0;
        for (DataMiningPattern rule : rules) {
            sum += rule.getConfidence()!=null?rule.getConfidence():0.0;
        }
        return NumberUtil.format((rules != null && !rules.isEmpty()) ? sum / rules.size() : sum);
    }

    public String getAverageLift() {
        double sum = 0;
        for (DataMiningPattern rule : rules) {
            sum += rule.getLift()!=null?rule.getLift():0.0;
        }
        return NumberUtil.format((rules != null && !rules.isEmpty()) ? sum / rules.size() : sum);
    }

    /**
     * @return the dataMiningResult
     */
    public DataMiningResult getDataMiningResult() {
        return dataMiningResult;
    }

    /**
     * @param dataMiningResult the dataMiningResult to set
     */
    public void setDataMiningResult(DataMiningResult dataMiningResult) {
        this.dataMiningResult = dataMiningResult;
    }
}
