package br.uff.ic.oceano.core.util;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
/**
 *
 * @author Dancastellani
 */
public class DefaultDatabaseLoaderNGTest extends AbstractNGTest{    

    @BeforeClass
    public static void setUpClass() throws Exception {
        JPAUtil.startUp();
    }

    @Test
    public void insertDefaultData() {
        println("Inserting default data into DB");
        try {
            if (!DefaultDatabaseLoader.isDefaultDataInserted()) {
                DefaultDatabaseLoader.insertDefaultData();
            }
        } catch (Exception ex) {
            fail(ex.getMessage(),ex);
        }
    }
}
