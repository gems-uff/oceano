/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.oceano.peixeespada.controller.command;

import br.uff.ic.oceano.controller.servlet.command.Command;
import br.uff.ic.oceano.core.dao.controle.JPAUtil;
import br.uff.ic.oceano.peixeespada.contexto.ContextoAmbiente;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Heliomar
 */
public class CommandCleanDataToExecuteExperiments implements Command{

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String strUser = request.getParameter("xpto");
        if(strUser == null || !strUser.equals("kann")){
            response.getWriter().println("\n Nada foi alterado ");
            return;
        }
        JPAUtil.beginTransaction();
        EntityManager em = JPAUtil.getEntityManager();
        Query deleteKnowledge = em.createQuery("delete from Knowledge");

        response.getWriter().println(deleteKnowledge.executeUpdate()+ " registros de conhecimento deletados <br/>");

        Query deleteCiclosAgentes = em.createNativeQuery("update espada_agent set cycles = 0, successCycles = 0, worsenCycles = 0, notImproveNorWorsenCycles = 0");
        response.getWriter().println(deleteCiclosAgentes.executeUpdate()+ " Agentes com os ciclos zerados (Sucesso, fracasso e Não Alteração) <br/>");

        JPAUtil.commitTransaction();


        ContextoAmbiente.getInstance().registraAllAtiveAgents();
        response.getWriter().println(" BD preparado para experimentos ");


    }

}
