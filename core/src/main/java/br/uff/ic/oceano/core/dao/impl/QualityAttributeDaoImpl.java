/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.QualityAttributeDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.QualityAttribute;
import java.util.List;

/**
 *
 * @author Kann
 */
public class QualityAttributeDaoImpl extends JPADaoGenerico<QualityAttribute, Long> implements QualityAttributeDao {

    public QualityAttributeDaoImpl() {
        super(QualityAttribute.class);
    }


    @MetodoRecuperaLista
    public List<QualityAttribute> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public QualityAttribute getByName(String name) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public QualityAttribute getByIdWithMetrics(long idQualityAtributte) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

}
