/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service;

import br.uff.ic.oceano.core.dao.RefactoringDao;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.impl.RefactoringDaoImpl;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import java.util.List;

/**
 *
 * @author Heliomar
 */
public class RefactoringService implements PersistenceService{

    private RefactoringDao refactoringDao;

    public void setup(){
        refactoringDao = ObjectFactory.getObjectWithDataBaseDependencies(RefactoringDaoImpl.class);
    }

    @Transacional
    public void save(Refactoring t) {
        refactoringDao.inclui(t);
    }

    public List<Refactoring> getTransformacoesPorTipo(int tipo) {
        return refactoringDao.getByType(tipo);
    }

    public List<Refactoring> getTransformacoesPopulares(int tipo, Integer quantidade) {
        return refactoringDao.getByPopularType(tipo, quantidade);
    }

    public List<Refactoring> getAll() {
        return refactoringDao.getAll();
    }

}
