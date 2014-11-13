package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

/**
 * An action that can be fired by the user.
 * The action will copy the currently data record data to the system clipboard.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Copy extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>Copy</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>Copy</code> action.
     */
    public Copy(DataFrame owner)
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

        StringBuffer copy = new StringBuffer();

        for(int i = 0; i < row.length; i++)
        {
            byte[] array = new byte[owner.getConfiguration().getMetaSchema()[i].getLength()];

            for(int j = 0; j < array.length; j++)
            {
                array[j] = ' ';
            }

            System.arraycopy(row[i].getBytes(), 0, array, 0, row[i].length());

            copy.append(new String(array));
        }

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection content = new StringSelection(copy.toString());
        cb.setContents(content, null);
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Copy");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/copy16.gif")));
        putValue(SHORT_DESCRIPTION, "Copy the selected data record");
        putValue(LONG_DESCRIPTION, "Copy the selected data record");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.SHIFT_MASK));
        putValue(ACTION_COMMAND_KEY, "copy-command");
    }

}
