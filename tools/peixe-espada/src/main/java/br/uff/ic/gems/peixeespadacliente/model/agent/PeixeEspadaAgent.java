package br.uff.ic.gems.peixeespadacliente.model.agent;

/**
 *
 * @author Heliomar
 */
public abstract class PeixeEspadaAgent {

    protected LocalManagerAgent agentPeixeEspada;

    protected abstract void plan();
    protected abstract void planTesting();


}
