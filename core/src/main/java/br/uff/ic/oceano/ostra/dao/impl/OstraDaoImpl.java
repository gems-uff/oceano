/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.dao.*;
import br.uff.ic.oceano.ostra.decorator.RevisionMetricValueDto;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.util.NumberUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author daniel
 */
public class OstraDaoImpl implements OstraDao {

    /**
     * From now on, i'll process this request by Java because it's faster.
     * @param project
     * @param metricName
     * @return
     * @deprecated
     */

    @Deprecated
    public List<RevisionMetricValueDto> getProjectMetricValueDto(SoftwareProject project, String metricName) {
        List<RevisionMetricValueDto> returnList = new ArrayList<RevisionMetricValueDto>();

        final String cleanMetricName = metricName.replace(" ", "");

        final StringBuilder strQuery = new StringBuilder();
        strQuery.append("select r.number, r.commiter");
//        strQuery.append("(select mv.doublevalue " +
//                " from metricValue mv " +
//                " where mv.idrevision = r.id and mv.idmetric = ?cleanMetricName?.id) as delta_?cleanMetricName? ");
        strQuery.append(" ,(select sum( CAST(vimv.doublevalue as float) ) " +
                " from ostra_versionedItem vi " +
                "   join ostra_versionedItemMetricValue vimv on vi.id = vimv.idVersionedItem " +
                " where vi.idRevision = r.id and vimv.idMetric = ?cleanMetricName?.id) as sum_?cleanMetricName? ");
        strQuery.append(" ,(select count(vi.id) " +
                " from ostra_versionedItem vi " +
                "   join ostra_versionedItemMetricValue vimv on vi.id = vimv.idVersionedItem " +
                "where vi.idRevision = r.id and vimv.idMetric = ?cleanMetricName?.id) as count_?cleanMetricName? ");
        strQuery.append(" ,(select avg( CAST(vimv.doublevalue as float) ) " +
                " from ostra_versionedItem vi " +
                "   join ostra_versionedItemMetricValue vimv on vi.id = vimv.idVersionedItem " +
                " where vi.idRevision = r.id and vimv.idMetric = ?cleanMetricName?.id) as avg_?cleanMetricName? ");
        strQuery.append(" from revision r join softwareProject p on p.id = r.idproject, metric ?cleanMetricName?");
        strQuery.append(" where p.id = ? and ?cleanMetricName?.name = \'?metricName?\' ");
        strQuery.append(" order by r.number");

        final String strQueryWithParameters = strQuery.toString().replace("?cleanMetricName?", cleanMetricName).replace("?metricName?", metricName);
        Output.println("-----------------");
        Output.println(strQueryWithParameters);
        Output.println("-----------------");

        Query query = JPAUtil.getEntityManager().createNativeQuery(strQueryWithParameters);
        query.setParameter(1, project.getId());

        Iterator result = query.getResultList().iterator();
        while (result.hasNext()) {
            RevisionMetricValueDto dto = new RevisionMetricValueDto();
            Object[] tuple = (Object[]) result.next();
            dto.setRevisionNumber(((BigInteger) tuple[0]).toString());
            dto.setCommiter((String) tuple[1]);

//            if (tuple[2] != null) {
//                dto.setDeltaMetricValue(String.valueOf(tuple[2])); //delta
//            }
            if (tuple[2] != null) {
                dto.setSumMetricValue(NumberUtil.format(((Double) tuple[2]))); //sum
            }
            if (tuple[3] != null) {
                dto.setCountItems(((BigInteger) tuple[3]).toString()); //count
            }
            if (tuple[4] != null) {
                dto.setAvgMetricValue(NumberUtil.format((Double) tuple[4])); //avg
            }

            returnList.add(dto);
        }

        return returnList;
    }
}
