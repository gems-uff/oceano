/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.peixeespada.model.Knowledge;
import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public interface KnowledgeDao extends DaoGenerico<Knowledge, Long>{

    public Knowledge getByQualitiatributteAndRefactoring(QualityAttribute qualityAttribute, Refactoring transformacao) throws ObjetoNaoEncontradoException;

    public List<Knowledge> getAll();

    public long getIdByQualitiatributteAndRefactoring(QualityAttribute qualityAttribute, Refactoring transformacao) throws ObjetoNaoEncontradoException;

}
