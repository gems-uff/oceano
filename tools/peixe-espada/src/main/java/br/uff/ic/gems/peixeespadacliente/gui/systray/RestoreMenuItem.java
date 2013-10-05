package br.uff.ic.gems.peixeespadacliente.gui.systray;

import br.uff.ic.gems.peixeespadacliente.gui.JDesktopAgent;
import java.awt.event.ActionEvent;

/**
 *
 * @author João Felipe
 */
public class RestoreMenuItem extends AbstractMenuItem {

    JDesktopAgent frame;

    public RestoreMenuItem(JDesktopAgent frame, String text) {
        super(text);
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.restore();
    }
}
