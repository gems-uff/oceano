package br.uff.ic.gems.peixeespadacliente.gui.systray;

import br.uff.ic.gems.peixeespadacliente.gui.JDesktopAgent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author João Felipe
 */
public class IconListener implements MouseListener {

    JDesktopAgent frame;

    public IconListener(JDesktopAgent frame) {
        this.frame = frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            frame.restore();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println("Tray Icon - Mouse entered!");
    }

    @Override
    public void mouseExited(MouseEvent e) {
//        System.out.println("Tray Icon - Mouse exited!");
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("Tray Icon - Mouse pressed!");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
//        System.out.println("Tray Icon - Mouse released!");
    }
}
