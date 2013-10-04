/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.decorator.RevisionMetricValueDto;
import java.util.List;

/**
 *
 * @author daniel
 */
public interface OstraDao {

     /**
     * From now on, i'll process this request by Java because it's faster.
     * @param project
     * @param metricName
     * @return
     * @deprecated
     */
    @Deprecated
    public List<RevisionMetricValueDto> getProjectMetricValueDto(SoftwareProject project, String metricNames);

}
