/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.dao.impl;

import br.uff.ic.oceano.core.model.QualityAttribute;
import br.uff.ic.oceano.peixeespada.dao.KnowledgeDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.peixeespada.model.Knowledge;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Heliomar
 */
public class KnowledgeDaoImpl extends JPADaoGenerico<Knowledge, Long> implements KnowledgeDao {

    public KnowledgeDaoImpl(){
        super(Knowledge.class);
    }

    @MetodoRecuperaLista
    public List<Knowledge> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaUnico
    public Knowledge getByQualitiatributteAndRefactoring(QualityAttribute qualityAttribute, Refactoring transformacao) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }


    @MetodoRecuperaUnico
    public long getIdByQualitiatributteAndRefactoring(QualityAttribute qualityAttribute, Refactoring transformacao) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

//    public long getIdByMetricaTransformacao(Metric metrica, Refactoring transformacao) {
//        EntityManager em = JPAUtil.getEntityManager();
//        String sql = "select codigo from espada_conhecimento where codigometrica=? and codigotransformacao=?";
//        Query q = em.createNativeQuery(sql);
//        q.setParameter(1, metrica.getId());
//        q.setParameter(2, transformacao.getId());
//
//        Long result = null;
//        try{
//            result =(Long) q.getSingleResult();
//        }catch(NoResultException ex){
//            return 0;
//        }
//
//        return result.longValue();
//    }

}
