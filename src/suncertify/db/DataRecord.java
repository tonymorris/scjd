package suncertify.db;

/**
 * Represents a record within a database.
 * A record may or may not be flagged as deleted, and data is stored as an array of <code>String</code> objects.
 *
 * @see suncertify.db.DataRecordImpl
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public interface DataRecord
{
    /**
     * Returns the deleted property of the database record.
     *
     * @return The deleted property of the database record.
     */
    public boolean isDeleted();

    /**
     * Sets the deleted property of the database record.
     *
     * @param deleted The deleted property of the database record.
     */
    public void setDeleted(boolean deleted);

    /**
     * Returns the data property of the database record.
     *
     * @return The data property of the database record.
     */
    public String[] getData();

    /**
     * Sets the data property of the database record.
     *
     * @param fields The data property of the database record.
     */
    public void setData(String[] fields);

    /**
     * Returns the index at which this <code>DataRecord</code> exists in the data file.
     * The index is used to pass messages to the data source about this instance of the <code>DataRecord</code>.
     *
     * @return The index at which this <code>DataRecord</code> exists in the data file.
     */
    public int getIndex();

    /**
     * Sets the index at which this <code>DataRecord</code> exists in the data file.
     * The index is used to pass messages to the data source about this instance of the <code>DataRecord</code>.
     *
     * @param index The new index at which this <code>DataRecord</code> exists in the data file.
     */
    public void setIndex(int index);

    /**
     * Returns the number of fields in the underlying row data.
     *
     * @return The number of fields in the underlying row data.
     */
    public int getRowLength();

    /**
     * Returns the field value at the given index in the underlying row data.
     *
     * @param index The index at which to retrieve the field data from the underlying data.
     * @return The field value at the given index in the underlying row data.
     */
    public String getValueAt(int index);

    /**
     * Sets the field value at the given index in the underlying row data.
     *
     * @param index The index at which to set the field data in the underlying data.
     * @param value The new value to set.
     */
    public void setValueAt(int index, String value);
}
