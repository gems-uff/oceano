/*
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.core.exception.InfraestruturaException;
import framework.agent.Agent;
import br.uff.ic.oceano.model.Revision;
import br.uff.ic.oceano.peixeespada.model.Conhecimento;
import br.uff.ic.oceano.model.Metric;
import br.uff.ic.oceano.peixeespada.model.Transformacao;
import br.uff.ic.oceano.contexto.ContextoAmbiente;
import br.uff.ic.oceano.core.service.impl.ConhecimentoService;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.uff.ic.oceano.factory.ObjectFactory;
import br.uff.ic.oceano.util.MetricaTransformacaoConfiguracao;
import br.uff.ic.oceano.asf.anotacoes.GeraConhecimento;
import br.uff.ic.oceano.util.ThreadPersisteInformacao;

public aspect AspectoAprendizado{

    pointcut aprende() : execution (@GeraConhecimento Revision *..*.*(..));

    Revision around() : aprende() {
throw new RuntimeException("Este metodo deve ser refatorado");
        Revision retorno = proceed();

        MetricaTransformacaoConfiguracao mtc = retorno.getOrigemCriacao();

        ThreadPersisteInformacao pi = new ThreadPersisteInformacao(mtc);

        if (retorno.getValorMetricaAtual() > mtc.getConfiguracaoPai().getValorMetricaAtual()) {
            pi.setSucesso(1);
        }else{
            pi.setSucesso(0);
        }
        pi.start();
        return retorno;
    }

}



*/
