package suncertify.db.client;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Represents the dialog that appears when a user selects the 'Search' option.
 * The dialog creates itself according to the application frame schema configuration.
 * For each field, a label containing its display name and a text field where data can be edited is created.
 * Optionally, schema fields that are keys may be set to not-editable.
 * A check box is displayed allowing the user to specify a search based on exact field matches.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class SearchRecordDialog extends EditRecordDialog
{
    private boolean exactMatch;
    private JCheckBox chkExactMatch;

    /**
     * Construct a <code>SearchRecordDialog</code> with the given owner and title, no initial values of the text fields,
     * key fields set to editable, and exact match criteria set to <code<false</code>.
     *
     * @param owner The owner that constructed this <code>SearchRecordDialog</code>.
     * @param title The title of the dialog.
     */
    public SearchRecordDialog(DataFrame owner, String title)
    {
        super(owner, title);

        this.exactMatch = false;

        setup();
    }

    /**
     * Construct a <code>SearchRecordDialog</code> with the given owner, title, initial initialValues of the text fields,
     * key fields set to editable, and exact match criteria set to <code<false</code>.
     *
     * @param owner The owner that constructed this <code>SearchRecordDialog</code>.
     * @param title The title of the dialog.
     * @param initialValues The initial initialValues of the text fields.
     * @throws IllegalArgumentException If <code>initialValues != null && initialValues.length != owner.getConfiguration().getMetaSchema().length</code>.
     */
    public SearchRecordDialog(DataFrame owner, String title, String[] initialValues) throws IllegalArgumentException
    {
        super(owner, title, initialValues);

        this.exactMatch = false;

        setup();
    }

    /**
     * Construct a <code>SearchRecordDialog</code> with the given owner, title, initial initialValues of the text fields,
     * the specicified editable attribute of the key fields and exact match criteria set to <code<false</code>.
     *
     * @param owner The owner that constructed this <code>EditRecordDialog</code>.
     * @param title The title of the dialog.
     * @param initialValues The initial initialValues of the text fields.
     * @param keyFieldsEnabled <code>true</code> if text fields of data fields that are unique keys should be editable by the user, <code>false</code> otherwise.
     * @throws IllegalArgumentException If <code>initialValues != null && initialValues.length != owner.getConfiguration().getMetaSchema().length</code>.
     */
    public SearchRecordDialog(DataFrame owner, String title, String[] initialValues, boolean keyFieldsEnabled) throws IllegalArgumentException
    {
        super(owner, title, initialValues, keyFieldsEnabled);

        this.exactMatch = false;

        setup();
    }

    /**
     * Construct a <code>SearchRecordDialog</code> with the given owner, title, initial values of the text fields,
     * the specicified editable attribute of the key fields and exact match criteria set to <code<false</code>.
     *
     * @param owner The owner that constructed this <code>EditRecordDialog</code>.
     * @param title The title of the dialog.
     * @param values The initial values of the text fields.
     * @param keyFieldsEnabled <code>true</code> if text fields of data fields that are unique keys should be editable by the user, <code>false</code> otherwise.
     * @param exactMatch Whether or not to set the exact match JCheckBox to selected or not.
     * @throws IllegalArgumentException If <code>values != null && values.length != owner.getConfiguration().getMetaSchema().length</code>.
     */
    public SearchRecordDialog(DataFrame owner, String title, String[] values, boolean keyFieldsEnabled, boolean exactMatch) throws IllegalArgumentException
    {
        super(owner, title, values, keyFieldsEnabled);

        this.exactMatch = exactMatch;

        setup();
    }

    /**
     * Determines if the user selected to perform a match using the exact given criteria.
     *
     * @return <code>true</code> if the user selected to perform a match using the exact given criteria, <code>false</code> otherwise.
     */
    public boolean isExactMatch()
    {
        return exactMatch;
    }

    // initialize the edit record dialog
    private void setup()
    {
        instantiate();

        layoutComponents();

        addListeners();

        setComponentAttributes();
    }

    private void instantiate()
    {
        chkExactMatch = new JCheckBox("Match Fields Exactly", exactMatch);
    }

    private void layoutComponents()
    {
        JPanel pnl = new JPanel(new FlowLayout());
        pnl.add(chkExactMatch);

        getContentPane().add(pnl, BorderLayout.NORTH);
    }

    private void addListeners()
    {
        chkExactMatch.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                exactMatch = chkExactMatch.isSelected();
                chkExactMatch.transferFocus();
            }
        });
    }

    private void setComponentAttributes()
    {
        chkExactMatch.setToolTipText("Match records with fields that match the exact given criteria");
        chkExactMatch.setMnemonic(KeyEvent.VK_E);
    }
}
