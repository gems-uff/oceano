/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.oceano.peixeespada.service;

import br.uff.ic.oceano.peixeespada.dao.KnowledgeDao;
import br.uff.ic.oceano.core.exception.ObjetoNaoEncontradoException;
import br.uff.ic.oceano.core.exception.ServiceException;
import br.uff.ic.oceano.core.factory.ObjectFactory;
import br.uff.ic.oceano.peixeespada.model.Knowledge;
import br.uff.ic.oceano.core.model.Metric;
import br.uff.ic.oceano.core.service.PersistenceService;
import br.uff.ic.oceano.peixeespada.model.Refactoring;
import br.uff.ic.oceano.core.service.controletransacao.Transacional;
import br.uff.ic.oceano.peixeespada.dao.impl.KnowledgeDaoImpl;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heliomar
 */
public class KnowledgeService implements PersistenceService {

    private KnowledgeDao knowledgeDao;

    public void setup() {
        knowledgeDao = ObjectFactory.getObjectWithDataBaseDependencies(KnowledgeDaoImpl.class);
    }

    public KnowledgeService() {
    }

    public List<Knowledge> getListaCompleta() {
        return knowledgeDao.getAll();
    }

    @Transacional
    public Knowledge salvar(Knowledge knowledge) throws ServiceException {
        long idConhecimento;
        try {
            idConhecimento = knowledgeDao.getIdByQualitiatributteAndRefactoring(knowledge.getQualityAttribute(), knowledge.getRefactoring());
            notify();
            return alterarConhecimento(idConhecimento, knowledge);
        } catch (ObjetoNaoEncontradoException ex) {
            return knowledgeDao.inclui(knowledge);
        }

    }

//    @Transacional
//    public synchronized Knowledge salvar(Knowledge conhecimento) throws ServiceException {
//        return conhecimentoDao.inclui(conhecimento);
//    }
    @Transacional
    public Knowledge alterarConhecimento(Knowledge knowledge, int improve) throws ServiceException {
        try {
            knowledge = knowledgeDao.getPorIdComLock(knowledge.getIdKnowledge());
        } catch (ObjetoNaoEncontradoException ex) {
            Logger.getLogger(KnowledgeService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("O objeto a ser alterado não encontra-se mais no banco de dados");
        }
        knowledge.setTotalUsed(knowledge.getTotalUsed() + 1);
        if (improve == 1) {
            knowledge.setTotalSuccess(knowledge.getTotalSuccess() + 1);
        } else if (improve == -1) {
            knowledge.setTotalWorsen(knowledge.getTotalWorsen() + 1);
        } else if (improve == 0) {
            knowledge.setTotalNotImproveNorWorsen(knowledge.getTotalNotImproveNorWorsen() + 1);
        }
        // ao término de um método transacional a informação é persistida no banco
        return knowledge;
    }

    @Transacional
    public Knowledge alterarConhecimento(long idConhecimento, Knowledge knowledge) throws ServiceException {
        int sucesso = knowledge.getTotalSuccess();

        try {
            knowledge = knowledgeDao.getPorIdComLock(idConhecimento);
        } catch (ObjetoNaoEncontradoException ex) {
            Logger.getLogger(KnowledgeService.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServiceException("O objeto a ser alterado não encontra-se mais no banco de dados");
        }
        knowledge.setTotalSuccess(knowledge.getTotalSuccess() + sucesso);
        knowledge.setTotalUsed(knowledge.getTotalUsed() + 1);
        // ao término de um método transacional a informação é persistida no banco
        return knowledge;
    }
}
