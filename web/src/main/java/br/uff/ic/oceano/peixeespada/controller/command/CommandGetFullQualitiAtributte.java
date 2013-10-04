/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricQualityAttribute;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.core.service.QualityAttributeService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jettison.json.JSONWriter;

/**
 *
 * @author Heliomar
 *
 * Revision by DHeraclio
 *  removed metricManagerClass reference
 */
public class CommandGetFullQualitiAtributte implements Command{

    private QualityAttributeService qualityAttributeService = ObjectFactory.getObjectWithDataBaseDependencies(QualityAttributeService.class);

    /**
     * //TODO check if is necessary to insert metric extractor classes
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        QualityAttribute attribute = qualityAttributeService.getByIdWithMetrics(Long.parseLong(request.getParameter("idQualityAttribute")));

        JSONWriter jSONWriter = new  JSONWriter(response.getWriter());
        jSONWriter.array();
        for (MetricQualityAttribute metricQualityAttribute : attribute.getMetricQualityAttributes()) {
            Metric metric = metricQualityAttribute.getMetric();
            jSONWriter.object()
                    .key("acronym")
                    .value(metric.getAcronym())
                    .key("description")
                    .value(metric.getDescription())
                    .key("extractsFromFont")
                    .value(metric.isExtractsFromFont())
                    .key("preRelease")
                    .value(metric.isPreRelease())
                    .key("extratcsFrom")
                    .value(metric.getExtratcsFrom())
                    .key("metricName")
                    .value(metric.getName())
                    .key("type")
                    .value(metric.getType())
                    .key("factor")
                    .value(metricQualityAttribute.getFactor())
                    .endObject();
        }
        jSONWriter.endArray();

        response.flushBuffer();

    }

}
