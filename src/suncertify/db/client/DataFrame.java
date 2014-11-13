package suncertify.db.client;

import suncertify.db.client.actions.CreateRecord;
import suncertify.db.client.actions.DeleteRecord;
import suncertify.db.client.actions.Refresh;
import suncertify.db.client.actions.Configure;
import suncertify.db.client.actions.About;
import suncertify.db.client.actions.Exit;
import suncertify.db.client.actions.Cut;
import suncertify.db.client.actions.Copy;
import suncertify.db.client.actions.Paste;
import suncertify.db.client.actions.Connect;
import suncertify.db.client.actions.Search;
import suncertify.db.client.actions.EditRecord;
import suncertify.db.client.actions.RefreshAll;
import suncertify.db.server.RemoteDB;
import suncertify.db.RecordNotFoundException;
import suncertify.db.DuplicateKeyException;
import suncertify.db.DataRecordImpl;

import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;
import javax.swing.JMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.rmi.RemoteException;

/**
 * Represents native frame and the core operations of the client application.
 * Exposes a public API of operations that may be performed during the execution of the client.
 * The frame consists of a data model that contains
 * <li>a <code>boolean</code> member that determines whether or not the application is executing in local or networked mode - this must be specified at construction time</li>
 * <li>a configuration object that represents how the application is to be configured - this must be specified at construction time</li>
 * <li>a {@link RemoteDB RemoteDB} reference to the server's exposed data object</li>
 * <li>a <code>String[]</code> representing the search criteria of the currently displayed data records</li>
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DataFrame extends JFrame
{
    private boolean networkedMode;
    private Configuration configuration;
    private RemoteDB data;
    private SearchCriteria criteria;

    private JMenuBar mb;
    private JToolBar toolbar;
    private JTable tblData;
    private JLabel lblStatus;
    private JPopupMenu popup;
    private Connect connect;
    private Action cut;
    private Action copy;
    private Action paste;
    private Action editRecord;
    private Action createRecord;
    private Action deleteRecord;
    private Action search;
    private Action refresh;
    private Action refreshAll;
    private Action configure;
    private Action about;
    private Action exit;

    /**
     * Construct a <code>DataFrame</code> in local or netowkred mode with the given configuration.
     * The frame with have no title.
     *
     * @param networkedMode <code>true</code> to start in networked mode, <code>false</code> otherwise.
     * @param configuration The configuration of the application. This should be loaded from a properties file.
     */
    public DataFrame(boolean networkedMode, Configuration configuration)
    {
        this(networkedMode, configuration, "");
    }

    /**
     * Construct a <code>DataFrame</code> in local or netowkred mode with the given configuration.
     *
     * @param networkedMode <code>true</code> to start in networked mode, <code>false</code> otherwise.
     * @param configuration The configuration of the application. This should be loaded from a properties file.
     * @param title The title of the frame
     */
    public DataFrame(boolean networkedMode, Configuration configuration, String title)
    {
        super(title);

        this.networkedMode = networkedMode;
        this.configuration = configuration;
        setup();

        updateSchema(configuration.getMetaSchema());
    }

    /**
     * Set the status bar according to the current state of the data model of the frame.
     * The status bar will display whether or not the application is connected to a data source,
     * and, if connected, the number of records displayed and the selected record.
     */
    public void setStatus()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");

        sb.append(data == null ? "Not Connected. " : "Connected. ");

        sb.append(networkedMode ? "Network Mode. " : "Local Mode. ");

        ListSelectionModel lsm = tblData.getSelectionModel();
        DataTableModel dtm = (DataTableModel)tblData.getModel();

        if(lsm.isSelectionEmpty())
        {
            sb.append("Displaying <b>(");
            sb.append(dtm.getRowCount());
            sb.append(")</b> Data DataRecord");

            if(dtm.getRowCount() != 1)
            {
                sb.append('s');
            }
        }
        else
        {
            sb.append("Selected Data DataRecord <b>(");
            sb.append(lsm.getMaxSelectionIndex() + 1);
            sb.append(")</b> of <b>(");
            sb.append(dtm.getRowCount());
            sb.append(")</b> Data DataRecord");

            if(dtm.getRowCount() != 1)
            {
                sb.append('s');
            }
        }

        sb.append("</html>");

        lblStatus.setText(sb.toString());
        tblData.setToolTipText(sb.toString());
    }

    /**
     * Determines whether or not the application is running in local or networked mode.
     *
     * @return <code>true</code> if the application is running in networked mode, <code>false</code> otherwise.
     */
    public boolean isNetworkedMode()
    {
        return networkedMode;
    }

    /**
     * Returns the configuration object that this frame is running with.
     * This configuration may be altered by the user during the execution of the application.
     *
     * @return The configuration object that this frame is running with.
     */
    public Configuration getConfiguration()
    {
        return configuration;
    }

    /**
     * Returns the table model of the data records that is used by the application.
     *
     * @return The table model of the data records that is used by the application.
     */
    public DataTableModel getDataTableModel()
    {
        return (DataTableModel)tblData.getModel();
    }

    /**
     * Returns the selected record index according to the <code>ListSelectionModel#getMaxSelectionIndex</code> method.
     *
     * @return The selected record index according to the <code>ListSelectionModel#getMaxSelectionIndex</code> method.
     */
    public int getSelectedRecordIndex()
    {
        return tblData.getSelectionModel().getMaxSelectionIndex();
    }

    /**
     * Updates the frames view according to the underlying data model.
     */
    public void update()
    {
        ListSelectionModel lsm = tblData.getSelectionModel();

        cut.setEnabled(data != null && !lsm.isSelectionEmpty());
        copy.setEnabled(data != null && !lsm.isSelectionEmpty());
        paste.setEnabled(data != null);
        editRecord.setEnabled(data != null && !lsm.isSelectionEmpty());
        createRecord.setEnabled(data != null);
        deleteRecord.setEnabled(data != null && !lsm.isSelectionEmpty());
        search.setEnabled(data != null);
        refreshAll.setEnabled(data != null);
        refresh.setEnabled(data != null);

        connect.updateProperties();

        setStatus();
    }

    /**
     * Sets the handle to the data source held by the application.
     * Setting a value of <code>null</code> indicates that the application has no handle
     * and therefore, is "not connected" to a data source.
     *
     * @param data The new value of the handle to the data source held by the application.
     */
    public void setData(RemoteDB data)
    {
        this.data = data;

        update();
    }

    /**
     * Determines if the application is connected to a data source.
     *
     * @return <code>true</code> if the application is connected to a data source, <code>false</code> otherwise.
     */
    public boolean isConnected()
    {
        return data != null;
    }

    /**
     * Refreshes the data record display to show all records from the data source.
     * The last searched criteria will be reset to 'all'.
     * Calling this method is equivalent to calling <code>{@link #refresh(SearchCriteria) refresh(null)}</code>.
     */
    public void refreshAll()
    {
        refresh(null);
    }

    /**
     * Refreshes the data record display according to the last searched criteria.
     */
    public void refresh()
    {
        refresh(this.criteria);
    }

    /**
     * Refreshes the data record display according to the given search criteria.
     * If there is no search criteria (i.e. the first time), the search criteria will be set to 'all'.
     * The last searched criteria will be updated.
     *
     * @param criteria The search criteria to refresh the record display with.
     */
    public void refresh(SearchCriteria criteria)
    {
        if(criteria == null)
        {
            criteria = new SearchCriteriaImpl(new String[configuration.getMetaSchema().length]);
        }

        DataTableModel dtm = (DataTableModel)tblData.getModel();
        dtm.clear();

        try
        {
            String[] criteriaFields = criteria.getFields();

            int[] indicies = data.find(criteriaFields);

            if(indicies == null)
            {
                JOptionPane.showMessageDialog(this, "An unknown error occurred on the server", "Unknown server error", JOptionPane.ERROR_MESSAGE);
                connect.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "connect"));
            }
            else
            {
                for(int i = 0; i < indicies.length; i++)
                {
                    try
                    {
                        String[] record = data.read(indicies[i]);

                        if(record != null && (!criteria.isExactMatch() || new ExactMatcherImpl().isExactMatch(criteriaFields, record)))
                        {
                            dtm.addDataRecord(new DataRecordImpl(indicies[i], record));
                        }

                        this.criteria = criteria;
                    }
                    catch(RecordNotFoundException rnfe)
                    {
                        JOptionPane.showMessageDialog(this, rnfe.getMessage(), rnfe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    }
                }

                update();
            }
        }
        catch(RemoteException re)
        {
            JOptionPane.showMessageDialog(this, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);

            setData(null);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);

            setData(null);
        }
    }

    /**
     * Adds a data row to the application frame display and calls. Data records are represented in a JTable.
     * {@link #update() update()} to update the display according to the underlying data model.
     *
     * @see suncertify.db.Data#create(String[])
     * @param row The row of data to add to the application frame.
     */
    public void addRow(String[] row)
    {
        try
        {
            int index = data.create(row);

            if(index >= 0)
            {
                DataTableModel dtm = (DataTableModel)tblData.getModel();
                dtm.addDataRecord(new DataRecordImpl(index, row));

                update();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "An unknown error occurred on the server", "Unknown server error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch(DuplicateKeyException dke)
        {
            JOptionPane.showMessageDialog(this, dke.getMessage(), dke.getClass().getName(), JOptionPane.ERROR_MESSAGE);
        }
        catch(RemoteException re)
        {
            JOptionPane.showMessageDialog(this, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);

            setData(null);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);

            setData(null);
        }
    }

    /**
     * Deletes the currently selected row in the application frame. Data records are represented in a JTable.
     * If no row has been selected, this call is ignored.
     *
     * @see suncertify.db.Data#lock(int)
     * @see suncertify.db.Data#delete(int, long)
     * @see suncertify.db.Data#unlock(int, long)
     */
    public void deleteSelectedRow()
    {
        ListSelectionModel lsm = tblData.getSelectionModel();
        int selected = lsm.getMaxSelectionIndex();

        if(selected >= 0)
        {
            try
            {
                DataTableModel dtm = (DataTableModel)tblData.getModel();

                int index = dtm.getDataRecord(selected).getIndex();

                long cookie = data.lock(index);

                data.delete(index, cookie);

                data.unlock(index, cookie);

                dtm.deleteDataRecord(selected);

                update();
            }
            catch(RecordNotFoundException rnfe)
            {
                JOptionPane.showMessageDialog(this, "No such record. The data view may be stale. Please refresh the data view.", rnfe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            }
            catch(RemoteException re)
            {
                JOptionPane.showMessageDialog(this, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);

                setData(null);
            }
        }
    }

    /**
     * Updates the currently selected row in the application frame. Data records are represented in a JTable.
     * If no row has been selected, this call is ignored.
     *
     * @see suncertify.db.Data#lock(int)
     * @see suncertify.db.Data#update(int, String[], long)
     * @see suncertify.db.Data#unlock(int, long)
     * @param row The new data to update the selected row with.
     */
    public void updateSelectedRow(String[] row)
    {
        ListSelectionModel lsm = tblData.getSelectionModel();
        int selected = lsm.getMaxSelectionIndex();

        if(selected >= 0)
        {
            try
            {
                DataTableModel dtm = (DataTableModel)tblData.getModel();

                int index = dtm.getDataRecord(selected).getIndex();

                long cookie = data.lock(index);

                data.update(index, row, cookie);

                data.unlock(index, cookie);

                dtm.updateDataRecord(new DataRecordImpl(row), selected);

                update();
            }
            catch(RecordNotFoundException rnfe)
            {
                JOptionPane.showMessageDialog(this, "No such record. The data view may be stale. Please refresh the data view.", rnfe.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            }
            catch(RemoteException re)
            {
                JOptionPane.showMessageDialog(this, re.getMessage(), re.getClass().getName(), JOptionPane.ERROR_MESSAGE);

                setData(null);
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);

                setData(null);
            }
        }
    }

    /**
     * Clears the application frame of data records. Data records are represented in a JTable.
     */
    public void clear()
    {
        DataTableModel dtm = (DataTableModel)tblData.getModel();
        dtm.clear();
    }

    /**
     * Updates the data schema of the data records that are represented in the application frames' JTable.
     * If the passed schema is <code>null</code> or length zero, this call is ignored.
     *
     * @param schema The new schema of the data records that are represented by the application frames' JTable.
     */
    public void updateSchema(SchemaColumn[] schema)
    {
        if(schema != null && schema.length > 0)
        {
            String[] columns = new String[schema.length];

            for(int i = 0; i < columns.length; i++)
            {
                columns[i] = schema[i].getDisplayName();
            }

            DataTableModel dtm = (DataTableModel)tblData.getModel();

            dtm.setColumns(columns);

            int totalLength = 0;

            for(int i = 0; i < schema.length; i++)
            {
                totalLength = totalLength + schema[i].getLength();
            }
        }
    }

    // initialize the application frame
    private void setup()
    {
        instantiate();

        layoutComponents();

        addListeners();

        setComponentAttributes();

        update();
    }

    // instantiate all components that make up this application frame
    private void instantiate()
    {
        mb = new JMenuBar();
        toolbar = new JToolBar("Data Client");
        tblData = new JTable(new DataTableModelImpl());
        lblStatus = new JLabel();
        popup = new JPopupMenu();
        connect = new Connect(this);
        cut = new Cut(this);
        copy = new Copy(this);
        paste = new Paste(this);
        editRecord = new EditRecord(this);
        createRecord = new CreateRecord(this);
        deleteRecord = new DeleteRecord(this);
        search = new Search(this);
        refreshAll = new RefreshAll(this);
        refresh = new Refresh(this);
        configure = new Configure(this);
        about = new About(this);
        exit = new Exit(this);
    }

    // lay the components out on the application frame
    private void layoutComponents()
    {
        setJMenuBar(mb);
        JPanel pnlMain = new JPanel(new BorderLayout());

        pnlMain.add(toolbar, BorderLayout.NORTH);
        pnlMain.add(new JScrollPane(tblData), BorderLayout.CENTER);

        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBorder(new BevelBorder(BevelBorder.LOWERED));
        pnlStatus.add(lblStatus);
        getContentPane().add(pnlStatus, BorderLayout.CENTER);

        getContentPane().add(pnlMain, BorderLayout.CENTER);
        getContentPane().add(pnlStatus, BorderLayout.SOUTH);
    }

   // add event handlers to the application frame
   private void addListeners()
    {
        tblData.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (!e.getValueIsAdjusting())
                {
                    update();
                }
            }
        });

        tblData.addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent me)
            {
                mouseReleased(me);
            }

            public void mouseReleased(MouseEvent me)
            {
                int selected = me.getY() / tblData.getRowHeight();

                if(selected >= 0 && selected < tblData.getRowCount())
                {
                    tblData.addRowSelectionInterval(selected, selected);
                }

                if(me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() >= 2)
                {
                    editRecord.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "edit-record"));
                }

                if(me.isPopupTrigger())
                {
                    popup.show(me.getComponent(), me.getX(), me.getY());
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

    // set attributes of components that make up this application frame
    private void setComponentAttributes()
    {
        setIconImage((new ImageIcon(getClass().getResource("/res/ficon.gif"))).getImage());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setData(null);
        tblData.setPreferredScrollableViewportSize(new Dimension(640, 480));
        tblData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblData.getTableHeader().setReorderingAllowed(false);

        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        file.add(connect);
        file.add(exit);
        mb.add(file);

        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        mb.add(edit);

        JMenu action = new JMenu("Action");
        action.setMnemonic(KeyEvent.VK_A);
        action.add(editRecord);
        action.add(createRecord);
        action.add(deleteRecord);
        action.add(search);
        action.add(refreshAll);
        action.add(refresh);
        mb.add(action);

        JMenu options = new JMenu("Options");
        options.setMnemonic(KeyEvent.VK_O);
        options.add(configure);
        mb.add(options);

        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.add(about);
        mb.add(help);

        toolbar.setRollover(true);
        toolbar.add(connect);
        toolbar.addSeparator();
        toolbar.add(cut);
        toolbar.add(copy);
        toolbar.add(paste);
        toolbar.addSeparator();
        toolbar.add(editRecord);
        toolbar.add(createRecord);
        toolbar.add(deleteRecord);
        toolbar.add(search);
        toolbar.add(refreshAll);
        toolbar.add(refresh);
        toolbar.add(configure);
        toolbar.addSeparator();
        toolbar.add(about);
        toolbar.add(exit);

        popup.add(cut);
        popup.add(copy);
        popup.add(paste);
        popup.addSeparator();
        popup.add(editRecord);
        popup.add(createRecord);
        popup.add(deleteRecord);
        popup.add(search);
        popup.add(refreshAll);
        popup.add(refresh);
        popup.add(configure);
        popup.addSeparator();
        popup.add(about);
        popup.add(exit);
    }
}
