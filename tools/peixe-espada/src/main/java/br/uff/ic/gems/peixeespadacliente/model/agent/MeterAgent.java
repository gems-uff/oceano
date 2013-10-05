package br.uff.ic.gems.peixeespadacliente.model.agent;

import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.MetricQualityAttribute;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.core.service.QualityAttributeService;
import java.util.ArrayList;
import java.util.List;
import translation.Translate;

/**
 *
 * @author Heliomar
 */
public class MeterAgent extends PeixeEspadaAgent {

    protected QualityAttributeService qualityAttributeService = ObjectFactory.getObjectWithoutDataBaseDependencies(QualityAttributeService.class);

    @Override
    protected void plan() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void planTesting() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mensureQualityAttribute(QualityAttribute originalQualityAttribute) {
    }

    public void calculeteFirstQualityAttributeValue(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        try {
            Translate translate = Translate.getTranslate();
            
            qualityAttributeService.calculateValue(agentPeixeEspada.getDefaultRevision(), agentPeixeEspada.getOrchestratorAgent().getQualityAttribute());
            // copy values from calculated quality attribute to next normalizations
            copyQualityAttribute(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute(), agentPeixeEspada.getFirstQualityAttribute());
            copyQualityAttribute(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute(), agentPeixeEspada.getCurrentQualityWithoutNormalize());
            agentPeixeEspada.appendMessage(translate.firstMeasurement());
            printQualityAttribute(agentPeixeEspada, agentPeixeEspada.getFirstQualityAttribute());
            agentPeixeEspada.appendMessage(translate.normalizedValue());

            getNormalizedQualitiAttribute(agentPeixeEspada.getFirstQualityAttribute(), agentPeixeEspada.getOrchestratorAgent().getQualityAttribute());
            agentPeixeEspada.setCurrentQualityAttributeValue(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute().getCurrentValue());

        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     *
     * Copy the values from qualityAttributeOriginal to qualityAttributeResulting
     * 
     * @param qualityAttributeOriginal
     * @param qualityAttributeResulting
     *
     */
    private void copyQualityAttribute(QualityAttribute qualityAttributeOriginal, QualityAttribute qualityAttributeResulting) {
        qualityAttributeResulting.setDescricao(qualityAttributeOriginal.getDescricao());
        qualityAttributeResulting.setName(qualityAttributeOriginal.getName());
        qualityAttributeResulting.setCurrentValue(qualityAttributeOriginal.getCurrentValue());
        List<MetricQualityAttribute> metricQualityAttributes = new ArrayList<MetricQualityAttribute>(qualityAttributeOriginal.getMetricQualityAttributes().size());
        for (MetricQualityAttribute metricQualityAttribute : qualityAttributeOriginal.getMetricQualityAttributes()) {
            MetricQualityAttribute metricQuali = new MetricQualityAttribute();
            metricQuali.setFactor(metricQualityAttribute.getFactor());
            metricQuali.setMetricValue(metricQualityAttribute.getMetricValue());
            metricQuali.setMetric(metricQualityAttribute.getMetric());
            metricQualityAttributes.add(metricQuali);
        }
        qualityAttributeResulting.setMetricQualityAttributes(metricQualityAttributes);
    }

    public void calculateNormalizedQualityAttributeValueSettingImprove(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        QualityAttribute retornedQualityAttribute = new QualityAttribute();
        copyQualityAttribute(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute(), retornedQualityAttribute);
        try {
            Double calculedValue = qualityAttributeService.calculateValue(agentPeixeEspada.getDefaultRevision(), retornedQualityAttribute);

//            agentPeixeEspada.appendMessage("\nNÃO NORMALIZADO");
//            printQualityAttribute(agentPeixeEspada, calculedValue);

            retornedQualityAttribute = getNormalizedQualitiAttribute(agentPeixeEspada.getFirstQualityAttribute(), retornedQualityAttribute);

            agentPeixeEspada.appendMessage(Translate.getTranslate().normalized());
            printQualityAttribute(agentPeixeEspada, retornedQualityAttribute);

            calculedValue = retornedQualityAttribute.getCurrentValue();

            if (calculedValue.compareTo(agentPeixeEspada.getCurrentQualityAttributeValue()) > 0) {
                setCurrentQualityAttributeBySucess(agentPeixeEspada, retornedQualityAttribute);
            } else if (calculedValue.compareTo(agentPeixeEspada.getCurrentQualityAttributeValue()) < 0) {
                agentPeixeEspada.setHasImproved(false);
            } else {
                agentPeixeEspada.setHasImproved(null);
            }
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    public QualityAttribute calculateNormalizedQualityAttributeValue(LocalManagerAgent agentPeixeEspada) throws ServiceException {
        QualityAttribute retornedQualityAttribute = new QualityAttribute();
        copyQualityAttribute(agentPeixeEspada.getOrchestratorAgent().getQualityAttribute(), retornedQualityAttribute);
        try {
            qualityAttributeService.calculateValue(agentPeixeEspada.getDefaultRevision(), retornedQualityAttribute);

            retornedQualityAttribute = getNormalizedQualitiAttribute(agentPeixeEspada.getFirstQualityAttribute(), retornedQualityAttribute);

            return retornedQualityAttribute;
        } catch (Exception ex) {
            throw new ServiceException(ex);
        }
    }

    private Double calculateQualityAttributeValue(QualityAttribute qualityAttribute) {
        Double value = 0d;
        for (MetricQualityAttribute metricQualityAttribute : qualityAttribute.getMetricQualityAttributes()) {
            value += metricQualityAttribute.getMetricValue() * metricQualityAttribute.getFactor();
        }
        return value;
    }

    private QualityAttribute getNormalizedQualitiAttribute(QualityAttribute baseQualityAttribute, QualityAttribute notNormalizedQualityAttribute) {
        int bound = baseQualityAttribute.getMetricQualityAttributes().size();
        List<MetricQualityAttribute> listNotNormalized = notNormalizedQualityAttribute.getMetricQualityAttributes();
        List<MetricQualityAttribute> baseList = baseQualityAttribute.getMetricQualityAttributes();
        for (int i = 0; i < bound; i++) {
            listNotNormalized.get(i).setMetricValue(listNotNormalized.get(i).getMetricValue() / baseList.get(i).getMetricValue());
        }
        notNormalizedQualityAttribute.setCurrentValue(calculateQualityAttributeValue(notNormalizedQualityAttribute));
        return notNormalizedQualityAttribute;
    }

    private QualityAttribute getBeforeNormalizeQualitiAttribute(QualityAttribute baseQualityAttribute, QualityAttribute normalizedQualityAttribute) {
        int bound = baseQualityAttribute.getMetricQualityAttributes().size();
        List<MetricQualityAttribute> listNotNormalized = normalizedQualityAttribute.getMetricQualityAttributes();
        List<MetricQualityAttribute> baseList = baseQualityAttribute.getMetricQualityAttributes();
        for (int i = 0; i < bound; i++) {
            listNotNormalized.get(i).setMetricValue(listNotNormalized.get(i).getMetricValue() * baseList.get(i).getMetricValue());
        }
        normalizedQualityAttribute.setCurrentValue(calculateQualityAttributeValue(normalizedQualityAttribute));
        return normalizedQualityAttribute;
    }

    public void printQualityAttribute(LocalManagerAgent agentPeixeEspada, QualityAttribute qualityAttributeValue) {
        Translate translate = Translate.getTranslate();
        
        agentPeixeEspada.appendMessage(translate.measuredValue(qualityAttributeValue.getCurrentValue()));
        agentPeixeEspada.appendMessage(translate.oldValue(agentPeixeEspada.getCurrentQualityAttributeValue()));

        StringBuilder stringBuilderMetricas = new StringBuilder(translate.metricValues());
        List<MetricQualityAttribute> metricQualityAttributes = qualityAttributeValue.getMetricQualityAttributes();
        for (MetricQualityAttribute metricQualityAttribute : metricQualityAttributes) {
            stringBuilderMetricas.append(
                    translate.metricValuesItem(
                        metricQualityAttribute.getMetric().getAcronym(),
                        metricQualityAttribute.getMetricValue()
                    ));
        }
        agentPeixeEspada.appendMessage(stringBuilderMetricas.toString());
        stringBuilderMetricas = null;
    }

    public boolean hasImprove() {
        return true;
    }

    public void setCurrentQualityAttributeBySucess(LocalManagerAgent agentPeixeEspada, QualityAttribute calculedValue) {
        agentPeixeEspada.getOrchestratorAgent().setQualityAttribute(calculedValue);
        agentPeixeEspada.setCurrentQualityAttributeValue(calculedValue.getCurrentValue());
        copyQualityAttribute(getBeforeNormalizeQualitiAttribute(agentPeixeEspada.getFirstQualityAttribute(), calculedValue), agentPeixeEspada.getCurrentQualityWithoutNormalize());
        agentPeixeEspada.setHasImproved(true);
    }

    public Boolean hasImproved(LocalManagerAgent agentPeixeEspada, QualityAttribute calculedValue) {
        if (calculedValue.getCurrentValue().compareTo(agentPeixeEspada.getCurrentQualityAttributeValue()) > 0) {
            return true;
        } else if (calculedValue.getCurrentValue().compareTo(agentPeixeEspada.getCurrentQualityAttributeValue()) < 0) {
            return false;
        }
        return null;
    }
}
