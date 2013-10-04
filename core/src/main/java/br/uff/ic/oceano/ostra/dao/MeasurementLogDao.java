/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.dao;

import br.uff.ic.oceano.core.dao.generics.DaoGenerico;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.model.SoftwareProject;
import br.uff.ic.oceano.ostra.model.MeasurementLog;
import java.util.List;

/**
 *
 * @author DanCastellani
 */
public interface MeasurementLogDao extends DaoGenerico<MeasurementLog, Long> {

    public List<MeasurementLog> getByProject(SoftwareProject softwareProject) throws ObjetoNaoEncontradoException;

}
