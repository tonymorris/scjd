package suncertify.db.server.ui;

import suncertify.db.utils.PositiveLongTextField;
import suncertify.db.utils.AboutDialog;
import suncertify.db.utils.LongOperationDialog;
import suncertify.db.utils.PassiveProperties;
import suncertify.db.client.MiddleLocator;
import suncertify.db.Constants;
import suncertify.db.server.RemoteData;
import suncertify.db.server.RemoteDB;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Date;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

/**
 * Provides a graphical interface for starting the data server. Once the user starts the server,
 * the application becomes non-responsive - the user can only select "About" or "Exit".
 * The implementation exports a {link RemoteData RemoteData} object to the configured port using
 * the configured JNDI name.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class ServerFrame extends JFrame implements Constants
{
    private ServerConfiguration configuration;

    private Action startServer;
    private Action about;
    private Action exit;

    private boolean started;

    private JToolBar toolbar;
    private JMenuBar mb;

    private JLabel lblDataFilename;
    private JTextField txtDataFilename;
    private JButton btnBrowseDataFilename;
    private JLabel lblPort;
    private PositiveLongTextField txtPort;
    private JLabel lblDbJndiName;
    private JTextField txtDbJndiName;
    private JCheckBox chkConfirmExit;
    private JButton btnStartServer;
    private JLabel lblStatus;

    /**
     * Construct a <code>ServerFrame</code> with the given configuration.
     *
     * @param configuration The configuration to construct this <code>ServerFrame</code> with.
     * @param title The window title.
     */
    public ServerFrame(ServerConfiguration configuration, String title)
    {
        super(title);

        this.configuration = configuration;

        setup();
    }

    // initialize the server frame
    private void setup()
    {
        instantiate();

        layoutComponents();

        addListeners();

        setComponentAttributes();

        updateConfigurationView();
    }

    // instantiate all components that make up this frame
    private void instantiate()
    {
        startServer = new StartServerAction();
        about = new AboutAction();
        exit = new ExitAction();

        mb = new JMenuBar();
        toolbar = new JToolBar("Sun Certified Developer for the Java 2 Platform - Server Application");
        lblDataFilename = new JLabel("Data File Name", JLabel.LEFT);
        txtDataFilename = new JTextField(20);
        btnBrowseDataFilename = new JButton("Browse");
        lblPort = new JLabel("Port", JLabel.LEFT);
        txtPort = new PositiveLongTextField(20);
        lblDbJndiName = new JLabel("DB JNDI Name");
        txtDbJndiName = new JTextField(20);
        chkConfirmExit = new JCheckBox("Confirm stopping and exiting the server");
        btnStartServer = new JButton(startServer);
        lblStatus = new JLabel("Status: Idle");
    }

    // lay the components out on the frame
    private void layoutComponents()
    {
        setJMenuBar(mb);

        GridBagConstraints gbc = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        gbc.insets = new Insets(4, 4, 4, 4);

        JPanel pnlMain = new JPanel(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(lblDataFilename, gbc);
        pnlMain.add(lblDataFilename);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(txtDataFilename, gbc);
        pnlMain.add(txtDataFilename);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(btnBrowseDataFilename, gbc);
        pnlMain.add(btnBrowseDataFilename);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(lblPort, gbc);
        pnlMain.add(lblPort);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(txtPort, gbc);
        pnlMain.add(txtPort);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(lblDbJndiName, gbc);
        pnlMain.add(lblDbJndiName);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(txtDbJndiName, gbc);
        pnlMain.add(txtDbJndiName);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(chkConfirmExit, gbc);
        pnlMain.add(chkConfirmExit);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbl.setConstraints(btnStartServer, gbc);
        pnlMain.add(btnStartServer);

        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.add(lblStatus);
        pnlStatus.setBorder(new BevelBorder(BevelBorder.LOWERED));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pnlMain, BorderLayout.CENTER);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(pnlStatus, BorderLayout.SOUTH);
    }

    // add event handlers to the frame
    private void addListeners()
    {
        txtDataFilename.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtDataFilename.setSelectionStart(txtDataFilename.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtDataFilename.setSelectionStart(0);
                txtDataFilename.setSelectionEnd(txtDataFilename.getText().length());
            }
        });

        txtPort.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtPort.setSelectionStart(txtPort.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtPort.setSelectionStart(0);
                txtPort.setSelectionEnd(txtPort.getText().length());
            }
        });

        txtDbJndiName.addFocusListener(new FocusListener()
        {
            public void focusLost(FocusEvent fe)
            {
                txtDbJndiName.setSelectionStart(txtDbJndiName.getText().length());
            }

            public void focusGained(FocusEvent fe)
            {
                txtDbJndiName.setSelectionStart(0);
                txtDbJndiName.setSelectionEnd(txtDbJndiName.getText().length());
            }
        });

        txtDataFilename.getDocument().addDocumentListener(new DocumentListener()
        {
            public void removeUpdate(DocumentEvent de)
            {
                update();
            }

            public void changedUpdate(DocumentEvent de)
            {
                update();
            }

            public void insertUpdate(DocumentEvent de)
            {
                update();
            }
        });

        txtPort.getDocument().addDocumentListener(new DocumentListener()
        {
            public void removeUpdate(DocumentEvent de)
            {
                update();
            }

            public void changedUpdate(DocumentEvent de)
            {
                update();
            }

            public void insertUpdate(DocumentEvent de)
            {
                update();
            }
        });

        txtDbJndiName.getDocument().addDocumentListener(new DocumentListener()
        {
            public void removeUpdate(DocumentEvent de)
            {
                update();
            }

            public void changedUpdate(DocumentEvent de)
            {
                update();
            }

            public void insertUpdate(DocumentEvent de)
            {
                update();
            }
        });

        txtDataFilename.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                startServer.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "start-server"));
            }
        });

        txtPort.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                startServer.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "start-server"));
            }
        });

        txtDbJndiName.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                startServer.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "start-server"));
            }
        });

        btnBrowseDataFilename.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(ServerFrame.this);

                if(result == JFileChooser.APPROVE_OPTION)
                {
                    txtDataFilename.setText(fc.getSelectedFile().getAbsolutePath());
                }
            }
        });

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
                exit.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "exit"));
            }
        });
    }

    // set attributes of components that make up this frame
    private void setComponentAttributes()
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIconImage((new ImageIcon(getClass().getResource("/res/ficon.gif"))).getImage());
        txtPort.setLimitValue(65535);

        toolbar.add(startServer);
        toolbar.add(about);
        toolbar.add(exit);

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        file.add(startServer);
        file.add(exit);
        mb.add(file);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.add(about);
        mb.add(help);

        txtDataFilename.setToolTipText("Specify the name of the data source file name (required)");
        btnBrowseDataFilename.setMnemonic(KeyEvent.VK_B);
        btnBrowseDataFilename.setToolTipText("Browse to the data source file name");
        txtDbJndiName.setToolTipText("Specify the name of the JNDI name to export the remote data object to (required)");
        txtPort.setToolTipText("Specify the TCP port to bind the server to (required)");
        chkConfirmExit.setToolTipText("Confirm with a dialog when requested to exit the server application");
    }

    // update the state of the UI
    private void update()
    {
        txtDataFilename.setEditable(!started);
        txtDataFilename.setEnabled(!started);
        btnBrowseDataFilename.setEnabled(!started);
        txtDbJndiName.setEditable(!started);
        txtDbJndiName.setEnabled(!started);
        txtPort.setEditable(!started);
        txtPort.setEnabled(!started);
        startServer.setEnabled(!started && txtDataFilename.getText().trim().length() > 0 && txtPort.getText().trim().length() > 0 && txtDbJndiName.getText().trim().length() > 0);
    }

    // When a request to stop the server, the user may be prompted
    private boolean requestStopServer()
    {
        boolean stop = true;

        if(chkConfirmExit.isSelected())
        {
            int selection = JOptionPane.showConfirmDialog(this, started ? "Stop the server and exit the application ?" : "Exit the application ?", started ? "Stop and exit ?" : "Exit ?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            stop = (selection == JOptionPane.YES_OPTION);
        }

        return stop;
    }

    // updates the configuration according to the UI state.
    private void updateConfigurationModel()
    {
        configuration.setConfirmServerStop(chkConfirmExit.isSelected());
        configuration.setDataFilename(txtDataFilename.getText());
        configuration.setDbJndiName(txtDbJndiName.getText());
        configuration.setPort((int)txtPort.getValue());
    }

    // updates the UI state according to the configuration.
    private void updateConfigurationView()
    {
        chkConfirmExit.setSelected(configuration.isConfirmServerStop());
        txtDataFilename.setText(configuration.getDataFilename());
        txtDbJndiName.setText(configuration.getDbJndiName());
        txtPort.setValue(configuration.getPort());
    }

    // The action to perform to start the server
    private class StartServerAction extends AbstractAction
    {
        public StartServerAction()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            final LongOperationDialog dialog = new LongOperationDialog(ServerFrame.this, "Starting...", "Starting the data server. Please Wait...");
            dialog.pack();
            MiddleLocator locator = new MiddleLocator(ServerFrame.this);
            locator.locate(dialog);

            final StartServerWorker worker = new StartServerWorker(dialog);

            new Thread(worker).start();

            dialog.show();
        }

        private void setup()
        {
            putValue(NAME, "Start Server");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/play16.gif")));
            putValue(SHORT_DESCRIPTION, "Starts the Data Server");
            putValue(LONG_DESCRIPTION, "Starts the Data Server");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_SPACE));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
            putValue(ACTION_COMMAND_KEY, "start-server-command");
        }
    }

    // The action to perform to display the "About" dialog.
    private class AboutAction extends AbstractAction
    {
        public AboutAction()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            Dialog dialog = new AboutDialog(ServerFrame.this);
            dialog.pack();
            MiddleLocator locator = new MiddleLocator(ServerFrame.this);
            locator.locate(dialog);
            dialog.show();
        }

        private void setup()
        {
            putValue(NAME, "About");
            putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/res/about16.gif")));
            putValue(SHORT_DESCRIPTION, "About this application");
            putValue(LONG_DESCRIPTION, "About this application");
            putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_F1));
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
            putValue(ACTION_COMMAND_KEY, "about-command");
        }
    }

    // the action to perform when requested to exit.
    private class ExitAction extends AbstractAction
    {
        public ExitAction()
        {
            setup();
        }

        public void actionPerformed(ActionEvent ae)
        {
            updateConfigurationModel();

            if(requestStopServer())
            {
                ServerFrame.this.hide();
                ServerFrame.this.dispose();

                try
                {
                    PassiveProperties props = new PassiveProperties(configuration.toProperties());
                    props.store(PROPERTIES_FILE, PROPERTIES_FILE_HEADER);
                }
                catch(IOException ioe)
                {
                    JOptionPane.showMessageDialog(ServerFrame.this, ioe.getMessage(), ioe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                }

                System.exit(0);
            }
        }

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

    // a worker thread that starts the server.
    private class StartServerWorker implements Runnable
    {
        private final LongOperationDialog dialog;

        public StartServerWorker(LongOperationDialog dialog)
        {
            this.dialog = dialog;
        }

        public void run()
        {
            boolean ok;

            try
            {
                RemoteDB data = new RemoteData(txtDataFilename.getText());

                Registry reg;

                reg = LocateRegistry.createRegistry((int)txtPort.getValue());

                reg.rebind(txtDbJndiName.getText(), data);

                updateConfigurationModel();

                PassiveProperties props = new PassiveProperties(configuration.toProperties());
                props.store(PROPERTIES_FILE, PROPERTIES_FILE_HEADER);

                ok = true;
            }
            catch(final RemoteException re)
            {
                if(!dialog.isCancelled())
                {
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            JOptionPane.showMessageDialog(ServerFrame.this, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }

                ok = false;
            }
            catch(final FileNotFoundException fnfe)
            {
                if(!dialog.isCancelled())
                {
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            JOptionPane.showMessageDialog(ServerFrame.this, fnfe.getMessage(), fnfe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }

                ok = false;
            }
            catch(final Exception e)
            {
                if(!dialog.isCancelled())
                {
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            JOptionPane.showMessageDialog(ServerFrame.this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }

                ok = false;
            }

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    dialog.hide();
                    dialog.dispose();
                }
            });

            if(ok)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        if(!dialog.isCancelled())
                        {
                            StringBuffer status = new StringBuffer();
                            status.append("Status: Server started at ");
                            status.append(new Date());
                            lblStatus.setText(status.toString());

                            started = true;
                            update();
                        }
                    }
                });
            }
        }
    }
}
