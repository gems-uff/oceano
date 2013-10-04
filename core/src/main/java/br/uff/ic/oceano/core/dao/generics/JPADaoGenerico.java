package br.uff.ic.oceano.core.dao.generics;

import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.InfraestruturaException;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * A implementação de um DAO genérico para a JPA
 * Uma implementação "typesafe" dos métodos CRUD e dos métodos de busca.
 */
public class JPADaoGenerico<T, PK extends Serializable>
        implements DaoGenerico<T, PK>, ExecutorDeBuscas<T> {

    private Class<T> tipo;

    public JPADaoGenerico(Class<T> tipo) { 	// //System.out.println("**************>>>>  Executou construtor de JPADaoGenerico");
        this.tipo = tipo;
    }

    public Class<T> getTipo() {
        return tipo;
    }

    @SuppressWarnings("unchecked")
    public final T inclui(T o) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.persist(o);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }

        return o;
    }

    public final void altera(T o) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.merge(o);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    public final void exclui(T o) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.remove(o);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    public final T getPorId(PK id) throws ObjetoNaoEncontradoException {
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            t = (T) em.find(tipo, id);

            if (t == null) {
                throw new ObjetoNaoEncontradoException();
            }
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
        return t;
    }

    public final T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException {
        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            t = (T) em.find(tipo, id);

            if (t != null) {
                em.lock(t, LockModeType.READ);
                em.refresh(t);
            } else {
                throw new ObjetoNaoEncontradoException();
            }
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }

        return t;
    }

    @SuppressWarnings("unchecked")
    public final T busca(Method metodo, Object[] argumentos, String nomeQuery)
            throws ObjetoNaoEncontradoException {
        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            EntityManager em = JPAUtil.getEntityManager();
            Query namedQuery = em.createNamedQuery(nomeDaBusca);

            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg);  // Par?metros de buscas s?o 1-based.
                }
            }
            return (T) namedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ObjetoNaoEncontradoException(e);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public final T buscaUltimoOuPrimeiro(Method metodo,
            Object[] argumentos, String nomeQuery)
            throws ObjetoNaoEncontradoException {

        EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try {
            List lista;
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);

            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg);
                }
            }
            lista = namedQuery.getResultList();

            t = (lista.size() == 0) ? null : (T) lista.get(0);

            if (t == null) {
                throw new ObjetoNaoEncontradoException();
            }

            return t;
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public final List<T> buscaLista(Method metodo,
            Object[] argumentos, String nomeQuery) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);

            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg); // Par?metros de buscas s?o 1-based.

                }
            }
            return (List<T>) namedQuery.getResultList();
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public final Set<T> buscaConjunto(Method metodo,
            Object[] argumentos, String nomeQuery) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            if (nomeQuery != null && !nomeQuery.equals("")) {
                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
            }
            Query namedQuery = em.createNamedQuery(nomeDaBusca);

            if (argumentos != null) {
                for (int i = 0; i < argumentos.length; i++) {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i + 1, arg); // Par?metros de buscas s?o 1-based.
                }
            }

            List<T> lista = namedQuery.getResultList();

            return new LinkedHashSet(lista);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    public String getNomeDaBuscaPeloMetodo(Method metodo) {
        return tipo.getSimpleName() + "." + metodo.getName();
    }

//    @SuppressWarnings("unchecked")
//    public final List buscaListaPaginada(Method metodo,
//            Object[] argumentos, String nomeQuery, String nomeQueryCount, int pageSize) {
//        EntityManager em = JPAUtil.getEntityManager();
//
//        String nomeDaBusca = null;
//
//        try {
//
//            nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
//
//            if (nomeQuery != null && !nomeQuery.equals("")) {
//                nomeDaBusca = tipo.getSimpleName() + "." + nomeQuery;
//            }
//
//            String nomeDoCount = nomeDaBusca+"Count";
//
//            if (nomeQueryCount != null && !nomeQueryCount.equals("")) {
//                nomeDoCount = tipo.getSimpleName() + "." + nomeQueryCount;
//            }
//
//            Query namedQueryCount = null;
//
//            namedQueryCount = em.createNamedQuery(nomeDoCount);
//
//            if (argumentos != null) {
//                for (int i = 0; i < argumentos.length; i++) {
//                    Object arg = argumentos[i];
//                    namedQueryCount.setParameter(i + 1, arg); // Par?metros de buscas s?o 1-based.
//                }
//            }
//
//            long tamanhoTotal = (Long) namedQueryCount.getSingleResult();
//
//            return new ListaComPaginacao(nomeDaBusca, argumentos, pageSize, tamanhoTotal,true);
//
//        } catch(PersistenceException pe) {
//            pe.printStackTrace();
//            throw new InfraestruturaException("A NAMED QUERY DO COUNT DEVE SER CRIADA! O SEU NOME DEVE SER: "+nomeDaBusca+"Count");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            throw new InfraestruturaException(e);
//        }
//    }
    public static <T> List<T> executaQuery(String hql, Object[] parametros, int inicio, int quantidade, boolean namedQuery) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Query query = null;
            if (namedQuery) {
                query = em.createNamedQuery(hql);
            } else {
                query = em.createQuery(hql);
            }

            for (int i = 0; i < parametros.length; i++) {
                Object arg = parametros[i];
                query.setParameter(i + 1, arg); // Par?metros de buscas s?o 1-based.
            }
            return query.setFirstResult(inicio).setMaxResults(quantidade).getResultList();

        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            JPAUtil.closeEntityManager();
        }
        return null;
    }

    public static <T> void executeUpdate(String updateQuery, Object[] parameters) {
        try {
            EntityManager em = JPAUtil.getEntityManager();
            Query query = em.createNativeQuery(updateQuery);

            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    Object arg = parameters[i];
                    query.setParameter(i + 1, arg); // Par?metros de buscas s?o 1-based.
                }
            }

            query.executeUpdate();
        } finally {
            JPAUtil.closeEntityManager();
        }
    }

    public List<T> getListaCompleta() {
        EntityManager em = JPAUtil.getEntityManager();
        Query query = em.createQuery("select x from "+tipo.getName()+" x");
        return query.getResultList();
    }
}
