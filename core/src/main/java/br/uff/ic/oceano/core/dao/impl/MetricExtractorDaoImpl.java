/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.MetricDao;
import br.uff.ic.oceano.core.dao.MetricExtractorDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.MetricExtractor;
import br.uff.ic.oceano.core.model.SoftwareProject;
import java.util.List;

/**
 *
 * @author dheraclio
 */
public class MetricExtractorDaoImpl extends JPADaoGenerico<MetricExtractor, Long> implements MetricExtractorDao {

    public MetricExtractorDaoImpl() {
        super(MetricExtractor.class);
    }

    @MetodoRecuperaLista
    public List<MetricExtractor> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<MetricExtractor> getMetricExtractorsByMetric(Metric metric) {
        throw new MetodoInterceptadoException();
    }

}
