package br.uff.ic.gems.peixeespadacliente.gui.systray;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class ExitMenuItem extends AbstractMenuItem {

    public ExitMenuItem(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ExitMenuItem.exitPerformed();
    }

    public static void exitPerformed() {
        if (JOptionPane.showConfirmDialog(null, Translate.getTranslate().closePE()) == 0) {
            System.exit(0);
        }
    }
}
