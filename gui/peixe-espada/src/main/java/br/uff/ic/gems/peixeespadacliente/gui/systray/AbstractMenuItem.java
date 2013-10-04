package br.uff.ic.gems.peixeespadacliente.gui.systray;

import java.awt.HeadlessException;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;

/**
 *
 * @author João Felipe
 */
public abstract class AbstractMenuItem extends MenuItem implements ActionListener {

    public AbstractMenuItem(String label, MenuShortcut s) throws HeadlessException {
        super(label, s);
        this.addActionListener(this);
    }

    public AbstractMenuItem(String label) throws HeadlessException {
        super(label);
        this.addActionListener(this);
    }

    public AbstractMenuItem() throws HeadlessException {
        this.addActionListener(this);
    }
}
