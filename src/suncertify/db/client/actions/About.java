package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.utils.AboutDialog;
import suncertify.db.client.MiddleLocator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Dialog;

/**
 * An action that can be fired by the user.
 * The action will bring up a dialog that contains information about the application.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class About extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct an <code>About</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>About</code> action.
     */
    public About(DataFrame owner)
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
        Dialog dialog = new AboutDialog(owner);
        dialog.pack();
        MiddleLocator locator = new MiddleLocator(owner);
        locator.locate(dialog);
        dialog.show();
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "About");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/about16.gif")));
        putValue(SHORT_DESCRIPTION, "About this application");
        putValue(LONG_DESCRIPTION, "About this application");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F1));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        putValue(ACTION_COMMAND_KEY, "about-command");
    }
}
