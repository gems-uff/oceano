/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.core.dao.impl;

import br.uff.ic.oceano.core.dao.RefactoringDao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Heliomar
 */
public class RefactoringDaoImpl extends JPADaoGenerico<Refactoring, Long> implements RefactoringDao {

    public RefactoringDaoImpl(){
        super(Refactoring.class);
    }

    @MetodoRecuperaUnico
    public Refactoring getByName(String name) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Refactoring> getAll() {
        throw new MetodoInterceptadoException();
    }

    @MetodoRecuperaLista
    public List<Refactoring> getByType(int tipo) {
        throw new MetodoInterceptadoException();
    }

    public List<Refactoring> getByPopularType(int tipo, Integer quantidade) {

        EntityManager em = JPAUtil.getEntityManager();

        String sql = "select t.id," +
                "t.description," +
                "t.name," +
                "t.tipo," +
                " cast(100*c.totalSucess as float) / cast(c.totalUsed as float) as percent " +
                "from espada_refactoring as t left outer join  espada_knowledge as c " +
                "on (c.idRefactoring = t.id ) " +
                "where t.tipo=? " +
                "order by percent desc ";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, tipo);
        List<Object[]> tabela = q.getResultList();
        List<Refactoring> transformacoesPopulares = new ArrayList<Refactoring>(tabela.size());
        List<Refactoring> transformacoesNaoPopulares = new ArrayList<Refactoring>(tabela.size());

        Refactoring t = null;
        for (Object[] tupla : tabela) {
              t = new Refactoring();
              t.setId(((BigInteger)tupla[0]).longValue());
              t.setDescription(tupla[1].toString());
              t.setName(tupla[2].toString());
              t.setTipo((Integer)tupla[3]);
              Double percent = (Double) tupla[4];
              if(percent != null){
                  transformacoesPopulares.add(t);
              }else{
                  transformacoesNaoPopulares.add(t);
              }
        }
        
        transformacoesPopulares.addAll(transformacoesNaoPopulares);

        List<Refactoring> retorno = new ArrayList<Refactoring>(transformacoesPopulares);
        if(quantidade != null && quantidade.intValue() >= retorno.size()){
            retorno = retorno.subList(0, quantidade);
        }

        return retorno;
    }
}
