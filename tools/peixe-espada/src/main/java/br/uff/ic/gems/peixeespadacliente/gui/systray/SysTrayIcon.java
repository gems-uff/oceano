package br.uff.ic.gems.peixeespadacliente.gui.systray;

import br.uff.ic.gems.peixeespadacliente.gui.JDesktopAgent;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import translation.Translate;

/**
 *
 * @author João Felipe
 */
public class SysTrayIcon {


    public SysTrayIcon(JDesktopAgent frame, String tooltip) {
        Translate translate = Translate.getTranslate();
        if (SystemTray.isSupported()) {
            Image image = Toolkit.getDefaultToolkit().getImage("icon.png");

            PopupMenu popup = new PopupMenu();
            popup.add(new RestoreMenuItem(frame, translate.backgroundRestore()));
            popup.addSeparator();
            popup.add(new AboutMenuItem(translate.about()));
            popup.addSeparator();
            popup.add(new ExitMenuItem(translate.exit()));

            TrayIcon trayIcon = new TrayIcon(image, tooltip, popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new IconListener(frame));

            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e) {
                new SysTrayForm(frame).setVisible(true);
            }
        } else {
            new SysTrayForm(frame).setVisible(true);
        }

    }
}