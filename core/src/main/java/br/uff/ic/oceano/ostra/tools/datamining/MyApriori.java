/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.tools.datamining;

import br.uff.ic.oceano.util.Logger;
import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import br.uff.ic.oceano.ostra.exception.DataMiningException;
import br.uff.ic.oceano.ostra.model.DataMiningPattern;
import java.util.LinkedList;
import java.util.List;
import weka.associations.Apriori;
import weka.associations.ItemSet;

/**
 *
 * @author DanCastellani
 */
public class MyApriori extends Apriori {

    public static final String ERROR_NO_RULES_FOUND = "No large itemsets and rules found!";

    public MyApriori() {
        super();
    }

    public MyApriori(DataMiningControl dataMiningControl) {
        super();
        m_verbose = true;
        setLowerBoundMinSupport(dataMiningControl.getMinSup());
        setUpperBoundMinSupport(dataMiningControl.getMaxSup());
        setDelta(0.01d);

        setOutputItemSets(true);
        setSignificanceLevel(-1d);

        setMetricType(dataMiningControl.getSelectedTag());
        setMinMetric(dataMiningControl.getMinMetric());

        setNumRules(dataMiningControl.getMaxRules());
    }

    public List<DataMiningPattern> getAssociationRules() throws DataMiningException {
        final List<DataMiningPattern> returnRules = new LinkedList<DataMiningPattern>();

        if (m_Ls == null || m_Ls.size() <= 1) {
            throw new DataMiningException(ERROR_NO_RULES_FOUND);
        }

        DataMiningPattern rule;
        String pattern;

        for (int i = 0; i < m_allTheRules[0].size(); i++) {
            rule = new DataMiningPattern();
            pattern = ((ItemSet) m_allTheRules[0].elementAt(i)).toString(m_instances) + " ==> " + ((ItemSet) m_allTheRules[1].elementAt(i)).toString(m_instances);

            rule.setPattern(pattern);
            rule.setSupport(new Double(((ItemSet) m_allTheRules[1].elementAt(i)).support()));
            rule.setConfidence((Double) m_allTheRules[2].elementAt(i));

            if (m_metricType != CONFIDENCE || m_significanceLevel != -1) {
                rule.setLift((Double) m_allTheRules[3].elementAt(i));
                rule.setLeverage((Double) m_allTheRules[4].elementAt(i));
                rule.setConviction((Double) m_allTheRules[5].elementAt(i));
            }

            Logger.debug("Adding rule: " + rule.toString());
            returnRules.add(rule);
        }
        return returnRules;
    }
}
