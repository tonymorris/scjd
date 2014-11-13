package suncertify.db.client;

import suncertify.db.utils.PositiveLongTextField;
import suncertify.db.utils.PassiveProperties;
import suncertify.db.Constants;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.DefaultCellEditor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Panel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;

/**
 * Represents the dialog that appears when the user selects the 'Configure' option.
 * The dialog allows the user to configure the client application.
 * Some configuration attributes will not be editable by the user while the client
 * application is 'connected' to the data source.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ConfigurationDialog extends JDialog implements Constants, ClientConstants
{
    private JButton btnOk;
    private JButton btnCancel;

    private DataFrame owner;

    private Action actAddField;
    private Action actDeleteField;
    private Action actMoveFieldUp;
    private Action actMoveFieldDown;

    private JTable tblSchema;
    private JButton btnAddField;
    private JButton btnDeleteField;
    private JButton btnMoveFieldUp;
    private JButton btnMoveFieldDown;
    private JLabel lblHost;
    private JTextField txtHost;
    private JLabel lblClientPort;
    private PositiveLongTextField txtClientPort;
    private JLabel lblClientDbJndiName;
    private JTextField txtClientDbJndiName;
    private JLabel lblClientDataFileName;
    private JTextField txtClientDataFileName;
    private JButton btnClientBrowseDataFile;
    private JCheckBox chkConfirmCut;
    private JCheckBox chkConfirmDelete;
    private JCheckBox chkConfirmExit;
    private JPopupMenu popup;

    /**
     * Construct a <code>ConfigurationDialog</code> with the given owner.
     * The dialog is set to be modal on the owner.
     *
     * @param owner The owner that created this <code>ConfigurationDialog</code>.
     */
    public ConfigurationDialog(DataFrame owner)
    {
        super(owner, "Configuration and Settings", true);

        this.owner = owner;

        setup();
    }

    // initialize the configuration dialog
    private void setup()
    {
        instantiate();

        layoutComponents();

        addListeners();

        setComponentAttributes();

        update();

        updateConfigurationView();
    }

    // instantiate all components that make up this dialog
    private void instantiate()
    {
        actAddField = new AddSchemaField();
        actDeleteField = new DeleteSchemaField();
        actMoveFieldUp = new MoveUpSchemaField();
        actMoveFieldDown = new MoveDownSchemaField();

        tblSchema = new JTable(new ConfigurationSchemaTableModelImpl());
        btnAddField = new JButton(actAddField);
        btnDeleteField = new JButton(actDeleteField);
        btnMoveFieldUp = new JButton(actMoveFieldUp);
        btnMoveFieldDown = new JButton(actMoveFieldDown);
        lblHost = new JLabel("Host Name", JLabel.LEFT);
        txtHost = new JTextField(20);
        lblClientPort = new JLabel("Port", JLabel.LEFT);
        txtClientPort = new PositiveLongTextField(20);
        lblClientDbJndiName = new JLabel("DB JNDI Name", JLabel.LEFT);
        txtClientDbJndiName = new JTextField(20);
        lblClientDataFileName = new JLabel("Data File Name", JLabel.LEFT);
        txtClientDataFileName = new JTextField(20);
        btnClientBrowseDataFile = new JButton("Browse");
        chkConfirmCut = new JCheckBox("Confirm 'Cut' data record");
        chkConfirmDelete = new JCheckBox("Confirm 'Delete' data record");
        chkConfirmExit = new JCheckBox("Confirm 'Exit' application");

        btnOk = new JButton("OK");
        btnCancel = new JButton("Cancel");

        popup = new JPopupMenu();
    }

    // lay the components out on the dialog
    private void layoutComponents()
    {
        SchemaPanel sp = new SchemaPanel();
        NetworkedModePanel nmp = new NetworkedModePanel();
        LocalModePanel lmp = new LocalModePanel();
        ConfirmPanel cp = new ConfirmPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        gbc.insets = new Insets(8, 8, 8, 8);

        JPanel pnlMain = new JPanel(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(sp, gbc);
        pnlMain.add(sp);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(nmp, gbc);
        pnlMain.add(nmp);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(lmp, gbc);
        pnlMain.add(lmp);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(cp, gbc);
        pnlMain.add(cp);

        JPanel pnlButtons = new JPanel(new FlowLayout());
        pnlButtons.add(btnOk);
        pnlButtons.add(btnCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(pnlMain), BorderLayout.CENTER);
        getContentPane().add(pnlButtons, BorderLayout.SOUTH);
    }

    // add event handlers to the dialog
    private void addListeners()
    {
        tblSchema.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    update();
                }
            }
        });

        tblSchema.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent me)
            {
                mouseReleased(me);
            }

            public void mouseReleased(MouseEvent me)
            {
                if(me.isPopupTrigger())
                {
                    int selected = me.getY() / tblSchema.getRowHeight();

                    tblSchema.addRowSelectionInterval(selected, selected);

                    popup.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        });

        btnClientBrowseDataFile.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(owner);

                if(result == JFileChooser.APPROVE_OPTION)
                {
                    txtClientDataFileName.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        txtHost.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtHost.setSelectionStart(txtHost.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtHost.setSelectionStart(0);
                txtHost.setSelectionEnd(txtHost.getText().length());
            }
        });

        txtClientPort.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtClientPort.setSelectionStart(txtClientPort.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtClientPort.setSelectionStart(0);
                txtClientPort.setSelectionEnd(txtClientPort.getText().length());
            }
        });

        txtClientDbJndiName.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtClientDbJndiName.setSelectionStart(txtClientDbJndiName.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtClientDbJndiName.setSelectionStart(0);
                txtClientDbJndiName.setSelectionEnd(txtClientDbJndiName.getText().length());
            }
        });

        txtClientDataFileName.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtClientDataFileName.setSelectionStart(txtClientDataFileName.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtClientDataFileName.setSelectionStart(0);
                txtClientDataFileName.setSelectionEnd(txtClientDataFileName.getText().length());
            }
        });

        btnOk.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ok();
            }
        });

        txtHost.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ok();
            }
        });

        txtClientPort.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ok();
            }
        });

        txtClientDbJndiName.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ok();
            }
        });

        txtClientDataFileName.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                ok();
            }
        });

        btnCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                hide();
            }
        });
    }

    // set attributes of components that make up this dialog
    private void setComponentAttributes()
    {
        tblSchema.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblSchema.setPreferredScrollableViewportSize(new Dimension(0, 100));
        tblSchema.getColumn("Key").setPreferredWidth(40);
        tblSchema.getColumn("Display Name").setPreferredWidth(160);
        tblSchema.getColumn("Length").setPreferredWidth(60);

        final PositiveLongTextField pltf = new PositiveLongTextField();
        pltf.setLimitValue(Short.MAX_VALUE);

        pltf.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                pltf.setSelectionStart(pltf.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                pltf.setSelectionStart(0);
                pltf.setSelectionEnd(pltf.getText().length());
            }
        });

        tblSchema.getColumn("Length").setCellEditor(new DefaultCellEditor(pltf));
        txtClientPort.setLimitValue(65535);

        btnClientBrowseDataFile.setMnemonic(KeyEvent.VK_B);
        btnOk.setMnemonic(KeyEvent.VK_ENTER);
        btnCancel.setMnemonic(KeyEvent.VK_ESCAPE);
        btnOk.setDefaultCapable(true);

        popup.add(actAddField);
        popup.add(actDeleteField);
        popup.add(actMoveFieldUp);
        popup.add(actMoveFieldDown);
    }

    // update the UI view according to the state of the dialog.
    private void update()
    {
        ListSelectionModel lsm = tblSchema.getSelectionModel();

        tblSchema.setEnabled(!owner.isConnected());
        actAddField.setEnabled(!owner.isConnected());
        actDeleteField.setEnabled(!lsm.isSelectionEmpty() && !owner.isConnected());
        actMoveFieldUp.setEnabled(lsm.getMaxSelectionIndex() > 0 && !lsm.isSelectionEmpty() && !owner.isConnected());
        actMoveFieldDown.setEnabled(lsm.getMaxSelectionIndex() < tblSchema.getRowCount() - 1 && !lsm.isSelectionEmpty() && !owner.isConnected());

        lblHost.setEnabled(owner.isNetworkedMode() && !owner.isConnected());
        txtHost.setEnabled(owner.isNetworkedMode() && !owner.isConnected());
        txtHost.setEditable(owner.isNetworkedMode() && !owner.isConnected());
        lblClientPort.setEnabled(owner.isNetworkedMode() && !owner.isConnected());
        txtClientPort.setEnabled(owner.isNetworkedMode() && !owner.isConnected());
        txtClientPort.setEditable(owner.isNetworkedMode() && !owner.isConnected());
        lblClientDbJndiName.setEnabled(owner.isNetworkedMode() && !owner.isConnected());
        txtClientDbJndiName.setEnabled(owner.isNetworkedMode() && !owner.isConnected());
        txtClientDbJndiName.setEditable(owner.isNetworkedMode() && !owner.isConnected());

        lblClientDataFileName.setEnabled(!owner.isNetworkedMode() && !owner.isConnected());
        txtClientDataFileName.setEnabled(!owner.isNetworkedMode() && !owner.isConnected());
        txtClientDataFileName.setEditable(!owner.isNetworkedMode() && !owner.isConnected());
        btnClientBrowseDataFile.setEnabled(!owner.isNetworkedMode() && !owner.isConnected());

        setComponentsToolTipText();
    }

    // update the UI view according to the attributes of the underlying data model.
    private void updateConfigurationView()
    {
        ConfigurationSchemaTableModel stm = (ConfigurationSchemaTableModel)tblSchema.getModel();
        stm.clear();

        if(owner.getConfiguration().getMetaSchema() != null)
        {
            for(int i = 0; i < owner.getConfiguration().getMetaSchema().length; i++)
            {
                stm.addRow(owner.getConfiguration().getMetaSchema()[i]);
            }
        }

        txtHost.setText(owner.getConfiguration().getHost());
        txtClientPort.setValue((short)owner.getConfiguration().getClientPort());
        txtClientDbJndiName.setText(owner.getConfiguration().getClientDbJndiName());
        txtClientDataFileName.setText(owner.getConfiguration().getClientDataFilename());
        chkConfirmCut.setSelected(owner.getConfiguration().isConfirmCut());
        chkConfirmDelete.setSelected(owner.getConfiguration().isConfirmDelete());
        chkConfirmExit.setSelected(owner.getConfiguration().isConfirmExit());
    }

    // update the data model according to the UI view.
    private void updateConfigurationModel()
    {
        ConfigurationSchemaTableModel stm = (ConfigurationSchemaTableModel)tblSchema.getModel();
        SchemaColumn[] schema = new SchemaColumn[stm.getRowCount()];

        for(int i = 0; i < schema.length; i++)
        {
            schema[i] = stm.getRow(i);
        }

        owner.getConfiguration().setMetaSchema(schema);
        owner.getConfiguration().setHost(txtHost.getText());
        owner.getConfiguration().setClientPort((short)txtClientPort.getValue());
        owner.getConfiguration().setClientDbJndiName(txtClientDbJndiName.getText());
        owner.getConfiguration().setClientDataFilename(txtClientDataFileName.getText());
        owner.getConfiguration().setConfirmCut(chkConfirmCut.isSelected());
        owner.getConfiguration().setConfirmDelete(chkConfirmDelete.isSelected());
        owner.getConfiguration().setConfirmExit(chkConfirmExit.isSelected());

        owner.updateSchema(schema);
    }

    // set tool tip text on components that make up this dialog.
    private void setComponentsToolTipText()
    {
        tblSchema.setToolTipText(owner.isConnected() ? "Disconnect to edit the schema" : "The schema of the data");
        txtHost.setToolTipText(owner.isConnected() && owner.isNetworkedMode() ? "Disconnect to edit the host name" : "The host name of the data server");
        txtClientPort.setToolTipText(owner.isConnected() && owner.isNetworkedMode() ? "Disconnect to edit the port" : "The port number of the data server");
        txtClientDbJndiName.setToolTipText(owner.isConnected() && owner.isNetworkedMode() ? "Disconnect to edit the JNDI name of the service object on the data server" : "The JNDI name of the service object on the data server");
        txtClientDataFileName.setToolTipText(owner.isConnected() && !owner.isNetworkedMode() ? "Disconnect to edit the name of the data file" : "The name of the data file");
        btnClientBrowseDataFile.setToolTipText(owner.isConnected() && !owner.isNetworkedMode() ? "Disconnect to edit the name of the data file" : "Browse to the data file");

        chkConfirmCut.setToolTipText("Confirm when performing a 'cut' on a data record");
        chkConfirmDelete.setToolTipText("Confirm when performing a 'delete' on a data record");
        chkConfirmExit.setToolTipText("Confirm when performing an 'exit' on the application");
    }

    // accept the configuration changes
    private void ok()
    {
        updateConfigurationModel();

        try
        {
            PassiveProperties props = new PassiveProperties(owner.getConfiguration().toProperties());
            props.store(PROPERTIES_FILE, PROPERTIES_FILE_HEADER);
        }
        catch(IOException ioe)
        {
           JOptionPane.showMessageDialog(owner, ioe.getMessage(), ioe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
        }

        hide();
    }

    private class SchemaPanel extends JPanel
    {
        public SchemaPanel()
        {
            setLayout(new BorderLayout());
            setBorder(new TitledBorder(new LineBorder(Color.black), "Data Schema"));
            add(new JScrollPane(tblSchema), BorderLayout.CENTER);
            add(new SchemaButtonsPanel(), BorderLayout.SOUTH);
        }
    }

    private class SchemaButtonsPanel extends Panel
    {
        public SchemaButtonsPanel()
        {
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            setLayout(gbl);

            gbc.weightx = 0.5;
            gbc.weighty = 0.5;

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(btnAddField, gbc);
            add(btnAddField);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(btnDeleteField, gbc);
            add(btnDeleteField);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(btnMoveFieldUp, gbc);
            add(btnMoveFieldUp);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(btnMoveFieldDown, gbc);
            add(btnMoveFieldDown);
        }
    }

    private class NetworkedModePanel extends JPanel
    {
        public NetworkedModePanel()
        {
            setBorder(new TitledBorder(new LineBorder(Color.black), "Networked Client Mode"));

            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            setLayout(gbl);

            gbc.weightx = 0.5;
            gbc.weighty = 0.5;
            gbc.insets = new Insets(4, 4, 4, 4);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(lblHost, gbc);
            add(lblHost);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(txtHost, gbc);
            add(txtHost);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(lblClientPort, gbc);
            add(lblClientPort);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(txtClientPort, gbc);
            add(txtClientPort);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(lblClientDbJndiName, gbc);
            add(lblClientDbJndiName);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(txtClientDbJndiName, gbc);
            add(txtClientDbJndiName);
        }
    }

    private class LocalModePanel extends JPanel
    {
        public LocalModePanel()
        {
            setBorder(new TitledBorder(new LineBorder(Color.black), "Local Client Mode"));

            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            setLayout(gbl);

            gbc.weightx = 0.5;
            gbc.weighty = 0.5;
            gbc.insets = new Insets(4, 4, 4, 4);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(lblClientDataFileName, gbc);
            add(lblClientDataFileName);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(txtClientDataFileName, gbc);
            add(txtClientDataFileName);

            gbc.gridx = 2;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(btnClientBrowseDataFile, gbc);
            add(btnClientBrowseDataFile);
        }
    }

    private class ConfirmPanel extends JPanel
    {
        public ConfirmPanel()
        {
            setBorder(new TitledBorder(new LineBorder(Color.black), "Confirm Actions"));

            GridBagConstraints gbc = new GridBagConstraints();
            GridBagLayout gbl = new GridBagLayout();
            setLayout(gbl);

            gbc.weightx = 0.5;
            gbc.weighty = 0.5;
            gbc.insets = new Insets(4, 4, 4, 4);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(chkConfirmCut, gbc);
            add(chkConfirmCut);

            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(chkConfirmDelete, gbc);
            add(chkConfirmDelete);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbl.setConstraints(chkConfirmExit, gbc);
            add(chkConfirmExit);
        }
    }

    private class AddSchemaField extends AbstractAction
    {
        public AddSchemaField()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            ((ConfigurationSchemaTableModel)tblSchema.getModel()).addRow(new SchemaColumnImpl());
            update();
        }

        private void setup()
        {
            putValue(NAME, "Add Schema Field");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/plus16.gif")));
            putValue(SHORT_DESCRIPTION, "Add a new field to the schema");
            putValue(LONG_DESCRIPTION, "Add a new field to the schema");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_PLUS));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_MASK));
            putValue(ACTION_COMMAND_KEY, "add-schema-field-command");
        }
    }

    private class DeleteSchemaField extends AbstractAction
    {
        public DeleteSchemaField()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            ListSelectionModel lsm = tblSchema.getSelectionModel();
            int selected = lsm.getMaxSelectionIndex();

            ConfigurationSchemaTableModel stm = (ConfigurationSchemaTableModel)tblSchema.getModel();
            SchemaColumn sc = stm.getRow(selected);

            boolean delete = true;

            if(sc.getDisplayName() != null && sc.getDisplayName().length() > 0)
            {
                StringBuffer message = new StringBuffer();
                message.append("Delete Field: ");
                message.append(sc.getDisplayName());
                message.append(" ?");

                int selection = JOptionPane.showConfirmDialog(owner, message, "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                delete = (selection == JOptionPane.YES_OPTION);
            }

            if(delete)
            {
                stm.deleteRow(selected);
                update();
            }
        }

        private void setup()
        {
            putValue(NAME, "Delete Schema Field");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/minus16.gif")));
            putValue(SHORT_DESCRIPTION, "Delete a field from the schema");
            putValue(LONG_DESCRIPTION, "Delete a field from the schema");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_MINUS));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_MASK));
            putValue(ACTION_COMMAND_KEY, "delete-schema-field-command");
        }
    }

    private class MoveUpSchemaField extends AbstractAction
    {
        public MoveUpSchemaField()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            ListSelectionModel lsm = tblSchema.getSelectionModel();
            int selected = lsm.getMaxSelectionIndex();
            ConfigurationSchemaTableModel stm = (ConfigurationSchemaTableModel)tblSchema.getModel();

            if(selected > 0)
            {
                stm.moveUp(selected);

                lsm.setLeadSelectionIndex(selected - 1);
                update();
            }
        }

        private void setup()
        {
            putValue(NAME, "Move Schema Field Up");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/moveup16.gif")));
            putValue(SHORT_DESCRIPTION, "Move the selected schema field up");
            putValue(LONG_DESCRIPTION, "Move the selected schema field up");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_UP));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK));
            putValue(ACTION_COMMAND_KEY, "move-up-schema-field-command");
        }
    }

    private class MoveDownSchemaField extends AbstractAction
    {
        public MoveDownSchemaField()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            ListSelectionModel lsm = tblSchema.getSelectionModel();
            int selected = lsm.getMaxSelectionIndex();
            ConfigurationSchemaTableModel stm = (ConfigurationSchemaTableModel)tblSchema.getModel();

            if(selected < tblSchema.getRowCount())
            {
                stm.moveDown(selected);

                lsm.setLeadSelectionIndex(selected + 1);
                update();
            }
        }

        private void setup()
        {
            putValue(NAME, "Move Schema Field Down");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/movedown16.gif")));
            putValue(SHORT_DESCRIPTION, "Move the selected schema field down");
            putValue(LONG_DESCRIPTION, "Move the selected schema field down");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_DOWN));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK));
            putValue(ACTION_COMMAND_KEY, "move-down-schema-field-command");
        }
    }
}



