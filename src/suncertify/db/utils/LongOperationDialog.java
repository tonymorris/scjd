package suncertify.db.utils;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.Action;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Represents the dialog that appears when a user selects to perform a time consuming operation.
 * The dialog contains a label that displays a message.
 * The dialog also contains a progress bar (set to indeterminate mode) and a cancel button.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class LongOperationDialog extends JDialog
{
    private JLabel lbl;
    private String message;
    private JProgressBar progress;
    private JButton btnCancel;
    private Action cancel;
    private boolean cancelled;

    /**
     * Construct a <code>LongOperationDialog</code> with the given owner.
     * The dialog is set to be modal on the owner.
     *
     * @param owner The owner that created this <code>LongOperationDialog</code>.
     * @param title The title of the dialog.
     * @param message The message to display while connecting.
     */
    public LongOperationDialog(Frame owner, String title, String message)
    {
        super(owner, title, true);
        this.message = message;
        this.cancel = new CancelAction();
        this.cancelled = false;

        setup();
    }

    /**
     * Determines if the user has cancelled the operation.
     * This is done by pressing the cancel button.
     *
     * @return <code>true</code> if the user has pressed the cancel button, <code>false</code> otherwise.
     */
    public boolean isCancelled()
    {
        return cancelled;
    }

    // this method is private because the cancelled property is read-only.
    private void setCancelled(boolean cancelled)
    {
        this.cancelled = cancelled;
    }

    // initialize the connecting dialog
    private void setup()
    {
        instantiate();

        layoutComponents();

        addListeners();

        setComponentAttributes();
    }

    // instantiate all components that make up this dialog
    private void instantiate()
    {
        lbl = new JLabel(message);
        progress = new JProgressBar();
        btnCancel = new JButton(cancel);
    }

    // lay the components out on the dialog
    private void layoutComponents()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();

        getContentPane().setLayout(gbl);

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(lbl, gbc);
        getContentPane().add(lbl);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbl.setConstraints(progress, gbc);
        getContentPane().add(progress);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbl.setConstraints(btnCancel, gbc);
        getContentPane().add(btnCancel);
    }

    // add event handlers to the dialog
    private void addListeners()
    {
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                cancel.actionPerformed(new ActionEvent(we, ActionEvent.ACTION_PERFORMED, "cancel"));
            }
        });
    }

    // set attributes of components that make up this dialog
    private void setComponentAttributes()
    {
        progress.setIndeterminate(true);
    }

    // action to be executed
    private class CancelAction extends AbstractAction
    {
        public CancelAction()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            LongOperationDialog.this.hide();
            LongOperationDialog.this.dispose();

            setCancelled(true);
        }

        private void setup()
        {
            putValue(NAME, "Cancel");
            putValue(SHORT_DESCRIPTION, "Cancel the operation");
            putValue(LONG_DESCRIPTION, "Cancel the operation");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_ESCAPE));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
            putValue(ACTION_COMMAND_KEY, "cancel-command");
        }
    }
}
