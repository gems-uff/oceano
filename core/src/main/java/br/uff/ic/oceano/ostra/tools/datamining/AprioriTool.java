/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.tools.datamining;

import br.uff.ic.oceano.util.Logger;
import br.uff.ic.oceano.ostra.controle.DataMiningControl;
import br.uff.ic.oceano.ostra.exception.DataMiningException;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import weka.core.Instances;

/**
 *
 * @author Dancastellani
 */
public class AprioriTool implements DataMiningTool {

    private MyApriori apriori;
    private static boolean executando = false;
    private static final String NAME = "Apriori";
    private static final String RATIONALE = "Apriori is a data mining method that mines Association Rules.";

    public String getRegras() {
        if (apriori == null) {
            return "You must mine before get the rules";
        }

        return apriori.toString();
    }

    public DataMiningResult mine(String arffContent, DataMiningControl dataMiningControl) throws DataMiningException {
        try {
            if (executando) {
                return null;
            }
            executando = true;

            apriori = new MyApriori(dataMiningControl);

            DataMiningResult dmr = new DataMiningResult();
            dmr.setMinedInTime(new Date());
            dmr.setUsedAlgorithmName(NAME);
            dmr.setUsedAlgorithmDescription(RATIONALE);
            dmr.setRuleMetricName(dataMiningControl.getReadableMetricType());
            dmr.setMinConfidence(dataMiningControl.getMinMetric());
            dmr.setMinSupport(dataMiningControl.getMinSup());

            StringReader arffReader = new StringReader(arffContent);
            if (arffReader == null) {
                dmr.setResultData("Não foram minerados padrões.");
                dmr.setArff("Não houve arff de entrada. A base deve estar vazia.");
                return dmr;
            }

            final Instances transacoes = new Instances(arffReader);
            apriori.buildAssociations(transacoes);

            dmr.setNumberOfInstances(transacoes.numInstances());
            dmr.setArff(arffContent);
            dmr.setResultData(getRegras());
            Logger.info("Generation pattern association rules from weka output.");
            dmr.setDataMiningPatterns(apriori.getAssociationRules());
            Logger.info("Patterns generated.");

            return dmr;
        } catch (IOException ex) {
            Logger.error(ex.getMessage());
            throw new DataMiningException(ex);

        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            throw new DataMiningException(ex);

        } finally {
            executando = false;
        }
    }

    public String getName() {
        return NAME;
    }

    public String getRationale() {
        return RATIONALE;
    }
}
