/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.QualityAttribute;
import java.util.List;

/**
 *
 * @author Kann
 */
public interface QualityAttributeDao extends DaoGenerico<QualityAttribute, Long> {

    public List<QualityAttribute> getAll();

    public QualityAttribute getByName(String name) throws ObjetoNaoEncontradoException;

    public QualityAttribute getByIdWithMetrics(long idQualityAtributte) throws ObjetoNaoEncontradoException;

}
