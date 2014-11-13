package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * An action that can be fired by the user.
 * The action will deleted the currently selected record.
 * The user may be prompted for confirmation depending on the application configuration.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DeleteRecord extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>DeleteRecord</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>DeleteRecord</code> action.
     */
    public DeleteRecord(DataFrame owner)
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
        boolean proceed = true;

        if(owner.getConfiguration().isConfirmDelete())
        {
            StringBuffer message = new StringBuffer();
            message.append("Delete data record (");
            message.append(owner.getSelectedRecordIndex() + 1);
            message.append(") ?");

            int selection = JOptionPane.showConfirmDialog(owner, message , "Delete data record ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            proceed = (selection == JOptionPane.YES_OPTION);
        }

        if(proceed)
        {
            owner.deleteSelectedRow();
        }
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Delete DataRecord");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/minus16.gif")));
        putValue(SHORT_DESCRIPTION, "Delete the selected data record");
        putValue(LONG_DESCRIPTION, "Delete the selected data record");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_DELETE));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        putValue(ACTION_COMMAND_KEY, "delete-record-command");
    }
}
