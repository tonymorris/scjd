package suncertify.db.client.actions;

import suncertify.db.client.DataFrame;
import suncertify.db.client.SchemaColumn;
import suncertify.db.utils.LongOperationDialog;
import suncertify.db.client.MiddleLocator;
import suncertify.db.server.RemoteData;
import suncertify.db.server.RemoteDB;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.EventQueue;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.FileNotFoundException;

/**
 * An action that can be fired by the user.
 * The action will either attempt to connect to the back end data source, or disconnect depending on the
 * state of the application. A dialog is displayed while the action attempts to connect to the data source.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class Connect extends AbstractAction
{
    private DataFrame owner;

    /**
     * Construct a <code>Connect</code> action with the given owner.
     *
     * @param owner The owner that constructed this <code>Connect</code> action.
     */
    public Connect(DataFrame owner)
    {
        this.owner = owner;

        updateProperties();
    }

    /**
     * Executed when the action is fired.
     *
     * @param ae Represents information about the event that was fired.
     */
    public void actionPerformed(ActionEvent ae)
    {
        if(owner.isConnected())
        {
            disconnect();
        }
        else
        {
            connect();
        }

        updateProperties();

        owner.update();
    }

    /**
     * Updates the properties of this <tt>Connect</tt> action.
     */
    public void updateProperties()
    {
        putValue(NAME, owner.isConnected() ? "Disconnect" : "Connect");
        putValue(SMALL_ICON, owner.isConnected() ? new ImageIcon(getClass().getResource("/res/disconnect16.gif")) : new ImageIcon(getClass().getResource("/res/connect16.gif")));
        putValue(SHORT_DESCRIPTION, owner.isConnected() ? "Disconnect from the data service" : "Connect to the data service");
        putValue(LONG_DESCRIPTION, owner.isConnected() ? "Disconnect from the data service" : "Connect to the data service");
        putValue(MNEMONIC_KEY, owner.isConnected() ? new Integer(KeyEvent.VK_ESCAPE) : new Integer(KeyEvent.VK_ENTER));
        putValue(ACCELERATOR_KEY, owner.isConnected() ? KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, InputEvent.CTRL_MASK) : KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK));
        putValue(ACTION_COMMAND_KEY, owner.isConnected() ? "connect-command" : "disconnect-command");
    }

    // connects in a worker thread while displaying an indeterminate progress bar during connection
    private void connect()
    {
        final LongOperationDialog dialog = new LongOperationDialog(owner, "Connecting...", "Connecting. Please Wait...");
        dialog.pack();
        MiddleLocator locator = new MiddleLocator(owner);
        locator.locate(dialog);

        final ConnectWorker worker = new ConnectWorker(dialog);

        new Thread(worker).start();

        dialog.show();

        updateProperties();
    }

    // connect the networked data source
    private Object connectNetworked() throws NotBoundException, RemoteException
    {
        SchemaColumn[] schema = owner.getConfiguration().getMetaSchema();

        if(schema == null || schema.length <= 0)
        {
            JOptionPane.showMessageDialog(owner, "Data schema not specified", "Data schema not specified", JOptionPane.ERROR_MESSAGE);
        }
        else if(owner.getConfiguration().getClientDbJndiName() == null || owner.getConfiguration().getClientDbJndiName().trim().length() == 0)
        {
            JOptionPane.showMessageDialog(owner, "Remote object name not specified", "Remote object name not specified", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            Registry reg;

            if(owner.getConfiguration().getHost() == null || owner.getConfiguration().getHost().trim().length() == 0)
            {
                reg = LocateRegistry.getRegistry(owner.getConfiguration().getClientPort());
            }
            else
            {
                reg = LocateRegistry.getRegistry(owner.getConfiguration().getHost(), owner.getConfiguration().getClientPort());
            }

            return reg.lookup(owner.getConfiguration().getClientDbJndiName());
        }

        return null;
    }

    // connect to the local data source
    private RemoteDB connectLocal()
    {
        SchemaColumn[] schema = owner.getConfiguration().getMetaSchema();
        String filename = owner.getConfiguration().getClientDataFilename();

        if(schema == null || schema.length <= 0)
        {
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    JOptionPane.showMessageDialog(owner, "Data schema not specified", "Data schema not specified", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        else if(filename == null || filename.trim().length() == 0)
        {
            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    JOptionPane.showMessageDialog(owner, "Data file name not specified", "Data file name not specified", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        else
        {
            try
            {
                return new RemoteData(filename);
            }
            catch(final RemoteException re)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JOptionPane.showMessageDialog(owner, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
            catch(final FileNotFoundException fnfe)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JOptionPane.showMessageDialog(owner, fnfe.getMessage(), fnfe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
            catch(final Exception e)
            {
                EventQueue.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        JOptionPane.showMessageDialog(owner, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        }

        return null;
    }

    // disconnect from the data source
    private void disconnect()
    {
        owner.setData(null);
        owner.clear();
        updateProperties();
    }

    // performs the work of connecting to the data source while the progress bar and cancel button are being displayed.
    private class ConnectWorker implements Runnable
    {
        private RemoteDB db;
        private final LongOperationDialog dialog;

        public ConnectWorker(LongOperationDialog dialog)
        {
            this.dialog = dialog;
        }

        public void run()
        {
            if(owner.isNetworkedMode())
            {
                try
                {
                    Object o = connectNetworked();

                    if(o instanceof RemoteDB)
                    {
                        db = (RemoteDB)o;
                    }
                    else
                    {
                        if(!dialog.isCancelled())
                        {
                            EventQueue.invokeLater(new Runnable()
                            {
                                public void run()
                                {
                                    JOptionPane.showMessageDialog(owner, "Invalid remote object not an instance of suncertify.db.server.RemoteDB", "Invalid remote object", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                        }
                    }
                }
                catch(final NotBoundException nbe)
                {
                    if(!dialog.isCancelled())
                    {
                        EventQueue.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                JOptionPane.showMessageDialog(owner, nbe.getMessage(), nbe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                }
                catch(final RemoteException re)
                {
                    if(!dialog.isCancelled())
                    {
                        EventQueue.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                JOptionPane.showMessageDialog(owner, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                }
                catch(final Exception e)
                {
                    if(!dialog.isCancelled())
                    {
                        EventQueue.invokeLater(new Runnable()
                        {
                            public void run()
                            {
                                JOptionPane.showMessageDialog(owner, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                }
            }
            else
            {
                db = connectLocal();
            }

            EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    if(db != null)
                    {
                        owner.setData(db);

                        owner.refreshAll();
                    }

                    dialog.hide();
                    dialog.dispose();
                }
            });
        }
    }
}
