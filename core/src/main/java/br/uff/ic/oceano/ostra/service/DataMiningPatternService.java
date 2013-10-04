/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import br.uff.ic.oceano.ostra.model.DataMiningResult;

/**
 *
 * @author DanCastellani
 */
public class DataMiningPatternService {

    public static final String SEPARATOR_ANTECESSOR_SUCESSOR_RULE = "==>";
    public static final String SEPARATOR_VALUE = "=";
    public static final String SPACE = " ";
    public static final String CONFIDENCE_PREFIX = "conf:(";
    public static final String CONFIDENCE_SUFIX = ")";
    public static final String DOT_SPACE = ". ";
    //

    public static DataMiningPattern getDataminingPaternFromOutputStringLineRule(final String lineRule, DataMiningResult dataMiningResult) {
        //getting confidence
        final int confidencePrefixIndice = lineRule.indexOf(CONFIDENCE_PREFIX);
        final int confidenceSufixIndice = lineRule.indexOf(CONFIDENCE_SUFIX);
        final String stringConfidence = lineRule.substring(confidencePrefixIndice + CONFIDENCE_PREFIX.length(), confidenceSufixIndice);
        final double confidence = Double.valueOf(stringConfidence);

        //removes the ruleNumber
        final int indexOfDotSpace = lineRule.indexOf(DOT_SPACE) + DOT_SPACE.length();
        final String almostCleanLineRule = lineRule.substring(indexOfDotSpace, confidencePrefixIndice).trim();

        //getting suport of this rule
        final int lastSpaceBeforeSupportValue = almostCleanLineRule.lastIndexOf(SPACE);
        final String stringSupport = almostCleanLineRule.substring(lastSpaceBeforeSupportValue);
        final double ruleSupport = Double.parseDouble(stringSupport);

        DataMiningPattern dmp = new DataMiningPattern();
        dmp.setDataMiningResult(dataMiningResult);
        dmp.setConfidence(confidence);
        dmp.setSupport(ruleSupport);
        dmp.setPattern(almostCleanLineRule.substring(0, lastSpaceBeforeSupportValue + stringSupport.length()));

        return dmp;
    }
//    public DataMiningPattern getDataminingPaternFromOutputStringLineRule(String lineRule) {
//        //examples
//        //1. dAvg-Weighted Methods Per Class=+ 18 ==> dAvg-Method Lines Of Code=+ 18    conf:(1)
//        //9. #files=1- dAvg-Method Lines Of Code=+ 14 ==> dAvg-Weighted Methods Per Class=+ 14    conf:(1)
//
//        String antecessor = lineRule.split(SEPARATOR_ANTECESSOR_SUCESSOR_RULE)[0];
//        String sucessor = lineRule.split(SEPARATOR_ANTECESSOR_SUCESSOR_RULE)[1];
//
//        List<String> antecessors = new LinkedList<String>();
//        List<String> sucessors = new LinkedList<String>();
//
//        //remove the number and dot in the begining of the line.
//        final int indexOfDot = antecessor.indexOf(".");
//        antecessor = antecessor.substring(indexOfDot);
//
//        while (!antecessor.isEmpty() && antecessor.contains(SEPARATOR_VALUE)) {
//            //the first space after a equal sign separates itens in a rule.
//            final int equalIndex = antecessor.indexOf(SEPARATOR_VALUE);
//            final int itemSeparatorIndex = antecessor.substring(equalIndex).indexOf(SPACE);
//
//            antecessors.add(antecessor.substring(0, itemSeparatorIndex));
//            antecessor = antecessor.substring(itemSeparatorIndex);
//        }
//
//    }
}


