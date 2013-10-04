package br.uff.ic.oceano.exception;

import com.sun.faces.application.ActionListenerImpl;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Classe responsável por tratar erros inesperados do sistema.
 *
 * @author Heliomar
 *
 */
public class ExceptionHandler extends ActionListenerImpl {

    @Override
    public void processAction(ActionEvent event) {
        // Obtem o contexto JSF
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Executa o método da classe Pai
            super.processAction(event);

        } catch (Exception e) {
//            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//            e.printStackTrace();
//            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            // Se ocorrer um erro inesperado, exibe a mensagem abaixo
            context.addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, e.getMessage(), null));


            // Redireciona para a pagina com o mapeamento 'erro' no faces-config.
            context.getApplication().getNavigationHandler().handleNavigation(
                    context, null, "erro");
        }
    }
}
