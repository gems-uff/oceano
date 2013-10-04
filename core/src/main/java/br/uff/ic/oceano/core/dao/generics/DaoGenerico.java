package br.uff.ic.oceano.core.dao.generics;

import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import java.io.Serializable;
import java.util.List;


/**
 * A interface GenericDao basica com os metodos CRUD. Os metodos
 * de busca sao adicionados por heranca de interface.
 *
 * Interfaces estendidas podem declarar metodos que comecam  por
 * busca...  recuperaCnjuntoDe...  ou recupera...  Estes metodos
 * irao  executar buscas pre-configuradas que sao localizadas em
 * funco do restante do nome dos metodos.
 * 
 */
public interface DaoGenerico<T, PK extends Serializable>
{
    T inclui(T obj);

    T getPorId(PK id) throws ObjetoNaoEncontradoException;

    T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException;
    
    void altera(T obj);

    void exclui(T obj);

    public List<T> getListaCompleta();
}
