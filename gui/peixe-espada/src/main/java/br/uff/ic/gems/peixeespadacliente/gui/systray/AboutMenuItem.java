package br.uff.ic.gems.peixeespadacliente.gui.systray;

import br.uff.ic.gems.peixeespadacliente.gui.JDesktopAgent;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class AboutMenuItem extends AbstractMenuItem {

    public AboutMenuItem(String text) {
        super(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AboutMenuItem.aboutActionPerformed();
    }

    public static void aboutActionPerformed() {
        Translate translate = Translate.getTranslate();
        ImageIcon icone = new ImageIcon(JDesktopAgent.class.getResource("img/computer.gif"));
        JOptionPane.showMessageDialog(null, 
                translate.aboutPE(), 
                translate.aboutPETitle(), 
                JOptionPane.INFORMATION_MESSAGE,
                icone);
    }
}
