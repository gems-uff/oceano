import java.lang.reflect.Method;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import br.uff.ic.oceano.core.exception.*;

import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUnico;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaConjunto;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaLista;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaListaPaginada;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaPrimeiro;
import br.uff.ic.oceano.core.dao.controle.anotations.MetodoRecuperaUltimo;
import br.uff.ic.oceano.core.dao.generics.ExecutorDeBuscas;

public aspect AspectoInterceptadorDao {

    pointcut buscaLista(ExecutorDeBuscas dao) : execution(@MetodoRecuperaLista+ * *.*(..)) && this(dao);
    pointcut buscaSet(ExecutorDeBuscas dao): execution(@MetodoRecuperaConjunto * *.* (..)) && this(dao);
    pointcut busca(ExecutorDeBuscas dao): execution(@MetodoRecuperaUnico * *.* (..)) && this(dao);
    //pointcut listaPaginada(ExecutorDeBuscas dao): execution(@MetodoRecuperaListaPaginada * *.* (..)) && this(dao);

    Object around(ExecutorDeBuscas dao) : buscaSet(dao) {
        Signature sig = thisJoinPoint.getSignature();
        Method metodo = ((MethodSignature)sig).getMethod();
        return dao.buscaConjunto(metodo, thisJoinPoint.getArgs(), metodo.getAnnotation(MetodoRecuperaConjunto.class).namedQuery());
    }

    Object around(ExecutorDeBuscas dao) throws ObjetoNaoEncontradoException : busca(dao)  {
        Signature sig = thisJoinPoint.getSignature();
        Method metodo = ((MethodSignature)sig).getMethod();
        return dao.busca(metodo, thisJoinPoint.getArgs(), metodo.getAnnotation(MetodoRecuperaUnico.class).namedQuery());
    }

    Object around(ExecutorDeBuscas dao): buscaLista(dao){
        Signature sig = thisJoinPoint.getSignature();
        Method metodo = ((MethodSignature)sig).getMethod();
        return dao.buscaLista(metodo, thisJoinPoint.getArgs(), metodo.getAnnotation(MetodoRecuperaLista.class).namedQuery());
    }
/**
    Object around(ExecutorDeBuscas dao): listaPaginada(dao){
        Signature sig = thisJoinPoint.getSignature();
        Method metodo = ((MethodSignature)sig).getMethod();
        return dao.buscaListaPaginada(metodo, thisJoinPoint.getArgs(), metodo.getAnnotation(MetodoRecuperaListaPaginada.class).namedQuery(),
                metodo.getAnnotation(MetodoRecuperaListaPaginada.class).namedQueryCount(),
                metodo.getAnnotation(MetodoRecuperaListaPaginada.class).tamanhoPagina());
    }
*/
}