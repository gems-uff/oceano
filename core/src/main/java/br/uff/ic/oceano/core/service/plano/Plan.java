/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.service.plano;

import br.uff.ic.oceano.core.exception.ExecutionPlanException;
import br.uff.ic.oceano.ostra.model.Task;

/**
 *
 * @author DanCastellani
 */
public interface Plan {

    public void execute(Task task) throws ExecutionPlanException;
}
