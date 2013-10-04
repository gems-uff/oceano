/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.MetricDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.model.SoftwareProject;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class MetricDaoImpl extends JPADaoGenerico<Metric, Long> implements MetricDao {

    public MetricDaoImpl() {
        super(Metric.class);
    }

    @MetodoRecuperaUnico
    public Metric getByNome(String nome) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Metric> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Metric> getMetricsByProjectRevisions(SoftwareProject project) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Metric> getMetricsByProjectVersionedItems(SoftwareProject project) {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Metric getByAcronym(String acronym) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }
}
