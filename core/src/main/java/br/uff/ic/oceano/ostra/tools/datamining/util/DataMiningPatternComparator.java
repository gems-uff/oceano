/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.tools.datamining.util;

import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import java.util.Comparator;

/**
 *
 * @author DanCastellani
 */
public class DataMiningPatternComparator implements Comparator<DataMiningPattern> {

    int metric = DataMiningControl.LIFT;

    /**
     * Defines the metric osed to order the results.
     * 
     * @param dataMiningMetric the metric passed shoud be one from DataMiningControl, like DataMiningControl.LIFT.
     */
    public void orderByMetric(int dataMiningMetric) {
        metric = dataMiningMetric;
    }

    public int compare(DataMiningPattern o1, DataMiningPattern o2) {
        if (o1 == null) {
            throw new NullPointerException("o1 cannot be null");
        }
        if (o2 == null) {
            throw new NullPointerException("o2 cannot be null");
        }
        Double mO1 = null;
        Double mO2 = null;

        switch (metric) {
            case DataMiningControl.LIFT: {
                mO1 = o1.getLift();
                mO2 = o2.getLift();
                break;
            }
            case DataMiningControl.CONFIDENCE: {
                mO1 = o1.getConfidence();
                mO2 = o2.getConfidence();
                break;
            }
            case DataMiningControl.CONVICTION: {
                mO1 = o1.getConviction();
                mO2 = o2.getConviction();
                break;
            }
            case DataMiningControl.LEVERAGE: {
                mO1 = o1.getLeverage();
                mO2 = o2.getLeverage();
                break;
            }
        }
        //this means that confiddence was used.
        //As it is hardcoded to use lift when invoing this method
        //lets change to confiddence that is never null
        if (mO1 == null || mO2 == null) {
            mO1 = o1.getConfidence();
            mO2 = o2.getConfidence();
        }

        return mO2.compareTo(mO1);
    }
}
