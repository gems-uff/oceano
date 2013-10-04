package br.uff.ic.oceano.core.dao.controle;

import static br.uff.ic.oceano.core.control.ApplicationConstants.MESSAGES_PROPERTIES;
import br.uff.ic.oceano.core.exception.InfraestruturaException;
import br.uff.ic.oceano.util.Output;
import br.uff.ic.oceano.util.ResourceUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAUtil {

    public final static String PERSISTENCE_UNIT_LOCAL = "Local";
    public final static String PERSISTENCE_UNIT_PRODUCTION = "Producao";
    public final static String PERSISTENCE_UNIT_MEMORY = "TesteMemoria";
    public final static String PERSISTENCE_UNIT_LOCALTEMP = "LocalTemp";
    private static String CURRENT_PERSISTENCE_UNIT = null;
    
    private static EntityManagerFactory emf = null;
    private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
    private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

    static{
        CURRENT_PERSISTENCE_UNIT = ResourceUtil.getStringBundleProperties(MESSAGES_PROPERTIES, "currentPersistence");
        Output.println("Persistence unit set from resources to: " + CURRENT_PERSISTENCE_UNIT);
    }
    
    public static void startUp() {
        if(isStarted()){
            return;
        }
        Output.println("========================JPAUTIL STARTUP STARTED=====================");  
        validatePersistenceUnit(getCurrentPersistenceUnit());
        Output.println("Current persistence unit: " + getCurrentPersistenceUnit());
        startUpEntityManagerFactory();
        startUpEntityManager();
        Output.println("========================JPAUTIL STARTUP ENDED=====================");
    }

    public static void shutdown() {
        Output.println("========================JPAUTIL SHUTDOWN STARTED=====================");
        closeEntityManager();
        shutdownEntityManagerFactory();
        Output.println("========================JPAUTIL SHUTDOWN ENDED=====================");
    }

    private static void startUpEntityManagerFactory() {
        validatePersistenceUnit(getCurrentPersistenceUnit());
        try {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory(getCurrentPersistenceUnit());
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    private static void shutdownEntityManagerFactory() {
        try {
            if (emf != null) {
                emf.close();
                emf = null;
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    private static void startUpEntityManager() {
        try {
            EntityManager em = threadEntityManager.get();
            if (em != null) {
                return;
            }
            threadEntityManager.set(emf.createEntityManager());
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    public static boolean isStarted() {
        return emf != null;
    }

    public static EntityManager getEntityManager() {
        try {
            if (!isStarted()) {
                startUp();
            }
            if (!isEntityManagerStarted()) {
                startUpEntityManager();
            }
            return threadEntityManager.get();
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    public static void closeEntityManager() {
        try {
            EntityManager s = threadEntityManager.get();
            threadEntityManager.set(null);
            if (s != null && s.isOpen()) {
                s.close();
            }
            if (isTransactionActive()) {
                rollbackTransaction();
                throw new RuntimeException("EntityManager sendo fechado "
                        + "com transação ativa.");
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    public static void beginTransaction() {
        EntityTransaction tx = threadTransaction.get();
        try {
            if (tx == null) {
                tx = getEntityManager().getTransaction();
                tx.begin();
                threadTransaction.set(tx);
            } else {
//                //System.out.println("Nao criou transacao para="+Thread.currentThread().getName());
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    public static boolean isTransactionActive() {
        EntityTransaction tx = threadTransaction.get();
        return (tx != null && tx.isActive());
    }

    public static void commitTransaction() {
        EntityTransaction tx = threadTransaction.get();
        if (tx != null && tx.isActive()) {
            tx.commit();
        }
        threadTransaction.set(null);
    }

    public static void rollbackTransaction() {	////System.out.println("Vai efetuar rollback de transacao");
        EntityTransaction tx = threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        } finally {
            closeEntityManager();
        }
    }

    private static boolean isEntityManagerStarted() {
        return threadEntityManager.get() != null;
    }

    public static void setCurrentPersistenceUnit(String unit){        
        CURRENT_PERSISTENCE_UNIT = unit;
        Output.println("Persistence unit set to: " + CURRENT_PERSISTENCE_UNIT);
    }
    
    public static String getCurrentPersistenceUnit() {
        return CURRENT_PERSISTENCE_UNIT;
    }

    public static boolean isRunningOnMemoryDB() {
        return getCurrentPersistenceUnit().equalsIgnoreCase(PERSISTENCE_UNIT_MEMORY);
    }

    private static void validatePersistenceUnit(String persitenceUnit) {
        if(persitenceUnit == null){
            throw new InfraestruturaException("Persistence unit is null");
        } else if(!persitenceUnit.equalsIgnoreCase(PERSISTENCE_UNIT_LOCAL)
                && !persitenceUnit.equalsIgnoreCase(PERSISTENCE_UNIT_LOCALTEMP)
                && !persitenceUnit.equalsIgnoreCase(PERSISTENCE_UNIT_MEMORY)
                && !persitenceUnit.equalsIgnoreCase(PERSISTENCE_UNIT_PRODUCTION)){
            throw new InfraestruturaException("Persistence unit is invalid: " + persitenceUnit);
        }
    }
    
}
