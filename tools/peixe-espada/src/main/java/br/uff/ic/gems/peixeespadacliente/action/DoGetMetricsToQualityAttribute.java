package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.model.agent.LocalManagerAgent;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.model.MetricQualityAttribute;
import br.uff.ic.oceano.peixeespada.model.Agent;
import java.util.List;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class DoGetMetricsToQualityAttribute extends AbstractAction {

    @Override
    public LocalManagerAgent execute(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        Translate translate = Translate.getTranslate();
        Agent orchestrator = agentPeixeEspada.getOrchestratorAgent();
        
        agentPeixeEspada.appendMessage(translate.requestMetrics(
            orchestrator.getQualityAttribute().getName()
        ));
        orchestrator.setQualityAttribute(
            clientService.getQualityAttributeWithMetricsAndFactors(agentPeixeEspada)
        );

        StringBuilder stringBuilderFormula = new StringBuilder(translate.formula());
        StringBuilder stringBuilderMetricas = new StringBuilder(translate.metrics());
        List<MetricQualityAttribute> metricQualityAttributes = 
                orchestrator.getQualityAttribute().getMetricQualityAttributes();
        for (MetricQualityAttribute metricQualityAttribute : metricQualityAttributes) {
            stringBuilderFormula
                    .append(metricQualityAttribute.getFactor() > 0 ? " + " : " - ")
                    .append(Math.abs(metricQualityAttribute.getFactor()))
                    .append(" * ")
                    .append(metricQualityAttribute.getMetric().getAcronym());
            stringBuilderMetricas.append(translate.metricItem(
                    metricQualityAttribute.getMetric().getAcronym(), 
                    metricQualityAttribute.getMetric().getName()
            ));
        }
        stringBuilderFormula.append(translate.formulaEnd());
        agentPeixeEspada.appendMessage(stringBuilderMetricas.toString());
        agentPeixeEspada.appendMessage(stringBuilderFormula.toString());

        return agentPeixeEspada;
    }
}
