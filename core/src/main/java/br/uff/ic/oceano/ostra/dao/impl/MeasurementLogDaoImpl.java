/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao.impl;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.generics.JPADaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.core.exception.MetodoInterceptadoException;
import br.uff.ic.oceano.ostra.dao.MeasurementLogDao;
import br.uff.ic.oceano.ostra.model.MeasurementLog;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public class MeasurementLogDaoImpl extends JPADaoGenerico<MeasurementLog, Long> implements MeasurementLogDao {

    public MeasurementLogDaoImpl() {
        super(MeasurementLog.class);
    }

    @MetodoRecuperaLista
    public List<MeasurementLog> getByProject(SoftwareProject softwareProject) throws ObjetoNaoEncontradoException {
        throw new MetodoInterceptadoException();
    }

}
