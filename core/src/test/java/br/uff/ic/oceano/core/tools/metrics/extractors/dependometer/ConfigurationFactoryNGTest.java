/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.tools.metrics.extractors.dependometer;

import br.uff.ic.oceano.core.tools.metrics.extractors.dependometer.util.ConfigurationFactory;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import br.uff.ic.oceano.core.model.Revision;
import br.uff.ic.oceano.CppProjectsHelper;
import java.io.File;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel
 */
public class ConfigurationFactoryNGTest extends AbstractNGTest{

    protected static final CppProjectsHelper testConstantsCpp = new CppProjectsHelper();

    public ConfigurationFactoryNGTest(){
        super(ConfigurationFactory.class);
    }
    
    /**
     * Teste de método createConfigFile, da classe ConfigurationFactory.
     */
    @Test
    public void testCreateConfigFile() throws Exception {
        println("createConfigFile");
        String path = createTempPath();
        String output = path + "output.xml";
        String configXMlFile = path + "config.xml";
        Revision revision = testConstantsCpp.getNeopzRevision();
        File config = ConfigurationFactory.createConfigFile(revision,output,configXMlFile);
        assertNotNull(config);
    }
}
