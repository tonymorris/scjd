package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.client.EditRecordDialog;
import suncertify.db.client.MiddleLocator;
import suncertify.db.client.DuplicateKeyDetectorImpl;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * An action that can be fired by the user.
 * The action will display dialog allowing the user to create a data record.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class CreateRecord extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>CreateRecord</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>CreateRecord</code> action.
     */
    public CreateRecord(DataFrame owner)
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
        EditRecordDialog dialog = new EditRecordDialog(owner, "Create a New Data Record");
        dialog.pack();
        MiddleLocator locator = new MiddleLocator(owner);
        locator.locate(dialog);
        dialog.show();

        if(dialog.getSelection() == EditRecordDialog.SELECTION_OK)
        {
            String[] data = dialog.getTextValues();

            boolean proceed = true;

            if(new DuplicateKeyDetectorImpl(owner).isDuplicateKey(data))
            {
                int selection = JOptionPane.showConfirmDialog(owner, "The schema setting suggests that the new data record might have a duplicate key. Proceed ?" , "Warning. Proceed ?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                proceed = (selection == JOptionPane.YES_OPTION);
            }

            if(proceed)
            {
                owner.addRow(data);
            }
        }
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Create DataRecord");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/plus16.gif")));
        putValue(SHORT_DESCRIPTION, "Create a new data record");
        putValue(LONG_DESCRIPTION, "Create a new data record");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_R));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        putValue(ACTION_COMMAND_KEY, "create-record-command");
    }
}
