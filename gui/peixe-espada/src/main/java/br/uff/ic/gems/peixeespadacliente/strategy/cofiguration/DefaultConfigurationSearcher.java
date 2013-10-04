/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy.cofiguration;

import br.uff.ic.gems.peixeespadacliente.configuration.Configuration;
import br.uff.ic.gems.peixeespadacliente.service.PeixeEspadaService;
import br.uff.ic.gems.peixeespadacliente.symptom.Symptom;
import java.util.List;

/**
 *
 * @author Gleiph
 */
public class DefaultConfigurationSearcher implements ConfigurationSearcher{

    PeixeEspadaService pes;

    public DefaultConfigurationSearcher() {
        pes = new PeixeEspadaService(null);
    }

    @Override
    public List<Configuration> getConfigurations(List<Symptom> symptoms) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
  
}
