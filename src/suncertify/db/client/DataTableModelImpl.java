package suncertify.db.client;

import suncertify.db.DataRecord;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.LinkedList;

/**
 * Represents the table model of the data record display in the application frames' JTable.
 * The table model contains an ordered list of {@link suncertify.db.DataRecord DataRecord}s and an array of column names.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DataTableModelImpl extends AbstractTableModel implements DataTableModel
{
    private String[] columns;
    private List dataRecords;

    /**
     * Construct an empty <code>DataTableModelImpl</code> with no data columns.
     */
    public DataTableModelImpl()
    {
        this(new String[0]);
    }

    /**
     * Construct a <code>DataTableModelImpl</code> with given data columns.
     *
     * @param columns The data columns to construct the table model with.
     */
    public DataTableModelImpl(String[] columns)
    {
        setColumns(columns);
        this.dataRecords = new LinkedList();
    }

    /**
     * Return the number of data records in the table model.
     *
     * @return The number of data records in the table model.
     */
    public int getRowCount()
    {
        return dataRecords.size();
    }

    /**
     * Returns the {@link suncertify.db.DataRecord DataRecord} at the given row index and column index in the table model.
     *
     * @param rowIndex The row index at which to retrieve the data record for.
     * @param columnIndex The column index at which to retrieve the data record for.
     * @return The data record at the given row index and column index in the table model.
     */
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return ((DataRecord)dataRecords.get(rowIndex)).getValueAt(columnIndex);
    }

    /**
     * Determines if the table model cell at the given row index and column index is editable.
     * Always returns <code>false</code>. This table model does not contain any cells that are editable.
     *
     * @param rowIndex The row index in the table model at which to determine if the table cell is editable.
     * @param columnIndex The column index in the table model at which to determine if the table cell is editable.
     * @return <code>false</code>.
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /**
     * Returns the name of the column at the given column index (indexed from 0) in the table model.
     * Always returns <code>java.lang.String.class</code>. All columns in the table model are represent by the class <code>String</code>.
     *
     * @param col The index of the column to retrieve the name for.
     * @return The name of the column at the given column index in the table model.
     */
    public Class getColumnClass(int col)
    {
        return String.class;
    }

    /**
     * Sets the value at the given row index and column index to the given value in the table model.
     *
     * @param value The new value to set.
     * @param rowIndex The row index at which to set the new value.
     * @param columnIndex The column index at which to set the new value.
     */
    public void setValueAt(Object value, int rowIndex, int columnIndex)
    {
        ((DataRecord)dataRecords.get(rowIndex)).setValueAt(columnIndex, (String)value);

        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * Sets the column names that this <code>DataTableModelImpl</code> represents.
     *
     * @param columns The new column names that this <code>DataTableModelImpl</code> represents.
     */
    public void setColumns(String[] columns)
    {
        this.columns = columns;

        fireTableStructureChanged();
    }

    /**
     * Returns the name of the column at the given column index.
     *
     * @param column The column index at which to retrieve the column name for.
     * @return The name of the column at the given column index.
     */
    public String getColumnName(int column)
    {
        return(columns[column]);
    }

    /**
     * Returns the number of columns that are represented in this <code>DataTableModelImpl</code>.
     *
     * @return The number of columns that are represented in this <code>DataTableModelImpl</code>.
     */
    public int getColumnCount()
    {
        return columns.length;
    }

    /**
     * Adds a {@link suncertify.db.DataRecord DataRecord} to the table model. The new data record will be appended to
     * the list of data records. It is not possible to add new data records to arbitrary positions.
     *
     * @param rec The new data record to append to the table model.
     * @throws IllegalArgumentException If the give records row length is not equal to the number of columns.
     */
    public void addDataRecord(DataRecord rec) throws IllegalArgumentException
    {
        if(rec.getRowLength() != columns.length)
        {
            StringBuffer message = new StringBuffer();
            message.append("Row length != column length [");
            message.append(rec.getRowLength());
            message.append(" != ");
            message.append(columns.length);
            message.append(']');

            throw new IllegalArgumentException(message.toString());
        }

        String[] data = rec.getData();

        for(int i = 0; i < data.length; i++)
        {
            if(data[i] != null)
            {
                rec.setValueAt(i, data[i].trim());
            }
        }

        dataRecords.add(rec);

        fireTableRowsInserted(dataRecords.size() - 1, dataRecords.size() - 1);
    }

    /**
     * Deletes the {@link suncertify.db.DataRecord DataRecord} at the given index from the table model.
     * The deleted data record is returned.
     *
     * @param rowIndex The index at which to delete the data record.
     * @return The deleted data record.
     */
    public DataRecord deleteDataRecord(int rowIndex)
    {
        DataRecord rec = (DataRecord)dataRecords.remove(rowIndex);

        fireTableRowsDeleted(rowIndex, rowIndex);

        return rec;
    }

    /**
     * Updates the {@link suncertify.db.DataRecord DataRecord} at the given index with the new data record in the table model.
     *
     * @param rec The new data record to update the table model with.
     * @param rowIndex The index at which to update the data record in the table model.
     * @throws IllegalArgumentException If the give records row length is not equal to the number of columns.
     */
    public void updateDataRecord(DataRecord rec, int rowIndex) throws IllegalArgumentException
    {
        if(rec.getRowLength() != columns.length)
        {
            StringBuffer message = new StringBuffer();
            message.append("Row length != column length [");
            message.append(rec.getRowLength());
            message.append(" != ");
            message.append(columns.length);
            message.append(']');

            throw new IllegalArgumentException(message.toString());
        }

        // enforce that the index is the same
        rec.setIndex(((DataRecord)dataRecords.get(rowIndex)).getIndex());

        String[] data = rec.getData();

        for(int i = 0; i < data.length; i++)
        {
            if(data[i] != null)
            {
                rec.setValueAt(i, data[i].trim());
            }
        }

        dataRecords.set(rowIndex, rec);

        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * Returns the {@link suncertify.db.DataRecord DataRecord} at the given index from the table model.
     *
     * @param rowIndex The index at which to retrieve the data record.
     * @return The {@link suncertify.db.DataRecord DataRecord} at the given index from the table model.
     */
    public DataRecord getDataRecord(int rowIndex)
    {
        return (DataRecord)dataRecords.get(rowIndex);
    }

    /**
     * Clears all {@link suncertify.db.DataRecord DataRecord}s from the table model.
     */
    public void clear()
    {
        int size = dataRecords.size();

        dataRecords.clear();

        fireTableRowsDeleted(0, size);
    }
}
