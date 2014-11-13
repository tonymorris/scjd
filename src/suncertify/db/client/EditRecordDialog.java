package suncertify.db.client;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Toolkit;

/**
 * Represents the dialog that appears when a user selects the 'Edit DataRecord' or 'Create DataRecord' option.
 * The dialog creates itself according to the application frame schema configuration.
 * For each field, a label containing its display name and a text field where data can be edited is created.
 * Optionally, schema fields that are keys may be set to not-editable.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class EditRecordDialog extends JDialog
{
    /**
     * The user hasn't selected the OK or Cancel button option.
     */
    public static final int SELECTION_NONE = 0;

    /**
     * The has selected the OK button option.
     */
    public static final int SELECTION_OK = 1;

    /**
     * The user has selected the Cancel button option.
     */
    public static final int SELECTION_CANCEL = 2;

    private int selection;

    private SchemaColumn[] schema;
    private String[] initialValues;

    private JButton btnOk;
    private JButton btnCancel;
    private JLabel[] labels;
    private JTextField[] textfields;

    /**
     * Construct a <code>EditRecordDialog</code> with the given owner and title, no initial values of the text fields and key fields set to editable.
     *
     * @param owner The owner that constructed this <code>EditRecordDialog</code>.
     * @param title The title of the dialog.
     */
    public EditRecordDialog(DataFrame owner, String title)
    {
        this(owner, title, null, true);
    }

    /**
     * Construct a <code>EditRecordDialog</code> with the given owner, title, initial initialValues of the text fields and key fields set to editable.
     *
     * @param owner The owner that constructed this <code>EditRecordDialog</code>.
     * @param title The title of the dialog.
     * @param initialValues The initial initialValues of the text fields.
     * @throws IllegalArgumentException If <code>initialValues != null && initialValues.length != owner.getConfiguration().getMetaSchema().length</code>.
     */
    public EditRecordDialog(DataFrame owner, String title, String[] initialValues) throws IllegalArgumentException
    {
        this(owner, title, initialValues, true);

        if(initialValues != null && initialValues.length != owner.getConfiguration().getMetaSchema().length)
        {
            StringBuffer message = new StringBuffer();
            message.append("value.length != owner.getConfiguration().getMetaSchema().length [");
            message.append(initialValues.length);
            message.append(" != ");
            message.append(schema.length);
            message.append(']');

            throw new IllegalArgumentException(message.toString());
        }
    }

    /**
     * Construct a <code>EditRecordDialog</code> with the given owner, title, initial initialValues of the text fields, and the specicified editable attribute of the key fields.
     *
     * @param owner The owner that constructed this <code>EditRecordDialog</code>.
     * @param title The title of the dialog.
     * @param initialValues The initial initialValues of the text fields.
     * @param keyFieldsEnabled <code>true</code> if text fields of data fields that are unique keys should be editable by the user, <code>false</code> otherwise.
     * @throws IllegalArgumentException If <code>initialValues != null && initialValues.length != owner.getConfiguration().getMetaSchema().length</code>.
     */
    public EditRecordDialog(DataFrame owner, String title, String[] initialValues, boolean keyFieldsEnabled) throws IllegalArgumentException
    {
        super(owner, title, true);

        if(initialValues != null && initialValues.length != owner.getConfiguration().getMetaSchema().length)
        {
            StringBuffer message = new StringBuffer();
            message.append("value.length != owner.getConfiguration().getMetaSchema().length [");
            message.append(initialValues.length);
            message.append(" != ");
            message.append(schema.length);
            message.append(']');

            throw new IllegalArgumentException(message.toString());
        }

        this.schema = owner.getConfiguration().getMetaSchema();
        this.initialValues = initialValues;
        this.selection = SELECTION_NONE;

        setup();

        setEnabledKeyFields(keyFieldsEnabled);
    }

    /**
     * Returns the last user button selection which may be one of:
     * <li>{@link #SELECTION_NONE SELECTION_NONE}</li>
     * <li>{@link #SELECTION_OK SELECTION_OK}</li>
     * <li>{@link #SELECTION_CANCEL SELECTION_CANCEL}</li>.
     *
     * @return The last user button selection.
     */
    public int getSelection()
    {
        return selection;
    }

    /**
     * Returns the text values that have been entered into the text fields.
     *
     * @return The text values that have been entered into the text fields.
     */
    public String[] getTextValues()
    {
        String[] values = new String[textfields.length];

        for(int i = 0; i < values.length; i++)
        {
            values[i] = textfields[i].getText();
        }

        return values;
    }

    /**
     * Sets whether or not text fields that represent unique keys according to the application configuration
     * should be enabled to allow user input.
     *
     * @param enabled <code>true</code> if text fields that represent unique keys according to the application configuration
     * should be editable by the user, <code>false</code> otherwise.
     */
    public void setEnabledKeyFields(boolean enabled)
    {
        for(int i = 0; i < schema.length; i++)
        {
            if(schema[i].isKey())
            {
                textfields[i].setEnabled(enabled);
                textfields[i].setEditable(enabled);
            }
        }
    }

    // initialize the edit record dialog
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
        btnOk = new JButton("OK");
        btnCancel = new JButton("Cancel");
        labels = new JLabel[schema.length];
        textfields = new JTextField[schema.length];

        for(int i = 0; i < schema.length; i++)
        {
            labels[i] = new JLabel(schema[i].getDisplayName());
            textfields[i] = new JTextField(20);
        }
    }

    // lay the components out on the dialog
    private void layoutComponents()
    {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();

        JPanel pnlMain = new JPanel(gbl);

        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.insets = new Insets(7, 7, 7, 7);

        for(int i = 0; i < schema.length; i++)
        {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(labels[i], gbc);
            pnlMain.add(labels[i]);

            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(textfields[i], gbc);
            pnlMain.add(textfields[i]);
        }

        JPanel pnlButtons = new JPanel(new FlowLayout());
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pnlMain, BorderLayout.CENTER);
        getContentPane().add(pnlButtons, BorderLayout.SOUTH);
    }

    // add event handlers to the dialog
    private void addListeners()
    {
        btnOk.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                EditRecordDialog.this.hide();
                EditRecordDialog.this.dispose();

                selection = SELECTION_OK;
            }
        });

        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                EditRecordDialog.this.hide();
                EditRecordDialog.this.dispose();

                selection = SELECTION_CANCEL;
            }
        });

        for(int i = 0; i < schema.length; i++)
        {
            final int j = i;

            textfields[i].setDocument(new PlainDocument()
            {
                public void insertString(int offset, String str, AttributeSet set) throws BadLocationException
                {
                    if(getLength() + str.length() > schema[j].getLength())
                    {
                        Toolkit.getDefaultToolkit().beep();
                    }
                    else
                    {
                        super.insertString(offset, str, set);
                    }
                }
            });

            textfields[i].addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ae)
                {
                    EditRecordDialog.this.hide();
                    EditRecordDialog.this.dispose();

                    selection = SELECTION_OK;
                }
            });

            textfields[i].addFocusListener(new FocusListener()
            {
                public void focusLost(FocusEvent fe)
                {
                    textfields[j].setSelectionStart(textfields[j].getText().length());
                }

                public void focusGained(FocusEvent fe)
                {
                    textfields[j].setSelectionStart(0);
                    textfields[j].setSelectionEnd(textfields[j].getText().length());
                }
            });

            if(initialValues != null)
            {
                textfields[i].setText(initialValues[i]);
            }
        }
    }

    // set attributes of components that make up this dialog
    private void setComponentAttributes()
    {
        btnOk.setMnemonic(KeyEvent.VK_ENTER);
        btnCancel.setMnemonic(KeyEvent.VK_CANCEL);
    }
}
