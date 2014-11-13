package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.client.EditRecordDialog;
import suncertify.db.client.MiddleLocator;
import suncertify.db.client.SearchRecordDialog;
import suncertify.db.client.SearchCriteriaImpl;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

/**
 * An action that can be fired by the user.
 * The action will display a dialog allowing the user to enter search criteria for data records from the data source.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Search extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>Search</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>Search</code> action.
     */
    public Search(DataFrame owner)
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
        SearchRecordDialog dialog = new SearchRecordDialog(owner, "Search Data Records");
        dialog.pack();
        MiddleLocator locator = new MiddleLocator(owner);
        locator.locate(dialog);
        dialog.show();

        if(dialog.getSelection() == EditRecordDialog.SELECTION_OK)
        {
            String[] fields = dialog.getTextValues();
            boolean exactMatch = dialog.isExactMatch();

            owner.refresh(new SearchCriteriaImpl(fields, exactMatch));
        }
    }

    // set attributes of this action
    private void setup()
    {
        putValue(NAME, "Search");
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/search16.gif")));
        putValue(SHORT_DESCRIPTION, "Search data records");
        putValue(LONG_DESCRIPTION, "Search data records");
        putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        putValue(ACTION_COMMAND_KEY, "about-command");
    }
}
