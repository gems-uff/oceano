package br.uff.ic.oceano.core.factory;

/**
 *
 * @author Heliokann
 */
import br.uff.ic.oceano.core.exception.InfraestruturaException;
import br.uff.ic.oceano.core.service.PersistenceService;
import java.util.Map;
//import java.util.WeakHashMap;
import java.util.HashMap;

public class ObjectFactory {

    private static final Map<Class, Object> cache = new HashMap<Class, Object>();

    private ObjectFactory() {
    }

    /**
     * Method that finds a Class by name
     */
    private Class find(String name) throws Throwable {
        //Busca a Classe
        Class c = Class.forName(name);
        //Não pode ser uma Interface
        if (c.isInterface()) {
            throw new Exception(
                    "Interface Injection is not suported.");
        }
        return c;
    }

    /**
     * Method that creates instances
     */
    public <T> T getObj(String classe) throws Throwable {
        //A classe é a chave de busca no Map
        Class key = find(classe);
        //Busca se a classe já foi criada
        T retorno = (T) cache.get(key);
        //Testa se o objeto não foi criado
        if (retorno == null) {
            //Log da Criação
//            System.out.println("Criando o Objeto " +key.getName());
            //Cria o objeto
            retorno = (T) key.newInstance();
//            System.out.println("Criou o Objeto " +key.getName());
            //Adiciona ao cache
            cache.put(key, retorno);
//            List l = new ArrayList();
        }
        return retorno;
    }

    public static synchronized <T> T getObjectWithDataBaseDependencies(Class key) {
        Object objectCached = cache.get(key);
        if (objectCached == null) {
            try {
                //Cria o objeto e invoca o método que faz a instanciação das dependências que utilizam o Banco de Dados
                objectCached = key.newInstance();
                if(objectCached instanceof PersistenceService){
                    ((PersistenceService) objectCached ).setup();
                }
            } catch (Throwable ex) {
                System.out.println("        [Erro]-----------------------> " + objectCached);
                InfraestruturaException ie = new InfraestruturaException("Was not possible to instanciate " + key);
                ie.initCause(ex);
                throw ie;
            }
            //Adiciona ao cache
            cache.put(key, objectCached);
        }
        return (T) objectCached;
    }

    public static synchronized <T> T getObjectWithoutDataBaseDependencies(Class key) {
        Object retorno = cache.get(key);
        if (retorno == null) {
            try {
                //Cria o objeto
                retorno = key.newInstance();
            } catch (Throwable ex) {
                System.out.println("        [Erro]-----------------------> " + retorno);
                InfraestruturaException ie = new InfraestruturaException("Was not possible to instanciate " + key);
                ie.initCause(ex);
                throw ie;
            }
            //Adiciona ao cache
            cache.put(key, retorno);
        }
        return (T) retorno;
    }
}
