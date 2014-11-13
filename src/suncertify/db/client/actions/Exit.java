package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.Constants;
import suncertify.db.utils.PassiveProperties;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.IOException;

/**
 * An action that can be fired by the user.
 * The action will exit the application.
 * The user may be be prompted for confirmation before exiting.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Exit extends AbstractAction implements Constants
{
    private DataFrame owner;

    /**
     * Construct a <code>Exit</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>Exit</code> action.
     */
    public Exit(DataFrame owner)
    {
        setup();

        this.owner = owner;
    }

    /**
     * Executed when the action is fired.
     *
     * @param ae Represents information about the event that was fired.
     */
    public void actionPerformed(ActionEvent ae)
    {
        boolean exit = true;

        if(owner.getConfiguration().isConfirmExit())
        {
            int selection = JOptionPane.showConfirmDialog(owner, "Exit the application ?" , "Exit ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            exit = (selection == JOptionPane.YES_OPTION);
        }

        if(exit)
        {
            owner.setData(null);
            owner.hide();
            owner.dispose();

            try
            {
                PassiveProperties props = new PassiveProperties(owner.getConfiguration().toProperties());
                props.store(PROPERTIES_FILE, PROPERTIES_FILE_HEADER);
            }
            catch(IOException ioe)
            {
                JOptionPane.showMessageDialog(owner, ioe.getMessage(), ioe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            }

            System.exit(0);
        }
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Exit");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/exit16.gif")));
        putValue(SHORT_DESCRIPTION, "Exit this application");
        putValue(LONG_DESCRIPTION, "Exit this application");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_Q));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        putValue(ACTION_COMMAND_KEY, "exit-command");
    }

}
