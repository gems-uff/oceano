/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ourico.service;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.ourico.dao.EstadoDao;
import br.uff.ic.oceano.ourico.dao.impl.EstadoDaoImpl;
import br.uff.ic.oceano.ourico.model.Estado;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marapao
 */
public class EstadoService implements PersistenceService{

    private EstadoDao estadoDao;

    public void setup(){
        estadoDao = ObjectFactory.getObjectWithDataBaseDependencies(EstadoDaoImpl.class);
    }

    public EstadoService() {
    }

    @Transacional
    public void save(Estado estado) {
        if (estado.getIdEstado() == null) {
            estadoDao.inclui(estado);
        } else {
            estadoDao.altera(estado);
        }
    }

    public List<Estado> getByAutobranch(Long autobranch){
        return estadoDao.getByAutobranch(autobranch);
    }

    public Estado getByAutobranchDescricao(Long autobranch, String descricao) throws ObjetoNaoEncontradoException{
        return estadoDao.getByAutobranchDescricao(autobranch, descricao);
    }

    public void saveEstado(Date inicio, Date fim, String descricao, String detalhe, String autobranch){
        Estado es = new Estado();
        if(autobranch != null)
            es.setAutobranch(Long.parseLong(autobranch));
        es.setDescricao(descricao);
        es.setDetalhe(detalhe);
        es.setFim(fim);
        es.setInicio(inicio);

        save(es);

    }

}
