/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.ourico.dao.DadosPoliticaDao;
import br.uff.ic.oceano.ourico.model.DadosPolitica;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author marapao
 */
public class DadosPoliticaDaoImpl extends JPADaoGenerico<DadosPolitica, Long> implements DadosPoliticaDao  {

    public DadosPoliticaDaoImpl() {
        super(DadosPolitica.class);
    }


    @MetodoRecuperaLista
    public List<DadosPolitica> getAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
