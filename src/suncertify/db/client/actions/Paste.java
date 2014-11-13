package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.client.DuplicateKeyDetectorImpl;
import suncertify.db.client.EditRecordDialog;
import suncertify.db.client.MiddleLocator;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.IOException;

/**
 * An action that can be fired by the user.
 * The action will paste the current system clipboard data into the application
 * as a new data record. The action will display dialog allowing the user to edit the data record before pasting it.
 * If the system clipboard does not contain valid record data, the user will given an error message.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Paste extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>Paste</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>Paste</code> action.
     */
    public Paste(DataFrame owner)
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
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

        try
        {
            String s = (String)cb.getContents(this).getTransferData(DataFlavor.stringFlavor);;

            int totalSchemaLength = 0;

            for(int i = 0; i < owner.getConfiguration().getMetaSchema().length; i++)
            {
                totalSchemaLength = totalSchemaLength + owner.getConfiguration().getMetaSchema()[i].getLength();
            }

            if(totalSchemaLength != s.length())
            {
                JOptionPane.showMessageDialog(owner, "Clipboard contents do not represent a valid data record" , "Invalid clipboard contents", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                String[] fields = new String[owner.getConfiguration().getMetaSchema().length];
                byte[] data = s.getBytes();

                int index = 0;

                for(int i = 0; i < fields.length; i++)
                {
                    byte[] field = new byte[owner.getConfiguration().getMetaSchema()[i].getLength()];
                    System.arraycopy(data, index, field, 0, field.length);
                    fields[i] = new String(field).trim();
                    index = index + field.length;
                }

                EditRecordDialog dialog = new EditRecordDialog(owner, "Paste a New Data DataRecord", fields);
                dialog.pack();
                MiddleLocator locator = new MiddleLocator(owner);
                locator.locate(dialog);
                dialog.show();

                if(dialog.getSelection() == EditRecordDialog.SELECTION_OK)
                {
                    String[] enteredData = dialog.getTextValues();

                    boolean proceed = true;

                    if(new DuplicateKeyDetectorImpl(owner).isDuplicateKey(enteredData))
                    {
                        int selection = JOptionPane.showConfirmDialog(owner, "The schema setting suggests that the new data record might have a duplicate key. Proceed ?" , "Warning. Proceed ?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        proceed = (selection == JOptionPane.YES_OPTION);
                    }

                    if(proceed)
                    {
                        owner.addRow(enteredData);
                    }
                }
            }
        }
        catch(UnsupportedFlavorException ufe)
        {
            JOptionPane.showMessageDialog(owner, ufe.getMessage() , ufe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
        }
        catch(IOException ioe)
        {
            JOptionPane.showMessageDialog(owner, ioe.getMessage(), ioe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
        }
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Paste");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/paste16.gif")));
        putValue(SHORT_DESCRIPTION, "Paste a data record at the end");
        putValue(LONG_DESCRIPTION, "Paste a data record at the end");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_V));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_MASK));
        putValue(ACTION_COMMAND_KEY, "paste-command");
    }

}

