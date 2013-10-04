/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.ourico.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.ourico.model.DadosPolitica;
import java.util.List;

/**
 *
 * @author marapao
 */
public interface DadosPoliticaDao extends DaoGenerico<DadosPolitica, Long> {

    public List<DadosPolitica> getAll();

}
