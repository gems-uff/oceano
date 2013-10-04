/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Heraclio
 */
public class MetricsLoaderNGTest extends AbstractNGTest{
    

    @Test
    public void testGetSelf() {
        println("getSelf");
        assertNotNull(MetricsLoader.getSelf(), "Null metric loader");
    }

}