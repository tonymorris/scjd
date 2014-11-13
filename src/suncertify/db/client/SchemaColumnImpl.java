package suncertify.db.client;

/**
 * Represents a schema of a column of the data that the application is configured to use.
 * Each column may (or may not) be a unique key, contains a display name and has a length in bytes.
 * These attributes are represented as a <code>boolean</code>, a <code>String</code>, and a <code>short</code> respectively.
 *
 * @author <a href="mailto:tonymorr@au1.ibm.com">Tony Morris</a>
 * @version 1.0 (build @build.number@)
 */
public class SchemaColumnImpl implements SchemaColumn
{
    private boolean key;
    private String displayName;
    private short length;

    /**
     * Constructs an empty <code>SchemaColumnImpl</code>.
     * The column is not a unique key, has an empty display name and has a length of zero.
     * Equivalent to calling {@link #SchemaColumnImpl(boolean, String, short) this(false, "", (short)0)}.
     */
    public SchemaColumnImpl()
    {
        this(false, "", (short)0);
    }

    /**
     * Constructs a <code>SchemaColumnImpl</code> with the given unique key, the given display name and the given length.
     *
     * @param key <code>true</code> if this column schema is a unique key, <code>false</code> otherwise.
     * @param displayName The display name of this column schema.
     * @param length The length, in bytes, of this column schema.
     */
    public SchemaColumnImpl(boolean key, String displayName, short length)
    {
        setKey(key);
        setDisplayName(displayName);
        setLength(length);
    }

    /**
     * Determines if this column schema is a unique key.
     *
     * @return <code>true</code> if this column schema is a unique key, <code>false</code> otherwise.
     */
    public boolean isKey()
    {
        return key;
    }

    /**
     * Sets whether or not this column schema is a unique key.
     *
     * @param key Set to <code>true</code> if this column schema is a unique key, <code>false</code> otherwise.
     */
    public void setKey(boolean key)
    {
        this.key = key;
    }

    /**
     * Returns the display name of this column schema.
     *
     * @return The display name of this column schema.
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Sets the display name of this column schema.
     *
     * @param displayName The new display name of this column schema.
     */
    public void setDisplayName(String displayName)
    {
        if(displayName == null)
        {
            displayName = "";
        }

        this.displayName = displayName;
    }

    /**
     * Returns the length, in bytes, of this column schema.
     *
     * @return The length, in bytes, of this column schema.
     */
    public short getLength()
    {
        return length;
    }

    /**
     * Sets the length, in bytes, of this column schema.
     *
     * @param length The new value of the length, in bytes, of this column schema.
     */
    public void setLength(short length)
    {
        this.length = length;
    }
}
