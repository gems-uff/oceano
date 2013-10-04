package br.uff.ic.oceano.core.dao.generics;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public interface ExecutorDeBuscas<T>
{
    public T busca(Method method, Object[] queryArgs, String namedQuery) 
		throws ObjetoNaoEncontradoException;
    
    public List<T> buscaLista(Method method, Object[] queryArgs, String namedQuery);
    
    public Set<T> buscaConjunto(Method method, Object[] queryArgs, String namedQuery);
    
    public T buscaUltimoOuPrimeiro (Method method, Object[] queryArgs, String namedQuery) 
		throws ObjetoNaoEncontradoException;
    
//    public List buscaListaPaginada(Method metodo, Object[] argumentos, String nomeQuery, String nomeQueryCount, int pageSize);
}
