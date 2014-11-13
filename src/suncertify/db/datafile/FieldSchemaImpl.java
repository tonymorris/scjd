package suncertify.db.datafile;

/**
 * Encapsulates the description (schema) of a data field as it would appear in the data file.
 * The data field description contains a descriptive name (the name property) and the length of
 * the data that the field may hold (the length property).
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class FieldSchemaImpl implements FieldSchema
{
    private String name;
    private short length;

    /**
     * Construct a <tt>SchemaColumnImpl</tt> with default values.
     * The name property is set to <code>null</code>.
     * The length property is set to zero.
     */
    public FieldSchemaImpl()
    {
        this(null, (short)0);
    }

    /**
     * Construct a <tt>SchemaColumnImpl</tt> with the given name property value and other properties set to default values.
     * The length property is set to zero.
     *
     * @param name The new value of the name property for the <tt>SchemaColumnImpl</tt>.
     */
    public FieldSchemaImpl(String name)
    {
        this(name, (short)0);
    }

    /**
     * Construct a <tt>SchemaColumnImpl</tt> with the given length property value and other properties set to default values.
     * The name property is set to <code>null</code>.
     *
     * @param length The new value of the length property for the <tt>SchemaColumnImpl</tt>.
     */
    public FieldSchemaImpl(short length)
    {
        this(null, length);
    }

    /**
     * Construct a <tt>SchemaColumnImpl</tt> with the given name property value and the given length property value.
     *
     * @param name The new value of the name property for the <tt>SchemaColumnImpl</tt>.
     * @param length The new value of the length property for the <tt>SchemaColumnImpl</tt>.
     */
    public FieldSchemaImpl(String name, short length)
    {
        setName(name);
        setLength(length);
    }

    /**
     * Returns the name property of the <tt>SchemaColumnImpl</tt>.
     *
     * @return The name property of the <tt>SchemaColumnImpl</tt>.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name property of the <tt>SchemaColumnImpl</tt>.
     *
     * @param name The new name property of the <tt>SchemaColumnImpl</tt>.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Returns the length property of the <tt>SchemaColumnImpl</tt>.
     *
     * @return The length property of the <tt>SchemaColumnImpl</tt>.
     */
    public short getLength()
    {
        return length;
    }

    /**
     * Sets the length property of the <tt>SchemaColumnImpl</tt>.
     *
     * @param length The new length property of the <tt>SchemaColumnImpl</tt>.
     */
    public void setLength(short length)
    {
        this.length = length;
    }

    /**
     * Performs a "deep equality" comparison between this <tt>SchemaColumnImpl</tt> and the given object.
     * If the given object is not an instance of <tt>SchemaColumnImpl</tt>, this method will return <code>false</code>.
     * Only if the name property and length property are equal will this method return <code>true</code>.
     * The name properties are determined to be equal iff (if and only if) they are both equal to <code>>null</code>
     * or they are determined to be equal according to the <code>java.lang.String.equals(Object)</code> method.
     *
     * @param o The object to perform a "deep equality" comparison with this <tt>SchemaColumnImpl</tt>.
     * @return <code>true</code> if this <tt>SchemaColumnImpl</tt> is determined to be equal to the given object,
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

        FieldSchemaImpl sf = (FieldSchemaImpl)o;

        if(this.name == null && sf.name != null)
        {
            return false;
        }

        if(this.name != null && sf.name == null)
        {
            return false;
        }

        if(this.length != sf.length)
        {
            return false;
        }

        return ((this.name == null && sf.name == null) || (this.name.equals(sf.name)));
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

        result = result * ODD_PRIME + this.name.hashCode();
        result = result * ODD_PRIME + this.length;

        return result;
    }

    /**
     * Returns a <code>String</code> representation of this <tt>SchemaColumnImpl</tt>.
     * The result will contain the following:
     * <li>[<i>name property</i>]</li>
     * <li>[<i>length property</i>]</li>
     *
     * @return A <code>String</code> representation of this <tt>SchemaColumnImpl</tt>.
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        sb.append(this.name);
        sb.append(']');
        sb.append('[');
        sb.append(this.length);
        sb.append(']');

        return sb.toString();
    }
}
