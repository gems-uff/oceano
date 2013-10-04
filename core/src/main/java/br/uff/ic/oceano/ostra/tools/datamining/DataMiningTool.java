/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.ostra.tools.datamining;

import br.uff.ic.oceano.ostra.exception.DataMiningException;
import br.uff.ic.oceano.ostra.model.DataMiningResult;
import br.uff.ic.oceano.core.tools.Tool;
import br.uff.ic.oceano.ostra.controle.DataMiningControl;

/**
 *
 * @author DanCastellani
 */
public interface DataMiningTool extends Tool {

    public DataMiningResult mine(String arffContent, DataMiningControl dataMiningControl) throws DataMiningException;
}
