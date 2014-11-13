package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * An action that can be fired by the user.
 * The action will refresh the current application data record display with the all data records from the data source.
 *
 * @see DataFrame#refreshAll()
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class RefreshAll extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>RefreshAll</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>RefreshAll</code> action.
     */
    public RefreshAll(DataFrame owner)
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
        owner.refreshAll();
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Refresh All");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/refreshall16.gif")));
        putValue(SHORT_DESCRIPTION, "Refresh the data record display for all data records");
        putValue(LONG_DESCRIPTION, "Refresh the data record display for all data records");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F5));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        putValue(ACTION_COMMAND_KEY, "refresh-all-command");
    }
}
