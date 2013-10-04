/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.MetricValueDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricValue;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.util.NumberUtil;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author DanCastellani
 */
public class MetricValueDaoImpl extends JPADaoGenerico<MetricValue, Long> implements MetricValueDao {

    public MetricValueDaoImpl() {
        super(MetricValue.class);
    }

    @MetodoRecuperaLista
    public List<MetricValue> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<MetricValue> getAllSortedByConfigurationAndMetric() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Long getMetricValueId(Revision revision, Metric metric, boolean delta) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    public long countByRevisionAndMetric(Revision revision, Metric metric) throws Exception{
        final String strQuery = "select count( distinct mv.id ) from MetricValue mv where mv.idrevision = ? and mv.idMetric = ? and mv.delta = false";
        if(revision.getId() == null || metric.getId() == null){
            throw new Exception("Null id");            
        }
        
        Query query = JPAUtil.getEntityManager().createNativeQuery(strQuery);
        query.setParameter(1, revision.getId());
        query.setParameter(2, metric.getId());        
        
        return NumberUtil.longValueOf(query.getSingleResult());
    }

    @MetodoRecuperaLista
    public List<MetricValue> getByRevision(Revision revision) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<MetricValue> getValuesByProjectAndMetric(SoftwareProject softwareProject, Metric metric, boolean isDelta) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public MetricValue getByRevisionMetricAndDelta(Revision revision, Metric metric, boolean delta) throws ObjetoNaoEncontradoException{
        throw new MetodoInterceptadoException();
    }

}
