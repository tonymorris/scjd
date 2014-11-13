package suncertify.db;

import suncertify.db.DataRecord;

/**
 * Encapsulates a record as it appears in the data file.
 * A record in the data file contains a 2-byte flag indicating a valid or deleted record.
 * This flag is represented as a <code>boolean</code> property named "deleted".
 * Each record also contains zero or more fields (represented as a <code>String[]</code>);
 * These fields are dependant on the schema,
 * which is presented in the {@link suncertify.db.datafile.DataFileHeader data file header}.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DataRecordImpl implements DataRecord
{
    private boolean deleted;
    private String[] data;
    private int index;

    /**
     * Construct a <tt>DataRecordImpl</tt> with default values.
     * The deleted property is set to <code>false</code>.
     * The data property is set to <code>null</code>.
     * The index property is set to 0.
     */
    public DataRecordImpl()
    {
        this(false, 0, null);
    }

    /**
     * Construct a <tt>DataRecordImpl</tt> with the given deleted property value and other properties set to default values.
     * The data property is set to <code>null</code>.
     * The index property is set to 0.
     *
     * @param deleted The new value of the deleted property for the <tt>DataRecordImpl</tt>.
     */
    public DataRecordImpl(boolean deleted)
    {
        this(deleted, 0, null);
    }

    /**
     * Construct a <tt>DataRecordImpl</tt> with the given data property value and other properties set to default values.
     * The deleted property is set to <code>false</code>.
     * The index property is set to 0.
     *
     * @param data The new value of the data property for the <tt>DataRecordImpl</tt>.
     */
    public DataRecordImpl(String[] data)
    {
        this(false, 0, data);
    }

    /**
     * Construct a <tt>DataRecordImpl</tt> with the given deleted property value and data property value.
     * The index property is set to 0.
     *
     * @param deleted The new value of the deleted property for the <tt>DataRecordImpl</tt>.
     * @param data The new value of the data property for the <tt>DataRecordImpl</tt>.
     */
    public DataRecordImpl(boolean deleted, String[] data)
    {
        this(deleted, 0, data);
    }

    /**
     * Construct a <tt>DataRecordImpl</tt> with the given deleted property value and data property value.
     * The deleted property is set to <code>false</code>.
     *
     * @param index The index that the record exists in the data file.
     * @param data The new value of the data property for the <tt>DataRecordImpl</tt>.
     */
    public DataRecordImpl(int index, String[] data)
    {
        this(false, index, data);
    }

    /**
     * Construct a <tt>DataRecordImpl</tt> with the given deleted property value and data property value.
     *
     * @param deleted The new value of the deleted property for the <tt>DataRecordImpl</tt>.
     * @param index The index that the record exists in the data file.
     * @param data The new value of the data property for the <tt>DataRecordImpl</tt>.
     */
    public DataRecordImpl(boolean deleted, int index, String[] data)
    {
        setDeleted(deleted);
        setData(data);
        setIndex(index);
    }

    /**
     * Returns the deleted property of the <tt>DataRecordImpl</tt>.
     *
     * @return The deleted property of the <tt>DataRecordImpl</tt>.
     */
    public boolean isDeleted()
    {
        return deleted;
    }

    /**
     * Sets the deleted property of the <tt>DataRecordImpl</tt>.
     *
     * @param deleted The new deleted property of the <tt>DataRecordImpl</tt>.
     */
    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    /**
     * Returns the data property of the <tt>DataRecordImpl</tt>.
     *
     * @return The data property of the <tt>DataRecordImpl</tt>.
     */
    public String[] getData()
    {
        return data;
    }

    /**
     * Sets the data property of the <tt>DataRecordImpl</tt>.
     *
     * @param data The mew data property of the <tt>DataRecordImpl</tt>.
     */
    public void setData(String[] data)
    {
        this.data = data;
    }

    /**
     * Returns the index at which this <code>DataRecordImpl</code> exists in the data source.
     * The index is used to pass messages to the data source about this instance of the <code>DataRecordImpl</code>.
     *
     * @return The index at which this <code>DataRecordImpl</code> exists in the data source.
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * Sets the index at which this <code>DataRecordImpl</code> exists in the data source.
     * The index is used to pass messages to the data source about this instance of the <code>DataRecordImpl</code>.
     *
     * @param index The new index at which this <code>DataRecordImpl</code> exists in the data source.
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Returns the number of fields in the underlying row data.
     *
     * @return The number of fields in the underlying row data.
     */
    public int getRowLength()
    {
        return this.data.length;
    }

    /**
     * Returns the field value at the given index in the underlying row data.
     *
     * @param index The index at which to retrieve the field data from the underlying data.
     * @return The field value at the given index in the underlying row data.
     */
    public String getValueAt(int index)
    {
        return this.data[index];
    }

    /**
     * Sets the field value at the given index in the underlying row data.
     * Trims the field of white space before adding it to the model.
     *
     * @param index The index at which to set the field data in the underlying data.
     * @param value The new value to set.
     */
    public void setValueAt(int index, String value)
    {
        this.data[index] = value.trim();
    }

    /**
     * Performs a "deep equality" comparison between this <tt>DataRecordImpl</tt> and the given object.
     * If the given object is not an instance of <tt>DataRecordImpl</tt>, this method will return <code>false</code>.
     * Only if the deleted property and the data property are equal, will this method return <code>true</code>.
     * The data is determined to be equal iff (if and only if) they are both equal to <code>null</code> or
     * the underlying data arrays are of the same length, at the same index, and each element within the array is
     * equal according to the <code>String.equals(Object)</code> method.
     *
     * @param o The object to perform a "deep equality" comparison with this <tt>DataRecordImpl</tt>.
     * @return <code>true</code> if this <tt>DataRecordImpl</tt> is determined to be equal to the given object,
     * <code>false</code> otherwise.
     */
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(o == null)
        {
            return false;
        }

        if(this.getClass() != o.getClass())
        {
            return false;
        }

        DataRecordImpl record = (DataRecordImpl)o;

        if(this.deleted == record.deleted && this.index == index)
        {
            if(this.data == null && record.data == null)
            {
                return true;
            }

            if(this.data == null || record.data == null)
            {
                return false;
            }

            if(this.data.length != record.data.length)
            {
                return false;
            }

            for(int i = 0; i < this.data.length; i++)
            {
                if(!this.data[i].equals(record.data[i]))
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     * Returns a hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     *
     * @return A hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     */
    public int hashCode()
    {
        final int ODD_PRIME = 461;
        int result = 73;

        result = result * ODD_PRIME + (this.deleted ? 1 : 0);
        result = result * ODD_PRIME + this.index;

        if(this.data != null)
        {
            for(int i = 0; i < this.data.length; i++)
            {
                result = result * ODD_PRIME + this.data[i].hashCode();
            }
        }

        return result;
    }

    /**
     * Returns a <code>String</code> representation of this <tt>DataRecordImpl</tt>.
     * The result will contain the following:
     * <li>[<i>deleted property</i>]</li>
     * <li>{<i>index property</i>}</li>
     * <li>{<i>data array property</i>}</li>
     *
     * <br>
     * Each element of the data array property is converted to a <code>String</code>
     * according to the <code>String.toString()</code> method.
     *
     * @return A <code>String</code> representation of this <tt>DataRecordImpl</tt>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        sb.append(this.deleted);
        sb.append(']');

        sb.append('[');
        sb.append(this.index);
        sb.append(']');

        if(this.data != null)
        {
            sb.append('{');

            for(int i = 0; i < this.data.length; i++)
            {
                sb.append(this.data[i]);
            }

            sb.append('}');
        }

        return sb.toString();
    }
}
