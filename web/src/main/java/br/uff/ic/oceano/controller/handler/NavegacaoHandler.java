package br.uff.ic.oceano.controller.handler;

import com.sun.faces.application.NavigationHandlerImpl;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 *
 * @author Mario
 */
public class NavegacaoHandler extends NavigationHandlerImpl{

    
    public NavegacaoHandler() {
        super();
    }
    
    /**
     * Override the default navigation handler to check for viewIds in
     * the outcome string. 
     * This is the how the path looks like
     * return "def:privado/cadastramento/formCadastramento";
     */
    @Override
    public void handleNavigation(FacesContext context, String fromAction,
                                 String outcome) {
        if (outcome != null && outcome.startsWith("def:")) {
            
            // canonicalize path relative to current view
            String dir = "/";
            outcome = outcome.replace("def:", dir);
            outcome = outcome.replace("//", "/");

            String defaultSuffix = context.getExternalContext().getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
            String suffix = defaultSuffix != null ? defaultSuffix : ViewHandler.DEFAULT_SUFFIX;
            
            outcome += suffix;
            
//            System.out.println("outcome="+outcome);
            
            // set the specified view
            ViewHandler vh = context.getApplication().getViewHandler();
            UIViewRoot view = vh.createView(context, outcome);
            context.setViewRoot(view);
        } else {
            super.handleNavigation(context, fromAction, outcome);
        }
    }

}
