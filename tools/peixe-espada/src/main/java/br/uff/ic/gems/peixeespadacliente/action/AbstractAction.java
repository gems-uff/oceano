package br.uff.ic.gems.peixeespadacliente.action;

import br.uff.ic.gems.peixeespadacliente.service.ClientService;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.core.service.QualityAttributeService;

/**
 *
 * @author Heliomar
 */
public abstract class AbstractAction implements Action {

    //services
    protected ClientService clientService = ObjectFactory.getObjectWithoutDataBaseDependencies(ClientService.class);
    protected QualityAttributeService qualityAttributeService = ObjectFactory.getObjectWithoutDataBaseDependencies(QualityAttributeService.class);
}
