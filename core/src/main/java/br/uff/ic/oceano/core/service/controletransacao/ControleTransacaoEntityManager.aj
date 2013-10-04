import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.InfraestruturaException;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;

public aspect ControleTransacaoEntityManager{
    
    // Inserindo variavel contadora na classe JPAUtil
    // toda chamada ao beginTransaction incrementara o contador
    // no finally de um método transacional decrementa o contador
    // Dessa forma sabemos que o Metodo Transacional eh Raiz caso o contador seja 1
    // ou seja ele incrementou o contador, executou o método e ainda nao fechou
    // o entityManager onde crecrementa o contador 
    // somente o metodo raiz pode comitar ou fazer roollback
    private static final ThreadLocal<Long> JPAUtil.threadCount = new ThreadLocal<Long>();
    
      private static synchronized void somaContador(){
        Long count = JPAUtil.threadCount.get();
        if(count == null) count = 0L;
        count++;
        JPAUtil.threadCount.set(count);
    }

      private static synchronized void decrementaContador(){
        Long count = JPAUtil.threadCount.get();
        if(count == null) count = 0L;
        // Caso lance execao no commit vai tentar decrementar novamente
        if(count>0) {
            count--;
            JPAUtil.threadCount.set(count);
        }
    }
    
    public static boolean isMetodoTansacionalRaiz(){
        Long count = JPAUtil.threadCount.get();
        return (count!= null && count ==1);
    }
    
    public static boolean podeFecharEntityManager(){
        Long count = JPAUtil.threadCount.get();
        return (count== null || count ==0);
    }
    
    pointcut transacao() : execution (@Transacional * br.uff.ic..*.service..*.*(..));
    
    pointcut entityManager() : execution (!@Transacional *  br.uff.ic.oceano..*.service..*.*(..))
    //&& !within(br.uff.ic.oceano.core.service.BaseService)
    && !within(br.uff.ic.oceano.core.service.aspecto..*)
    && !within(ControleTransacaoEntityManager);
    
    Object around() throws RuntimeException : transacao() {
        Object retorno = null;
        try{
            JPAUtil.beginTransaction();
            
            somaContador();
            retorno = proceed();
            if(isMetodoTansacionalRaiz()){
                JPAUtil.commitTransaction();
            }
        } catch (RuntimeException t){
            try {
                if(isMetodoTansacionalRaiz()){
                    JPAUtil.rollbackTransaction();
                }
            } catch (InfraestruturaException ie) {
            }
            throw t;

        } finally {
            try {
                decrementaContador();
                if(podeFecharEntityManager()){
                    JPAUtil.closeEntityManager();
                }    
            } catch (InfraestruturaException ie) {
                System.out.println("InfraestruturaException  no closeEntityManager() do JPAUtil");
            }
        }
        return retorno;
    }
    
    
    after(): entityManager() {
        if(podeFecharEntityManager()){
            JPAUtil.closeEntityManager();
        }    
    }

}



