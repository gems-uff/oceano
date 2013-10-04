/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ourico.dao.DadosPoliticaDao;
import br.uff.ic.oceano.ourico.dao.impl.DadosPoliticaDaoImpl;
import br.uff.ic.oceano.ourico.model.DadosPolitica;
import java.util.List;

/**
 *
 * @author marapao
 */
public class DadosPoliticaService implements PersistenceService{

    DadosPoliticaDao dadosPoliticaDao;

    public void setup(){
        dadosPoliticaDao = ObjectFactory.getObjectWithDataBaseDependencies(DadosPoliticaDaoImpl.class);
    }

    public DadosPoliticaService() {
    }

    @Transacional
    public void save(DadosPolitica dadosPolitica) {
        if (dadosPolitica.getId() == null) {
            dadosPoliticaDao.inclui(dadosPolitica);
        } else {
            dadosPoliticaDao.altera(dadosPolitica);
        }
    }

    public List<DadosPolitica> getAll(){
        return dadosPoliticaDao.getAll();
    }
}
