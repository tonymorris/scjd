package suncertify.db.datafile;

/**
 * Encapsulates a data structure that describes the header of a data file.
 * A data file header consists of the following elements in the given order:
 * <li>4-byte numeric magic number that identifies the file as a data file.</li>
 * <li>4-byte numeric - the offset (in bytes) to the beginning of the data.</li>
 * <li>2-byte numeric - the number of fields in each data record.</li>
 * <li><i>Repeating Schema Description. Each repeating section describes a field in the schema.</i></li>
 * <li>2-byte numeric - the length (in bytes) of the field name.</li>
 * <li>n-byte String - the field name.</li>
 * <li>2-byte numeric - the length (in bytes) of the data held by this field</li>.
 * <li><i>End Repeating Schema Description.</i></li>
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class DataFileHeader
{
    private int magicNumber;
    private int dataOffset;
    private short totalFields;
    private FieldSchema[] schema;

    /**
     * Construct a <tt>DataFileHeader</tt> with all default values.
     * The magic number is set to zero.
     * The data offset is set to zero.
     * The total number of fields is set to zero.
     * There is no schema, set to <code>null</code>.
     */
    public DataFileHeader()
    {
        this(0, 0, (short)0, null);
    }

    /**
     * Construct a <tt>DataFileHeader</tt> with the given magic number; all other properties are set to default values.
     * The data offset is set to zero.
     * The total number of fields is set to zero.
     * There is no schema, set to <code>null</code>.
     *
     * @param magicNumber The value of the magic number for the new <tt>DataFileHeader</tt>.
     */
    public DataFileHeader(int magicNumber)
    {
        this(magicNumber, 0, (short)0, null);
    }

    /**
     * Construct a <tt>DataFileHeader</tt> with the given magic number and data offset;
     * all other properties are set to default values.
     *
     * @param magicNumber The value of the magic number for the new <tt>DataFileHeader</tt>.
     * @param dataOffset The value of the data offset for the new <tt>DataFileHeader</tt>.
     */
    public DataFileHeader(int magicNumber, int dataOffset)
    {
        this(magicNumber, dataOffset, (short)0, null);
    }

    /**
     * Construct a <tt>DataFileHeader</tt> with the given magic number, data offset,
     * total number of fields and schema.
     *
     * @param magicNumber The value of the magic number for the new <tt>DataFileHeader</tt>.
     * @param dataOffset The value of the data offset for the new <tt>DataFileHeader</tt>.
     * @param totalFields The value of the total number of fields for the new <tt>DataFileHeader</tt>.
     * @param schema The value of the schema for the new <tt>DataFileHeader</tt>.
     */
    public DataFileHeader(int magicNumber, int dataOffset, short totalFields, FieldSchema[] schema)
    {
        setMagicNumber(magicNumber);
        setDataOffset(dataOffset);
        setTotalFields(totalFields);
        setSchema(schema);
    }

    /**
     * Returns the magic number property of the <tt>DataFileHeader</tt>.
     *
     * @return The magic number property of the <tt>DataFileHeader</tt>.
     */
    public int getMagicNumber()
    {
        return magicNumber;
    }

    /**
     * Sets the magic number property of the <tt>DataFileHeader</tt>.
     *
     * @param magicNumber The new magic number property of the <tt>DataFileHeader</tt>.
     */
    public void setMagicNumber(int magicNumber)
    {
        this.magicNumber = magicNumber;
    }

    /**
     * Returns the data offset property of the <tt>DataFileHeader</tt>.
     *
     * @return The data offset property of the <tt>DataFileHeader</tt>.
     */
    public int getDataOffset()
    {
        return dataOffset;
    }

    /**
     * Sets the data offset property of the <tt>DataFileHeader</tt>.
     *
     * @param dataOffset The new data offset property of the <tt>DataFileHeader</tt>.
     */
    public void setDataOffset(int dataOffset)
    {
        this.dataOffset = dataOffset;
    }

    /**
     * Returns the total number of fields property of the <tt>DataFileHeader</tt>.
     *
     * @return The total number of fields property of the <tt>DataFileHeader</tt>.
     */
    public short getTotalFields()
    {
        return totalFields;
    }

    /**
     * Sets the total number of fields property of the <tt>DataFileHeader</tt>.
     *
     * @param totalFields The new total number of fields property of the <tt>DataFileHeader</tt>.
     */
    public void setTotalFields(short totalFields)
    {
        this.totalFields = totalFields;
    }

    /**
     * Returns the schema property of the <tt>DataFileHeader</tt>.
     *
     * @return The schema property of the <tt>DataFileHeader</tt>.
     */
    public FieldSchema[] getSchema()
    {
        return schema;
    }

    /**
     * Sets the schema property of the <tt>DataFileHeader</tt>.
     *
     * @param schema The new schema property of the <tt>DataFileHeader</tt>.
     */
    public void setSchema(FieldSchema[] schema)
    {
        this.schema = schema;
    }

    /**
     * Returns the length, in bytes, of a data record according to information contained within this header.
     * All records begin with a 2-byte offset that is flag indicating a valid or deleted.
     * If the schema property is set, the length will also include those given in the schema.
     *
     * @see FieldSchema#getLength()
     * @return The length, in bytes, of a data record according to information contained within this header.
     */
    public int recordLength()
    {
        // offset into record - a flag indicating deleted or valid (0x0080 or 0x0000 respectively)
        int length = 2;

        if(this.schema != null)
        {
            for(int i = 0; i < this.schema.length; i++)
            {
                length = length + this.schema[i].getLength();
            }
        }

        return length;
    }

    /**
     * Performs a "deep equality" comparison between this <tt>DataFileHeader</tt> and the given object.
     * If the given object is not an instance of <tt>DataFileHeader</tt>, this method will return <code>false</code>.
     * Only if the magic number property, data offset property, total number of fields property and schema are equal,
     * will this method return <code>true</code>.
     * The schema properties are determined to be equal iff (if and only if) they are both equal to <code>null</code> or
     * each {@link FieldSchema SchemaColumn} element within the array is equal according to the
     * the <code>SchemaColumn</code> implementation <code>equals(Object)</code> method.
     *
     * @see FieldSchemaImpl#equals(Object)
     * @param o The object to perform a "deep equality" comparison with this <tt>DataFileHeader</tt>.
     * @return <code>true</code> if this <tt>DataFileHeader</tt> is determined to be equal to the given object,
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

        DataFileHeader dfh = (DataFileHeader)o;

        if(this.magicNumber == dfh.magicNumber && this.dataOffset == dfh.dataOffset && this.totalFields == dfh.totalFields)
        {
            if(this.schema == null && dfh.schema == null)
            {
                return true;
            }

            if(this.schema == null || dfh.schema == null)
            {
                return false;
            }

            if(this.schema.length == dfh.schema.length)
            {
                for(int i = 0; i < this.schema.length; i++)
                {
                    if(!this.schema[i].equals(dfh.schema[i]))
                    {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Returns a hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     *
     * @see FieldSchemaImpl#hashCode()
     * @return A hash code for the benefit of of data structures that perform a "hash" on their elements
     * (such as java.util.HashMap).
     */
    public int hashCode()
    {
        final int ODD_PRIME = 461;
        int result = 73;

        result = result * ODD_PRIME + this.magicNumber;
        result = result * ODD_PRIME + this.dataOffset;
        result = result * ODD_PRIME + this.totalFields;

        return result;
    }

    /**
     * Returns a <code>String</code> representation of this <tt>DataFileHeader</tt>.
     * The result will contain the following in the given order:
     * <li>[<i>magic number property</i>]</li>
     * <li>[<i>data offset property</i>]</li>
     * <li>[<i>total number of fields property</i>]</li>
     * <li>{<i>schema property</i>}</li>
     * <br>
     * The <code>String</code> representation of the schema property will be determined by the
     * the <code>SchemaColumn</code> implementation <code>toString()</code> method.
     *
     * @see FieldSchemaImpl#toString()
     * @return A <code>String</code> representation of this <tt>DataFileHeader</tt>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        sb.append(this.magicNumber);
        sb.append(']');
        sb.append('[');
        sb.append(this.dataOffset);
        sb.append(']');
        sb.append('[');
        sb.append(this.totalFields);
        sb.append(']');

        if(this.schema != null)
        {
            sb.append('{');

            for(int i = 0; i < this.schema.length; i++)
            {
                sb.append(this.schema[i]);
            }

            sb.append('}');
        }

        return sb.toString();
    }
}
