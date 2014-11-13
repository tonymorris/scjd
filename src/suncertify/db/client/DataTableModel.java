package suncertify.db.client;

import suncertify.db.DataRecord;

import javax.swing.table.TableModel;

/**
 * Represents the table model of the data record display in the application frames' JTable.
 * The table model contains an ordered list of {@link suncertify.db.DataRecord DataRecord}s and an array of column names.
 *
 * @see DataTableModelImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface DataTableModel extends TableModel
{
    /**
     * Sets the column names that this <code>DataTableModel</code> represents.
     *
     * @param columns The new column names that this <code>DataTableModel</code> represents.
     */
    public void setColumns(String[] columns);

    /**
     * Returns the name of the column at the given column index.
     *
     * @param column The column index at which to retrieve the column name for.
     * @return The name of the column at the given column index.
     */
    public String getColumnName(int column);

    /**
     * Returns the number of columns that are represented in this <code>DataTableModel</code>.
     *
     * @return The number of columns that are represented in this <code>DataTableModel</code>.
     */
    public int getColumnCount();

    /**
     * Adds a {@link suncertify.db.DataRecord DataRecord} to the table model. The new data record will be appended to
     * the list of data records. It is not possible to add new data records to arbitrary positions.
     *
     * @param rec The new data record to append to the table model.
     */
    public void addDataRecord(DataRecord rec);

    /**
     * Deletes the {@link suncertify.db.DataRecord DataRecord} at the given index from the table model.
     * The deleted data record is returned.
     *
     * @param rowIndex The index at which to delete the data record.
     * @return The deleted data record.
     */
    public DataRecord deleteDataRecord(int rowIndex);

    /**
     * Updates the {@link suncertify.db.DataRecord DataRecord} at the given index with the new data record in the table model.
     *
     * @param rec The new data record to update the table model with.
     * @param rowIndex The index at which to update the data record in the table model.
     */
    public void updateDataRecord(DataRecord rec, int rowIndex);

    /**
     * Returns the {@link suncertify.db.DataRecord DataRecord} at the given index from the table model.
     *
     * @param rowIndex The index at which to retrieve the data record.
     * @return The {@link suncertify.db.DataRecord DataRecord} at the given index from the table model.
     */
    public DataRecord getDataRecord(int rowIndex);

    /**
     * Clears all {@link suncertify.db.DataRecord DataRecord}s from the table model.
     */
    public void clear();
}
