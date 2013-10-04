/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.QualityAttributeDao;
import br.uff.ic.oceano.core.dao.impl.QualityAttributeDaoImpl;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricQualityAttribute;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.QualityAttribute;
import static br.uff.ic.oceano.core.model.QualityAttribute.*;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.transiente.FactorMetric;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.core.tools.metrics.MetricEnumeration;
import br.uff.ic.oceano.core.tools.metrics.service.MeasurementService;
import br.uff.ic.oceano.core.tools.metrics.service.MetricService;
import br.uff.ic.oceano.util.file.Archive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Heliomar
 */
public class QualityAttributeService implements PersistenceService{

    protected QualityAttributeDao qualityAttributeDao;
    protected MetricService metricService;

    public static List<String> allAtributtesNames = Arrays.asList(NAME_REUSABILITY, NAME_FLEXIBILITY, NAME_UNDERSTANDIBILITY, NAME_FUNCIONALITY, NAME_EXTENDIBILITY, NAME_EFFECTIVENESS);
    // the key is a string of QUALITY_ATTRIBUTE
    // the value is a List as { [-0,25 ; "DCC"] , [+0,25 ; "CAM"], [+0,25 ; "CIS"], [+0,25 ; "DSC"] }
    private static final Map<String, List<FactorMetric>> evaluationMap;

    public void setup() {
        qualityAttributeDao = ObjectFactory.getObjectWithDataBaseDependencies(QualityAttributeDaoImpl.class);
        metricService = ObjectFactory.getObjectWithDataBaseDependencies(MetricService.class);
    }

    static {
        evaluationMap = new HashMap<String, List<FactorMetric>>();

        evaluationMap.put(NAME_REUSABILITY,
                Arrays.asList(new FactorMetric(-0.25f, MetricEnumeration.DCC.getAcronym()),
                new FactorMetric(0.25f, MetricEnumeration.CAM.getAcronym()),
                new FactorMetric(0.5f, MetricEnumeration.CIS.getAcronym()),
                new FactorMetric(0.5f, MetricEnumeration.DSC.getAcronym())));
        evaluationMap.put(NAME_FLEXIBILITY,
                Arrays.asList(new FactorMetric(0.25f, MetricEnumeration.DAM.getAcronym()),
                new FactorMetric(-0.25f, MetricEnumeration.DCC.getAcronym()),
                new FactorMetric(0.5f, MetricEnumeration.MOA.getAcronym()),
                new FactorMetric(0.5f, MetricEnumeration.NOP.getAcronym())));
        evaluationMap.put(NAME_UNDERSTANDIBILITY,
                Arrays.asList(new FactorMetric(-0.33f, MetricEnumeration.ANA.getAcronym()),
                new FactorMetric(0.33f, MetricEnumeration.DAM.getAcronym()),
                new FactorMetric(-0.33f, MetricEnumeration.DCC.getAcronym()),
                new FactorMetric(0.33f, MetricEnumeration.CAM.getAcronym()),
                new FactorMetric(-0.33f, MetricEnumeration.NOP.getAcronym()),
                new FactorMetric(-0.33f, MetricEnumeration.NOM.getAcronym()),
                new FactorMetric(-0.33f, MetricEnumeration.DSC.getAcronym())));
        evaluationMap.put(NAME_FUNCIONALITY,
                Arrays.asList(new FactorMetric(0.12f, MetricEnumeration.CAM.getAcronym()),
                new FactorMetric(0.22f, MetricEnumeration.NOP.getAcronym()),
                new FactorMetric(0.22f, MetricEnumeration.CIS.getAcronym()),
                new FactorMetric(0.22f, MetricEnumeration.DSC.getAcronym()),
                new FactorMetric(0.22f, MetricEnumeration.NOH.getAcronym())));
        evaluationMap.put(NAME_EXTENDIBILITY,
                Arrays.asList(new FactorMetric(0.5f, MetricEnumeration.ANA.getAcronym()),
                new FactorMetric(-0.5f, MetricEnumeration.DCC.getAcronym()),
                new FactorMetric(0.5f, MetricEnumeration.MFA.getAcronym()),
                new FactorMetric(0.5f, MetricEnumeration.NOP.getAcronym())));
        evaluationMap.put(NAME_EFFECTIVENESS,
                Arrays.asList(new FactorMetric(0.20f, MetricEnumeration.ANA.getAcronym()),
                new FactorMetric(0.20f, MetricEnumeration.DAM.getAcronym()),
                new FactorMetric(0.20f, MetricEnumeration.MOA.getAcronym()),
                new FactorMetric(0.20f, MetricEnumeration.MFA.getAcronym()),
                new FactorMetric(0.20f, MetricEnumeration.NOP.getAcronym())));

    }

    /**
     * <QMOOD>
    Reusability 	( -0.25*DCC + 0.25*CAM + 0.50*CIS + 0.50*DSC )                                  FALTA CAM
    Flexibility 	( +0.25*DAM - 0.25*DCC + 0.50*MOA + 0.50*NOP)
    Undestandibility 	( -0.33*ANA + 0.33*DAM - 0.33*DCC + 0.33*CAM - 0.33*NOP - 0.33*NOM - 0.33*DSC) 	FALTA CAM
    Functionality 	( +0.12*CAM + 0.22*NOP + 0.22*CIS + 0.22*DSC + 0.22*NOH)                        FALTA CAM
    Extendibility 	( +0.50*ANA - 0.50*DCC + 0.50*MFA + 0.50*NOP)
    Effectiveness 	( +0.20*ANA + 0.20*DAM + 0.20*MOA + 0.20* MFA + 0.20*NOP)
     */
    public QualityAttributeService() {
        qualityAttributeDao = ObjectFactory.getObjectWithDataBaseDependencies(QualityAttributeDaoImpl.class);
    }

    public List<MetricQualityAttribute> metricsEvaluationToQualityAttribute(QualityAttribute qualityAttribute) throws ServiceException {
        List<MetricQualityAttribute> metricQualityAttributes = new ArrayList<MetricQualityAttribute>();
        List<FactorMetric> factorMetrics = evaluationMap.get(qualityAttribute.getName());
        for (FactorMetric factorMetric : factorMetrics) {
            MetricQualityAttribute mqa = new MetricQualityAttribute();
            try {
                mqa.setMetric(metricService.getByAcronym(factorMetric.metric));
            } catch (ObjetoNaoEncontradoException ex) {
                throw new ServiceException("The Metric [" + factorMetric.metric + "] does not exist");
            }
            mqa.setQualityAttribute(qualityAttribute);
            mqa.setFactor(factorMetric.factor);
            metricQualityAttributes.add(mqa);
        }
        return metricQualityAttributes;
    }

    @Transacional
    public void save(QualityAttribute qualityAttribute) throws ServiceException {
        if (qualityAttribute.getId() == null) {
            qualityAttribute.setMetricQualityAttributes(metricsEvaluationToQualityAttribute(qualityAttribute));
            qualityAttributeDao.inclui(qualityAttribute);
        } else {
            qualityAttributeDao.altera(qualityAttribute);
        }
    }

    public List<QualityAttribute> getAll() {
        return qualityAttributeDao.getListaCompleta();
    }

    public QualityAttribute getByName(String name) throws ObjetoNaoEncontradoException{
        return qualityAttributeDao.getByName(name);
    }

    public Double calculateValue(Revision revision, QualityAttribute attribute) throws ServiceException {
        List<MetricQualityAttribute> FactorMetrics = attribute.getMetricQualityAttributes();
        MetricManagerFactory  metricManagerFactory = MetricManagerFactory.getInstance(new ArrayList<Metric>(attribute.getMetricsMap().values()));
        Double result = 0d;
        /**
         * Os arquivos de metricas devem ser utilizados apenas para medicoes especificas de tempo
         * Ao fazer o experimento os mesmos n devem ser utilizados
         * Os resultados sao sempre acumulados, portanto, a utilizacao dos mesmos deve ser restrita, de forma a haver entendimento dos dados.
         */
        Archive arquivoGeralMetricas = new Archive(revision.getProject().getConfigurationItem().getName());
        Archive arquivoMetricasResumido = new Archive(revision.getProject().getConfigurationItem().getName()+"_metricas");
        StringBuilder sb = new StringBuilder();

        for (MetricQualityAttribute metricQualityAttribute : FactorMetrics) {

            long tempo = System.currentTimeMillis();
            arquivoGeralMetricas.openAppendAndClose("Antes:"+tempo);
            arquivoGeralMetricas.openAppendAndClose(metricQualityAttribute.getMetric().getAcronym());
            MetricValue metricValue = MeasurementService.extractMetric(metricManagerFactory.getMetricManager(metricQualityAttribute.getMetric()), revision);
            result += metricQualityAttribute.getFactor()*metricValue.getDoubleValue();
            sb.append(result.toString());
            sb.append(";");
            arquivoGeralMetricas.openAppendAndClose("Tempo Gasto: "+(System.currentTimeMillis()-tempo));
            metricQualityAttribute.setMetricValue(metricValue.getDoubleValue());
        }
        arquivoMetricasResumido.openAppendAndClose(sb.toString());
        attribute.setCurrentValue(result);
        return result;
    }

    public QualityAttribute getByIdWithMetrics(long idQualityAtributte) throws ObjetoNaoEncontradoException{
        return qualityAttributeDao.getByIdWithMetrics(idQualityAtributte);
    }
}
