package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.client.EditRecordDialog;
import suncertify.db.client.MiddleLocator;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * An action that can be fired by the user.
 * The action will display dialog allowing the user to edit the currently selected a data record.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class EditRecord extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>EditRecord</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>EditRecord</code> action.
     */
    public EditRecord(DataFrame owner)
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
        int selected = owner.getSelectedRecordIndex();

        String[] row = owner.getDataTableModel().getDataRecord(selected).getData();

        StringBuffer title = new StringBuffer();
        title.append("Edit Data DataRecord (");
        title.append(selected + 1);
        title.append(')');

        EditRecordDialog dialog = new EditRecordDialog(owner, title.toString(), row, false);
        dialog.pack();
        MiddleLocator locator = new MiddleLocator(owner);
        locator.locate(dialog);
        dialog.show();

        if(dialog.getSelection() == EditRecordDialog.SELECTION_OK)
        {
            String[] data = dialog.getTextValues();

            owner.updateSelectedRow(data);
        }
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Edit DataRecord");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/edit16.gif")));
        putValue(SHORT_DESCRIPTION, "Edit the selected data record");
        putValue(LONG_DESCRIPTION, "Edit the selected data record");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_SPACE));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        putValue(ACTION_COMMAND_KEY, "edit-record-command");
    }
}
