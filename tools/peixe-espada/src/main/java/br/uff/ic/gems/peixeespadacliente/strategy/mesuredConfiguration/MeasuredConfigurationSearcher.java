/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gems.peixeespadacliente.strategy.mesuredConfiguration;

import br.uff.ic.gems.peixeespadacliente.configuration.Configuration;
import java.util.List;

/**
 *
 * @author Gleiph
 */
public interface MeasuredConfigurationSearcher {
    
    public List<Configuration> getMeasuredConfiguration(List<Configuration> measuredConfigurations);
    
}
