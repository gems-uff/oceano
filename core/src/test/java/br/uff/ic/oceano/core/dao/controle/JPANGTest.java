/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.core.dao.controle;

import br.uff.ic.oceano.util.test.AbstractNGTest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 */
public class JPANGTest extends AbstractNGTest {

    @Test
    public void testJPAStartUpAndShutdown(){
        JPAUtil.startUp();
        JPAUtil.shutdown();
    }

    @Test
    public void testConnectionStringToDB() {
        try {
            Connection connection = null;
            String connectionString;
            if (JPAUtil.getCurrentPersistenceUnit().equals(JPAUtil.PERSISTENCE_UNIT_LOCAL)) {
                connectionString = "jdbc:postgresql://localhost:5432/oceano";
                connection = DriverManager.getConnection(connectionString, "postgres", "postgres");
            } else if (JPAUtil.getCurrentPersistenceUnit().equals(JPAUtil.PERSISTENCE_UNIT_LOCALTEMP)) {
                connectionString = "jdbc:postgresql://localhost:5432/oceanotemp";
                connection = DriverManager.getConnection(connectionString, "postgres", "postgres");
            } else if (JPAUtil.getCurrentPersistenceUnit().equals(JPAUtil.PERSISTENCE_UNIT_MEMORY)) {
                connectionString = "jdbc:hsqldb:mem:oceano";
                connection = DriverManager.getConnection(connectionString);
            }            
            assertTrue(connection != null, "Failed to make connection!");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Connection Failed! Check output console above");
        }


    }
}
