package br.uff.ic.oceano;

import br.uff.ic.oceano.contexto.ConstantesAplicacao;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.util.CargaDefaultWeb;
import br.uff.ic.oceano.util.test.AbstractNGTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
public class StartUpDatabase extends AbstractNGTest{

    @BeforeClass
    public static void setUpClass() throws Exception {        
        JPAUtil.startUp();
    }

    @Test
    public void insertDefaultData() {
        if(!CargaDefaultWeb.isDefaultDataInserted()){
            CargaDefaultWeb.insertDefaultData();
        }        
    }
}
