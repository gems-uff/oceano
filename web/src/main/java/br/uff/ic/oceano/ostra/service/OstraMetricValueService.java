/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.service;

import br.uff.ic.oceano.core.dao.MetricValueDao;
import br.uff.ic.oceano.core.dao.RevisionDao;
import br.uff.ic.oceano.core.dao.impl.MetricValueDaoImpl;
import br.uff.ic.oceano.core.dao.impl.RevisionDaoImpl;
import br.uff.ic.oceano.core.factory.MetricManagerFactory;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.decorator.RevisionMetricValueDto;
import br.uff.ic.oceano.core.service.MetricValueService;
import br.uff.ic.oceano.core.tools.metrics.MetricManager;
import br.uff.ic.oceano.util.NumberUtil;
import br.uff.ic.oceano.ostra.dao.VersionedItemMetricValueDao;
import br.uff.ic.oceano.ostra.dao.impl.VersionedItemMetricValueDaoImpl;
import br.uff.ic.oceano.ostra.model.VersionedItemMetricValue;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author daniel
 */
public class OstraMetricValueService extends MetricValueService {

//    private OstraDao ostraDao = ObjectFactory.getObjectWithDataBaseDependencies(OstraDaoImpl.class);
    private MetricValueDao metricValueDao = ObjectFactory.getObjectWithDataBaseDependencies(MetricValueDaoImpl.class);
    private VersionedItemMetricValueDao versionedItemMetricValueDao = ObjectFactory.getObjectWithDataBaseDependencies(VersionedItemMetricValueDaoImpl.class);
    private RevisionDao revisionDao = ObjectFactory.getObjectWithDataBaseDependencies(RevisionDaoImpl.class);

    public OstraMetricValueService() {
        super();
    }

    public List<RevisionMetricValueDto> getProjectMetricsToDetail(SoftwareProject project, String metricName) {
        final Metric metric = ((MetricManager) MetricManagerFactory.getInstance().getMetricByName(metricName)).getMetric();

        final List<RevisionMetricValueDto> dtos = new LinkedList<RevisionMetricValueDto>();
        //this value will be used to store the last avg value during list building to calculate the delta value
        double lastAvgValue = 0;

        if (metric.getExtratcsFrom() == Metric.EXTRACTS_FROM_PROJECT) {
            List<MetricValue> valuesByProjectAndMetric = metricValueDao.getValuesByProjectAndMetric(project, metric, false);
            for (MetricValue metricValue : valuesByProjectAndMetric) {
                final Revision revision = metricValue.getRevision();

                final int count = revision.getNumberOfComittedFiles();
                final Double sum = metricValue.getDoubleValue();
                final Double avg = (count > 0) ? sum / count : sum;
                final Double delta = avg - lastAvgValue;
                lastAvgValue = avg;

                RevisionMetricValueDto dto = new RevisionMetricValueDto("" + revision.getNumber(), revision.getCommiter(), "" + count, revision.getCommitDate(), avg, delta);
                dto.setAvgMetricValue(NumberUtil.format(avg));
                dto.setSumMetricValue(NumberUtil.format(sum));
                dtos.add(dto);
            }

        } else {
            for (Revision revision : revisionDao.getByProject(project)) {
                List<VersionedItemMetricValue> vimvs = versionedItemMetricValueDao.getByRevisionAndMetric(revision, metric);

                Double sum = 0d;
                for (VersionedItemMetricValue versionedItemMetricValue : vimvs) {
                    sum += versionedItemMetricValue.getDoubleValue();
                }
                final Double avg = (vimvs.size() > 0) ? sum / vimvs.size() : sum;

                final Double delta = avg - lastAvgValue;
                lastAvgValue = avg;

                RevisionMetricValueDto dto = new RevisionMetricValueDto("" + revision.getNumber(), revision.getCommiter(), "" + vimvs.size(), revision.getCommitDate(), avg, delta);
                dto.setAvgMetricValue(NumberUtil.format(avg));
                dto.setSumMetricValue(NumberUtil.format(sum));
                dtos.add(dto);
            }
        }

        return dtos;
//        return ostraDao.getProjectMetricValueDto(project, metricName);
    }
}
