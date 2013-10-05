package br.uff.ic.gems.peixeespadacliente.gui;

import br.uff.ic.oceano.core.tools.compiler.CompilerService;
import javax.swing.UIManager;

/**
 *
 * @author Heliomar
 */
public class Main {

    public static JDesktopAgent jDesktop;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jDesktop = new JDesktopAgent();
        jDesktop.setVisible(true);
        CompilerService.COMPILE_WITH_COMMAND_LINE = false;
    }
}
