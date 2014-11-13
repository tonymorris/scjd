package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.client.ConfigurationDialog;
import suncertify.db.client.MiddleLocator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Dialog;

/**
 * An action that can be fired by the user.
 * The action will bring up a dialog that allows the user to configure the application.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Configure extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>Configure</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>Configure</code> action.
     */
    public Configure(DataFrame owner)
    {
        this.owner = owner;

        setup();
    }

    /**
     * Executed when the action is fired.
     *
     * @param ae Represents information about the event that was fired.
     */
    public void actionPerformed(ActionEvent ae)
    {
        Dialog dialog = new ConfigurationDialog(owner);
        dialog.pack();
        MiddleLocator locator = new MiddleLocator(owner);
        locator.locate(dialog);
        dialog.show();
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Configure");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/preferences16.gif")));
        putValue(SHORT_DESCRIPTION, "Settings and configuration");
        putValue(LONG_DESCRIPTION, "Settings and configuration");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F9));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        putValue(ACTION_COMMAND_KEY, "configure-command");
    }
}
